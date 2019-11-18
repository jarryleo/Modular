package cn.leo.base

import cn.leo.base.bean.BaseBean
import cn.leo.frame.network.MInterceptor
import cn.leo.frame.network.MLiveData
import cn.leo.frame.network.exceptions.BusinessException
import cn.leo.frame.network.exceptions.FactoryException

/**
 * @author : ling luo
 * @date : 2019-09-17
 */
class MyInterceptor : MInterceptor {
    override fun <T : Any> intercept(obj: Any?, data: T, liveData: MLiveData<T>): Boolean {
        if (data is BaseBean) {
            if (data.errcode != 0) {
                liveData.failed(BusinessException(data.errcode))
            } else {
                liveData.success(data, obj)
            }
            return true
        }
        return super.intercept(obj, data, liveData)
    }
}