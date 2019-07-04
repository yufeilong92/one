package com.zzzh.akhalteke.mvp.model

import android.content.Context
import com.zzzh.akhalteke.mvp.contract.AgreementContract
import com.zzzh.akhalteke.mvp.view.RequestResultInterface
import com.zzzh.akhalteke.mvp.view.StringResultInterface
import com.zzzh.akhalteke.net.UserNet

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.mvp.model
 * @Email : yufeilong92@163.com
 * @Time :2019/3/20 10:04
 * @Purpose :查看司机端订单详情
 */
class AgreementModel : AgreementContract.Model {
    override fun submtAgreement(context: Context, orderId: String, agree: Boolean, request: RequestResultInterface) {
        val net = UserNet.getInstance
        net.submitAgreementConfirm(context, orderId, agree, object : StringResultInterface {
            override fun onError(ex: Throwable) {
                request.onError(ex)
            }

            override fun onComplise() {
                request.onComplise()
            }

            override fun <T> Success(t: T) {
                request.Success(t)
            }

        })
    }

    override fun requestOrderInfo(context: Context, orderId: String, request: RequestResultInterface) {
        val net = UserNet.getInstance
        net.requestOrderInfom(context, orderId, object : StringResultInterface {
            override fun onError(ex: Throwable) {
                request.onError(ex)
            }

            override fun onComplise() {
                request.onComplise()
            }

            override fun <T> Success(t: T) {
                request.Success(t)
            }

        })
    }
}