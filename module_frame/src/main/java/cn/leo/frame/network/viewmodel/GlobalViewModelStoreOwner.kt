package cn.leo.frame.network.viewmodel

import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

/**
 * @author : ling luo
 * @date : 2019-12-10
 */
class GlobalViewModelStoreOwner : ViewModelStoreOwner {
    companion object {
        private val globalViewModelStore by lazy { ViewModelStore() }
        val globalModelStoreOwner by lazy { GlobalViewModelStoreOwner() }
    }

    override fun getViewModelStore(): ViewModelStore {
        return globalViewModelStore
    }
}