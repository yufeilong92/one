package com.zzzh.akhalteke.utils.helper

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.lipo.utils.KeyBoardUtils
import com.zzzh.akhalteke.ui.BaseActivity

import kotlinx.android.synthetic.main.cell_getcode.view.*

class GetCodeHelper(
    val mContext: BaseActivity,
    val mView: View,
    val compleInterface: InputCompterInterface
) {

    init {
        mView.apply {
            val mViews = arrayOf(
                login_getcode_code1,
                login_getcode_code2,
                login_getcode_code3,
                login_getcode_code4,
                login_getcode_code5,
                login_getcode_code6
            )

            login_getcode_edit.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    val editStr = login_getcode_edit.text.toString().trim()
                    val length = editStr.length
                    for (i in 0..5) {
                        mViews[i].text = ""
                    }
                    for (i in 0 until length) {
                        mViews[i].text = editStr.substring(i, i + 1)
                    }

                    if (length == 6) {
                        compleInterface.competer(editStr)
//                        ToastView.setToasd(mContext, "获取到$editStr")
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }
            })
        }

        mView.apply {
            setOnClickListener { KeyBoardUtils.openKeybord(login_getcode_edit, mContext) }
        }
    }

    fun closeKeyBord() {
        mView.apply {
            KeyBoardUtils.closeKeybord(login_getcode_edit, mContext)
        }
    }

    fun getTextCode(): String {
        mView.apply {
            return login_getcode_edit.text.toString().trim()
        }
    }

    interface InputCompterInterface {
        fun competer(str: String)
    }
}