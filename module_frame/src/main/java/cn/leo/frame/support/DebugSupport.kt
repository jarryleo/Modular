package cn.leo.frame.support

import cn.leo.frame.BuildConfig


/**
 * @author : ling luo
 * @date : 2019-12-07
 */

/**
 * 测试代码段，只有在debug下会执行
 */
inline fun Any.debug(block: () -> Unit) {
    if (BuildConfig.DEBUG) {
        block()
    }
}

/**
 * 测量代码块运行时间
 */
inline fun measureTimeMillis(block: () -> Unit): Long {
    val start = System.currentTimeMillis()
    block()
    return System.currentTimeMillis() - start
}

