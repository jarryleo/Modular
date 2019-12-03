package cn.leo.frame

import android.content.Context
import cn.leo.frame.log.Logger

/**
 * @author : ling luo
 * @date : 2019-07-03
 */
object MFrame {
    lateinit var context: Context
    fun init(c: Context) {
        context = c
        Logger.init(context)
    }
}