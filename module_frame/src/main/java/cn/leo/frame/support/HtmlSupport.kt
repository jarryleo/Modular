package cn.leo.frame.support

import android.os.Build
import android.text.Html
import android.text.Spanned
import android.widget.TextView

/**
 * @author : ling luo
 * @date : 2019-11-29
 */

fun TextView.fromHtml(source: String) {
    this.text = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
        Html.fromHtml(source)
    } else {
        Html.fromHtml(source, 0)
    }
}

fun String.toHtml(): Spanned {
    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
        Html.fromHtml(this)
    } else {
        Html.fromHtml(this, 0)
    }
}