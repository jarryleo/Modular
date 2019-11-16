package cn.leo.frame.network

import cn.leo.frame.network.exceptions.ApiException
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
    ReadOnlyProperty<MModel<*>, ViewModelHelper<T>> {

    val mLiveDataCache = ConcurrentHashMap<String, MLiveData<*>>()

    lateinit var model: MModel<*>

    override fun getValue(thisRef: MModel<*>, property: KProperty<*>): ViewModelHelper<T> {
        model = thisRef
        return this
    }

    inline fun <reified R : Any> request(deferred: Deferred<R>): Job {
        return model.executeRequest(deferred, getLiveData())
    }

    inline fun <reified R : Any> apis(api: T.() -> Deferred<R>): Job {
        return request(api(apis))
    }

    inline fun <reified R : Any> apis(): T {
        return Proxy.newProxyInstance(
            javaClass.classLoader,
            arrayOf(*apis.javaClass.interfaces)
        ) { _, method, args ->
            val deferred = method.invoke(apis, *args) as Deferred<R>
            request(deferred)
            return@newProxyInstance deferred
        } as T
    }

    inline fun <reified R : Any> observe(
        noinline failed: (e: ApiException) -> Unit = {},
        noinline success: (value: R) -> Unit = {}
    ) {
        getLiveData<R>().observe(failed, success)
    }

    inline fun <reified R : Any> observeForever(
        noinline failed: (e: ApiException) -> Unit = {},
        noinline success: (value: R) -> Unit = {}
    ) {
        getLiveData<R>().observeForever(failed, success)
    }

    inline fun <reified R : Any> getLiveData(): MLiveData<R> {
        val key = R::class.java.name
        return if (mLiveDataCache.containsKey(key)) {
            mLiveDataCache[key] as MLiveData<R>
        } else {
            val liveData = MLiveData<R>()
            liveData.mLifecycleOwner = model.mLifecycleOwner
            mLiveDataCache[key] = liveData
            liveData
        }
    }
}