package cn.leo.frame.network

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.ViewModel
import cn.leo.frame.network.exceptions.ApiException
import cn.leo.frame.network.exceptions.FactoryException
import cn.leo.frame.network.interceptor.CacheInterceptor
import kotlinx.coroutines.*
import java.lang.reflect.ParameterizedType
import java.util.concurrent.ConcurrentHashMap

/**
 * @author : ling luo
 * @date : 2019-07-03
 */
abstract class JLModel<T> : ViewModel() {

    companion object {
        private val apiMap = ConcurrentHashMap<Class<*>, Any>()
    }

    var modelLifeOwner: LifecycleOwner? = null

    @Suppress("UNCHECKED_CAST")
    protected val api: T
        get() {
            val clazz = getSuperClassGenericType(javaClass)
            return if (apiMap.containsKey(clazz)) {
                apiMap[clazz] as T
            } else {
                val create = getHttpLoader().create(getSuperClassGenericType(javaClass))
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

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)

    /**
     * 协程执行网络请求，获取结果
     */
    protected fun <R> Deferred<R>.request(
        error: (e: ApiException) -> Unit = {},
        result: (R) -> Unit = {}
    ): Job {
        return scope.launch {
            try {
                result(this@request.await())
            } catch (e: Exception) {
                e.printStackTrace()
                error(FactoryException.analysisException(e))
            }
        }
    }


    /**
     * 协程执行网络请求，并把结果给上层的LiveData
     * @param isFailed 根据上层的返回值判断是否是失败的请求
     */
    protected fun <R : Any> Deferred<R>.request(
        jlLiveData: JLLiveData<R>,
        isFailed: (R) -> Boolean = { false }
    ): Job {
        jlLiveData.liveDataLifecycleOwner = modelLifeOwner
        return scope.launch {
            try {
                val result = this@request.await()
                JLNet.interceptors.forEach {
                    if (it.intercept(result, jlLiveData)) {
                        return@launch
                    }
                }
                if (isFailed(result)) {
                    jlLiveData.failed(result)
                } else {
                    jlLiveData.success(result)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                jlLiveData.error(FactoryException.analysisException(e))
            }
        }
    }


    /**
     * 直接返回JLLiveData 的请求
     */
    protected fun <R : Any> Deferred<R>.request(): JLLiveData<R> {
        val liveData = JLLiveData<R>()
        liveData.liveDataLifecycleOwner = modelLifeOwner
        scope.launch {
            try {
                val result = this@request.await()
                JLNet.interceptors.forEach {
                    if (it.intercept(result, liveData)) {
                        return@launch
                    }
                }
                liveData.success(result)
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.error(FactoryException.analysisException(e))
            }
        }
        return liveData
    }

    /**
     * 通过反射,获得定义Class时声明的父类的范型参数的类型.
     * 如public BookManager extends GenericManager<Book>
     *
     * @param clazz The class to introspect
     * @return the first generic declaration,
     * or <code>Object.class</code> if cannot be determine
     */
    private fun getSuperClassGenericType(clazz: Class<*>): Class<T> {
        return getSuperClassGenericType(clazz, 0)
    }

    /**
     * 通过反射,获得定义Class时声明的父类的范型参数的类型.
     * 如public BookManager extends GenericManager<Book>
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.</Book>
     */
    private fun getSuperClassGenericType(clazz: Class<*>, index: Int): Class<T> {
        var cls = clazz
        var genType = cls.genericSuperclass
        while (genType !is ParameterizedType) {
            cls = cls.superclass
            if (cls == null) {
                throw IllegalArgumentException()
            }
            genType = cls.genericSuperclass
        }
        val params = genType.actualTypeArguments
        if (index >= params.size || index < 0) {
            throw IllegalArgumentException()
        }
        return if (params[index] !is Class<*>) {
            throw IllegalArgumentException()
        } else params[index] as Class<T>
    }
}