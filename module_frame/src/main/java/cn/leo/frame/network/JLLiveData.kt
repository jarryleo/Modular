package cn.leo.frame.network

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import cn.leo.frame.log.toLogE
import cn.leo.frame.network.exceptions.ApiException

/**
 * @author : ling luo
 * @date : 2019-07-03
 */
open class JLLiveData<T> : MutableLiveData<JLLiveData.Wrapper<T>>() {

    var liveDataLifecycleOwner: LifecycleOwner? = null

    var mError: (e: ApiException?) -> Unit = {}
    var mFailed: (value: T?) -> Unit = {}
    var mFailedOrError: () -> Unit = {}
    var mSuccess: (value: T?) -> Unit = {}

    private val observer = Observer<Wrapper<T>> {
        when (it) {
            is Wrapper.Error -> {
                mError(it.exception)
                mFailedOrError()
            }
            is Wrapper.Failed -> {
                mFailed(it.data)
                mFailedOrError()
            }
            is Wrapper.Success -> mSuccess(it.data)
        }
    }

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
        error: (e: ApiException?) -> Unit = {},
        failed: (value: T?) -> Unit = {},
        failedOrError: () -> Unit = {},
        success: (value: T?) -> Unit = {}
    ) {
        super.removeObserver(observer)
        mError = error
        mFailed = failed
        mFailedOrError = failedOrError
        mSuccess = success
        super.observe(liveDataLifecycleOwner!!, observer)
    }

    fun observeForever(
        error: (e: ApiException?) -> Unit = {},
        failed: (value: T?) -> Unit = {},
        failedOrError: () -> Unit = {},
        success: (value: T?) -> Unit = {}
    ) {
        super.removeObserver(observer)
        mError = error
        mFailed = failed
        mFailedOrError = failedOrError
        mSuccess = success
        super.observeForever(observer)
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