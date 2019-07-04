package com.zzzh.akhalteke.mvp.contract

import android.content.Context
import com.zzzh.akhalteke.bean.vo.SelectPath
import com.zzzh.akhalteke.mvp.view.RequestResultInterface
import com.zzzh.akhalteke.mvp.view.StringResultInterface
/**
 * @Author : YFL  is Creating a porject in com.zzzh.akhalteke.mvp.contract
 * @Package com.zzzh.akhalteke.mvp.contract
 * @Email : yufeilong92@163.com
 * @Time :2019/3/29 15:22
 * @Purpose :查看回单
 */
interface UpLoadReceiptContract {
    interface  Model {
        fun  submitUpLoadRecepit(context :Context, orderId:String, map:MutableList<SelectPath>, request:RequestResultInterface)
        fun requestRecepit(context: Context,orderId: String,request:RequestResultInterface)
    }

    interface View{
        fun  UpLoadRecepitSuccess(t: Any?)
        fun  UpLoadRecepitError(ex: Throwable)
        fun UpLoadRecepitCompter()
        fun  LookRecepitSuccess(t: Any?)
        fun  LookRecepitError(ex: Throwable)
        fun LookRecepitCompter()
    }

    interface Presenter {
        fun initMvp(v:View,m:Model)
        fun  submitUpLoadRecepit(context :Context,orderId:String, map:MutableList<SelectPath>)
        fun requestRecepit(context: Context,orderId: String)
    }
}