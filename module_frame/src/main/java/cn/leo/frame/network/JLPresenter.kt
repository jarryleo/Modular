package cn.leo.frame.network

import android.arch.lifecycle.*
import android.os.Looper
import java.util.concurrent.ConcurrentHashMap

/**
 * @author : ling luo
 * @date : 2019-07-03
 */
open class JLPresenter<V : LifecycleOwner>(protected val mView: V) : LifecycleObserver {
    init {
        mView.lifecycle.addObserver(this)
    }

    val modelCache = ConcurrentHashMap<Class<*>, JLModel<*>>()
    /**
     * 获取ViewModel
     */
    inline fun <reified T : JLModel<*>> model(): T {
        return if (modelCache.containsKey(T::class.java)) {
            modelCache[T::class.java] as T
        } else {
            ViewModelProvider.NewInstanceFactory().create(T::class.java).apply {
                modelCache[T::class.java] = this
            }
        }.apply {
            this.modelLifeOwner = getView
        }
    }


    /**
     * 空闲时候加载的方法，可以用来初始化或者请求数据
     */
    protected open fun lazyInitOnce() {}

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    open fun onAny(owner: LifecycleOwner, event: Lifecycle.Event) {
        //子类继承此方法可以拿到详细的生命周期
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    open fun onCreate() {
        Looper.myQueue().addIdleHandler {
            lazyInitOnce()
            false
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    open fun onStart() {
        //子类可继承此方法进行请求数据
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    open fun onResume() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    open fun onPause() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    open fun onStop() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    open fun onDestroy() {
        modelCache.clear()
    }

    @PublishedApi
    internal val getView: V
        get() = mView
}