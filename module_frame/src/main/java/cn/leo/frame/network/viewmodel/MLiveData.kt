package cn.leo.frame.network.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import cn.leo.frame.network.exceptions.ApiException
import cn.leo.frame.network.exceptions.FactoryException

/**
 * @author : ling luo
 * @date : 2019-07-03
 */
@Suppress("UNUSED", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate")
open class MLiveData<T> : MediatorLiveData<Result<T>>() {

    private fun getObserver(result: (Result<T>).() -> Unit): Observer<Result<T>> {
        return Observer {
            result(it)
            when (it) {
                is Result.Loading<T> -> it.loading(it.isShow)
                is Result.Success<T> -> it.success(it.data)
                is Result.Failed<T> -> it.failed(it.exception)
            }
        }
    }

    /**
     * 主线程执行成功方法
     */
    open fun success(value: T, obj: Any? = null) {
        super.postValue(Result.Success(value).apply { this.obj = obj })
    }

    /**
     * 原线程执行成功方法
     */
    fun setSuccess(value: T) = super.setValue(Result.Success(value))

    /**
     * 通知loading状态
     */
    fun showLoading() = super.postValue(Result.Loading(true))

    /**
     * 线程转换的失败方法
     */
    open fun failed(e: Exception, obj: Any? = null) {
        if (e is ApiException) {
            val failed = Result.Failed<T>(e)
            failed.obj = obj
            super.postValue(failed)
        } else {
            val failed = Result.Failed<T>(
                FactoryException.analysisException(e)
            )
            failed.obj = obj
            super.postValue(failed)
        }
    }

    /**
     * 订阅
     */
    fun observe(
        lifecycleOwner: LifecycleOwner,
        result: (Result<T>).() -> Unit = {}
    ) = super.observe(lifecycleOwner, getObserver(result))

    fun observeForever(
        result: (Result<T>).() -> Unit = {}
    ) = super.observeForever(getObserver(result))

    /**
     * LiveData 类型转换，类似与RxJava的map
     */
    fun <R> map(mapFunction: (input: T) -> R): MLiveData<R> {
        val newLiveData = MLiveData<R>()
        newLiveData.addSource(this) {
            when (it) {
                is Result.Failed -> {
                    val failed =
                        Result.Failed<R>(it.exception)
                    failed.obj = it.obj
                    newLiveData.postValue(failed)
                }
                is Result.Success -> {
                    val success =
                        Result.Success(
                            mapFunction(it.data)
                        )
                    success.obj = it.obj
                    newLiveData.postValue(success)
                }
            }
        }
        return newLiveData
    }
}