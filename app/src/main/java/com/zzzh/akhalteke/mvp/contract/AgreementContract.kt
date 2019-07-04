package com.zzzh.akhalteke.mvp.contract

import android.content.Context
import com.zzzh.akhalteke.mvp.view.RequestResultInterface

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.mvp.contract
 * @Email : yufeilong92@163.com
 * @Time :2019/3/20 10:02
 * @Purpose :查看司机端订单详情
 */
interface AgreementContract {
    interface View {
        fun InfoSuccess(t: Any?)
        fun InfoError(ex: Throwable)
        fun InfoComplise()
        fun SubmitSuccess(t: Any?)
        fun SubmitError(ex: Throwable)
        fun SubmitComplise()
    }

    interface Model {
        /***
         * @param context
         * @param orderId 订单详情
         * @param request
         */
        fun requestOrderInfo(context: Context, orderId: String, request: RequestResultInterface)

        fun submtAgreement(context: Context, orderId: String, agree: Boolean, request: RequestResultInterface)
    }

    interface Presenter {
        fun initMvp(view: View, model: Model)
        fun requestOrderInfo(context: Context, orderId: String)
        fun submtAgreement(context: Context, orderId: String, agree: Boolean)
    }
}