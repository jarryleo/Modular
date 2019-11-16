package cn.leo.frame.network

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import cn.leo.frame.network.interceptor.CacheInterceptor
import cn.leo.frame.utils.ClassUtils
import kotlinx.coroutines.*
import java.util.concurrent.ConcurrentHashMap

/**
 * @author : ling luo
 * @date : 2019-07-03
 */
@Suppress("UNUSED", "MemberVisibilityCanBePrivate")
abstract class MModel<T : Any> : ViewModel() {

    companion object {
        private val apiMap = ConcurrentHashMap<Class<*>, Any>()
    }

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    lateinit var mLifecycleOwner: LifecycleOwner

    val request by ViewModelHelper(api)

    @Suppress("UNCHECKED_CAST")
    private val api: T
        get() {
            val clazz = ClassUtils.getSuperClassGenericType<T>(javaClass)
            return if (apiMap.containsKey(clazz)) {
                apiMap[clazz] as T
            } else {
                val create =
                    getHttpLoader().create(
                        ClassUtils.getSuperClassGenericType<T>(javaClass)
                    )
                apiMap[clazz] = create as Any
                create
            }
        }

    abstract fun getBaseUrl(): String

    open fun getHttpLoader(): HttpLoader {
        return HttpLoader.Builder(getBaseUrl())
            .client(
                OkHttp3Builder()
                    .addInterceptor(CacheInterceptor())
                    .build()
            )
            .build()
    }


    override fun onCleared() {
        job.cancel()
    }


    /**
     * 协程执行网络请求，并把结果给上层的LiveData
     */
    protected fun <R : Any> Deferred<R>.request(
        mLiveData: MLiveData<R>
    ): Job {
        return scope.launch {
            try {
                val result = this@request.await()
                JLNet.interceptors.forEach {
                    if (it.intercept(result, mLiveData)) {
                        return@launch
                    }
                }
                mLiveData.success(result)
            } catch (e: Exception) {
                e.printStackTrace()
                mLiveData.failed(e)
            }
        }
    }

    fun <R : Any> executeRequest(deferred: Deferred<R>, liveData: MLiveData<R>): Job {
        return deferred.request(liveData)
    }

}