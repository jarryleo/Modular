package cn.leo.frame.network

import androidx.lifecycle.*
import cn.leo.frame.network.exceptions.ApiException

/**
 * @author : ling luo
 * @date : 2019-07-03
 */
open class MLiveData<T> : MutableLiveData<MLiveData.Wrapper<T>>() {

    lateinit var mLifecycleOwner: LifecycleOwner
    var mFailed: (e: ApiException) -> Unit = {}
    var mSuccess: (value: T) -> Unit = {}

    private val observer = Observer<Wrapper<T>> {
        when (it) {
            is Wrapper.Failed -> mFailed(it.exception)
            is Wrapper.Success -> mSuccess(it.data)
        }
    }

    open fun success(value: T) {
        super.postValue(Wrapper.Success(value))
    }

    open fun failed(e: Exception) {
        if (e is ApiException) {
            super.postValue(Wrapper.Failed(e))
        } else {
            super.postValue(Wrapper.Failed(ApiException(e)))
        }
    }


    fun observe(
        failed: (e: ApiException) -> Unit = {},
        success: (value: T) -> Unit = {}
    ) {
        //super.removeObserver(observer)
        mFailed = failed
        mSuccess = success
        super.observe(mLifecycleOwner, observer)
    }

    fun observeForever(
        failed: (e: ApiException) -> Unit = {},
        success: (value: T) -> Unit = {}
    ) {
        //super.removeObserver(observer)
        mFailed = failed
        mSuccess = success
        super.observeForever(observer)
    }

    fun <R> map(mapFunction: (input: Wrapper<T>) -> R): LiveData<R> {
        return Transformations.map(this, mapFunction)
    }

    sealed class Wrapper<T> {
        class Success<T>(val data: T) : Wrapper<T>()
        class Failed<T>(var exception: ApiException) : Wrapper<T>()
    }
}