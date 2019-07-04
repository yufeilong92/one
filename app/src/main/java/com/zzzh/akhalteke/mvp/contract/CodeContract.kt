package com.zzzh.akhalteke.mvp.contract

import android.content.Context
import com.zzzh.akhalteke.mvp.view.RequestResultInterface
import com.zzzh.akhalteke.mvp.view.StringResultInterface

interface CodeContract {
    interface Model {
        fun getCode(context: Context, phone: String, m: RequestResultInterface)
        fun checkCode(context: Context, phone: String, code: String, m: RequestResultInterface)
    }

    interface View {
        fun CodeSuccess(t: Any?)
        fun CodeError(ex: Throwable)
        fun CodeCompter()
        fun CheckCodeSuccess(t: Any?)
        fun CheckCodeError(ex: Throwable)
        fun CheckCodeCompter()
    }

    interface Presenter {
        fun initMvp(v: View, m: Model)
        fun getCode(context: Context, phone: String)
        fun checkCode(context: Context, phone: String, code: String)
    }
}