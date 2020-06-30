package cn.leo.frame.ext

import android.app.Activity
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.launcher.ARouter
import java.io.Serializable


/**
 * 跳转 无参数
 */
fun String.jump() {
    ARouter.getInstance().build(this).navigation()
}

/**
 * 构造 Postcard
 */
private fun <V> buildPostcard(path: String, vararg pairs: Pair<String, V>): Postcard {
    return ARouter.getInstance().build(path).apply {
        pairs.forEach {
            val (key,value) = it
            when (value) {
                is String -> withString(key, value as String)
                is Int -> withInt(key, value as Int)
                is Long -> withLong(key, value as Long)
                is Float -> withFloat(key, value as Float)
                is Boolean -> withBoolean(key, value as Boolean)
                is Bundle -> withBundle(key, value as Bundle)
                is Serializable -> withSerializable(key, value as Serializable)
                is Parcelable -> withParcelable(key, value as Parcelable)
                null -> {}
                else -> withObject(key, value)
            }
        }
    }
}

/**
 * 跳转 参数注入
 */
fun <V> String.jump(vararg pairs: Pair<String, V>): Any? {
    return buildPostcard(this, *pairs).navigation()
}

/**
 * 跳转 参数注入
 */
fun <V> String.jumpForResult(
    context: Activity,
    requestCode: Int,
    vararg pairs: Pair<String, V>
): Any? {
    return buildPostcard(this, *pairs)
        .navigation(context, requestCode)
}


/**
 * 跳转且返回
 */
fun String.jumpForResult(context: Activity, requestCode: Int) {
    ARouter.getInstance().build(this).navigation(context, requestCode)
}

/**
 * 获取fragment
 */
fun String.newFragment(bundle: Bundle? = null): Fragment {
    return ARouter.getInstance().build(this).with(bundle).navigation() as Fragment
}

/**
 * fragment 参数注入
 */
fun <V> String.newFragment(vararg pairs: Pair<String, V>): Fragment? {
    return this.jump(*pairs) as? Fragment
}
