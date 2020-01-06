package cn.leo.modular.test

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import cn.leo.modular.R
import kotlin.math.*


/**
 * @author : ling luo
 * @date : 2019-12-09
 */
@Suppress("UNUSED", "UNCHECKED_CAST", "MemberVisibilityCanBePrivate")
open class ArcNodeProgress @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(
    context,
    attrs,
    defStyleAttr
) {
    //节点数据
    private var mNodes: Array<Node> = arrayOf()
    //画笔
    private var mBackPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mForePaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mTextPaint: TextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    //控件宽高
    private var mWidth: Int = 0
    private var mHeight: Int = 0
    //线条粗细
    private var mStrokeWidth: Float = 6.dp.toFloat()
    //当前进度
    private var mProgress: Int = 0
    //最大节点数
    private var mMaxNode: Int = 6
    //选中的节点
    private var mSelectNode: Int = 0
    //开始显示节点。尽量保证选中节点居中
    private var mStartNode: Int = 0
    //结束节点
    private var mEndNode: Int = 0
    //每个节点中间的圆弧角度
    private var mPart: Float = 0f
    //圆弧与边界距离
    private var mMargin: Float = 50.dp.toFloat()
    //圆弧绘制区域
    private var mRectF = RectF()
    //当前进度角度
    private var mCurrentSweepAngle: Float = 0f
    //圆弧所在圆直径
    private var mDiameter: Float = 0f
    //圆弧总角度
    private var mAngle: Float = 140f
    //进度圆球大小
    private var mThumbSize = 20.dp

    //单位转换
    private val Int.dp
        get() = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            resources.displayMetrics
        ).roundToInt()

    init {
        //背景
        mBackPaint.color = Color.GRAY
        mBackPaint.strokeWidth = mStrokeWidth
        mBackPaint.style = Paint.Style.STROKE
        mBackPaint.strokeCap = Paint.Cap.ROUND
        //前景
        mForePaint.shader =
            LinearGradient(
                0f, 0f, 200f, 0f,
                Color.parseColor("#FF5C58E0"),
                Color.parseColor("#FFB425FE"),
                Shader.TileMode.MIRROR
            )

        mForePaint.strokeWidth = mStrokeWidth
        mForePaint.style = Paint.Style.STROKE
        mForePaint.strokeCap = Paint.Cap.ROUND
        //文字
        mTextPaint.textAlign = Paint.Align.CENTER
        mTextPaint.textSize = 15.dp.toFloat()
        mTextPaint.color = Color.BLACK

        /*if (BuildConfig.DEBUG) {
            mCurrentSweepAngle = 30f
        }*/
    }

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var wms = widthMeasureSpec
        var hms = heightMeasureSpec
        mWidth = MeasureSpec.getSize(widthMeasureSpec)
        mHeight = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        if (mWidth < 100.dp) {
            mWidth = 100.dp
            wms = MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.EXACTLY)
        }
        if (mHeight < mWidth / 2 || heightMode != MeasureSpec.EXACTLY) {
            mHeight = mWidth / 2
            hms = MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY)
        }
        super.onMeasure(wms, hms)
        mDiameter = mWidth - (mMargin / 2)
        if (layoutParams is ViewGroup.MarginLayoutParams) {
            val lp = layoutParams as ViewGroup.MarginLayoutParams
            mDiameter = mDiameter - lp.leftMargin - lp.rightMargin
        }
        mRectF.set(
            (mWidth - mDiameter) / 2,
            mMargin,
            (mWidth - mDiameter) / 2 + mDiameter,
            mDiameter + mMargin
        )
        //布局预览
        if (isInEditMode && mNodes.isNullOrEmpty()) {
            mNodes = arrayOf(
                Node(1500, "1"),
                Node(3000, "2"),
                Node(8000, "3"),
                Node(18000, "4"),
                Node(28000, "5"),
                Node(38000, "6")
            )
            val minNode = min(mMaxNode, mNodes.size)
            mPart = (mWidth / minNode - 1).toFloat()
            mCurrentSweepAngle = mAngle / 2
        }
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (mNodes.isNotEmpty()) {
            setNodes(mNodes)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.apply {
            drawProgress(canvas)
        }
    }

    /**
     * 圆点坐标：(x0,y0) 
     * 半径：r 
     * 角度：a0 
     * 则圆上任一点为：（x1,y1） 
     * x1   =   x0   +   r   *   cos(ao   *   3.14   /180   ) 
     * y1   =   y0   +   r   *   sin(ao   *   3.14   /180   ) 
     */
    private fun drawProgress(canvas: Canvas) {
        //起始角度
        val startAngle = -90 - mAngle / 2
        //绘制圆弧背景
        canvas.drawArc(
            mRectF,
            startAngle,
            mAngle,
            false,
            mBackPaint
        )

        //圆弧圆心
        val cx = mRectF.centerX()
        val cy = mRectF.centerY()
        //圆弧半径
        val r = mDiameter / 2
        //起点坐标
        val sx: Float =
            (cx + r * cos(startAngle * Math.PI / 180)).toFloat()
        val sy: Float =
            (cy + r * sin(startAngle * Math.PI / 180)).toFloat()
        //当前进度坐标
        val x: Float =
            (cx + r * cos((startAngle + mCurrentSweepAngle) * Math.PI / 180)).toFloat()
        val y: Float =
            (cy + r * sin((startAngle + mCurrentSweepAngle) * Math.PI / 180)).toFloat()
        //进度渐变
        mForePaint.shader =
            LinearGradient(
                sx, sy, x, y,
                Color.parseColor("#FF5C58E0"),
                Color.parseColor("#FFB425FE"),
                Shader.TileMode.MIRROR
            )

        //绘制圆弧当前进度
        canvas.drawArc(
            mRectF,
            startAngle,
            mCurrentSweepAngle,
            false,
            mForePaint
        )

        if (mNodes.isNullOrEmpty()) {
            return
        }
        //绘制节点标签

        //计算节点间隔角度
        val minNode = min(mMaxNode, mNodes.size)
        mPart = mAngle / (minNode - 1)
        //圆弧半径 + 文字与圆弧间距
        val textRadius = mDiameter / 2 + mMargin / 2
        for (i in mStartNode until mEndNode) {
            val index = i - mStartNode
            val node = mNodes[i]
            val tx: Float =
                (cx + textRadius * cos((startAngle + index * mPart) * Math.PI / 180)).toFloat()
            val ty: Float =
                (cy + textRadius * sin((startAngle + index * mPart) * Math.PI / 180)).toFloat()
            //绘制选中文字背景
            if (i == mSelectNode) {
                val drawable = ContextCompat.getDrawable(context, R.drawable.thumb_select)
                drawable?.apply {
                    val textBounds = Rect()
                    mTextPaint.getTextBounds(node.text, 0, node.text.length, textBounds)
                    val w = max(25.dp, textBounds.width())
                    val h = max(25.dp, textBounds.height())
                    bounds.right = w
                    bounds.bottom = h
                    canvas.save()
                    canvas.translate(
                        tx - bounds.width() / 2,
                        ty - (bounds.height() + textBounds.height()) / 2
                    )
                    draw(canvas)
                    canvas.restore()
                }
            }
            //绘制节点文字
            canvas.drawText(node.text, tx, ty, mTextPaint)
        }

        //绘制圆球
        val drawable = ContextCompat.getDrawable(context, R.drawable.thumb)
        drawable?.apply {
            val b = bounds
            b.right = mThumbSize
            b.bottom = mThumbSize
            canvas.save()
            canvas.translate(x - (mThumbSize / 2), y - (mThumbSize / 2))
            draw(canvas)
            canvas.restore()
        }
    }

    /**
     * 传入节点字符串
     *
     * @param nodes
     */
    fun setNodes(nodes: Array<Node>) {
        if (nodes.isNullOrEmpty())
            throw  IllegalArgumentException("nodes is empty")
        mNodes = nodes
        //重新绘制
        setProgress(mProgress)
    }

    /**
     * 完成度，进度高亮到节点
     *
     * @param progress 0-mNodes[mNodes.size()].num
     */
    fun setProgress(progress: Int) {
        if (mNodes.isNullOrEmpty())
            throw  IllegalArgumentException("nodes is empty")
        mProgress = progress
        val minNode = min(mMaxNode, mNodes.size)
        //选中的节点
        if (progress >= mNodes.last().num) {
            mSelectNode = mNodes.size - 1
            mEndNode = mNodes.size - 1
            mStartNode = mEndNode - minNode
            mCurrentSweepAngle = mAngle
        } else {
            mNodes.forEachIndexed { index, node ->
                if (progress < node.num) {
                    //计算出选中的节点
                    mSelectNode = index - 1
                    //计算起始和结束节点
                    mStartNode = mSelectNode - (minNode / 2f).roundToInt() + 1
                    mEndNode = mStartNode + minNode
                    while (mStartNode < 0) {
                        mStartNode++
                        mEndNode++
                    }
                    while (mEndNode > mNodes.size) {
                        mStartNode--
                        mEndNode--
                    }
                    //计算到下一个节点的百分比
                    if (mSelectNode < 0) {
                        mSelectNode = 0
                        mCurrentSweepAngle = 0f
                    } else {
                        //计算节点间隔角度
                        mPart = mAngle / (minNode - 1)
                        val num = mNodes[mSelectNode].num
                        val nodeRange = node.num - num
                        val over: Float = (progress - num).toFloat()
                        val partAngle = over / nodeRange * mPart
                        mCurrentSweepAngle = mPart * (mSelectNode - mStartNode) + partAngle
                    }
                    postInvalidate()
                    return
                }
            }
        }
        //重新绘制
        postInvalidate()
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


    //节点数据
    data class Node(val num: Int, val text: String)
}