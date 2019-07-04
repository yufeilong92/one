package com.zzzh.akhalteke.mvp.contract

import android.content.Context
import com.zzzh.akhalteke.mvp.view.RequestResultInterface

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.mvp.contract
 * @Email : yufeilong92@163.com
 * @Time :2019/3/19 15:44
 * @Purpose :我的运单
 */
interface OrderContract {
    interface View {
        fun OneSuccess(t: Any?)
        fun OneError(ex: Throwable)
        fun OneComplise()
        fun TwoSuccess(t: Any?)
        fun TwoError(ex: Throwable)
        fun TwoComplise()

    }

    interface Model {
        fun requestOrderList(context: Context, status: String,page:Int, request: RequestResultInterface)
    }

    interface Presenter {
        fun initMvp(view: View, model: Model)
        fun requestOrderListOne(context: Context, status: String ,page:Int)
        fun requestOrderListTwo(context: Context, status: String ,page:Int)
    }
}