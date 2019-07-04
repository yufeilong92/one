package com.zzzh.akhalteke.mvp.contract

import android.content.Context
import com.zzzh.akhalteke.mvp.view.RequestResultInterface

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.mvp.contract
 * @Email : yufeilong92@163.com
 * @Time :2019/4/9 9:24
 * @Purpose :主界面
 */
interface CheckInfomContract {

    interface View {
        fun CheckSuccess(t: Any?)
        fun CheckError(ex: Throwable)
        fun CheckComplise()
    }

    interface Model {
        fun requestCheckInfom(context: Context, request: RequestResultInterface)
    }

    interface Presenter {
        fun initMvp(view: View, model: Model)
        fun requestCheckInfom(context: Context)
    }
}