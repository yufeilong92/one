package com.weigan.loopview.dialog

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.weigan.loopview.LoopView
import com.weigan.loopview.R


abstract class WheelDialog(var mContext: Context) : AlertDialog(mContext, R.style.my_dialog), View.OnClickListener {

    private var metrics: DisplayMetrics = context.resources.displayMetrics

    private lateinit var cancelView: TextView
    private lateinit var sureView: TextView
    private var loopView: LoopView? = null
    private var itemsData:MutableList<String> = mutableListOf()

    private var positionSelect = 0

    init {
        window!!.setWindowAnimations(R.style.popup_animation)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSizeMode()
        setContentView(R.layout.dialog_loop)

        initView()
    }

    private fun initView() {
        cancelView = findViewById(R.id.dialog_wheel_cancel)
        sureView = findViewById(R.id.dialog_wheel_sure)
        loopView = findViewById(R.id.dialog_wheel_loop)
        loopView!!.setListener { index -> positionSelect = index }

        cancelView.setOnClickListener(this)
        sureView.setOnClickListener(this)

        loopView!!.setNotLoop()

        if(itemsData.size>0){
            loopView!!.setItems(itemsData)
        }
    }

    fun toSetData(datas: MutableList<String>) {
        itemsData = datas
        if (loopView != null) {
            loopView!!.setItems(datas)
        }
    }

    fun toSetSelecedItemPosition(sPosition: Int = 0) {
        if (loopView != null) {
            loopView!!.setInitPosition(sPosition)
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            cancelView -> {
                dismiss()
            }
            sureView -> {
                dialogCallBack(positionSelect)
                dismiss()
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

    override fun dismiss() {
        super.dismiss()
        dismissLinstener()
    }

    abstract fun dialogCallBack(position: Int)
    abstract fun dismissLinstener()
}