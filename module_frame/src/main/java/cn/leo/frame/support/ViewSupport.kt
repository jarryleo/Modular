package cn.leo.frame.support

import android.view.View

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