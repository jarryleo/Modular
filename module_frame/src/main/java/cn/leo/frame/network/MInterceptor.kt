package cn.leo.frame.network

/**
 * @author : ling luo
 * @date : 2019-09-17
 */

open class MInterceptor {
    var priority: Int = 0
    /**
     * @return 返回true 拦截
     */
    open fun <T> intercept(obj: Any? = null, data: T, liveData: MLiveData<T>): Boolean {
        return false
    }
}