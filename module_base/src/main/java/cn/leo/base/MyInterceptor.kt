package cn.leo.base

import android.os.Bundle
import cn.leo.base.bean.BaseBean
import cn.leo.frame.network.JLInterceptor
import cn.leo.frame.network.MLiveData

/**
 * @author : ling luo
 * @date : 2019-09-17
 */
class MyInterceptor : JLInterceptor {
    override fun <T : Any> intercept(bundle: Bundle?, data: T, liveData: MLiveData<T>): Boolean {
        if (data is BaseBean) {
            if (data.errcode != 0) {
                //liveData.failed(HttpTimeException())
            } else {
                liveData.success(data, bundle)
            }
            return true
        }
        return super.intercept(bundle, data, liveData)
    }
}