package cn.leo.base

import androidx.multidex.MultiDexApplication
import cn.leo.base.db.helper.DBHelper
import cn.leo.frame.MFrame
import cn.leo.frame.network.MInterceptorManager

/**
 * @author : ling luo
 * @date : 2019-07-03
 */
open class BaseApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        MFrame.init(this)
        DBHelper.initGreenDao(this)
        MInterceptorManager.addInterceptor(MyInterceptor())
    }
}