package cn.leo.frame.network

import kotlin.reflect.KFunction

/**
 * @author : ling luo
 * @date : 2019-11-26
 */

infix fun <R : Any> KFunction<MJob<R>>.observe(result: (MLiveData.Result<R>).() -> Unit) {

}