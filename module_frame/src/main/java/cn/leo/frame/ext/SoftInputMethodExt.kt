package cn.leo.frame.ext

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import cn.leo.frame.utils.trueLet

/**
 * @author : ling luo
 * @date : 2019-11-29
 */


/**
 * 弹出输入法
 */
fun View.showSoftInputMethod() {
    isFocusable = true
    isFocusableInTouchMode = true
    requestFocus()
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val activity = context as? Activity
    inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_FORCED)
    activity?.window?.setSoftInputMode(
        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
    )
}

/**
 * 收起输入法
 */
fun View.hideSoftInputMethod() {
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.isActive.trueLet {
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }
}