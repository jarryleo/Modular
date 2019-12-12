package cn.leo.frame.network.http

import java.util.*
import kotlin.collections.ArrayList


/**
 * @author : ling luo
 * @date : 2019-09-17
 */
object MInterceptorManager {
    val interceptors: ArrayList<MInterceptor> = ArrayList()

    /**
     * 加载全局拦截器
     * @param priority 优先级，越大越优先
     */
    fun addInterceptor(interceptor: MInterceptor, priority: Int = 0) {
        interceptor.priority = priority
        interceptors.add(interceptor)
        interceptors.sortWith(Comparator { o1, o2 ->
            o2.priority - o1.priority
        })
    }
}