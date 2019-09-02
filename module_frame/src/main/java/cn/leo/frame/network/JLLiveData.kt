package cn.leo.frame.network

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import cn.leo.frame.network.exceptions.ApiException

/**
 * @author : ling luo
 * @date : 2019-07-03
 */
open class JLLiveData<T> : MutableLiveData<JLLiveData.Wrapper<T>>() {


    open fun success(value: T) {
        super.postValue(Wrapper.Success(value))
    }

    open fun failed(value: T) {
        super.postValue(Wrapper.Failed(value))
    }

    open fun error(e: ApiException) {
        super.postValue(Wrapper.Error(e))
    }

    fun observe(
        owner: LifecycleOwner,
        error: (e: ApiException?) -> Unit = {},
        failed: (value: T?) -> Unit = {},
        success: (value: T?) -> Unit = {}
    ) {
        super.observe(owner, Observer<Wrapper<T>> {
            when (it) {
                is Wrapper.Error -> error(it.exception)
                is Wrapper.Failed -> failed(it.data)
                is Wrapper.Success -> success(it.data)
            }
        })
    }

    fun observeForever(
        error: (e: ApiException?) -> Unit = {},
        failed: (value: T?) -> Unit = {},
        success: (value: T?) -> Unit = {}
    ) {
        super.observeForever(Observer<Wrapper<T>> {
            when (it) {
                is Wrapper.Error -> error(it.exception)
                is Wrapper.Failed -> failed(it.data)
                is Wrapper.Success -> success(it.data)
            }
        })
    }

    sealed class Wrapper<T>(val data: T?) {
        var exception: ApiException? = null

        class Success<T>(data: T) : Wrapper<T>(data)
        class Failed<T>(data: T) : Wrapper<T>(data)
        class Error<T>(e: ApiException) : Wrapper<T>(null) {
            init {
                exception = e
            }
        }
    }
}