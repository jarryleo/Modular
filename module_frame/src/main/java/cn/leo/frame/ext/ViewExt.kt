package cn.leo.frame.ext

import android.graphics.Outline
import android.os.Build
import android.view.View
import android.view.ViewOutlineProvider
import kotlin.math.min

/**
 * @author : ling luo
 * @date : 2019-12-02
 */

fun <T : View> T.visibleOrGone(visible: (T) -> Boolean) {
    visibility = if (visible(this)) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

fun <T : View> T.visibleOrInvisible(visible: (T) -> Boolean) {
    visibility = if (visible(this)) {
        View.VISIBLE
    } else {
        View.INVISIBLE
    }
}

fun <T : View> T.visible() {
    visibility = View.VISIBLE
}

fun <T : View> T.invisible() {
    visibility = View.INVISIBLE
}

fun <T : View> T.gone() {
    visibility = View.GONE
}


/**
 * 设置View圆角矩形
 */
fun <T : View> T.roundCorner(corner: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                if (outline.canClip()) {
                    outline.setRoundRect(
                        0,
                        0,
                        view.width,
                        view.height,
                        corner.toFloat()
                    )
                }
            }
        }
        clipToOutline = true
    }
}

/**
 * 设置View为圆形
 */
fun <T : View> T.circle() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                if (outline.canClip()) {
                    val min = min(view.width, view.height)
                    val left = (view.width - min) / 2
                    val top = (view.height - min) / 2
                    outline.setOval(left, top, min, min)
                }
            }
        }
        clipToOutline = true
    }
}