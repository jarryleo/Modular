package cn.leo.frame.network

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import cn.leo.frame.network.exceptions.ApiException
import cn.leo.frame.network.exceptions.FactoryException

/**
 * @author : ling luo
 * @date : 2019-07-03
 */
open class MLiveData<T> : MediatorLiveData<MLiveData.Result<T>>() {

    private fun getObserver(result: (Result<T>).() -> Unit): Observer<Result<T>> {
        return Observer { result(it) }
    }

    open fun success(value: T, obj: Any? = null) {
        super.postValue(Result.Success(value).apply { this.obj = obj })
    }

    open fun failed(e: Exception, obj: Any? = null) {
        if (e is ApiException) {
            val failed = Result.Failed<T>(e)
            failed.obj = obj
            super.postValue(failed)
        } else {
            val failed = Result.Failed<T>(FactoryException.analysisException(e))
            failed.obj = obj
            super.postValue(failed)
        }
    }


    fun observe(
        lifecycleOwner: LifecycleOwner,
        result: (Result<T>).() -> Unit = {}
    ) {
        super.observe(lifecycleOwner, getObserver(result))
    }

    fun observeForever(
        result: (Result<T>).() -> Unit = {}
    ) {
        super.observeForever(getObserver(result))
    }

    /**
     * LiveData 类型转换，类似与RxJava的map
     */
    fun <R> map(mapFunction: (input: T) -> R): MLiveData<R> {
        val newLiveData = MLiveData<R>()
        newLiveData.addSource(this) {
            when (it) {
                is Result.Failed -> {
                    val failed = Result.Failed<R>(it.exception)
                    failed.obj = it.obj
                    newLiveData.postValue(failed)
                }
                is Result.Success -> {
                    val success = Result.Success(mapFunction(it.data))
                    success.obj = it.obj
                    newLiveData.postValue(success)
                }
            }
        }
        return newLiveData
    }

    sealed class Result<T> {
        var obj: Any? = null

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