package cn.leo.frame.support

import androidx.fragment.app.FragmentActivity
import cn.leo.frame.utils.PermissionUtil

/**
 * @author : ling luo
 * @date : 2019-12-07
 */

/**
 * 检查权限
 */
fun FragmentActivity.checkPermission(
    permission: Array<String>,
    onDenied: (failedPermissions: Array<out String>) -> Unit,
    onGranted: () -> Unit
) {
    PermissionUtil.getInstance(this).request(*permission)
        .execute(object : PermissionUtil.Result {
            override fun onSuccess() {
                onGranted()
            }

            override fun onFailed(failedPermissions: Array<String>?) {
                if (!failedPermissions.isNullOrEmpty()) {
                    onDenied(failedPermissions)
                }
            }
        })
}