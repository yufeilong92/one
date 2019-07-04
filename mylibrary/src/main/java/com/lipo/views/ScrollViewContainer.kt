package com.lipo.views

import android.content.Context
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.widget.RelativeLayout
import android.widget.ScrollView

import java.util.Timer
import java.util.TimerTask

/**
 * 包含两个ScrollView的容器
 * 更多详解见博客http://dwtedx.com
 *
 * @author chenjing
 */
class ScrollViewContainer : RelativeLayout {

    private var isMeasured = false

    /**
     * 用于计算手滑动的速度
     */
    private var vt: VelocityTracker? = null

    private var mViewHeight: Int = 0
    private var mViewWidth: Int = 0

    private var topView: View? = null
    private var bottomView: View? = null

    private var canPullDown: Boolean = false
    private var canPullUp: Boolean = false
    private var state = DONE

    /**
     * 记录当前展示的是哪个view，0是topView，1是bottomView
     */
    private var mCurrentViewIndex = 0
    /**
     * 手滑动距离，这个是控制布局的主要变量
     */
    private var mMoveLen: Float = 0.toFloat()
    private var mTimer: MyTimer? = null
    private var mLastY: Float = 0.toFloat()
    /**
     * 用于控制是否变动布局的另一个条件，mEvents==0时布局可以拖拽了，mEvents==-1时可以舍弃将要到来的第一个move事件，
     * 这点是去除多点拖动剧变的关键
     */
    private var mEvents: Int = 0

    private val handler = object : Handler() {

        override fun handleMessage(msg: Message) {
            if (mMoveLen != 0f) {
                if (state == AUTO_UP) {
                    mMoveLen -= SPEED
                    if (mMoveLen <= -mViewHeight) {
                        mMoveLen = (-mViewHeight).toFloat()
                        state = DONE
                        mCurrentViewIndex = 1
                    }
                } else if (state == AUTO_DOWN) {
                    mMoveLen += SPEED
                    if (mMoveLen >= 0) {
                        mMoveLen = 0f
                        state = DONE
                        mCurrentViewIndex = 0
                    }
                } else {
                    mTimer!!.cancel()
                }
            }
            requestLayout()
        }

    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    private fun init() {
        mTimer = MyTimer(handler)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                if (vt == null)
                    vt = VelocityTracker.obtain()
                else
                    vt!!.clear()
                mLastY = ev.y
                vt!!.addMovement(ev)
                mEvents = 0
            }
            MotionEvent.ACTION_POINTER_DOWN, MotionEvent.ACTION_POINTER_UP ->
                // 多一只手指按下或抬起时舍弃将要到来的第一个事件move，防止多点拖拽的bug
                mEvents = -1
            MotionEvent.ACTION_MOVE -> {
                vt!!.addMovement(ev)
                if (canPullUp && mCurrentViewIndex == 0 && mEvents == 0) {
                    mMoveLen += ev.y - mLastY
                    // 防止上下越界
                    if (mMoveLen > 0) {
                        mMoveLen = 0f
                        mCurrentViewIndex = 0
                    } else if (mMoveLen < -mViewHeight) {
                        mMoveLen = (-mViewHeight).toFloat()
                        mCurrentViewIndex = 1

                    }
                    if (mMoveLen < -8) {
                        // 防止事件冲突
                        ev.action = MotionEvent.ACTION_CANCEL
                    }
                } else if (canPullDown && mCurrentViewIndex == 1 && mEvents == 0) {
                    mMoveLen += ev.y - mLastY
                    // 防止上下越界
                    if (mMoveLen < -mViewHeight) {
                        mMoveLen = (-mViewHeight).toFloat()
                        mCurrentViewIndex = 1
                    } else if (mMoveLen > 0) {
                        mMoveLen = 0f
                        mCurrentViewIndex = 0
                    }
                    if (mMoveLen > 8 - mViewHeight) {
                        // 防止事件冲突
                        ev.action = MotionEvent.ACTION_CANCEL
                    }
                } else
                    mEvents++
                mLastY = ev.y
                requestLayout()
            }
            MotionEvent.ACTION_UP -> {
                mLastY = ev.y
                vt!!.addMovement(ev)
                vt!!.computeCurrentVelocity(700)
                // 获取Y方向的速度
                val mYV = vt!!.yVelocity
                if (mMoveLen != 0f && mMoveLen != (-mViewHeight).toFloat()) {


                    if (Math.abs(mYV) < 500) {
                        // 速度小于一定值的时候当作静止释放，这时候两个View往哪移动取决于滑动的距离
                        if (mMoveLen <= -mViewHeight / 2) {
                            state = AUTO_UP
                        } else if (mMoveLen > -mViewHeight / 2) {
                            state = AUTO_DOWN
                        }
                    } else {
                        // 抬起手指时速度方向决定两个View往哪移动
                        if (mYV < 0)
                            state = AUTO_UP
                        else
                            state = AUTO_DOWN
                    }
                    mTimer!!.schedule(2)
                    try {
                        vt!!.recycle()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
        super.dispatchTouchEvent(ev)
        return true
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        topView!!.layout(0, mMoveLen.toInt(), mViewWidth,
                topView!!.measuredHeight + mMoveLen.toInt())
        bottomView!!.layout(0, topView!!.measuredHeight + mMoveLen.toInt(),
                mViewWidth, topView!!.measuredHeight + mMoveLen.toInt()
                + bottomView!!.measuredHeight)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (!isMeasured) {
            isMeasured = true

            mViewHeight = measuredHeight
            mViewWidth = measuredWidth

            topView = getChildAt(0)
            bottomView = getChildAt(1)

            bottomView!!.setOnTouchListener(bottomViewTouchListener)
            topView!!.setOnTouchListener(topViewTouchListener)
        }
    }

    private val topViewTouchListener = OnTouchListener { v, event ->
        val sv = v as ScrollView
        if (sv.scrollY == sv.getChildAt(0).measuredHeight - sv
                .measuredHeight && mCurrentViewIndex == 0)
            canPullUp = true
        else
            canPullUp = false
        false
    }
    private val bottomViewTouchListener = OnTouchListener { v, event ->
        val sv = v as ScrollView
        if (sv.scrollY == 0 && mCurrentViewIndex == 1)
            canPullDown = true
        else
            canPullDown = false
        false
    }

    internal inner class MyTimer(private val handler: Handler) {
        private val timer: Timer
        private var mTask: MyTask? = null

        init {
            timer = Timer()
        }

        fun schedule(period: Long) {
            if (mTask != null) {
                mTask!!.cancel()
                mTask = null
            }
            mTask = MyTask(handler)
            timer.schedule(mTask, 0, period)
        }

        fun cancel() {
            if (mTask != null) {
                mTask!!.cancel()
                mTask = null
            }
        }

        internal inner class MyTask(private val handler: Handler) : TimerTask() {

            override fun run() {
                handler.obtainMessage().sendToTarget()
            }

        }
    }

    companion object {

        /**
         * 自动上滑
         */
        val AUTO_UP = 0
        /**
         * 自动下滑
         */
        val AUTO_DOWN = 1
        /**
         * 动画完成
         */
        val DONE = 2
        /**
         * 动画速度
         */
        val SPEED = 6.5f
    }

}
