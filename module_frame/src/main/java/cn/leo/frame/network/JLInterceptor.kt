package cn.leo.frame.network

import android.os.Bundle

/**
 * @author : ling luo
 * @date : 2019-09-17
 */

interface JLInterceptor {
    /**
     * @return 返回true 拦截
     */
    fun <T : Any> intercept(bundle: Bundle? = null, data: T, liveData: MLiveData<T>): Boolean {
        return false
    }
}