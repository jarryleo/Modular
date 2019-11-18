package cn.leo.frame.network


/**
 * @author : ling luo
 * @date : 2019-09-17
 */
object MNet {
    val interceptors: LinkedHashSet<MInterceptor> = LinkedHashSet()

    fun addInterceptor(interceptor: MInterceptor) {
        interceptors.add(interceptor)
    }
}