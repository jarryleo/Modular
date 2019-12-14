package cn.leo.frame.network.model

import cn.leo.frame.network.http.MInterceptorManager
import cn.leo.frame.network.http.MJob
import kotlinx.coroutines.*
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * @author : ling luo
 * @date : 2019-12-12
 */
class ViewModelCoroutineHelper : ReadOnlyProperty<MViewModel<*>, ViewModelCoroutineHelper> {

    /**
     * 协程任务
     */
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    private lateinit var model: MViewModel<*>

    override fun getValue(
        thisRef: MViewModel<*>,
        property: KProperty<*>
    ): ViewModelCoroutineHelper {
        model = thisRef
        return this
    }

    /**
     * 协程执行网络请求，并把结果给上层的LiveData
     */
    fun <R> executeRequest(
        deferred: Deferred<R>,
        liveData: MLiveData<R>,
        obj: Any? = null
    ): Job = scope.launch {
        try {
            liveData.showLoading()
            val result = deferred.await()
            MInterceptorManager.interceptors.forEach {
                if (it.intercept(obj, result, liveData)) {
                    return@launch
                }
            }
            liveData.success(result, obj)
        } catch (e: Exception) {
            e.printStackTrace()
            liveData.failed(e, obj)
        }
    }

    /**
     * 异步方法 回调在主线程
     */
    fun <R> async(block: suspend CoroutineScope.() -> R): MJob<R> {
        val liveData = model.getLiveData<R>()
        //协程
        val deferred = scope.async { block() }
        //异步执行
        val job = scope.launch {
            try {
                liveData.showLoading()
                val value = deferred.await()
                if (value != null) {
                    liveData.success(value)
                } else {
                    liveData.failed(NullPointerException("null"))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.failed(e)
            }
        }
        return MJob(job)
    }


    /**
     * 释放资源
     */
    fun clear() {
        job.cancel()
    }
}