package com.zzzh.akhalteke.view.customview

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.utils.Constant


class MyBarView:View{
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        var tda: TypedArray = context!!.obtainStyledAttributes(attrs, R.styleable.MyBarStyle)

        val barbg = tda.getColor(R.styleable.MyBarStyle_barBg,Color.TRANSPARENT)

        setBackgroundColor(barbg)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(
            Constant.screenWidth,
            Constant.statusHeight
        )
    }

}