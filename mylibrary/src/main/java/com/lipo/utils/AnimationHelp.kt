package com.lipo.utils


import android.content.Context
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation

import com.lipo.views.ToastView

class AnimationHelp(view: View, context: Context) {
    private val am: Animation

    init {

        ToastView.setToasd(context, "开始摇晃")

        val lin = LinearInterpolator()
        am = RotateAnimation(0f, 45f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)


        am.setDuration(50)


        am.setRepeatMode(Animation.REVERSE)
        am.setRepeatCount(20)
        am.setInterpolator(lin)

        am.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationRepeat(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                am.cancel()
                am.reset()
            }
        })


        //		view.setAnimation(am);
        view.startAnimation(am)

        //		am.startNow();
    }

    fun cancel() {
        am.cancel()
        am.reset()
    }

}
