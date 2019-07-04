package com.lipo.utils

import android.graphics.Bitmap
import android.graphics.Bitmap.Config
import android.graphics.Canvas
import android.view.View

object CropView {

    fun cropImageView(imageView: View): Bitmap {
        val width = imageView.width
        val height = imageView.height
        val bm = Bitmap.createBitmap(height / 2, height / 2,
                Config.ARGB_8888)
        val canvas = Canvas(bm)
        canvas.translate((-width - height / 2).toFloat() / 2, -height.toFloat() / 4)
        imageView.draw(canvas)
        return bm
    }

}
