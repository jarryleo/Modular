package cn.leo.frame.support

import android.graphics.drawable.Drawable
import androidx.core.app.ActivityCompat
import cn.leo.frame.MFrame

/**
 * @author : ling luo
 * @date : 2019-11-29
 */
fun Int.getString(): String {
    return MFrame.context.getString(this)
}

fun Int.getStringArray(): Array<String> {
    return MFrame.context.resources.getStringArray(this)
}


fun Int.getDrawable(): Drawable? {
    return ActivityCompat.getDrawable(MFrame.context, this)
}

fun Int.getColor(): Int {
    return ActivityCompat.getColor(MFrame.context, this)
}