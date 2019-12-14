package cn.leo.frame.network.model

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import cn.leo.frame.log.logD
import cn.leo.frame.network.http.HttpLoader
import cn.leo.frame.network.http.MJob
import cn.leo.frame.network.http.OkHttp3Builder
import cn.leo.frame.network.interceptor.CacheInterceptor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlin.reflect.KFunction

/**
 * @author : ling luo
 * @date : 2019-07-03
 *
 * ViewModel 生命周期比 Activity 长，不应该持有任何view引用或者包含view的引用，
 */
@Suppress("UNUSED", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate")
abstract class MViewModel<T : Any> : ViewModel() {

    /**
     * 网络请求帮助类
     */
    private val mApiHelper by ViewModelApiHelper<T>()
    /**
     * liveData帮助类
     */
    private val mLiveDataHelper by lazy { LiveDataHelper() }
    /**
     * 协程帮助类
     */
    private val mCoroutineHelper by ViewModelCoroutineHelper()

    /**
     * 非代理网络请求
     */
    val api = mApiHelper.api

    /**
     * 网络请求代理
     */
    fun apis(obj: Any? = null) =
        mApiHelper.apis<Any>(obj)


    /**
     * 获取接口基础地址
     */
    abstract fun getBaseUrl(): String

    /**
     * 获取http请求加载器retrofit
     */
    open fun getHttpLoader(): HttpLoader {
        return HttpLoader.Builder(getBaseUrl())
            .client(
                OkHttp3Builder()
                    .addInterceptor(CacheInterceptor())
                    .build()
            )
            .build()
    }


    /**
     * 释放资源
     */
    override fun onCleared() {
        mApiHelper.clear()
        mLiveDataHelper.clear()
        mCoroutineHelper.clear()
        logD("${javaClass.name} onCleared()")
    }

    /**
     * 协程执行网络请求，并把结果给上层的LiveData
     */
    fun <R> executeRequest(
        deferred: Deferred<R>,
        liveData: MLiveData<R>,
        obj: Any? = null
    ): Job = mCoroutineHelper.executeRequest(deferred, liveData, obj)

    /**
     * 异步方法 回调在主线程
     */
    fun <R> async(block: suspend CoroutineScope.() -> R): MJob<R> = mCoroutineHelper.async(block)

    /**
     * 协程延迟任务
     */
    @Throws(Exception::class)
    suspend fun <R : Any> MJob<R>.await(): R? {
        (this.job as? Deferred<R>)?.let {
            return it.await()
        }
        return null
    }

    /**
     * 获取liveData
     */
    fun <R> getLiveData(key: String? = null): MLiveData<R> {
        //获取当前方法名称
        val methodName = key ?: Thread.currentThread().stackTrace.find {
            it.className == this::class.java.name
        }?.methodName ?: "no-name"
        logD("methodName = $methodName")
        val names = key ?: Thread.currentThread().stackTrace.map { it.methodName }
        logD("methodNames = $names")
        return mLiveDataHelper.getLiveData(methodName)
    }

    /**
     * 订阅方法回调(本地方法和网络请求)
     * @param kFunction 参数写法 model::test
     */
    fun <R> observe(
        lifecycleOwner: LifecycleOwner,
        kFunction: KFunction<MJob<R>>,
        result: (MLiveData.Result<R>).() -> Unit = {}
    ) = getLiveData<R>(kFunction.name).observe(lifecycleOwner, result)

    /**
     * 订阅方法，正确结果直接传递
     * @param viewModelSupport 写法 model::setTitle + ::setActionBarTitle
     * 前面为订阅方法，后面为回调方法
     * 前面的方法必须是本model的方法，后面的方法参数必须是前面方法的返回值
     */
    fun <R> observe(
        lifecycleOwner: LifecycleOwner,
        viewModelSupport: ViewModelSupport<R>
    ) {
        observe(lifecycleOwner, viewModelSupport.modelFuc) {
            success { viewModelSupport.obFunc(it) }
        }
    }

    /**
     * 无生命周期的监听，谨慎使用，防止泄露
     * @param kFunction 参数写法 Api::test
     */
    fun <R> observeForever(
        kFunction: KFunction<MJob<R>>,
        result: (MLiveData.Result<R>).() -> Unit = {}
    ) = getLiveData<R>(kFunction.name).observeForever(result)

}