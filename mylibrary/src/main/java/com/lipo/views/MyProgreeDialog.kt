package com.lipo.views

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView

import com.lipo.mylibrary.R

class MyProgreeDialog : AlertDialog {

    private var dialog_image: ImageView? = null
    private var animation: Animation? = null

    constructor(context: Context) : super(context, R.style.my_dialog) {
    }

    protected constructor(context: Context, theme: Int) : super(context, theme) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_view)
        setCanceledOnTouchOutside(false)
        initView()
    }

    private fun initView() {
        dialog_image = findViewById<ImageView>(R.id.dialog_image)

        animation = AnimationUtils.loadAnimation(context,
                R.anim.loading_animation)

//        dialog_image!!.startAnimation(animation)
        
    }

    override fun show() {
        super.show()
        dialog_image!!.startAnimation(animation)
    }

}
