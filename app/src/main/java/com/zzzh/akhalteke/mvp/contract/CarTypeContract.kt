package com.zzzh.akhalteke.mvp.contract

import android.content.Context
import com.zzzh.akhalteke.mvp.view.RequestResultInterface

interface CarTypeContract {
    interface View {
        fun CarTypeSuccess(t: Any?);
        fun CarTypeError(ex: Throwable);
        fun CarTypeComplise();

    }

    interface Model {
        fun getCarType(context:Context ,n:RequestResultInterface)
    }

    interface Presenter {
        fun initMvp(v: View, m: Model)
        fun getCarType(context:Context )
    }
}
