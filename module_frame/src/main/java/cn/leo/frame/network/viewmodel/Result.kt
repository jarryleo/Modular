package cn.leo.frame.network.viewmodel

import cn.leo.frame.network.exceptions.ApiException

/**
 * @author : ling luo
 * @date : 2019-12-17
 *
 * liveData使用的结果回调封装
 */
sealed class Result<T> {
    class Loading<T>(val isShow: Boolean) : Result<T>()
    class Success<T>(val data: T) : Result<T>()
    class Failed<T>(val exception: ApiException) : Result<T>()

    var obj: Any? = null
    val successData by lazy { (this as? Success<T>)?.data }
    val failedException by lazy { (this as? Failed<T>)?.exception }
    var loading: (isShow: Boolean) -> Unit = {}
    var success: (data: T) -> Unit = {}
        set(value) {
            field = {
                loading(false)
                value(it)
            }
        }
    var failed: (exception: ApiException) -> Unit = {}
        set(value) {
            field = {
                loading(false)
                value(it)
            }
        }

    fun loading(block: (isShow: Boolean) -> Unit = {}) {
        loading = block
    }

    fun success(block: (data: T) -> Unit = {}) {
        success = block
    }

    fun failed(block: (exception: ApiException) -> Unit = {}) {
        failed = block
    }


}