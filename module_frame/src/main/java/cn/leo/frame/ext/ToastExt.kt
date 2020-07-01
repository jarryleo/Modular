package cn.leo.frame.ext

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import cn.leo.frame.utils.toast

/**
 * @author : ling luo
 * @date : 2020/7/1
 */

fun Context.toast(msg: CharSequence?, duration: Int = Toast.LENGTH_SHORT) {
    if (msg.isNullOrEmpty()) {
        return
    }
    main {
        Toast.makeText(this@toast, msg, duration).show()
    }
}

fun Context.toast(@StringRes msgRes: Int, duration: Int = Toast.LENGTH_SHORT) {
    main {
        Toast.makeText(this@toast, msgRes, duration).show()
    }
}

fun Fragment.toast(msg: CharSequence?, duration: Int = Toast.LENGTH_SHORT) {
    this.context?.apply {
        toast(msg, duration)
    }
}

fun Fragment.toast(@StringRes msgRes: Int, duration: Int = Toast.LENGTH_SHORT) {
    this.context?.apply {
        toast(msgRes, duration)
    }
}