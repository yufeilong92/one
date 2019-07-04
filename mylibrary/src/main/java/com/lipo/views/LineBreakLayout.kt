package com.lipo.views

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

/**
 * Created by lipo on 2017/6/11.
 */

class LineBreakLayout : ViewGroup {

    private var onItemClickListener: OnItemClickListener? = null


    constructor(context: Context) : super(context) {}
    //  public LineBreakLayout(Context context, AttributeSet attrs, int defStyle) {
    //      super(context, attrs, defStyle);
    //  }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val maxWidth = View.MeasureSpec.getSize(widthMeasureSpec)
        val childCount = childCount
        var x = 0
        var y = 0
        var row = 0

        for (index in 0..childCount - 1) {
            val child = getChildAt(index)
            if (child.visibility != View.GONE) {
                child.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                // 此处增加onlayout中的换行判断，用于计算所需的高度
                val width = child.measuredWidth
                val height = child.measuredHeight
                x += width
                y = row * height + height
                if (x > maxWidth) {
                    x = width
                    row++
                    y = row * height + height
                }
            }
        }
        // 设置容器所需的宽度和高度
        setMeasuredDimension(maxWidth, y)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val childCount = childCount
        val maxWidth = r - l
        var x = 0
        var y = 0
        var row = 0
        for (i in 0..childCount - 1) {
            val child = this.getChildAt(i)
            if (child.visibility != View.GONE) {
                val width = child.measuredWidth
                val height = child.measuredHeight
                x += width
                y = row * height + height
                if (x > maxWidth) {
                    x = width
                    row++
                    y = row * height + height
                }
                child.layout(x - width, y - height, x, y)
            }
        }
    }

    fun setAdapter(adapter: BaseAdapter) {
        removeAllViews()
        for (i in 0..adapter.count - 1) {
            val itemView = adapter.getView(i, null, null)
            itemView.setOnClickListener {
                if (onItemClickListener != null) {
                    onItemClickListener!!.onclick(i)
                }
            }
            addView(itemView)
        }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onclick(position: Int)
    }

    companion object {

        private val TAG = "LineBreakLayout"

        private val VIEW_MARGIN = 2
    }

}
