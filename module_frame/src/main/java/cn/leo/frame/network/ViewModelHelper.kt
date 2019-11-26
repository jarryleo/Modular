package cn.leo.frame.network

import androidx.lifecycle.LifecycleOwner
import cn.leo.frame.log.Logger
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Proxy
import java.util.concurrent.ConcurrentHashMap
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty

/**
 * @author : ling luo
 * @date : 2019-11-15
 */
@Suppress("UNUSED", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate")
class ViewModelHelper<T : Any>(private val apis: T) :
    ReadOnlyProperty<MViewModel<*>, ViewModelHelper<T>> {

    private val mLiveDataCache = ConcurrentHashMap<String, MLiveData<*>>()

    private lateinit var model: MViewModel<*>

    //请求代理相关
    private var mApiProxy: T? = null
    private var mApiHandler: InvocationHandler? = null
    @Volatile
    private var mObj: Any? = null

    override fun getValue(thisRef: MViewModel<*>, property: KProperty<*>): ViewModelHelper<T> {
        model = thisRef
        return this
    }

    /**
     * 发起请求
     */
    private fun <R : Any> request(
        deferred: Deferred<R>,
        obj: Any? = null,
        key: String = ""
    ): Job {
        return model.executeRequest(deferred, getLiveData(key), obj)
    }

    /**
     * 请求接口
     * @param obj 请求携带附加数据，会在回调监听里原样返回，适合list条目数据变化，记录条目位置等
     *
     */
    inline fun <reified R : Any> apis(
        obj: Any? = null,
        api: T.() -> MJob<R>
    ) = api(apis<R>(obj))

    /**
     * 代理请求接口
     */
    fun <R : Any> apis(obj: Any?): T {
        mObj = obj
        mApiHandler = mApiHandler ?: InvocationHandler { _, method, args ->
            val mJob = method.invoke(apis, *args ?: arrayOf()) as MJob<R>
            val deferred = mJob.job as Deferred<R>
            MJob<R>(request(deferred, mObj, method.name))
        }
        mApiProxy = mApiProxy ?: Proxy.newProxyInstance(
            javaClass.classLoader,
            arrayOf(*apis.javaClass.interfaces),
            mApiHandler
        ) as T
        return mApiProxy!!
    }

    /**
     * 监听请求回调
     * @param kFunction 参数写法 Api::test
     */
    fun <R : Any> observe(
        lifecycleOwner: LifecycleOwner,
        kFunction: KFunction<MJob<R>>,
        result: (MLiveData.Result<R>).() -> Unit = {}
    ) {
        getLiveData<R>(kFunction.name).observe(lifecycleOwner, result)
    }

    /**
     * 无生命周期的监听，谨慎使用，防止泄露
     * @param kFunction 参数写法 Api::test
     */
    fun <R : Any> observeForever(
        kFunction: KFunction<MJob<R>>,
        result: (MLiveData.Result<R>).() -> Unit = {}
    ) {
        getLiveData<R>(kFunction.name).observeForever(result)
    }

    /**
     * 获取LiveData
     */
    fun <R : Any> getLiveData(key: String): MLiveData<R> {
        Logger.d("LiveData key  = $key")
        return if (mLiveDataCache.containsKey(key)) {
            mLiveDataCache[key] as MLiveData<R>
        } else {
            val liveData = MLiveData<R>()
            mLiveDataCache[key] = liveData
            liveData
        }
    }

    /**
     * 释放资源
     */
    fun clear(){
        mLiveDataCache.clear()
    }


}