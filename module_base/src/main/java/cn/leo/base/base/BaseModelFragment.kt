package cn.leo.base.base

import androidx.annotation.CallSuper
import cn.leo.base.dialog.LoadingDialog
import cn.leo.frame.network.model.MViewModel
import cn.leo.frame.network.model.ModelCreator
import cn.leo.frame.support.main
import cn.leo.frame.ui.ILoading
import cn.leo.frame.utils.ClassUtils
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

/**
 * @author : ling luo
 * @date : 2019-11-30
 */
@Suppress("UNUSED", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate")
abstract class BaseModelFragment<T : MViewModel<*>> : SuperActionBarFragment(),
    ILoading {

    val model by ModelCreator<T>(
        ClassUtils.getSuperClassGenericType(
            this::class.java
        )
    )
    //加载弹窗
    private var mLoadingDialog: LoadingDialog? = null
    var showLoadingJob: Job? = null
    //延时弹loading框，如果在500毫秒内返回结果则不弹出loading框
    val loadingLazyFun: (isShow: Boolean) -> Unit = { isShow ->
        if (isShow) {
            showLoadingJob = main {
                delay(500L)
                showLoading()
            }
        } else {
            dismissLoading()
        }
    }
    //及时弹出loading框
    val loadingFun: (isShow: Boolean) -> Unit = { isShow ->
        if (isShow) {
            showLoading()
        } else {
            dismissLoading()
        }
    }

    open fun onInitObserve() {
    }


    @CallSuper
    override fun onInitialize() {
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
        showLoadingJob?.cancel()
        if (mLoadingDialog?.isShowing == true) {
            mLoadingDialog?.dismiss()
        }
        mLoadingDialog = null
    }


}