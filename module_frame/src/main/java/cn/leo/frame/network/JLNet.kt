package cn.leo.frame.network


/**
 * @author : ling luo
 * @date : 2019-09-17
 */
object JLNet {
    val interceptors: LinkedHashSet<JLInterceptor> = LinkedHashSet()

    fun addInterceptor(interceptor: JLInterceptor) {
        interceptors.add(interceptor)
    }
}