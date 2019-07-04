package com.zzzh.akhalteke.mvp.contract

import android.content.Context
import com.zzzh.akhalteke.mvp.view.RequestResultInterface

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.mvp.contract
 * @Email : yufeilong92@163.com
 * @Time :2019/3/25 17:51
 * @Purpose :体现
 */
interface TiXianContract {
    interface View {
        fun Success(t: Any?)
        fun Error(ex: Throwable)
        fun Complise()
        fun BankSuccess(t: Any?)
        fun BankError(ex: Throwable)
        fun BankComplise()

    }

    interface Model {
        fun submiteTiXian(context: Context,sum:String, bankId:String,request: RequestResultInterface)
        fun requestBanlList(context: Context,request: RequestResultInterface)
    }

    interface Presenter {
        fun initMvp(view: View, model: Model)
        fun submiteTiXian(context: Context,sum:String, bankId:String)
        fun requestBanlList(context: Context)
    }
}