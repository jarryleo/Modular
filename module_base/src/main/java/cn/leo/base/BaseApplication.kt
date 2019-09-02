package cn.leo.base

import android.app.Application
import cn.leo.frame.network.JL

/**
 * @author : ling luo
 * @date : 2019-07-03
 */
class BaseApplication:Application() {
    override fun onCreate() {
        super.onCreate()
        JL.init(this)
    }
}