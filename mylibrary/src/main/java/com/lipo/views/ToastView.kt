package com.lipo.views

import java.util.Timer
import java.util.TimerTask

import com.lipo.mylibrary.R

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast

class ToastView {
    private var time: Int = 0
    private var timer: Timer? = null

    constructor(context: Context, text: String) {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.toast_view, null)
        val t = view.findViewById(R.id.toast_text) as TextView
        t.text = text
        if (toast != null) {
            toast!!.cancel()
        }
        toast = Toast(context)
        toast!!.duration = Toast.LENGTH_SHORT
        toast!!.view = view
    }

    constructor(context: Context, text: Int) {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.toast_view, null)
        val t = view.findViewById(R.id.toast_text) as TextView
        t.setText(text)
        if (toast != null) {
            toast!!.cancel()
        }
        toast = Toast(context)
        toast!!.duration = Toast.LENGTH_SHORT
        toast!!.view = view
    }

    // 设置toast显示位置
    fun setGravity(gravity: Int, xOffset: Int, yOffset: Int) {
        // toast.setGravity(Gravity.CENTER, 0, 0); //居中显示
        toast!!.setGravity(gravity, xOffset, yOffset)
    }

    // 设置toast显示时间
    fun setDuration(duration: Int) {
        toast!!.duration = duration
    }

    // 设置toast显示时间(自定义时间)
    fun setLongTime(duration: Int) {
        // toast.setDuration(duration);
        time = duration
        timer = Timer()
        timer!!.schedule(object : TimerTask() {
            override fun run() {

                if (time - 1000 >= 0) {
                    show()
                    time = time - 1000
                } else {
                    timer!!.cancel()
                }
            }
        }, 0, 1000)
    }

    fun show() {
        toast!!.show()
    }

    companion object {

        var toast: Toast? = null
        private val falsh = false

        fun cancel() {
            if (toast != null) {
                toast!!.cancel()
            }
        }

        fun setToasd(context: Context, nor: String) {
            val toast = ToastView(context, nor)
            toast.setGravity(Gravity.CENTER, 0, 0)
            toast.show()
        }

        fun setToasdC(context: Context, nor: String) {
            if (falsh) {
                val toast = ToastView(context, nor)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
            }
        }
    }

}
