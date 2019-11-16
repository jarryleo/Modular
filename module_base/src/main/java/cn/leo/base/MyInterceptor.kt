package cn.leo.base

import cn.leo.base.bean.BaseBean
import cn.leo.frame.network.JLInterceptor
import cn.leo.frame.network.MLiveData
import cn.leo.frame.network.exceptions.HttpTimeException

/**
 * @author : ling luo
 * @date : 2019-09-17
 */
class MyInterceptor : JLInterceptor {
    override fun <T : Any> intercept(data: T, liveData: MLiveData<T>): Boolean {
        if (data is BaseBean) {
            if (data.errcode != 0) {
                //liveData.failed(HttpTimeException())
            } else {
                liveData.success(data)
            }
            return true
        }
        return super.intercept(data, liveData)
    }
}