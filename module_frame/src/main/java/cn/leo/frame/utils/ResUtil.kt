package cn.leo.frame.utils

import android.graphics.drawable.Drawable
import android.support.annotation.ArrayRes
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.v4.app.ActivityCompat
import cn.leo.frame.network.JL

/**
 * @author : ling luo
 * @date : 2019-04-23
 */
object ResUtil {
    fun getString(@StringRes id: Int): String {
        return JL.context.getString(id)
    }

    fun getStringArray(@ArrayRes id: Int): Array<String> {
        return JL.context.resources.getStringArray(id)
    }


    fun getDrawable(@DrawableRes id: Int): Drawable? {
        return ActivityCompat.getDrawable(JL.context, id)
    }

    fun getColor(@ColorRes id: Int): Int {
        return ActivityCompat.getColor(JL.context, id)
    }
}
