package cn.leo.frame.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import cn.leo.frame.R
import java.util.*

/**
 * Created by JarryLeo on 2018/2/6.
 * 安卓8.0以下申请一个权限，用户同意后整个权限组的权限都不用申请可以直接使用
 * 8.0后每个权限都要单独申请，不能一次申请通过后,整个权限组都不用申请;
 * 但是用户同意权限组内一个之后，其它申请直接通过(之前是不申请可以直接用，现在是申请直接过(但是必须申请))
 */
@Suppress("UNUSED", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate")
class PermissionUtil private constructor(private val mActivity: FragmentActivity) {
    private var mFragmentCallback: FragmentCallback? = null

    @FunctionalInterface
    interface Result {
        fun onSuccess()
        fun onFailed(failedPermissions: Array<String>?)
    }

    interface RationaleResult : Result
    /**
     * fragment，作为权限回调监听，和从设置界面返回监听
     */
    class FragmentCallback : Fragment() {
        private var mResult: Result? = null
        private var mPermissions: Array<String>? = null
        private var mRequestTime: Long = 0
        fun setRequestTime() {
            mRequestTime = SystemClock.elapsedRealtime()
        }

        fun setResult(result: Result?) {
            mResult = result
        }

        fun setPermissions(permissions: Array<String>?) {
            mPermissions = permissions
        }

        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
        ) {
            var result = true
            when (requestCode) {
                REQUEST_CODE -> {
                    var i = 0
                    while (i < permissions.size) {
                        if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                            result = false
                            break
                        }
                        i++
                    }
                }
                else -> {
                }
            }
            if (mResult != null) {
                if (result) {
                    detach()
                    mResult!!.onSuccess()
                } else {
                    if (SystemClock.elapsedRealtime() - mRequestTime < 300) {
                        val sb = StringBuilder()
                        for (permission in mPermissions!!) {
                            if (!checkPermission(
                                    activity,
                                    permission
                                )
                            ) {
                                val permissionName =
                                    getPermissionName(
                                        activity,
                                        permission
                                    )
                                if (permissionName.isNotEmpty()) {
                                    sb.append(" [")
                                        .append(permissionName)
                                        .append("] ")
                                }
                            }
                        }
                        val permissionList = sb.toString()
                        val s =
                            permissionList.replace("(\\s\\[.*]\\s)\\1+".toRegex(), "$1")
                        openSettingActivity(
                            getString(
                                R.string.base_permission_should_show_rationale,
                                s
                            )
                        )
                    } else {
                        mResult!!.onFailed(mPermissions)
                    }
                }
            }
        }

        override fun onActivityResult(
            requestCode: Int,
            resultCode: Int,
            data: Intent?
        ) {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == REQUEST_CODE) {
                if (mResult != null && mPermissions != null) {
                    val list: MutableList<String> =
                        ArrayList()
                    var result = true
                    for (mPermission in mPermissions!!) {
                        if (!checkPermission(activity, mPermission)) {
                            result = false
                            list.add(mPermission)
                        }
                    }
                    if (result) {
                        detach()
                        mResult?.onSuccess()
                    } else {
                        mResult?.onFailed(list.toTypedArray())
                    }
                }
            }
        }

        /**解绑fragment */
        private fun detach() {
            if (!isAdded) {
                return
            }
            val fragmentTransaction =
                parentFragmentManager.beginTransaction()
            fragmentTransaction.detach(this)
            fragmentTransaction.remove(this)
            fragmentTransaction.commitAllowingStateLoss()
        }

        /**
         * 打开应用权限设置界面
         */
        fun openSettingActivity(message: String) {
            if (mResult is RationaleResult) {
                mResult?.onFailed(mPermissions)
                return
            }
            showMessageOKCancel(
                message,
                DialogInterface.OnClickListener { _, _ ->
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri =
                        Uri.fromParts("package", activity?.packageName, null)
                    intent.data = uri
                    startActivityForResult(intent, REQUEST_CODE)
                },
                DialogInterface.OnClickListener { _, _ -> mResult!!.onFailed(mPermissions) })
        }

        /**
         * 弹出对话框
         *
         * @param message    消息内容
         * @param okListener 点击回调
         */
        private fun showMessageOKCancel(
            message: String,
            okListener: DialogInterface.OnClickListener,
            cancelListener: DialogInterface.OnClickListener
        ) {
            AlertDialog.Builder(activity)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.base_permission_dialog_granted), okListener)
                .setNegativeButton(
                    getString(R.string.base_permission_dialog_denied),
                    cancelListener
                )
                .create()
                .show()
        }
    }

    private var mPermissions: Array<String>? = null
    /**
     * 需要请求的权限列表
     *
     * @param permissions 权限列表
     * @return 返回自身链式编程
     */
    fun request(vararg permissions: String): PermissionUtil {
        mPermissions = arrayOf(*permissions)
        return this
    }

    /**
     * 筛选出需要申请的权限
     *
     * @param permissions 权限列表
     * @return 需要申请的权限
     */
    private fun getNeedRequestPermissions(permissions: Array<String>): Array<String> {
        val list: MutableList<String> =
            ArrayList()
        for (p in permissions) {
            if (!checkPermission(mActivity, p)) {
                list.add(p)
            }
        }
        return list.toTypedArray()
    }

    /**
     * 执行权限请求
     *
     * @param result 请求结果回调
     */
    fun execute(result: Result?) {
        if (!checkManifestPermission()) {
            return
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || checkPermissions()) {
            result?.onSuccess()
            return
        }
        //创建fragment回调
        val fragmentManager =
            mActivity.supportFragmentManager
        val fragmentByTag =
            fragmentManager.findFragmentByTag(tag)
        if (fragmentByTag != null) {
            mFragmentCallback = fragmentByTag as FragmentCallback?
            mFragmentCallback!!.setResult(result)
        } else {
            mFragmentCallback = FragmentCallback()
            mFragmentCallback!!.setResult(result)
            fragmentManager
                .beginTransaction()
                .add(mFragmentCallback!!, tag)
                .commitAllowingStateLoss()
            fragmentManager.executePendingTransactions()
        }
        //开始请求
        requestPermission()
    }

    /**
     * 检查动态权限是否都在清单文件注册
     */
    private fun checkManifestPermission(): Boolean {
        val notRegPermissions = notRegPermissions
        val empty = notRegPermissions.isEmpty()
        if (empty) {
            return true
        }
        val sb = StringBuilder()
        for (notRegPermission in notRegPermissions) {
            sb.append(" [")
                .append(notRegPermission)
                .append("] ")
        }
        sb.append(mActivity.getString(R.string.base_permission_not_reg_in_manifest))
        val permissionList = sb.toString()
        val s = permissionList.replace("(\\s\\[.*]\\s)\\1+".toRegex(), "$1")
        Log.e("MagicPermission Error: ", s)
        Toast.makeText(mActivity, s, Toast.LENGTH_SHORT).show()
        return false
    }

    /**
     * 获取动态申请却没有在清单文件注册的权限
     */
    private val notRegPermissions: List<String>
        get() {
            val requiredPermissions = requiredPermissions
            val list = requiredPermissions.toList()
            val notReg: MutableList<String> =
                ArrayList()
            for (permission in mPermissions!!) {
                if (!list.contains(permission)) {
                    notReg.add(permission)
                }
            }
            return notReg
        }

    /**
     * 获取清单文件中注册的权限
     */
    private val requiredPermissions: Array<String?>
        get() = try {
            val info = mActivity.packageManager
                .getPackageInfo(mActivity.packageName, PackageManager.GET_PERMISSIONS)
            val ps = info.requestedPermissions
            if (ps != null && ps.isNotEmpty()) {
                ps
            } else {
                arrayOfNulls(0)
            }
        } catch (e: Exception) {
            arrayOfNulls(0)
        }

    /**
     * 检查权限列表是否全部通过
     *
     * @return 权限列表是否全部通过
     */
    private fun checkPermissions(): Boolean {
        for (mPermission in mPermissions!!) {
            if (!checkPermission(mPermission)) {
                return false
            }
        }
        return true
    }

    /**
     * 检查权限
     *
     * @param permission 权限列表
     * @return 权限是否通过
     */
    private fun checkPermission(permission: String): Boolean { //检查权限
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        val checkSelfPermission = ContextCompat
            .checkSelfPermission(mActivity, permission)
        return checkSelfPermission == PackageManager.PERMISSION_GRANTED
    }

    /**
     * 申请权限
     */
    private fun requestPermission() {
        if (mActivity.supportFragmentManager.findFragmentByTag(tag) == null) {
            throw PermissionRequestException(mActivity.getString(R.string.base_permission_request_exception))
        }
        if (mFragmentCallback != null && mPermissions != null) { //提取权限列表里面没通过的
            val per = getNeedRequestPermissions(mPermissions!!)
            mFragmentCallback!!.setPermissions(per)
            val sb = StringBuilder()
            for (i in per.indices) {
                val permissionName =
                    getPermissionName(mActivity, per[i])
                if (permissionName.isNotEmpty()) {
                    sb.append(" [")
                        .append(permissionName)
                        .append("] ")
                }
            }
            val permissionList = sb.toString()
            val s = permissionList.replace("(\\s\\[.*]\\s)\\1+".toRegex(), "$1")
            /*            //如果用户点了不提示(或者同时申请多个权限)，我们主动提示用户
            boolean flag = false;
            for (String aPer : per) {
                boolean b = ActivityCompat.shouldShowRequestPermissionRationale(mActivity, aPer);
                if (b) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                mFragmentCallback.openSettingActivity(
                        mActivity.getString(R.string.permission_should_show_rationale, s));
            } else {*/
            //申请权限
            try {
                mFragmentCallback!!.setRequestTime()
                mFragmentCallback!!.requestPermissions(per, REQUEST_CODE)
            } catch (e: Exception) {
                mFragmentCallback!!.openSettingActivity(
                    mActivity.getString(R.string.base_permission_should_show_rationale, s)
                )
            }
            /*}*/
        }
    }

    inner class PermissionRequestException(s: String?) :
        RuntimeException(s)

    companion object {
        private const val tag = "fragmentRequestPermissionCallBack"
        private const val REQUEST_CODE = 110
        /**
         * 获取请求权限实例
         *
         * @param activity FragmentActivity
         * @return 请求权限工具对象
         */
        fun getInstance(activity: FragmentActivity): PermissionUtil {
            return PermissionUtil(activity)
        }

        /**
         * 静态检查权限
         *
         * @param context    上下文
         * @param permission 权限列表
         * @return 权限是否通过
         */
        private fun checkPermission(
            context: Context?,
            permission: String
        ): Boolean {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                return true
            }
            val checkSelfPermission = ContextCompat
                .checkSelfPermission(context!!, permission)
            return checkSelfPermission == PackageManager.PERMISSION_GRANTED
        }

        /**
         * 获取权限的名称,自动按设备语言显示
         *
         * @param context    上下文
         * @param permission 权限
         * @return 权限名称
         */
        private fun getPermissionName(
            context: Context?,
            permission: String
        ): String {
            var permissionName = ""
            val pm = context!!.packageManager
            try {
                val permissionInfo = pm.getPermissionInfo(permission, 0)
                val groupInfo =
                    pm.getPermissionGroupInfo(permissionInfo.group, 0)
                permissionName = groupInfo.loadLabel(pm).toString()
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            return permissionName
        }
    }

}