package cn.leo.frame.utils

import android.graphics.drawable.Drawable
import androidx.annotation.ArrayRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
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
