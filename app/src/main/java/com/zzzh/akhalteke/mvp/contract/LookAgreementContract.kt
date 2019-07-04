package com.zzzh.akhalteke.mvp.contract

import android.content.Context
import com.zzzh.akhalteke.mvp.view.RequestResultInterface

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.mvp.contract
 * @Email : yufeilong92@163.com
 * @Time :2019/3/20 15:09
 * @Purpose :司机端查看协议详情
 */
interface LookAgreementContract {
    interface View {
        fun LookAgreementSuccess(t: Any?)
        fun LookAgreementError(ex: Throwable)
        fun LookAgreementComplise()
        fun SubmitAgreementSucces(t: Any?)
        fun SubmitAgreementError(ex: Throwable)
        fun SubmitAgreementComplise()

    }

    interface Model {
        fun requestLookAgreement(context: Context,order:String, request: RequestResultInterface)
        fun submtAgreement(context: Context, orderId: String, agree: Boolean, request: RequestResultInterface)

    }

    interface Presenter {
        fun initMvp(view: View, model: Model)
        fun requestLookAgreement(context: Context,order:String)
        fun submtAgreement(context: Context, orderId: String, agree: Boolean)

    }
}