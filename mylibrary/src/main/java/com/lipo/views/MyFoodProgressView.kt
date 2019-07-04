package com.lipo.views

import android.R.color
import android.content.Context
import android.content.res.AssetManager
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.Style
import android.graphics.Path
import android.graphics.Path.Direction
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View

class MyFoodProgressView : View {
    private var foodsbgColor: Int = 0//
    private var widthScreen: Int = 0
    private var widthFoods: Int = 0
    private var widthIn: Int = 0
    private var borderW: Int = 0

    private var mPaint: Paint? = null
    private var path: Path? = null
    private var rect: RectF? = null
    private var rectBorder: RectF? = null
    private var rectBounds: Rect? = null

    private var mPercent = 0
    private var content = ""
    private var percentStr = ""

    private var mTextColor = DEFAULT_TEXTCOLOT

    private var asset: AssetManager? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        asset = context.assets
        widthScreen = context.resources.displayMetrics.widthPixels
        widthFoods = widthScreen / 5
        widthIn = widthFoods * 5 / 6
        borderW = widthFoods / 12
        path = Path()
        mPaint = Paint()
        rectBounds = Rect()
        rect = RectF(0f, 0f, widthFoods.toFloat(), widthFoods.toFloat())
        rectBorder = RectF(borderW.toFloat(), borderW.toFloat(), (widthFoods - borderW).toFloat(),
                (widthFoods - borderW).toFloat())
        foodsbgColor = resources.getColor(color.black)

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        path!!.addRoundRect(rect, (widthFoods / 6).toFloat(), (widthFoods / 6).toFloat(), Direction.CCW)
        canvas.clipPath(path!!)

        mPaint!!.isAntiAlias = true
        mPaint!!.isSubpixelText = true

        mPaint!!.color = foodsbgColor
        mPaint!!.style = Style.FILL
        mPaint!!.strokeWidth = 0f
        canvas.drawRoundRect(rect!!, (widthFoods / 6).toFloat(), (widthFoods / 6).toFloat(), mPaint!!)

        mPaint!!.color = mTextColor
        mPaint!!.style = Style.FILL
        mPaint!!.strokeWidth = 0f
        canvas.drawRect(0f, ((100 - mPercent) * widthIn / 100 + borderW).toFloat(),
                widthFoods.toFloat(), widthFoods.toFloat(), mPaint!!)

        mPaint!!.color = DEFAULT_TEXTCOLOT
        mPaint!!.strokeWidth = (borderW * 2).toFloat()
        mPaint!!.style = Style.STROKE
        canvas.drawRoundRect(rect!!, (widthFoods / 6).toFloat(), (widthFoods / 6).toFloat(), mPaint!!)

        mPaint!!.color = DEFAULT_TEXTCOLOT
        mPaint!!.strokeWidth = 5f
        mPaint!!.textSize = (widthIn * 2 / 5).toFloat()
        mPaint!!.typeface = Typeface.createFromAsset(asset, "font/FZY4JW.TTF")
        canvas.drawText(content, (borderW + widthIn / 10).toFloat(),
                (borderW + widthIn * 3 / 5).toFloat(), mPaint!!)

        mPaint!!.color = mTextColor
        mPaint!!.style = Style.FILL
        mPaint!!.strokeWidth = 0f
        mPaint!!.textSize = (widthIn * 2 / 5).toFloat()
        canvas.drawText(content, (borderW + widthIn / 10).toFloat(),
                (borderW + widthIn * 3 / 5).toFloat(), mPaint!!)

        mPaint!!.color = resources.getColor(color.white)
        mPaint!!.strokeWidth = 0f
        mPaint!!.textSize = (widthIn * 3 / 10).toFloat()
        mPaint!!.getTextBounds(percentStr, 0, percentStr.length, rectBounds)
        canvas.drawText(percentStr, ((widthFoods - rectBounds!!.width()) / 2).toFloat(),
                (borderW + widthIn * 19 / 20).toFloat(), mPaint!!)
    }

    fun setContent(content: String) {
        this.content = content
    }

    fun setPercent(percent: Int) {
        this.mPercent = percent
        postInvalidate()
    }

    fun setContentColor(color: Int) {
        this.mTextColor = color
    }

    fun showPercent() {
        percentStr = mPercent.toString() + "%"
    }

    fun dismissPercent() {
        percentStr = ""
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    companion object {

        private val DEFAULT_TEXTCOLOT = 0xFFFFFFFF.toInt()
        private val DEFAULT_TEXTSIZE = 250
    }
}
