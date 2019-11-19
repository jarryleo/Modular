package cn.leo.frame.network

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import cn.leo.frame.log.Logger
import cn.leo.frame.network.interceptor.CacheInterceptor
import cn.leo.frame.utils.ClassUtils
import kotlinx.coroutines.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KFunction

/**
 * @author : ling luo
 * @date : 2019-07-03
 */
@Suppress("UNUSED", "MemberVisibilityCanBePrivate")
abstract class MViewModel<T : Any> : ViewModel() {

    companion object {
        private val apiMap = ConcurrentHashMap<Class<*>, Any>()
    }

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    private val request by ViewModelHelper(api)

    fun <R : Any> request(obj: Any? = null, api: T.() -> MJob<R>) = api(request.apis<R>(obj))

    fun <R : Any> apis(obj: Any? = null) = request.apis<R>(obj)

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
    fun <R : Any> Deferred<R>.request(
        obj: Any? = null,
        liveData: MLiveData<R>
    ): Job {
        return scope.launch {
            try {
                val result = this@request.await()
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
    }

    fun <R : Any> executeRequest(
        deferred: Deferred<R>,
        liveData: MLiveData<R>,
        obj: Any? = null
    ): Job {
        return deferred.request(obj, liveData)
    }

    /**
     * 异步方法
     */
    fun <R : Any> async(
        block: suspend CoroutineScope.() -> R
    ): MJob<R> {
        //获取当前方法名称
        val methodName =
            Thread.currentThread().stackTrace.find {
                it.className == this::class.java.name
            }?.methodName
        Logger.d("methodName = $methodName")

        //异步协程
        val deferred = GlobalScope.async(
            context = Dispatchers.IO,
            start = CoroutineStart.LAZY
        ) {
            block()
        }
        //执行
        val job = executeRequest(deferred, request.getLiveData(methodName ?: ""))
        return MJob(job)
    }


    /**
     * 订阅异步方法回调
     * @param kFunction 参数写法 model::test
     */
    fun <R : Any> observe(
        lifecycleOwner: LifecycleOwner,
        kFunction: KFunction<MJob<R>>,
        result: (MLiveData.Result<R>).() -> Unit = {}
    ) {
        request.observe(lifecycleOwner, kFunction, result)
    }

}