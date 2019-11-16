package cn.leo.frame.network

import android.os.Bundle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import cn.leo.frame.network.exceptions.ApiException

/**
 * @author : ling luo
 * @date : 2019-07-03
 */
open class MLiveData<T> : MediatorLiveData<MLiveData.Result<T>>() {

    lateinit var mLifecycleOwner: LifecycleOwner
    var mResult: (result: Result<T>) -> Unit = {}

    private val observer = Observer<Result<T>> {
        mResult(it)
    }

    open fun success(value: T, bundle: Bundle? = null) {
        super.postValue(Result.Success(value).apply { this.bundle = bundle })
    }

    open fun failed(e: Exception, bundle: Bundle? = null) {
        if (e is ApiException) {
            val failed = Result.Failed<T>(e)
            failed.bundle = bundle
            super.postValue(failed)
        } else {
            val failed = Result.Failed<T>(ApiException(e))
            failed.bundle = bundle
            super.postValue(failed)
        }
    }


    fun observe(
        result: (Result<T>).() -> Unit = {}
    ) {
        mResult = result
        super.observe(mLifecycleOwner, observer)
    }

    fun observeForever(
        result: (Result<T>).() -> Unit = {}
    ) {
        mResult = result
        super.observeForever(observer)
    }

    fun <R> map(mapFunction: (input: T) -> R): MLiveData<R> {
        val newLiveData = MLiveData<R>()
        newLiveData.mLifecycleOwner = mLifecycleOwner
        newLiveData.addSource(this) {
            when (it) {
                is Result.Failed -> {
                    val failed = Result.Failed<R>(it.exception)
                    failed.bundle = it.bundle
                    newLiveData.postValue(failed)
                }
                is Result.Success -> {
                    val success = Result.Success(mapFunction(it.data))
                    success.bundle = it.bundle
                    newLiveData.postValue(success)
                }
            }
        }
        return newLiveData
    }

    sealed class Result<T> {
        var bundle: Bundle? = null

        class Success<T>(val data: T) : Result<T>()
        class Failed<T>(var exception: ApiException) : Result<T>()

        fun get(
            failed: (exception: ApiException) -> Unit = {},
            success: (data: T) -> Unit = {}
        ) {
            when (this) {
                is Success -> success(data)
                is Failed -> failed(exception)
            }
        }
    }
}