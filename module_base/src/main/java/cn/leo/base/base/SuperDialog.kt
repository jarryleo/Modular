package cn.leo.base.base

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes

/**
 * 基础弹窗
 * @author Max
 * @date 2019-10-12.
 */
abstract class SuperDialog : Dialog {

    constructor(context: Context, themeResId: Int) : super(context, themeResId) {
        init()
    }

    constructor(
        context: Context,
        cancelable: Boolean,
        cancelListener: DialogInterface.OnCancelListener?
    ) : super(context, cancelable, cancelListener) {
        init()
    }

    protected fun init() {
        val layoutId = getLayoutResId()
        if (layoutId != -1) {
            setContentView(layoutId)
        } else {
            setContentView(createView())
        }
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isNeedFullScreen()) {
            setFullScreen()
        }
        onInitialize()
    }

    /**
     * 初始化逻辑
     */
    abstract fun onInitialize()

    /**
     * 创建内容视图
     */
    protected open fun createView(): View? {
        return null
    }

    /**
     * 创建内容视图 根据布局ID
     */
    @LayoutRes
    protected open fun getLayoutResId(): Int {
        return -1
    }

    /**
     * 设置方向
     */
    fun setGravity(gravity: Int) {
        val params = window!!.attributes
        params.gravity = gravity
        val win = window
        win!!.decorView
            .setPadding(0, 0, 0, 0)
        val lp = win.attributes
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        win.attributes = lp
    }

    /**
     * 设置全屏
     */
    protected fun setFullScreen() {
        //设置window背景，默认的背景会有Padding值，不能全屏。当然不一定要是透明，你可以设置其他背景，替换默认的背景即可。
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //一定要在setContentView之后调用，否则无效
        window.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    /**
     * 是否需要全屏
     */
    protected open fun isNeedFullScreen(): Boolean {
        return false
    }

}