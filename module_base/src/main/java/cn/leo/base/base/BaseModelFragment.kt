package cn.leo.base.base

import androidx.annotation.CallSuper
import androidx.lifecycle.Observer
import cn.leo.base.dialog.LoadingDialog
import cn.leo.frame.network.model.MViewModel
import cn.leo.frame.network.model.ModelCreator
import cn.leo.frame.ui.ILoading
import cn.leo.frame.utils.ClassUtils

/**
 * @author : ling luo
 * @date : 2019-11-30
 */
abstract class BaseModelFragment<T : MViewModel<*>> : SuperActionBarFragment(),
    ILoading {

    val model by ModelCreator<T>(
        ClassUtils.getSuperClassGenericType(
            this::class.java
        )
    )
    //加载弹窗
    private var mLoadingDialog: LoadingDialog? = null

    open fun onInitObserve() {
    }


    @CallSuper
    override fun onInitialize() {
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
        context?.apply {
            mLoadingDialog = LoadingDialog(this, message)
            mLoadingDialog?.show()
        }
    }

    override fun dismissLoading() {
        if (mLoadingDialog?.isShowing == true) {
            mLoadingDialog?.dismiss()
        }
        mLoadingDialog = null
    }

}