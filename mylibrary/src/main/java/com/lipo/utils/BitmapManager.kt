package com.lipo.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.Config
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PorterDuff.Mode
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.net.Uri
import android.util.DisplayMetrics

import android.graphics.Bitmap.CompressFormat
import java.io.*


object BitmapManager {

    fun scaleBitmap(context: Context, bitmap: Bitmap): Bitmap {
        val dm = context.resources.displayMetrics
        val screenWidth = dm.widthPixels
        val width = bitmap.width
        val height = bitmap.height
        val scaleWidth = (screenWidth - DisplayUtil.dip2px(context,
                24f)).toFloat() / width

        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleWidth)

        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
    }

    /**
     * 缩放图片
     *
     * @param dst
     * @param width
     * @param height
     * @return
     */
    fun getBitmapFromFile(dst: File?, width: Int, height: Int): Bitmap? {
        if (null != dst && dst.exists()) {
            var opts: BitmapFactory.Options? = null
            if (width > 0 && height > 0) {
                opts = BitmapFactory.Options()
                opts.inJustDecodeBounds = true
                BitmapFactory.decodeFile(dst.path, opts)

                if (isScale(opts)) {
                    // 计算图片缩放比例
                    val minSideLength = Math.min(width, height)
                    opts.inSampleSize = computeSampleSize(opts, minSideLength,
                            width * height)
                }
                opts.inJustDecodeBounds = false
                opts.inInputShareable = true
                opts.inPurgeable = true
            }
            try {
                return BitmapFactory.decodeFile(dst.path, opts)
            } catch (e: OutOfMemoryError) {
                e.printStackTrace()
            }

        }
        return null
    }

    /**
     * 缩放图片
     *
     * @param context
     * @param uri
     * @param width
     * @param height
     * @return
     * @throws FileNotFoundException
     */
    @Throws(FileNotFoundException::class)
    fun getBitmapFromUri(context: Context, uri: Uri?, width: Int,
                         height: Int): Bitmap? {
        if (null != uri) {
            var opts: BitmapFactory.Options? = null
            if (width > 0 && height > 0) {
                opts = BitmapFactory.Options()
                opts.inJustDecodeBounds = true
                BitmapFactory.decodeStream(context.contentResolver
                        .openInputStream(uri), null, opts)
                // 计算图片缩放比例
                val minSideLength = Math.min(width, height)
                opts.inSampleSize = computeSampleSize(opts, minSideLength,
                        width * height)
                opts.inJustDecodeBounds = false
                opts.inInputShareable = true
                opts.inPurgeable = true
            }
            try {
                return BitmapFactory.decodeStream(context.contentResolver
                        .openInputStream(uri), null, opts)
            } catch (e: OutOfMemoryError) {
                e.printStackTrace()
            }

        }
        return null
    }

    fun computeSampleSize(options: BitmapFactory.Options,
                          minSideLength: Int, maxNumOfPixels: Int): Int {
        val initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels)

        var roundedSize: Int
        if (initialSize <= 8) {
            roundedSize = 1
            while (roundedSize < initialSize) {
                roundedSize = roundedSize shl 1
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8
        }

        return roundedSize
    }

    private fun computeInitialSampleSize(options: BitmapFactory.Options,
                                         minSideLength: Int, maxNumOfPixels: Int): Int {
        val w = options.outWidth.toDouble()
        val h = options.outHeight.toDouble()

        val lowerBound = if (maxNumOfPixels == -1)
            1
        else
            Math.ceil(Math
                    .sqrt(w * h / maxNumOfPixels)).toInt()
        val upperBound = if (minSideLength == -1)
            128
        else
            Math.min(
                    Math.floor(w / minSideLength), Math.floor(h / minSideLength)).toInt()

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound
        }

        return if (maxNumOfPixels == -1 && minSideLength == -1) {
            1
        } else if (minSideLength == -1) {
            lowerBound
        } else {
            upperBound
        }
    }

    private fun big(bitmap: Bitmap): Bitmap {
        val matrix = Matrix()
        matrix.postScale(1.5f, 1.5f) // 长和宽放大缩小的比例
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width,
                bitmap.height, matrix, true)
    }

    fun GetRoundedCornerBitmap(bitmap: Bitmap, roundPx: Float): Bitmap {
        try {
            val output = Bitmap.createBitmap(bitmap.width,
                    bitmap.height, Config.ARGB_8888)
            val canvas = Canvas(output)
            val paint = Paint()
            val rect = Rect(0, 0, bitmap.width,
                    bitmap.height)
            val rectF = RectF(Rect(0, 0, bitmap.width,
                    bitmap.height))
            paint.isAntiAlias = true
            canvas.drawARGB(0, 0, 0, 0)
            paint.color = Color.BLACK
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint)

            val block = Rect(0, 0, roundPx.toInt(), roundPx.toInt())
            canvas.drawRect(block, paint)
            val blockr = Rect(bitmap.width - roundPx.toInt(), 0,
                    bitmap.width, roundPx.toInt())
            canvas.drawRect(blockr, paint)

            paint.xfermode = PorterDuffXfermode(Mode.SRC_IN)

            val src = Rect(0, 0, bitmap.width,
                    bitmap.height)

            canvas.drawBitmap(bitmap, src, rect, paint)
            return output
        } catch (e: Exception) {
            return bitmap
        }

    }

    fun isScale(options: BitmapFactory.Options): Boolean {
        val width = options.outWidth
        val height = options.outHeight

        val whmin = Math.min(width, height)
        val whmax = Math.max(width, height)

        return if (whmin < 600 && whmax < 600) {
            false
        } else {
            true
        }
    }

    fun saveMyBitmap(mBitmap: Bitmap, bitName: String) {
        val f = File(bitName)
        var fOut: FileOutputStream? = null
        try {
            fOut = FileOutputStream(f)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
        try {
            fOut!!.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        try {
            fOut!!.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    fun bmpToByteArray(bmp: Bitmap, needRecycle: Boolean): ByteArray {
        val output = ByteArrayOutputStream()
        bmp.compress(CompressFormat.PNG, 100, output)
        if (needRecycle) {
            bmp.recycle()
        }

        val result = output.toByteArray()
        try {
            output.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return result
    }

}
