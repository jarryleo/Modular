package cn.leo.frame.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 *  @author : lingluo
 *  RecyclerView 分割线和边距
 *
 */
class SpaceSideDecoration : RecyclerView.ItemDecoration {

    var leftSpace: Int = 0
    var rightSpace: Int = 0
    var topSpace: Int = 0
    var bottomSpace: Int = 0
    var leftSide: Int = 0
    var rightSide: Int = 0
    var topSide: Int = 0
    var bottomSide: Int = 0

    constructor(space: Int = 0) {
        this.leftSpace = space
        this.rightSpace = space
        this.topSpace = space
        this.bottomSpace = space
    }

    constructor(
        leftSpace: Int = 0,
        rightSpace: Int = 0,
        topSpace: Int = 0,
        bottomSpace: Int = 0
    ) {
        this.leftSpace = leftSpace
        this.rightSpace = rightSpace
        this.topSpace = topSpace
        this.bottomSpace = bottomSpace
    }

    constructor(
        leftSpace: Int = 0,
        rightSpace: Int = 0,
        topSpace: Int = 0,
        bottomSpace: Int = 0,
        leftSide: Int = 0,
        rightSide: Int = 0,
        topSide: Int = 0,
        bottomSide: Int = 0
    ) {
        this.leftSpace = leftSpace
        this.rightSpace = rightSpace
        this.topSpace = topSpace
        this.bottomSpace = bottomSpace
        this.leftSide = leftSide
        this.rightSide = rightSide
        this.topSide = topSide
        this.bottomSide = bottomSide
    }


    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        when (val layoutManager = parent.layoutManager) {
            is GridLayoutManager -> {
                val vertical =
                    layoutManager.orientation == GridLayoutManager.VERTICAL
                val spanCount = layoutManager.spanCount
                val itemCount = layoutManager.itemCount
                val position = layoutManager.getPosition(view)
                setRect(outRect, spanCount, itemCount, position, vertical)
            }
            is StaggeredGridLayoutManager -> {
                val lp =
                    view.layoutParams as StaggeredGridLayoutManager.LayoutParams
                val vertical = layoutManager.orientation ==
                        StaggeredGridLayoutManager.VERTICAL
                val spanCount = layoutManager.spanCount
                val itemCount = layoutManager.itemCount
                val position = lp.spanIndex
                setRect(outRect, spanCount, itemCount, position, vertical)
            }
            is LinearLayoutManager -> {
                val vertical =
                    layoutManager.orientation == LinearLayoutManager.VERTICAL
                val spanCount = 1
                val itemCount = layoutManager.itemCount
                val position = layoutManager.getPosition(view)
                setRect(outRect, spanCount, itemCount, position, vertical)
            }
            else -> {
                outRect.left = leftSpace
                outRect.bottom = bottomSpace
                outRect.top = topSpace
                outRect.right = rightSpace
            }
        }

    }

    private fun setRect(
        outRect: Rect,
        spanCount: Int,
        totalCount: Int,
        position: Int,
        isVertical: Boolean
    ) {
        val totalRow = totalCount / spanCount +
                if (totalCount % spanCount == 0) {
                    0
                } else {
                    1
                }
        //行
        val row = position / spanCount
        //列
        val column = position % spanCount

        outRect.left = if (isVertical) {
            if (column == 0) {
                leftSide
            } else {
                leftSpace
            }
        } else {
            if (row == 0) {
                leftSide
            } else {
                leftSpace
            }
        }

        outRect.right = if (isVertical) {
            if (column == spanCount - 1) {
                rightSide
            } else {
                rightSpace
            }
        } else {
            if (row == totalRow - 1) {
                rightSide
            } else {
                rightSpace
            }
        }

        outRect.top = if (isVertical) {
            if (row == 0) {
                topSide
            } else {
                topSpace
            }
        } else {
            if (column == spanCount - 1) {
                topSide
            } else {
                topSpace
            }
        }

        outRect.bottom = if (isVertical) {
            if (row == totalRow - 1) {
                bottomSide
            } else {
                bottomSpace
            }
        } else {
            if (column == 0) {
                bottomSide
            } else {
                bottomSpace
            }
        }

    }

}