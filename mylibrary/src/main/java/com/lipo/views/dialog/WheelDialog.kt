//package com.lipo.views.dialog
//
//import android.app.AlertDialog
//import android.content.Context
//import android.os.Bundle
//import android.util.DisplayMetrics
//import android.view.Gravity
//import android.view.View
//import android.view.WindowManager
//import android.widget.TextView
//import com.lipo.mylibrary.R
//
//
//abstract class WheelDialog(var mContext: Context) : AlertDialog(mContext, R.style.my_dialog), View.OnClickListener {
//
//    private var metrics: DisplayMetrics = context.resources.displayMetrics
//
//    private lateinit var cancelView:TextView
//    private lateinit var sureView:TextView
////    private lateinit var wheelView:WheelPicker
//
//    private var dataSelect = ""
//    private var positionSelect = 0
//
//    init {
//        window!!.setWindowAnimations(R.style.popup_animation)
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setSizeMode()
//        setContentView(R.layout.dialog_wheel)
//
////        initView()
//    }
//
////    private fun initView() {
////        cancelView = findViewById(R.id.dialog_wheel_cancel)
////        sureView = findViewById(R.id.dialog_wheel_sure)
////        wheelView = findViewById(R.id.dialog_wheel_picker)
////        wheelView.setOnItemSelectedListener { picker, data, position ->
////            dataSelect = data as String
////            positionSelect = position
////        }
////    }
////
////    fun toSetData(datas:MutableList<String>){
////        if(wheelView!=null){
////            wheelView.data = datas
////        }
////    }
////
////    fun toSetSelecedItemPosition(sPosition:Int){
////        wheelView.selectedItemPosition = sPosition
////    }
//
//    override fun onClick(v: View?) {
//        when (v) {
//            cancelView -> {
//                dialogCallBack(dataSelect,positionSelect)
//                dismiss()
//            }
//            sureView -> {
//                dismiss()
//            }
//        }
//    }
//
//    private fun setSizeMode() {
//        val params = window!!.attributes
//        params.width = metrics.widthPixels
//        params.height = WindowManager.LayoutParams.WRAP_CONTENT
//        window!!.attributes = params
//        window!!.setGravity(Gravity.BOTTOM)
//    }
//
//    override fun dismiss() {
//        super.dismiss()
//        dismissLinstener()
//    }
//
//    abstract fun dialogCallBack(item:String,position:Int)
//    abstract fun dismissLinstener()
//}