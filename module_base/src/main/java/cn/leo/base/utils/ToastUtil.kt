package cn.leo.base.utils

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import cn.leo.frame.MFrame

/**
 * @author : ling luo
 * @date : 2019-07-04
 */

fun Context.toast(msg: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}

fun Context.toast(@StringRes msgRes: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msgRes, duration).show()
}

fun toast(msg: CharSequence, duration: Int = Toast.LENGTH_SHORT) =
    MFrame.context.toast(msg, duration)


fun toast(@StringRes msgRes: Int, duration: Int = Toast.LENGTH_SHORT) =
    MFrame.context.toast(msgRes, duration)
