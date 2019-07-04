package com.lipo.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.Style
import android.graphics.Path
import android.graphics.Path.Direction
import android.graphics.Region.Op
import android.util.AttributeSet
import android.widget.FrameLayout

class MyRinklineView(context: Context, attrs: AttributeSet)// TODO Auto-generated constructor stub
    : FrameLayout(context, attrs) {
    internal var paint = Paint()

    var bgColor = DEFAULT_COLOR
    var borderColor = DEFAULT_COLOR

    private var mPercent: Int = 0

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)

        val width = width
        val height = height

        val path = Path()
        canvas.save()
        path.addRect(0f, 0f, width.toFloat(), height.toFloat(), Direction.CCW)
        canvas.clipPath(path, Op.REPLACE)

        paint.color = bgColor
        paint.style = Style.FILL
        canvas.drawRect(0f, 0f, (mPercent * width / 100).toFloat(), height.toFloat(), paint)

        paint.color = borderColor
        paint.isAntiAlias = true
        paint.style = Style.STROKE
        paint.strokeWidth = 2f
        val path2 = Path()
        path2.addRect(1f, 1f, (width - 1).toFloat(), (height - 1).toFloat(), Direction.CCW)
        canvas.drawPath(path2, paint)

        canvas.restore()
    }

    fun getmPercent(): Float {
        return mPercent.toFloat()
    }

    fun setmPercent(mPercent: Int) {
        this.mPercent = mPercent
        postInvalidate()
    }

    companion object {
        private val DEFAULT_COLOR = 0xFFFFFFFF.toInt()
    }


}
