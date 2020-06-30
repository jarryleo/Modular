package cn.leo.frame.ext

import android.text.InputFilter
import android.view.inputmethod.EditorInfo
import android.widget.EditText

/**
 * @author : ling luo
 * @date : 2019-12-13
 * 输入框帮助类
 */

/**
 * 输入法事件
 */
inline fun EditText.imeNext(crossinline onEvent: (text: String) -> Unit) {
    event(onEvent, EditorInfo.IME_ACTION_NEXT)
}

inline fun EditText.imeGo(crossinline onEvent: (text: String) -> Unit) {
    event(onEvent, EditorInfo.IME_ACTION_GO)
}

inline fun EditText.imeSearch(crossinline onEvent: (text: String) -> Unit) {
    event(onEvent, EditorInfo.IME_ACTION_SEARCH)
}

inline fun EditText.imeSend(crossinline onEvent: (text: String) -> Unit) {
    event(onEvent, EditorInfo.IME_ACTION_SEND)
}

inline fun EditText.imeDone(crossinline onEvent: (text: String) -> Unit) {
    event(onEvent, EditorInfo.IME_ACTION_DONE)
}

inline fun EditText.event(crossinline onEvent: (text: String) -> Unit, imeOp: Int) {
    setSingleLine()
    imeOptions = imeOp
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == imeOp) {
            onEvent(text.toString().trim())
        }
        true
    }
}

/**
 * 获取值
 */
fun EditText.getInt() = text.toString().toIntOrNull() ?: 0
fun EditText.getLong() = text.toString().toLongOrNull() ?: 0L
fun EditText.getFloat() = text.toString().toFloatOrNull() ?: 0f
fun EditText.getDouble() = text.toString().toDoubleOrNull() ?: 0.0

/**
 * 当输入文字为空时候执行block
 */
inline fun EditText.checkEmpty(block: () -> Unit): EditText {
    check({ text -> text.isEmpty() }, block)
    return this
}

/**
 * 当输入文字字数不在[start,end]之间时，执行block
 */
inline fun EditText.checkNum(start: Int = 0, end: Int, block: () -> Unit): EditText {
    check({ text -> text.length < start || text.length > end }, block)
    return this
}

/**
 * 输入符合条件时候执行block
 */
inline fun EditText.check(condition: (text: String) -> Boolean, block: () -> Unit): EditText {
    if (condition(text.toString().trim())) {
        block()
    }
    return this
}