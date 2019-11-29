package cn.leo.base.base

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import cn.leo.frame.network.MViewModel
import cn.leo.frame.network.ModelCreator
import cn.leo.frame.utils.ClassUtils

/**
 * @author : ling luo
 * @date : 2019-11-27
 */
abstract class BaseModelActivity<T : MViewModel<*>> : AppCompatActivity() {

    val model by ModelCreator<T>(ClassUtils.getSuperClassGenericType(this::class.java))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onInitialize()
    }

    open fun onInitObserve() {
    }

    @CallSuper
    open fun onInitialize() {
        onInitObserve()
    }
}