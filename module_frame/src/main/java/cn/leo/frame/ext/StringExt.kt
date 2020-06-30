package cn.leo.frame.ext

import android.graphics.Color
import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.annotation.ColorInt
import java.math.BigDecimal

/**
 * @author : ling luo
 * @date : 2020/6/30
 */


/**
 * 字符串转html
 */
fun String.fromHtml() = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
    Html.fromHtml(this)
} else {
    Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
}

/**
 * 字符串高亮
 */
fun String.highLight(text: String, colorStr: String = "#ff0000"): Spanned {
    return this.replace(text, "<font color='$colorStr'>$text</font>", true)
        .fromHtml()
}

/**
 * 字符串高亮
 */
fun String.highLight(text: String, @ColorInt color: Int = Color.RED): Spanned {
    val r = Integer.toHexString(Color.red(color))
    val g = Integer.toHexString(Color.green(color))
    val b = Integer.toHexString(Color.blue(color))
    val colorStr = "#${r.formatNum()}${g.formatNum()}${b.formatNum()}"
    return this.replace(text, "<font color='$colorStr'>$text</font>", true)
        .fromHtml()
}

/**
 * 整数前面加零，保持位数
 * @param length 整体长度
 */
fun String.formatNum(length: Int = 2): String {
    val sb = StringBuilder()
    val left = length - this.length
    repeat(left) {
        sb.append("0")
    }
    sb.append(this)
    return sb.toString()
}

/**
 * 转换简单数量
 * @param num 大小 如：9，999，99999
 * @param suffix 后缀 如：千，万，w
 * @param scale 保留几位小数
 */
fun Long.toShortCount(num: Long = 9999, suffix: String = "万", scale: Int = 1): String {
    var count = this.toString()
    if (this > num) {
        count = BigDecimal(this)
            .divide(BigDecimal(num + 1), scale, BigDecimal.ROUND_DOWN)
            .toString()
            .plus(suffix)
    }
    return count
}

