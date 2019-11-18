package cn.leo.base

import androidx.multidex.MultiDexApplication
import cn.leo.base.db.helper.DBHelper
import cn.leo.frame.network.JL
import cn.leo.frame.network.MNet

/**
 * @author : ling luo
 * @date : 2019-07-03
 */
open class BaseApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        JL.init(this)
        DBHelper.initGreenDao(this)
        MNet.addInterceptor(MyInterceptor())
    }
}