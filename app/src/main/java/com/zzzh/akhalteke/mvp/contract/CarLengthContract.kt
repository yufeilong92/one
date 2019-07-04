package com.zzzh.akhalteke.mvp.contract

import android.content.Context
import com.zzzh.akhalteke.mvp.view.RequestResultInterface

interface CarLengthContract {
    interface View {
        fun Success(t:Any?)
        fun Error(ex:Throwable)
        fun Complise()

    }

    interface Model {
        fun getCarLength(context:Context,request:RequestResultInterface)
    }

    interface Presenter {
        fun initMvp(view: View, model: Model)
        fun getCarLength(context:Context)
    }
}