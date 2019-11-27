package cn.leo.frame.network.annotations

/**
 * @author : ling luo
 * @date : 2019-11-27
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Observe(val kFunc: String, val success: Boolean)