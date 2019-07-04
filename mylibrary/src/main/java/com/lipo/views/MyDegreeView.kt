package com.lipo.views

import android.R.color
import android.content.Context
import android.content.res.AssetManager
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.Style
import android.graphics.Path
import android.graphics.Path.Direction
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View

class MyDegreeView : View {

    private var widthScreen: Int = 0// 屏幕宽度
    private var widthDegree: Int = 0// 控件的宽度
    private var heightDegree: Int = 0// 控件的高度
    private var heightBorder: Int = 0// 边框的高度
    private var widthIn: Int = 0// 内部的宽
    private var heightIn: Int = 0// 内部的高度

    private var bgdColor: Int = 0// 灰色
    private var borderColor: Int = 0// 白色
    private var degreeColor: Int = 0// 红色

    private var percent: Int = 0

    private var paint: Paint? = null
    private var path: Path? = null
    private var pathIn: Path? = null
    private var rectfBg: RectF? = null
    private var rectfBorder: RectF? = null
    private var rectIn: RectF? = null

    private var asset: AssetManager? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        widthScreen = context.resources.displayMetrics.widthPixels
        heightDegree = widthScreen / 15
        heightBorder = heightDegree / 7
        heightIn = heightDegree * 5 / 7

        bgdColor = resources.getColor(color.black)
        borderColor = resources.getColor(color.black)
        degreeColor = resources.getColor(color.black)

        paint = Paint()
        path = Path()
        pathIn = Path()
        rectfBg = RectF()
        rectfBorder = RectF()
        rectIn = RectF()

        asset = context.assets
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        widthDegree = width
        widthIn = widthDegree - 2 * heightBorder

        rectfBg!!.set(0f, 0f, widthDegree.toFloat(), heightDegree.toFloat())
        path!!.addRoundRect(rectfBg, (heightDegree / 2).toFloat(), (heightDegree / 2).toFloat(),
                Direction.CCW)
        canvas.clipPath(path!!)

        paint!!.isAntiAlias = true
        paint!!.isSubpixelText = true

        paint!!.color = bgdColor
        paint!!.style = Style.FILL
        paint!!.strokeWidth = 0f
        canvas.drawRoundRect(rectfBg!!, (heightDegree / 2).toFloat(), (heightDegree / 2).toFloat(), paint!!)

        rectfBorder!!.set((heightBorder / 2).toFloat(), (heightBorder / 2).toFloat(),
                (widthDegree - heightBorder / 2).toFloat(),
                (heightDegree - heightBorder / 2).toFloat())
        paint!!.color = borderColor
        paint!!.style = Style.STROKE
        paint!!.strokeWidth = heightBorder.toFloat()
        canvas.drawRoundRect(rectfBorder!!, ((heightIn + heightBorder) / 2).toFloat(),
                ((heightIn + heightBorder) / 2).toFloat(), paint!!)

        canvas.save()
        rectIn!!.set(heightBorder.toFloat(), heightBorder.toFloat(), (percent * (widthDegree - heightBorder) / 100).toFloat(),
                (heightDegree - heightBorder).toFloat())
        pathIn!!.addRoundRect(rectIn, (widthIn / 2).toFloat(), (widthIn / 2).toFloat(), Direction.CCW)
        canvas.clipPath(path!!)

        paint!!.color = degreeColor
        paint!!.style = Style.FILL
        paint!!.strokeWidth = 0f
        canvas.drawRoundRect(rectIn!!, (widthIn / 2).toFloat(), (widthIn / 2).toFloat(), paint!!)
        canvas.restore()

        paint!!.color = borderColor
        paint!!.textSize = heightIn.toFloat()
        paint!!.typeface = Typeface.createFromAsset(asset, "font/FZY4JW.TTF")
        canvas.drawText(percent.toString() + "%", (widthIn / 2 - heightIn).toFloat(), (heightBorder + heightIn - 2).toFloat(), paint!!)

    }

    fun setPercent(percent: Int) {
        this.percent = percent
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpec = heightMeasureSpec
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        if (heightMode != View.MeasureSpec.EXACTLY) {
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(heightDegree,
                    heightMode)
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

}
