package cn.leo.base.base

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import cn.leo.base.dialog.LoadingDialog
import cn.leo.frame.network.MJob
import cn.leo.frame.network.MViewModel
import cn.leo.frame.network.ModelCreator
import cn.leo.frame.ui.ILoading
import cn.leo.frame.utils.ClassUtils
import com.alibaba.android.arouter.launcher.ARouter
import kotlin.reflect.KFunction

/**
 * @author : ling luo
 * @date : 2019-11-27
 */
abstract class BaseModelActivity<T : MViewModel<*>> : AppCompatActivity(),
    ILoading {

    val model by ModelCreator<T>(ClassUtils.getSuperClassGenericType(this::class.java))
    //加载弹窗
    private var mLoadingDialog: LoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onInitialize()
    }

    open fun onInitObserve() {
    }

    @CallSuper
    open fun onInitialize() {
        ARouter.getInstance().inject(this)
        model.loading.observe(this, Observer {
            if (it) {
                showLoading()
            } else {
                dismissLoading()
            }
        })
        onInitObserve()
    }

    override fun showLoading(message: CharSequence?) {
        if (mLoadingDialog != null && mLoadingDialog?.isShowing == true) {
            return
        }
        mLoadingDialog = LoadingDialog(this, message)
        mLoadingDialog?.show()
    }

    override fun dismissLoading() {
        if (mLoadingDialog?.isShowing == true) {
            mLoadingDialog?.dismiss()
        }
        mLoadingDialog = null
    }

    /**
     * 协助订阅方法
     */
    infix fun <R> KFunction<MJob<R>>.ob(obFunc: (R) -> Any) {
        model.observe(this@BaseModelActivity, this) {
            success{
                obFunc(it)
            }
        }
    }

    /**
     * 重载操作符协助订阅方法
     */
    protected operator fun <R> KFunction<MJob<R>>.plus(obFunc: (R) -> Any) {
        model.observe(this@BaseModelActivity, this) {
            success{
                obFunc(it)
            }
        }
    }
}