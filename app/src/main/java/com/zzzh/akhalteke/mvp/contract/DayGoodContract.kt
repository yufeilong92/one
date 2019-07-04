package com.zzzh.akhalteke.mvp.contract

import android.content.Context
import com.zzzh.akhalteke.mvp.view.RequestResultInterface

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.mvp.contract
 * @Email : yufeilong92@163.com
 * @Time :2019/3/19 10:34
 * @Purpose :当天数据源
 */
interface DayGoodContract {
    interface View {
        fun ShipperGoodOneSuccess(t: Any?)
        fun ShipperGoodOneError(ex: Throwable)
        fun ShipperGoodOneComplise()
        fun ShipperGoodTwoSuccess(t: Any?)
        fun ShipperGoodTwoError(ex: Throwable)
        fun ShipperGoodTwoComplise()
    }

    interface Model {
        fun requestShipperGood(context: Context, shipperId: String, page: Int, request: RequestResultInterface)
    }

    interface Presenter {
        fun initMvp(view: View, model: Model)
        fun requestShipperGoodOne(context: Context, shipperId: String, page: Int)
        fun requestShipperGoodTwo(context: Context, shipperId: String, page: Int)
    }
}