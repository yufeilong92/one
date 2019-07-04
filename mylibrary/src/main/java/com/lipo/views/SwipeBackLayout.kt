package com.lipo.views

import android.app.Activity
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Scroller

import java.util.LinkedList

/**
 *
 * @author xiaanming
 *
 * @blog http://blog.csdn.net/xiaanming
 */
class SwipeBackLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet, defStyle: Int = 0) : FrameLayout(context, attrs, defStyle) {
    private var mContentView: View? = null
    private val mTouchSlop: Int
    private var downX: Int = 0
    private var downY: Int = 0
    private var tempX: Int = 0
    private val mScroller: Scroller
    private var viewWidth: Int = 0
    private var isSilding: Boolean = false
    private var isFinish: Boolean = false
    private var mShadowDrawable: Drawable? = null
    private var mActivity: Activity? = null
    private val mViewPagers = LinkedList<ViewPager>()

    init {

        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
        mScroller = Scroller(context)

        //		mShadowDrawable = getResources().getDrawable(R.drawable.shadow_left);
    }

    fun attachToActivity(activity: Activity) {
        mActivity = activity
        val a = activity.theme.obtainStyledAttributes(
                intArrayOf(android.R.attr.windowBackground))
        val background = a.getResourceId(0, 0)
        a.recycle()

        val decor = activity.window.decorView as ViewGroup
        val decorChild = decor.getChildAt(0) as ViewGroup
        decorChild.setBackgroundResource(background)
        decor.removeView(decorChild)
        addView(decorChild)
        setContentView(decorChild)
        decor.addView(this)
    }

    private fun setContentView(decorChild: View) {
        mContentView = decorChild.parent as View
        //		mContentView.setBackgroundColor(color.transparent);
    }

    /**
     */
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val mViewPager = getTouchViewPager(mViewPagers, ev)
        Log.i(TAG, "mViewPager = " + mViewPager!!)

        if (mViewPager.currentItem != 0) {
            return super.onInterceptTouchEvent(ev)
        }

        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                tempX = ev.rawX.toInt()
                downX = tempX
                downY = ev.rawY.toInt()
            }
            MotionEvent.ACTION_MOVE -> {
                val moveX = ev.rawX.toInt()
                if (moveX - downX > mTouchSlop && Math.abs(ev.rawY.toInt() - downY) < mTouchSlop) {
                    return true
                }
            }
        }

        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                val moveX = event.rawX.toInt()
                val deltaX = tempX - moveX
                tempX = moveX
                if (moveX - downX > mTouchSlop && Math.abs(event.rawY.toInt() - downY) < mTouchSlop) {
                    isSilding = true
                }

                if (moveX - downX >= 0 && isSilding) {
                    mContentView!!.scrollBy(deltaX, 0)
                }
            }
            MotionEvent.ACTION_UP -> {
                isSilding = false
                if (mContentView!!.scrollX <= -viewWidth / 2) {
                    isFinish = true
                    scrollRight()
                } else {
                    scrollOrigin()
                    isFinish = false
                }
            }
        }

        return true
    }

    /**
     * @param mViewPagers
     * @param parent
     */
    private fun getAlLViewPager(mViewPagers: MutableList<ViewPager>, parent: ViewGroup) {
        val childCount = parent.childCount
        for (i in 0..childCount - 1) {
            val child = parent.getChildAt(i)
            if (child is ViewPager) {
                mViewPagers.add(child)
            } else if (child is ViewGroup) {
                getAlLViewPager(mViewPagers, child)
            }
        }
    }

    /**
     * @param mViewPagers
     * @param ev
     * @return
     */
    private fun getTouchViewPager(mViewPagers: List<ViewPager>?,
                                  ev: MotionEvent): ViewPager? {
        if (mViewPagers == null || mViewPagers.size == 0) {
            return null
        }
        val mRect = Rect()
        for (v in mViewPagers) {
            v.getHitRect(mRect)

            if (mRect.contains(ev.x.toInt(), ev.y.toInt())) {
                return v
            }
        }
        return null
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (changed) {
            viewWidth = this.width

            getAlLViewPager(mViewPagers, this)
            Log.i(TAG, "ViewPager size = " + mViewPagers.size)
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (mShadowDrawable != null && mContentView != null) {

            val left = mContentView!!.left - mShadowDrawable!!.intrinsicWidth
            val right = left + mShadowDrawable!!.intrinsicWidth
            val top = mContentView!!.top
            val bottom = mContentView!!.bottom

            mShadowDrawable!!.setBounds(left, top, right, bottom)
            mShadowDrawable!!.draw(canvas)
        }

    }

    /**
     */
    private fun scrollRight() {
        val delta = viewWidth + mContentView!!.scrollX
        mScroller.startScroll(mContentView!!.scrollX, 0, -delta, 0,
                Math.abs(delta))

        postInvalidate()
    }

    /**
     */
    private fun scrollOrigin() {
        val delta = mContentView!!.scrollX
        mScroller.startScroll(mContentView!!.scrollX, 0, -delta, 0,
                Math.abs(delta))
        postInvalidate()
    }

    override fun computeScroll() {
        if (mScroller.computeScrollOffset()) {
            mContentView!!.scrollTo(mScroller.currX, mScroller.currY)

            postInvalidate()

            if (mScroller.isFinished && isFinish) {
                mShadowDrawable = null
                mActivity!!.finish()
            }
        }
    }

    companion object {
        private val TAG = SwipeBackLayout::class.java.simpleName
    }

}
