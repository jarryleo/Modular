package cn.leo.base

import androidx.multidex.MultiDexApplication
import cn.leo.base.net.MyInterceptor
import cn.leo.frame.MFrame
import cn.leo.frame.network.MInterceptorManager
import com.alibaba.android.arouter.launcher.ARouter

/**
 * @author : ling luo
 * @date : 2019-07-03
 */
open class BaseApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        ARouter.init(this)
        MInterceptorManager.addInterceptor(MyInterceptor())
    }
}