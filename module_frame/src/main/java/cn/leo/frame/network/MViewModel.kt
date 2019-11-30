package cn.leo.frame.network

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import cn.leo.frame.log.Logger
import cn.leo.frame.network.interceptor.CacheInterceptor
import cn.leo.frame.ui.ILoading
import cn.leo.frame.utils.ClassUtils
import kotlinx.coroutines.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KFunction

/**
 * @author : ling luo
 * @date : 2019-07-03
 */
@Suppress("UNUSED", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate")
abstract class MViewModel<T : Any> : ViewModel() {

    companion object {
        private val apiMap = ConcurrentHashMap<Class<*>, Any>()
    }

    var lifecycleOwner: LifecycleOwner? = null
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    private val request by ViewModelHelper(api)

    fun apis(obj: Any? = null, showLoading: Boolean = false) = request.apis<Any>(obj, showLoading)

    protected val api: T
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
        request.clear()
        lifecycleOwner = null
    }


    /**
     * 协程执行网络请求，并把结果给上层的LiveData
     */
    fun <R> Deferred<R>.request(
        obj: Any? = null,
        liveData: MLiveData<R>,
        showLoading: Boolean = false
    ): Job {
        return scope.launch {
            try {
                if (showLoading) {
                    launch(Dispatchers.Main) { onRequestStart() }
                }
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
            } finally {
                if (showLoading) {
                    launch(Dispatchers.Main) { onRequestEnd() }
                }
            }
        }
    }

    @Throws(Exception::class)
    protected suspend fun <R : Any> MJob<R>.await(): R? {
        (this.job as? Deferred<R>)?.let {
            return it.await()
        }
        return null
    }

    fun <R> executeRequest(
        deferred: Deferred<R>,
        liveData: MLiveData<R>,
        obj: Any? = null,
        showLoading: Boolean = false
    ): Job {
        return deferred.request(obj, liveData, showLoading)
    }

    /**
     * 异步方法 回调在主线程
     */
    fun <R> async(block: suspend CoroutineScope.() -> R): MJob<R> {
        val liveData = getLiveData<R>()
        //协程
        val deferred = scope.async { block() }
        //异步执行
        val job = scope.launch {
            try {
                liveData.success(deferred.await())
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.failed(e)
            }
        }
        return MJob(job)
    }

    /**
     * 主线程方法 回调在主线程
     */
    fun <R> sync(block: suspend CoroutineScope.() -> R): MJob<R> {
        val liveData = getLiveData<R>()
        //协程
        val deferred = scope.async { block() }
        //主线程执行
        val job = scope.launch(context = Dispatchers.Main) {
            try {
                liveData.setSuccess(deferred.await())
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.failed(e)
            }
        }
        return MJob(job)
    }

    private fun <R> getLiveData(): MLiveData<R> {
        //获取当前方法名称
        val methodName =
            Thread.currentThread().stackTrace.find {
                it.className == this::class.java.name
            }?.methodName ?: ""
        Logger.d("methodName = $methodName")
        return request.getLiveData(methodName)
    }

    /**
     * 订阅方法回调(本地方法和网络请求)
     * @param kFunction 参数写法 model::test
     */
    fun <R> observe(
        kFunction: KFunction<MJob<R>>,
        result: (MLiveData.Result<R>).() -> Unit = {}
    ) {
        lifecycleOwner?.let {
            request.observe(it, kFunction.name, result)
        }
    }


    /**
     * 请求开始
     */
    protected fun onRequestStart() {
        (lifecycleOwner as? ILoading)?.showLoading()
    }

    /**
     * 请求结束
     */
    protected fun onRequestEnd() {
        (lifecycleOwner as? ILoading)?.dismissLoading()
    }

}