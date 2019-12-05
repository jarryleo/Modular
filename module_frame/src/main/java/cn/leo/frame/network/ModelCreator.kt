package cn.leo.frame.network

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * @author : ling luo
 * @date : 2019-11-15
 */
class ModelCreator<T : MViewModel<*>>(private val clazz: Class<T>) :
    ReadOnlyProperty<ViewModelStoreOwner, T> {
    override fun getValue(thisRef: ViewModelStoreOwner, property: KProperty<*>): T {
        /**
         * 绑定ViewModelStoreOwner 在 view层销毁时候会通知所有 ViewModel 调用 clear() 方法
         * 用户需重写 ViewModel 的 onCleared() 来执行回收操作
         */
        return ViewModelProvider(thisRef).get(clazz)
    }
}