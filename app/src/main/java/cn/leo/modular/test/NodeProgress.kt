package cn.leo.modular.test

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.text.TextPaint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.annotation.ColorInt
import kotlin.math.roundToInt


/**
 * @author : ling luo
 * @date : 2019-12-09
 */
@Suppress("UNUSED", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate")
open class NodeProgress @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(
    context,
    attrs,
    defStyleAttr
) {

    private var mNodes: Array<String> = arrayOf()
    private var mBackPaint: Paint
    private var mForePaint: Paint
    private var mTextPaint: TextPaint
    private var mWidth: Int = 0
    private var mHeight: Int = 0
    private var mStrokeWidth: Float = 0f
    private var mProgress: Int = 0
    private var mPart: Float = 0f

    init {
        mStrokeWidth = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            1f,
            resources.displayMetrics
        )
        //背景
        mBackPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mBackPaint.color = Color.GRAY
        mBackPaint.strokeWidth = mStrokeWidth
        //前景
        mForePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mForePaint.color = Color.GREEN
        mForePaint.strokeWidth = mStrokeWidth * 2
        //文字
        mTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        mTextPaint.textAlign = Paint.Align.CENTER
        mTextPaint.textSize = mStrokeWidth * 15
        mTextPaint.color = Color.BLACK
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var wms = widthMeasureSpec
        var hms = heightMeasureSpec
        mWidth = MeasureSpec.getSize(widthMeasureSpec)
        mHeight = MeasureSpec.getSize(heightMeasureSpec)
        if (mWidth < 100.dp()) {
            mWidth = 100.dp()
            wms = MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.EXACTLY)
        }
        if (mHeight < 50.dp()) {
            mHeight = 50.dp()
            hms = MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY)
        }
        super.onMeasure(wms, hms)
        if (isInEditMode && mNodes.isNullOrEmpty()) {
            mNodes = arrayOf("v1", "v2", "v3", "v4", "v5")
            mPart = (mWidth / (mNodes.size)).toFloat()
        }
    }

    private fun Int.dp(): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            resources.displayMetrics
        ).roundToInt()
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (mNodes.isNotEmpty()) {
            setNodes(mNodes)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.apply {
            drawLine(
                0f, (mHeight / 5).toFloat(), mWidth.toFloat(),
                (mHeight / 5).toFloat(), mBackPaint
            )
            drawLine(
                0f, (mHeight / 5).toFloat(), mWidth.toFloat(),
                (mHeight / 5).toFloat(), mBackPaint
            )
            val progress = mProgress * mPart - (mPart / 2)
            drawLine(0f, (mHeight / 5).toFloat(), progress, (mHeight / 5).toFloat(), mForePaint)
            mNodes.forEachIndexed { i, node ->
                val x = (i + 1) * mPart - (mPart / 2)
                if (x <= progress) {
                    mForePaint.style = Paint.Style.STROKE
                    mForePaint.strokeWidth = mStrokeWidth / 2
                    canvas.drawCircle(x, (mHeight / 5).toFloat(), mStrokeWidth * 6, mForePaint)
                    mForePaint.style = Paint.Style.FILL
                    mForePaint.strokeWidth = mStrokeWidth * 2
                    canvas.drawCircle(x, (mHeight / 5).toFloat(), mStrokeWidth * 4, mForePaint)
                } else {
                    canvas.drawCircle(x, (mHeight / 5).toFloat(), mStrokeWidth * 4, mBackPaint)
                }
                canvas.drawText(node, x, (mHeight * 3 / 5).toFloat(), mTextPaint)
            }
        }
    }

    /**
     * 传入节点字符串
     *
     * @param nodes
     */
    fun setNodes(nodes: Array<String>) {
        if (nodes.isNullOrEmpty())
            throw  IllegalArgumentException("nodes is empty")
        mNodes = nodes
        //分段长度
        mPart = (mWidth / (mNodes.size)).toFloat()
        postInvalidate()
    }

    /**
     * 完成度，进度高亮到节点
     *
     * @param progress 0-mNodes.length
     */
    fun setProgress(progress: Int) {
        if (mNodes.isNullOrEmpty())
            throw  IllegalArgumentException("nodes is empty")
        mProgress = progress
    }

    fun setBackColor(@ColorInt backColor: Int) {
        mBackPaint.color = backColor
        postInvalidate()
    }

    fun setForeColor(@ColorInt foreColor: Int) {
        mForePaint.color = foreColor
        postInvalidate()
    }

    fun setTextColor(@ColorInt textColor: Int) {
        mTextPaint.color = textColor
        postInvalidate()
    }

}