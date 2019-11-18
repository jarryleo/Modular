package cn.leo.frame.network

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

    inline fun <reified R : Any> request(
        obj: Any? = null,
        flag: String = "",
        deferred: Deferred<R>
    ): Job {
        return model.executeRequest(obj, deferred, getLiveData(flag))
    }

    inline fun <reified R : Any> apis(
        obj: Any? = null,
        flag: String = "",
        api: T.() -> Deferred<R>
    ): Job {
        return request(obj, flag, api(apis))
    }

    inline fun <reified R : Any> apis(obj: Any? = null, flag: String = ""): T {
        return Proxy.newProxyInstance(
            javaClass.classLoader,
            arrayOf(*apis.javaClass.interfaces)
        ) { _, method, args ->
            val deferred = method.invoke(apis, *args) as Deferred<R>
            request(obj, flag, deferred)
            return@newProxyInstance deferred
        } as T
    }

    inline fun <reified R : Any> observe(
        flag: String = "",
        noinline result: (MLiveData.Result<R>).() -> Unit = {}
    ) {
        getLiveData<R>(flag).observe(result)
    }

    inline fun <reified R : Any> observeForever(
        flag: String = "",
        noinline result: (MLiveData.Result<R>).() -> Unit = {}
    ) {
        getLiveData<R>(flag).observeForever(result)
    }

    inline fun <reified R : Any> getLiveData(flag: String = ""): MLiveData<R> {
        val key = R::class.java.name + flag
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