package cn.leo.frame.network

import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

/**
 * @author : ling luo
 * @date : 2019-12-10
 */
class MViewModelStoreOwner : ViewModelStoreOwner {
    companion object {
        private val globalViewModelStore by lazy { ViewModelStore() }
        val globalModelStoreOwner by lazy { MViewModelStoreOwner() }
    }

    override fun getViewModelStore(): ViewModelStore {
        return globalViewModelStore
    }
}