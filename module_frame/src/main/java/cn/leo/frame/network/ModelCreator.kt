package cn.leo.frame.network

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * @author : ling luo
 * @date : 2019-11-15
 */
class ModelCreator<T : ViewModel>(private val clazz: Class<T>) :
    ReadOnlyProperty<ViewModelStoreOwner, T> {
    override fun getValue(thisRef: ViewModelStoreOwner, property: KProperty<*>): T {
        val model = ViewModelProvider(thisRef).get(clazz)
        if (model is MViewModel<*> && thisRef is LifecycleOwner) {
            model.mLifecycleOwner = thisRef
        }
        return model
    }
}