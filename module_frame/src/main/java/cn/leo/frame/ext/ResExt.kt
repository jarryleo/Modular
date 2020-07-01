package cn.leo.frame.ext

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

/**
 * @author : ling luo
 * @date : 2019-11-29
 *
 * 有些机型，如果用application上下文读取资源会导致 夜间模式资源读取的还是白天模式的
 */

inline fun Context.getString(resId: Int): String {
    return getString(resId)
}

inline fun Context.getStringArray(resId: Int): Array<String> {
    return resources.getStringArray(resId)
}

inline fun Context.getDrawable(resId: Int): Drawable? {
    return ActivityCompat.getDrawable(this, resId)
}

inline fun Context.getColor(resId: Int): Int {
    return ActivityCompat.getColor(this, resId)
}

inline fun Fragment.getColor(@ColorRes colorResId: Int): Int {
    return ContextCompat.getColor(context!!, colorResId)
}

inline fun Fragment.getDrawable(@DrawableRes drawableResId: Int): Drawable? {
    return ContextCompat.getDrawable(context!!, drawableResId)
}
