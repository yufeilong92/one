package com.lipo.views

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.widget.ScrollView

class MyScrollView : ScrollView {

    constructor(context: Context) : super(context) {
        initView()
        // TODO Auto-generated constructor stub
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView()
        // TODO Auto-generated constructor stub
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
        // TODO Auto-generated constructor stub
    }


    private var mGestureDetector: GestureDetector? = null
    internal var mGestureListener: View.OnTouchListener? = null

    private fun initView() {
        mGestureDetector = GestureDetector(context, YScrollDetector())
        setFadingEdgeLength(0)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return super.onInterceptTouchEvent(ev) && mGestureDetector!!.onTouchEvent(ev)
    }

    internal inner class YScrollDetector : SimpleOnGestureListener() {
        override fun onScroll(e1: MotionEvent, e2: MotionEvent,
                              distanceX: Float, distanceY: Float): Boolean {
            return if (Math.abs(distanceY) > Math.abs(distanceX)) {
                true
            } else false
        }
    }

}
