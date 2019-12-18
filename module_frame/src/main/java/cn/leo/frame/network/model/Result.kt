package cn.leo.frame.network.model

import cn.leo.frame.network.exceptions.ApiException

/**
 * @author : ling luo
 * @date : 2019-12-17
 *
 * liveData使用的结果回调封装
 */
sealed class Result<T> {
    var obj: Any? = null

    class Loading<T>(val isShow: Boolean) : Result<T>()
    class Success<T>(val data: T) : Result<T>()
    class Failed<T>(val exception: ApiException) : Result<T>()

    private fun get(
        loading: (isShow: Boolean) -> Unit = {},
        failed: (exception: ApiException) -> Unit = {},
        success: (data: T) -> Unit = {}
    ) {
        when (this) {
            is Loading -> loading(isShow)
            is Success -> success(data)
            is Failed -> failed(exception)
        }
    }

    fun loading(block: (isShow: Boolean) -> Unit = {}) {
        get(loading = block)
    }

    fun success(block: (data: T) -> Unit = {}) {
        get(success = block)
        loading(false)
    }

    fun failed(block: (exception: ApiException) -> Unit = {}) {
        get(failed = block)
        loading(false)
    }

    val successData by lazy { (this as? Success<T>)?.data }
    val failedException by lazy { (this as? Failed<T>)?.exception }

    var loading: (isShow: Boolean) -> Unit = {}
    var success: (data: T) -> Unit = {}
    var failed: (exception: ApiException) -> Unit = {}

}