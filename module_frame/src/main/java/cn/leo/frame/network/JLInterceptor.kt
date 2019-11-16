package cn.leo.frame.network

/**
 * @author : ling luo
 * @date : 2019-09-17
 */

interface JLInterceptor {
    /**
     * @return 返回true 拦截
     */
    fun <T : Any> intercept(data: T, liveData: MLiveData<T>): Boolean {
        return false
    }
}