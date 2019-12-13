package cn.leo.base.support

import android.app.Activity
import android.os.SystemClock
import cn.leo.frame.utils.toast

/**
 * @author : ling luo
 * @date : 2019-12-13
 */

fun Activity.doubleClickExit(config: (DoubleClickExitConfig).() -> Unit = {}) {
    config(DoubleClickExitConfig())
    val time = SystemClock.elapsedRealtime()
    if (time - DoubleClickExitConfig.sLastClickTime > DoubleClickExitConfig.sInterval) {
        DoubleClickExitConfig.sLastClickTime = time
        toast(DoubleClickExitConfig.sMsg)
    } else {
        finish()
    }
}

class DoubleClickExitConfig {
    companion object {
        var sLastClickTime: Long = 0L
        var sInterval: Long = 2000L
        var sMsg: String = "再次按返回退出"
    }

    var interval
        get() = sInterval
        set(value) {
            sInterval = value
        }

    var msg
        get() = sMsg
        set(value) {
            sMsg = value
        }
}