package cn.leo.frame.ext

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.app.ActivityCompat

/**
 * @author : ling luo
 * @date : 2019-11-29
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