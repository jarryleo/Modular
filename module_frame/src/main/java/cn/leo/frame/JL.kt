package cn.leo.frame.network

import android.app.Application
import cn.leo.frame.log.Logger

/**
 * @author : ling luo
 * @date : 2019-07-03
 */
object JL {
    lateinit var context:Application
    fun init(application: Application){
        context = application
        Logger.init(application)
    }
}