package com.zzzh.akhalteke.view.dialog

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.bean.DataMessageVo
import kotlinx.android.synthetic.main.dialog_show_car_color.*

class SelectCarColorDialog(context: Context, var m: SelectCarColorInterface) :
    AlertDialog(context, R.style.mydialogNobg), View.OnClickListener {
    private var metrics: DisplayMetrics = context.resources.displayMetrics

    init {
        window!!.setWindowAnimations(R.style.popup_animation)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSizeMode()
        setContentView(R.layout.dialog_show_car_color)
        initView()
    }

    fun initView() {
        iv_select_color_close.setOnClickListener(this)
        iv_bule_pz.setOnClickListener(this)
        iv_green_pz.setOnClickListener(this)
        iv_yellow_pz.setOnClickListener(this)
        iv_huan_pz.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            iv_select_color_close -> {
                dismiss()
            }
            iv_bule_pz -> {
                dismiss()
                m.onItemClickListener(DataMessageVo.BULE)
            }
            iv_green_pz -> {
                dismiss()
                m.onItemClickListener(DataMessageVo.GREEN)
            }
            iv_huan_pz -> {
                dismiss()
                m.onItemClickListener(DataMessageVo.HUN)
            }
            iv_yellow_pz -> {
                dismiss()
                m.onItemClickListener(DataMessageVo.YELLOW)
            }
        }
    }

    private fun setSizeMode() {
        val params = window!!.attributes
        params.width = metrics.widthPixels
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        window!!.attributes = params
        window!!.setGravity(Gravity.BOTTOM)
    }

    interface SelectCarColorInterface {
        fun onItemClickListener(id: Int);
    }
}