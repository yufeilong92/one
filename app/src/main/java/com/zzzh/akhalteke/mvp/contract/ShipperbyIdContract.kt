package com.zzzh.akhalteke.mvp.contract

import android.content.Context
import com.zzzh.akhalteke.mvp.view.RequestResultInterface

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.mvp.contract
 * @Email : yufeilong92@163.com
 * @Time :2019/3/18 17:02
 * @Purpose :货主信息简介
 */
interface ShipperbyIdContract {
    interface View {
        fun Success(t: Any?)
        fun Error(ex: Throwable)
        fun Complise()

//        fun ListError(ex: Throwable)
//        fun ListSuccess(t: Any?)
//        fun ListComplise()

    }

    interface Model {
        fun requestShipperId(context: Context, shipperId: String, request: RequestResultInterface)
        fun requestShipperList(context: Context, shipperid: String, page: Int, request: RequestResultInterface)
    }

    interface Presenter {
        fun initMvp(view: View, model: Model)
        fun requestShipperId(context: Context, shipperId: String)
//        fun requestShipperList(context: Context, shipperid: String, page: Int)
    }
}