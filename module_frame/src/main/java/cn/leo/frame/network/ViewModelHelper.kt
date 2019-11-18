package cn.leo.frame.network

import androidx.lifecycle.LifecycleOwner
import cn.leo.frame.log.Logger
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import java.lang.reflect.Proxy
import java.util.concurrent.ConcurrentHashMap
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * @author : ling luo
 * @date : 2019-11-15
 */
@Suppress("UNUSED", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate")
class ViewModelHelper<T : Any>(val apis: T) :
    ReadOnlyProperty<MViewModel<*>, ViewModelHelper<T>> {

    val mLiveDataCache = ConcurrentHashMap<String, MLiveData<*>>()

    lateinit var model: MViewModel<*>

    override fun getValue(thisRef: MViewModel<*>, property: KProperty<*>): ViewModelHelper<T> {
        model = thisRef
        return this
    }

    /**
     * 发起请求
     */
    inline fun <reified R : Any> request(
        deferred: Deferred<R>,
        obj: Any? = null,
        flag: String = ""
    ): Job {
        return model.executeRequest(deferred, getLiveData(flag), obj)
    }

    /**
     * 建议使用此方法实现网络请求
     * @param obj 请求携带附加数据，会在回调监听里原样返回，适合list条目数据变化，记录条目位置等
     * @param flag 多个请求返回相同类型对象的时候附加 标记，区分是哪个请求
     *
     */
    inline fun <reified R : Any> apis(
        obj: Any? = null,
        flag: String = "",
        api: T.() -> Deferred<R>
    ): Job {
        return request(api(apis), obj, flag)
    }


    /**
     * 性能较低，不建议使用
     */
    inline fun <reified R : Any> apis(obj: Any? = null, flag: String = ""): T {
        return Proxy.newProxyInstance(
            javaClass.classLoader,
            arrayOf(*apis.javaClass.interfaces)
        ) { _, method, args ->
            val deferred = method.invoke(apis, *args) as Deferred<R>
            request(deferred, obj, flag)
            return@newProxyInstance deferred
        } as T
    }

    /**
     * 监听请求回调
     * @param flag 多个请求返回相同类型对象的时候附加 标记，区分是哪个请求
     */
    inline fun <reified R : Any> observe(
        lifecycleOwner: LifecycleOwner,
        flag: String = "",
        noinline result: (MLiveData.Result<R>).() -> Unit = {}
    ) {
        getLiveData<R>(flag).observe(lifecycleOwner, result)
    }

    /**
     * 无生命周期的监听，谨慎使用，防止泄露
     */
    inline fun <reified R : Any> observeForever(
        flag: String = "",
        noinline result: (MLiveData.Result<R>).() -> Unit = {}
    ) {
        getLiveData<R>(flag).observeForever(result)
    }

    /**
     * 获取LiveData
     */
    inline fun <reified R : Any> getLiveData(flag: String = ""): MLiveData<R> {
        val key = R::class.java.name + flag
        Logger.d("LiveData key  = $key")
        return if (mLiveDataCache.containsKey(key)) {
            mLiveDataCache[key] as MLiveData<R>
        } else {
            val liveData = MLiveData<R>()
            mLiveDataCache[key] = liveData
            liveData
        }
    }
}