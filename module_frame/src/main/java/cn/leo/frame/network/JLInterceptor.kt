package cn.leo.frame.network

/**
 * @author : ling luo
 * @date : 2019-09-17
 */

interface JLInterceptor{
   fun <T:Any> intercept(data:T,liveData: JLLiveData<T>):Boolean{
       return false
   }
}