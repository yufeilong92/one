package com.zzzh.akhalteke.utils

import android.app.Activity
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView

import android.text.InputType
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.LinearLayout
import com.zzzh.akhalteke.R

import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

/**
 * Created by yechao on 2018/5/15/015.
 * Describe :
 */
class KeyboardUtil(
    private val mActivity: Activity, private val mEdit: EditText,
    val mKeyboardView: KeyboardView,var mLl:LinearLayout
) {

    /**
     * 省份简称键盘
     */
    private val provinceKeyboard: Keyboard
    /**
     * 数字与大写字母键盘
     */
    private val numberKeyboard: Keyboard

    private val listener = object : KeyboardView.OnKeyboardActionListener {
        override fun swipeUp() {}

        override fun swipeRight() {}

        override fun swipeLeft() {}

        override fun swipeDown() {}

        override fun onText(text: CharSequence) {

        }

        override fun onRelease(primaryCode: Int) {}

        override fun onPress(primaryCode: Int) {}

        override fun onKey(primaryCode: Int, keyCodes: IntArray) {
            val editable = mEdit.text
            val start = mEdit.selectionStart
            //判定是否是中文的正则表达式 [\\u4e00-\\u9fa5]判断一个中文 [\\u4e00-\\u9fa5]+多个中文
            val reg = "[\\u4e00-\\u9fa5]"
            if (primaryCode == -1) {// 省份简称与数字键盘切换
                if (mEdit.text.toString().matches(reg.toRegex())) {
                    changeKeyboard(true)
                }
            } else if (primaryCode == -3) {
                if (editable != null && editable.length > 0) {
                    //没有输入内容时软键盘重置为省份简称软键盘
                    if (editable.length == 1) {
                        changeKeyboard(false)
                    }
                    if (start > 0) {
                        editable.delete(start - 1, start)
                    }
                }
            } else {
                editable!!.insert(start, Character.toString(primaryCode.toChar()))
                // 判断第一个字符是否是中文,是，则自动切换到数字软键盘
                if (mEdit.text.toString().matches(reg.toRegex())) {
                    changeKeyboard(true)
                }
            }
        }
    }

    /**
     * 软键盘展示状态
     */
    val isShow: Boolean
        get() = mKeyboardView.visibility == View.VISIBLE

    init {
        provinceKeyboard = Keyboard(mActivity, R.xml.province_abbreviation)
        numberKeyboard = Keyboard(mActivity, R.xml.number_or_letters)
        mKeyboardView.keyboard = provinceKeyboard
        mKeyboardView.isEnabled = true
        mKeyboardView.isPreviewEnabled = false
        mKeyboardView.setOnKeyboardActionListener(listener)
    }

    /**
     * 指定切换软键盘 isNumber false表示要切换为省份简称软键盘 true表示要切换为数字软键盘
     */
    private fun changeKeyboard(isNumber: Boolean) {
        if (isNumber) {
            mKeyboardView.keyboard = numberKeyboard
        } else {
            mKeyboardView.keyboard = provinceKeyboard
        }
    }

    /**
     * 软键盘展示
     */
    fun showKeyboard() {
        val visibility = mKeyboardView.visibility
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            mKeyboardView.visibility = View.VISIBLE
            mLl.visibility=View.VISIBLE
        }
        val llvisibilty = mLl.visibility
        if (llvisibilty==View.GONE){
            mLl.visibility=View.VISIBLE
        }
    }

    /**
     * 软键盘隐藏
     */
    fun hideKeyboard() {
        val visibility = mKeyboardView.visibility


        if (visibility == View.VISIBLE) {
            mKeyboardView.visibility = View.INVISIBLE

        }
        val llvisibilty = mLl.visibility
        if (llvisibilty==View.VISIBLE){
            mLl.visibility=View.GONE
        }
    }

    /**
     * 禁掉系统软键盘
     */
    fun hideSoftInputMethod() {
        mActivity.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        val currentVersion = android.os.Build.VERSION.SDK_INT
        var methodName: String? = null
        if (currentVersion >= 16) {
            // 4.2
            methodName = "setShowSoftInputOnFocus"
        } else if (currentVersion >= 14) {
            // 4.0
            methodName = "setSoftInputShownOnFocus"
        }
        if (methodName == null) {
            mEdit.inputType = InputType.TYPE_NULL
        } else {
            val cls = EditText::class.java
            val setShowSoftInputOnFocus: Method
            try {
                setShowSoftInputOnFocus = cls.getMethod(methodName, Boolean::class.javaPrimitiveType!!)
                setShowSoftInputOnFocus.isAccessible = true
                setShowSoftInputOnFocus.invoke(mEdit, false)
            } catch (e: NoSuchMethodException) {
                mEdit.inputType = InputType.TYPE_NULL
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            }

        }
    }

}
