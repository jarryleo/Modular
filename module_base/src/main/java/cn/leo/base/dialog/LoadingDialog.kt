package cn.leo.base.dialog

import android.content.Context
import android.view.View
import cn.leo.base.R
import cn.leo.base.base.SuperDialog
import kotlinx.android.synthetic.main.base_dialog_loading.*

/**
 * 加载弹窗
 * @author Max
 * @date 2019-09-17.
 */
class LoadingDialog(context: Context) : SuperDialog(context, R.style.base_Loading_Dialog) {

    var message: CharSequence? = null
    private var mCancelable: Boolean = false

    constructor(
        context: Context,
        message: CharSequence?,
        cancelable: Boolean = false
    ) : this(
        context
    ) {
        this.message = message
        this.mCancelable = cancelable
    }

    override fun onInitialize() {
        setCancelable(mCancelable)

        if (message.isNullOrEmpty()) {
            tv_content.visibility = View.GONE
        } else {
            tv_content.visibility = View.VISIBLE
            tv_content.text = message
        }
    }

    override fun getLayoutResId(): Int {
        return R.layout.base_dialog_loading
    }
}