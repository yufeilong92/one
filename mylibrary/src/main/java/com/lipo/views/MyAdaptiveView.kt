package com.lipo.views

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import android.widget.BaseAdapter
import android.widget.FrameLayout

import com.lipo.utils.DisplayUtil

class MyAdaptiveView : FrameLayout {

    private var adapter: BaseAdapter? = null
    private var count: Int = 0
    private var contentView: View? = null
    private var totalWidth: Int = 0
    private var totalHeight: Int = 0
    private var screenWidth: Int = 0

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        screenWidth = context.resources.displayMetrics.widthPixels - DisplayUtil.dip2px(context, 24f)
    }

    fun setAdapter(adapter: BaseAdapter) {
        this.adapter = adapter

        count = adapter.count
        initView()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

    }

    private fun initView() {
        for (i in 0..count - 1) {
            contentView = adapter!!.getView(i, contentView, null)
            contentView!!.measure(0, 0)
            val width = contentView!!.measuredWidth
            val height = contentView!!.measuredHeight

            if (totalWidth + width > screenWidth) {
                totalHeight += height
                totalWidth = 0
            }

            val params = FrameLayout.LayoutParams(width, height)

            params.setMargins(totalWidth, totalHeight, 0, 0)
            contentView!!.layoutParams = params
            //			ViewGroup parent = (ViewGroup) contentView.getParent();
            //			if (parent != null) {
            //				parent.removeView(contentView);
            //			}
            this.addView(contentView)
            totalWidth += width
        }
    }

}
