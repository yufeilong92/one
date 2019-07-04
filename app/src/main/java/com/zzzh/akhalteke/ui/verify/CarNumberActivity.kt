package com.zzzh.akhalteke.ui.verify

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.ui.BaseActivity
import com.zzzh.akhalteke.ui.verify.newverify.CarNewActivity
import com.zzzh.akhalteke.utils.KeyboardUtil
import com.zzzh.akhalteke.utils.StringUtil
import com.zzzh.akhalteke.utils.Utils
import kotlinx.android.synthetic.main.activity_carnumber.*

/**
 * @Author : YFL  is Creating a porject in Lenovo
 * @Email : yufeilong92@163.com
 * @Time :2019/3/6 12:07
 * @Purpose :车牌号选择页
 */
class CarNumberActivity : BaseActivity() {
    var keyboardUtil: KeyboardUtil? = null

    companion object {
        val CARNUMBERKEY: String = "carnumber"
    }

    override fun setContentView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_carnumber)
        initView()
        initEvent()

    }

    fun initEvent() {
        tv_car_number_cancle.setOnClickListener {
            finishBase()
        }
        tv_car_number_sure.setOnClickListener {
            val str = Utils.getObjToStr(et_input_car_number)
            if (StringUtil.isEmpty(str)) {
                showToast("请输入车牌号")
                return@setOnClickListener
            }
            if (str.length==8||str.length==9){
                var intent = Intent();
                intent.putExtra(CARNUMBERKEY, str)
                setResult(CarNewActivity.CARLENGTHNUMBER, intent)
                finishBase()
            }else{
                showToast("请输入正确的车牌号")
            }
        }
    }

    fun initView() {
        et_input_car_number.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (keyboardUtil == null) {
                    keyboardUtil = KeyboardUtil(
                            this@CarNumberActivity, et_input_car_number,
                            keyboard_view,ll_car_number
                    );
                    keyboardUtil!!.hideSoftInputMethod()
                    keyboardUtil!!.showKeyboard()
                } else {
                    keyboardUtil!!.showKeyboard()
                }
                return false
            }

        })

        et_input_car_number.addTextChangedListener(object : TextWatcher {
            var afterLengh: Int = 0
            var changerLengh: Int = 0
            var beforeLengh: Int = 0
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                afterLengh = s!!.length;
                beforeLengh = s!!.length;
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                changerLengh = s!!.length
            }

            override fun afterTextChanged(s: Editable?) {
                if (afterLengh == 1 && changerLengh == 2) {
                    val str: String = Utils.getObjToStr(et_input_car_number)
                    if (!StringUtil.isEmpty(str)) {
                        var a = str.length
                        if (a == 2) {
                            et_input_car_number.setText(str + "•")
                            et_input_car_number.setSelection(3)
                        }
                    }
                }
                if (afterLengh == 3 && changerLengh == 2) {
                    val str: String = Utils.getObjToStr(et_input_car_number)
                    if (!StringUtil.isEmpty(str)) {
                        var a = str.subSequence(0, 1)
                        et_input_car_number.setText(a)
                        et_input_car_number.setSelection(1)
                    }
                }
            }
        })


    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        var boole = Utils.isInEditext(keyboard_view, event!!)
        if (boole == false) {
            if (keyboardUtil != null && keyboardUtil!!.isShow) {
                keyboardUtil!!.hideKeyboard()
            }
        }
        return super.onTouchEvent(event)

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (keyboardUtil != null && keyboardUtil!!.isShow) {
                keyboardUtil!!.hideKeyboard()
            } else {
                finish()
            }
        }
        return false
    }


}