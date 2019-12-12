package cn.leo.frame.network.model

import cn.leo.frame.network.http.MJob
import cn.leo.frame.utils.ClassUtils
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Proxy
import java.util.concurrent.ConcurrentHashMap
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * @author : ling luo
 * @date : 2019-11-15
 */
@Suppress("UNUSED", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate")
class ViewModelApiHelper<T : Any> :
    ReadOnlyProperty<MViewModel<*>, ViewModelApiHelper<T>> {

    companion object {
        private val apiMap = ConcurrentHashMap<Class<*>, Any>()
    }

    /**
     * viewModel
     */
    private lateinit var model: MViewModel<*>


    //请求代理相关
    private var mApiProxy: T? = null
    private var mApiHandler: InvocationHandler? = null
    //请求携带的额外数据
    @Volatile
    private var mObj: Any? = null

    override fun getValue(thisRef: MViewModel<*>, property: KProperty<*>): ViewModelApiHelper<T> {
        model = thisRef
        return this
    }

    /**
     * 网络接口实例化和缓存
     */
    private val api: T
        get() {
            val clazz = ClassUtils.getSuperClassGenericType<T>(model.javaClass)
            return if (apiMap.containsKey(clazz)) {
                apiMap[clazz] as T
            } else {
                val create =
                    model.getHttpLoader().create(
                        ClassUtils.getSuperClassGenericType<T>(model.javaClass)
                    )
                apiMap[clazz] = create as Any
                create
            }
        }

    /**
     * 发起请求
     */
    private fun <R : Any> request(
        deferred: Deferred<R>,
        obj: Any? = null,
        showLoading: Boolean = false,
        key: String = ""
    ): Job = model.executeRequest(deferred, model.getLiveData(key), obj, showLoading)

    /**
     * 请求接口
     * @param obj 请求携带附加数据，会在回调监听里原样返回，适合list条目数据变化，记录条目位置等
     */
    inline fun <reified R : Any> apis(
        obj: Any? = null,
        api: T.() -> MJob<R>
    ) = api(apis<R>(obj))

    /**
     * 代理请求接口
     */
    fun <R : Any> apis(obj: Any?, showLoading: Boolean = false): T {
        mObj = obj
        mApiHandler = mApiHandler ?: InvocationHandler { _, method, args ->
            val mJob = method.invoke(api, *args ?: arrayOf()) as MJob<R>
            val deferred = mJob.job as Deferred<R>
            MJob<R>(
                request(
                    deferred,
                    mObj,
                    showLoading,
                    method.name
                )
            )
        }
        mApiProxy = mApiProxy ?: Proxy.newProxyInstance(
            javaClass.classLoader,
            arrayOf(*api.javaClass.interfaces),
            mApiHandler
        ) as T
        return mApiProxy!!
    }

    /**
     * 释放资源
     */
    fun clear() {
        mApiProxy = null
        mApiHandler = null
        mObj = null
    }


}