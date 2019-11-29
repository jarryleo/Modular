package cn.leo.frame.ui

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.AbsoluteSizeSpan
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes

/**
 * Created by Leo on 2018/5/18.
 */
@Suppress("UNUSED", "MemberVisibilityCanBePrivate")
class MagicTextViewUtil private constructor(private val mTextView: TextView) {
    private val mStringBuilder = StringBuilder()
    private val mSpannableItemList: MutableList<SpannableItem> =
        mutableListOf()

    data class SpannableItem(
        val spannable: Any,
        val start: Int,
        val end: Int,
        val flag: Int
    )

    fun append(text: String): MagicTextViewUtil {
        mStringBuilder.append(text)
        return this
    }

    fun append(text: String, @ColorInt color: Int): MagicTextViewUtil {
        val spannable = ForegroundColorSpan(color)
        val start = mStringBuilder.length
        append(text)
        val end = mStringBuilder.length
        mSpannableItemList.add(
            SpannableItem(
                spannable, start, end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        )
        return this
    }

    fun append(text: String, underline: Boolean): MagicTextViewUtil {
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {}
            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = underline //false去掉下划线
            }
        }
        val start = mStringBuilder.length
        append(text)
        val end = mStringBuilder.length
        mSpannableItemList.add(
            SpannableItem(
                clickableSpan, start, end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        )
        return this
    }

    fun append(textSize: Int, text: String): MagicTextViewUtil {
        val absoluteSizeSpan = AbsoluteSizeSpan(textSize, true)
        val start = mStringBuilder.length
        append(text)
        val end = mStringBuilder.length
        mSpannableItemList.add(
            SpannableItem(
                absoluteSizeSpan, start, end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        )
        return this
    }

    fun append(text: String, clickListener: (String) -> Unit): MagicTextViewUtil {
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                clickListener(text)
            }
        }
        val start = mStringBuilder.length
        append(text)
        val end = mStringBuilder.length
        mSpannableItemList.add(
            SpannableItem(
                clickableSpan, start, end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        )
        return this
    }

    fun append(
        text: String, @ColorInt color: Int,
        underline: Boolean
    ): MagicTextViewUtil {
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {}
            override fun updateDrawState(ds: TextPaint) {
                ds.color = color
                ds.isUnderlineText = underline
            }
        }
        val start = mStringBuilder.length
        append(text)
        val end = mStringBuilder.length
        mSpannableItemList.add(
            SpannableItem(
                clickableSpan, start, end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        )
        return this
    }

    fun append(
        text: String, @ColorInt color: Int,
        clickListener: (String) -> Unit
    ): MagicTextViewUtil {
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                clickListener(text)
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.color = color //点击文字颜色
            }
        }
        val start = mStringBuilder.length
        append(text)
        val end = mStringBuilder.length
        mSpannableItemList.add(
            SpannableItem(
                clickableSpan, start, end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        )
        return this
    }

    fun append(
        text: String, @ColorInt color: Int,
        underline: Boolean,
        clickListener: (String) -> Unit
    ): MagicTextViewUtil {
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                clickListener(text)
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.color = color //点击文字颜色
                ds.isUnderlineText = underline //false去掉下划线
            }
        }
        val start = mStringBuilder.length
        append(text)
        val end = mStringBuilder.length
        mSpannableItemList.add(
            SpannableItem(
                clickableSpan, start, end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        )
        return this
    }

    fun append(@DrawableRes resourceId: Int): MagicTextViewUtil {
        val imageSpan = ImageSpan(mTextView.context, resourceId)
        val start = mStringBuilder.length
        append("icon")
        val end = mStringBuilder.length
        mSpannableItemList.add(
            SpannableItem(
                imageSpan, start, end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        )
        return this
    }

    fun show() {
        val spannableString = SpannableString(mStringBuilder.toString())
        for (spannableItem in mSpannableItemList) {
            spannableString.setSpan(
                spannableItem.spannable,
                spannableItem.start,
                spannableItem.end,
                spannableItem.flag
            )
        }
        mTextView.highlightColor = Color.TRANSPARENT //设置选中文字高亮颜色
        mTextView.text = spannableString
        mTextView.movementMethod = LinkMovementMethod.getInstance()
    }

    companion object {
        fun getInstance(textView: TextView): MagicTextViewUtil {
            return MagicTextViewUtil(textView)
        }
    }

}