package com.lipo.views

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.widget.ImageView
import android.graphics.Bitmap.Config
import android.graphics.drawable.Drawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.PorterDuff.Mode

/**
 * User: howie Date: 13-4-22 Time: 下午5:31
 */
class RoundedImageView(context: Context, attSet: AttributeSet) : ImageView(context, attSet) {

    private var paint: Paint? = null
    private var roundWidth = 5
    private var roundHeight = 5
    private var paint2: Paint? = null

    init {
        init(context, attSet)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        paint = Paint()
        paint!!.color = Color.WHITE
        paint!!.isAntiAlias = true
        paint!!.xfermode = PorterDuffXfermode(Mode.DST_OUT)

        paint2 = Paint()
        paint2!!.xfermode = null
    }

    override fun draw(canvas: Canvas) {

        roundWidth = width / 2
        roundHeight = height / 2
        val bitmap = Bitmap.createBitmap(width, height,
                Config.ARGB_8888)
        val canvas2 = Canvas(bitmap)
        super.draw(canvas2)
        drawLiftUp(canvas2)
        drawRightUp(canvas2)
        drawLiftDown(canvas2)
        drawRightDown(canvas2)
        canvas.drawBitmap(bitmap, 0f, 0f, paint2)
        bitmap.recycle()
    }

    private fun drawLiftUp(canvas: Canvas) {
        val path = Path()
        path.moveTo(0f, roundHeight.toFloat())
        path.lineTo(0f, 0f)
        path.lineTo(roundWidth.toFloat(), 0f)
        path.arcTo(RectF(0f, 0f, (roundWidth * 2).toFloat(), (roundHeight * 2).toFloat()), -90f, -90f)
        path.close()
        canvas.drawPath(path, paint!!)
    }

    private fun drawLiftDown(canvas: Canvas) {
        val path = Path()
        path.moveTo(0f, (height - roundHeight).toFloat())
        path.lineTo(0f, height.toFloat())
        path.lineTo(roundWidth.toFloat(), height.toFloat())
        path.arcTo(RectF(0f, (height - roundHeight * 2).toFloat(),
                (0 + roundWidth * 2).toFloat(), width.toFloat()), 90f, 90f)
        path.close()
        canvas.drawPath(path, paint!!)
    }

    private fun drawRightDown(canvas: Canvas) {
        val path = Path()
        path.moveTo((width - roundWidth).toFloat(), height.toFloat())
        path.lineTo(width.toFloat(), height.toFloat())
        path.lineTo(width.toFloat(), (height - roundHeight).toFloat())
        path.arcTo(RectF((width - roundWidth * 2).toFloat(), (height - roundHeight * 2).toFloat(), width.toFloat(), height.toFloat()), 0f, 90f)
        path.close()
        canvas.drawPath(path, paint!!)
    }

    private fun drawRightUp(canvas: Canvas) {
        val path = Path()
        path.moveTo(width.toFloat(), roundHeight.toFloat())
        path.lineTo(width.toFloat(), 0f)
        path.lineTo((width - roundWidth).toFloat(), 0f)
        path.arcTo(RectF((width - roundWidth * 2).toFloat(), 0f, width.toFloat(),
                (0 + roundHeight * 2).toFloat()), -90f, 90f)
        path.close()
        canvas.drawPath(path, paint!!)
    }

    /**
     * 转换图片成圆形
     *
     * @param bitmap
     * 传入Bitmap对象
     * @return
     */
    fun toRoundBitmap(bitmap: Bitmap): Bitmap {
        var width = bitmap.width
        var height = bitmap.height
        val roundPx: Float
        val left: Float
        val top: Float
        val right: Float
        val bottom: Float
        val dst_left: Float
        val dst_top: Float
        val dst_right: Float
        val dst_bottom: Float
        if (width <= height) {
            roundPx = (width / 2).toFloat()
            top = 0f
            bottom = width.toFloat()
            left = 0f
            right = width.toFloat()
            height = width
            dst_left = 0f
            dst_top = 0f
            dst_right = width.toFloat()
            dst_bottom = width.toFloat()
        } else {
            roundPx = (height / 2).toFloat()
            val clip = ((width - height) / 2).toFloat()
            left = clip
            right = width - clip
            top = 0f
            bottom = height.toFloat()
            width = height
            dst_left = 0f
            dst_top = 0f
            dst_right = height.toFloat()
            dst_bottom = height.toFloat()
        }

        val output = Bitmap.createBitmap(width, height, Config.ARGB_8888)
        val canvas = Canvas(output)

        val color = 0xff424242.toInt()
        val paint = Paint()
        val src = Rect(left.toInt(), top.toInt(), right.toInt(),
                bottom.toInt())
        val dst = Rect(dst_left.toInt(), dst_top.toInt(),
                dst_right.toInt(), dst_bottom.toInt())
        val rectF = RectF(dst)

        paint.isAntiAlias = true

        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint)

        paint.xfermode = PorterDuffXfermode(Mode.SRC_IN)
        canvas.drawBitmap(bitmap, src, dst, paint)
        return output
    }

    override fun setImageBitmap(bm: Bitmap) {
        val square = toRoundBitmap(bm)
        super.setImageBitmap(square)
        // if (bm.getWidth() != bm.getHeight())
        // {
        //
        // }
        // else
        // {
        // super.setImageBitmap(bm);
        // }

    }
}