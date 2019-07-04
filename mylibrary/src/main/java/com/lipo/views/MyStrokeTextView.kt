package com.lipo.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.Style
import android.graphics.Rect
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View

class MyStrokeTextView : View {

    private val DEFLAUT_COLOR = 0xffffff

    private val paint = Paint()
    private val rect = Rect()

    private var face: Typeface? = null
    private var borderColor: Int = 0
    private var contentColor: Int = 0
    private var contentSize = 0
    private var strokeWidth = 0
    private var content = ""

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        face = Typeface.createFromAsset(context.assets, "font/FZY4JW.TTF")
        contentColor = Color.WHITE
        borderColor = contentColor
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        paint.isAntiAlias = true
        paint.isSubpixelText = true
        paint.typeface = face
        paint.textSize = contentSize.toFloat()
        paint.getTextBounds(content, 0, content.length, rect)
        paint.color = borderColor
        paint.style = Style.STROKE
        paint.strokeWidth = strokeWidth.toFloat()
        canvas.drawText(content, (strokeWidth / 2).toFloat(), (rect.height() + strokeWidth / 2).toFloat(), paint)

        paint.color = contentColor
        paint.strokeWidth = 0f
        paint.style = Style.FILL
        canvas.drawText(content, (strokeWidth / 2).toFloat(), (rect.height() + strokeWidth / 2).toFloat(), paint)

    }

    fun setFace(face: Typeface) {
        this.face = face
        postInvalidate()
    }

    fun setBorderColor(borderColor: Int) {
        this.borderColor = borderColor
        postInvalidate()
    }

    fun setContentColor(contentColor: Int) {
        this.contentColor = contentColor
        postInvalidate()
    }

    fun setContentSize(contentSize: Int) {
        this.contentSize = contentSize
        postInvalidate()
    }

    fun setStrokeWidth(strokeWidth: Int) {
        this.strokeWidth = strokeWidth
        postInvalidate()
    }

    fun setContent(content: String) {
        this.content = content
        postInvalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthMeasureSpec = widthMeasureSpec
        var heightMeasureSpec = heightMeasureSpec
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)

        if (widthMode != View.MeasureSpec.EXACTLY) {
            widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(rect.width() + strokeWidth, widthMode)
        }

        if (heightMode != View.MeasureSpec.EXACTLY) {
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(rect.height() + strokeWidth, heightMode)
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int,
                          bottom: Int) {
        super.onLayout(changed, 0, 0, rect.width() + strokeWidth, rect.height() + strokeWidth)
    }

}
