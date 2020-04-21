package cn.leo.frame.network.viewmodel

import androidx.lifecycle.viewModelScope
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
    ): Job = model.viewModelScope.launch(
        model.viewModelScope.coroutineContext + Dispatchers.IO
    ) {
        try {
            liveData.showLoading()
            val result = deferred.await()
            MInterceptorManager.interceptors.forEach {
                if (it.intercept(obj, result, liveData)) {
                    return@launch
                }
            }
            liveData.success(result, obj)
        } catch (e: CancellationException) {
            //取消请求
            e.printStackTrace()
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
        //协程包装(异步)
        val deferred = model.viewModelScope.async(
            model.viewModelScope.coroutineContext + Dispatchers.IO
        ) { block() }
        //执行结果
        val job = model.viewModelScope.launch {
            try {
                liveData.showLoading()
                val value = deferred.await()
                if (value != null) {
                    liveData.success(value)
                } else {
                    liveData.failed(NullPointerException("null"))
                }
            } catch (e: CancellationException) {
                //取消请求
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.failed(e)
            }
        }
        return MJob(job)
    }
}