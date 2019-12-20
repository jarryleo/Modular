package cn.leo.base.utils

import android.os.SystemClock

/**
 * @author : ling luo
 * @date : 2019-12-19
 * 服务器时间工具类
 */

object ServiceTime {
    private var serviceTimeDiff: Long = 0L

    val time get() = SystemClock.elapsedRealtime() + serviceTimeDiff

    /**
     * 刷新服务器时间
     */
    fun refreshServiceTimes(times: Long) {
        serviceTimeDiff = times - SystemClock.elapsedRealtime()
    }

}