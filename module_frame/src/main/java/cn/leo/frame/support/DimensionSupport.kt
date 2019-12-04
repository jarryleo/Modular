package cn.leo.frame.support

import cn.leo.frame.MFrame
import kotlin.math.roundToInt

/**
 * @author : ling luo
 * @date : 2019-11-29
 */

/**
 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
 */
fun Float.px(): Int {
    val scale = MFrame.context.resources.displayMetrics.density
    return (this * scale).roundToInt()
}

fun Int.px(): Int {
    val scale = MFrame.context.resources.displayMetrics.density
    return (this * scale).roundToInt()
}

/**
 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
 */
fun Float.dp(): Int {
    val scale = MFrame.context.resources.displayMetrics.density
    return (this / scale).roundToInt()
}

fun Int.dp(): Int {
    val scale = MFrame.context.resources.displayMetrics.density
    return (this / scale).roundToInt()
}