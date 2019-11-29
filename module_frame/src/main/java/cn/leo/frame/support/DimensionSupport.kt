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
fun Float.toPx(): Int {
    val scale = MFrame.context.resources.displayMetrics.density
    return (this * scale).roundToInt()
}

fun Int.toPx(): Int {
    val scale = MFrame.context.resources.displayMetrics.density
    return (this * scale).roundToInt()
}

/**
 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
 */
fun Float.toDp(): Int {
    val scale = MFrame.context.resources.displayMetrics.density
    return (this / scale).roundToInt()
}

fun Int.toDp(): Int {
    val scale = MFrame.context.resources.displayMetrics.density
    return (this / scale).roundToInt()
}