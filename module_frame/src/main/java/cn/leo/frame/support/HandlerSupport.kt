package cn.leo.frame.support

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import cn.leo.frame.utils.SafetyMainHandler
import kotlin.math.abs

/**
 * @author : ling luo
 * @date : 2019-11-29
 */

/**
 * 计时器 生命周期安全的计时器(可选界面不可见的时候不触发事件)
 * @param times 计时次数
 * @param start 开始值
 * @param end 结束值
 * @param step 每次累计数值
 * @param interval 计时间隔
 * @param invisibleCount 界面不可见的时候是否继续执行触发事件，false 不执行，true，执行
 * @param onStart 开始计时触发事件
 * @param onComplete 结束计时触发事件
 * @param onCount 每次计时触发事件
 */
inline fun LifecycleOwner.count(
    times: Int = 5,
    start: Int = times,
    end: Int = 0,
    step: Int = 1,
    interval: Long = 1000L,
    invisibleCount: Boolean = false,
    crossinline onStart: (Int) -> Unit = {},
    crossinline onComplete: (Int) -> Unit = {},
    crossinline onCount: (Int) -> Unit
) {
    var count = start
    val handler = SafetyMainHandler(this)
    val liveData = MutableLiveData<Int>().apply {
        observe(this@count, Observer {
            onCount(it)
        })
    }
    val runnable = object : Runnable {
        override fun run() {
            if (count < end) count += step else count -= step
            if (abs(count - end) > step) {
                if (invisibleCount) {
                    onCount(count)
                } else {
                    liveData.postValue(count)
                }
                handler.postDelayed(this, interval)
            } else {
                onComplete(end)
            }
        }
    }
    onStart(start)
    liveData.postValue(count)
    handler.removeCallbacks(runnable)
    handler.postDelayed(runnable, interval)
}


/**
 * 无限循环事件触发器 生命周期安全
 * @param interval 循环间隔
 * @param action 循环执行事件
 */
inline fun LifecycleOwner.repeat(
    interval: Long = 1000L,
    crossinline action: () -> Unit
) {
    val handler = SafetyMainHandler(this)
    val runnable = object : Runnable {
        override fun run() {
            action()
            handler.postDelayed(this, interval)
        }
    }
    handler.postDelayed(runnable, interval)
}