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

    inline fun <reified R : Any> request(
        deferred: Deferred<R>,
        obj: Any? = null,
        flag: String = ""
    ): Job {
        return model.executeRequest(deferred, getLiveData(flag), obj)
    }

    inline fun <reified R : Any> apis(
        obj: Any? = null,
        flag: String = "",
        api: T.() -> Deferred<R>
    ): Job {
        return request(api(apis), obj, flag)
    }

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

    inline fun <reified R : Any> observe(
        lifecycleOwner: LifecycleOwner,
        flag: String = "",
        noinline result: (MLiveData.Result<R>).() -> Unit = {}
    ) {
        getLiveData<R>(flag).observe(lifecycleOwner, result)
    }

    inline fun <reified R : Any> observeForever(
        flag: String = "",
        noinline result: (MLiveData.Result<R>).() -> Unit = {}
    ) {
        getLiveData<R>(flag).observeForever(result)
    }

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