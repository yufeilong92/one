package com.zzzh.akhalteke.mvp.contract

import com.zzzh.akhalteke.mvp.view.StringResultInterface

interface LoginContract {
    interface  Model {
      fun getCode(phone:String,a:StringResultInterface)
    }

    interface View{
    }

    interface Presenter {
    }
}