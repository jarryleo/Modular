package cn.leo.frame.ext

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import androidx.annotation.DrawableRes
import cn.leo.frame.utils.ResUtil
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * @author : ling luo
 * @date : 2019-11-28
 */


class TextLengthWatcher(private var lengthWatcher: (length: Int) -> Unit = {}) : TextWatcher {
    override fun afterTextChanged(s: Editable?) {
        lengthWatcher(s?.length ?: 0)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }
}

inline fun TextView.textLengthWatcher(crossinline lengthWatcher: (length: Int) -> Unit = {}) {
    this.addTextChangedListener(TextLengthWatcher {
        lengthWatcher(it)
    })
}

abstract class TextProperty<T>(view: () -> TextView) : ReadWriteProperty<Any, T> {
    private val textView: TextView by lazy { view.invoke() }
    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        textView.text = value.toString()
    }

    fun getText() = textView.text.toString().trim()
}


fun text(view: () -> TextView) = object : TextProperty<String>(view) {
    override fun getValue(thisRef: Any, property: KProperty<*>): String {
        return getText()
    }
}

fun int(view: () -> TextView) = object : TextProperty<Int>(view) {
    override fun getValue(thisRef: Any, property: KProperty<*>): Int {
        return getText().toIntOrNull() ?: 0
    }
}

fun long(view: () -> TextView) = object : TextProperty<Long>(view) {
    override fun getValue(thisRef: Any, property: KProperty<*>): Long {
        return getText().toLongOrNull() ?: 0L
    }
}

fun float(view: () -> TextView) = object : TextProperty<Float>(view) {
    override fun getValue(thisRef: Any, property: KProperty<*>): Float {
        return getText().toFloatOrNull() ?: 0f
    }
}

fun double(view: () -> TextView) = object : TextProperty<Double>(view) {
    override fun getValue(thisRef: Any, property: KProperty<*>): Double {
        return getText().toDoubleOrNull() ?: 0.0
    }
}


fun TextView.setDrawables(
    @DrawableRes left: Int = 0,
    @DrawableRes top: Int = 0,
    @DrawableRes right: Int = 0,
    @DrawableRes bottom: Int = 0,
    drawablePadding: Int = -1
) {
    val leftDrawable = if (left == 0) {
        null
    } else {
        ResUtil.getDrawable(left)
    }?.apply { setBounds(0, 0, minimumWidth, minimumHeight) }
    val topDrawable = if (top == 0) {
        null
    } else {
        ResUtil.getDrawable(top)
    }?.apply { setBounds(0, 0, minimumWidth, minimumHeight) }
    val rightDrawable = if (right == 0) {
        null
    } else {
        ResUtil.getDrawable(right)
    }?.apply { setBounds(0, 0, minimumWidth, minimumHeight) }
    val bottomDrawable = if (bottom == 0) {
        null
    } else {
        ResUtil.getDrawable(bottom)
    }?.apply { setBounds(0, 0, minimumWidth, minimumHeight) }
    if (drawablePadding != -1) compoundDrawablePadding = drawablePadding
    setCompoundDrawables(leftDrawable, topDrawable, rightDrawable, bottomDrawable)
}