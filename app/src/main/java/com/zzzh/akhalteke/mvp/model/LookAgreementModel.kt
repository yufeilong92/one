package com.zzzh.akhalteke.mvp.model

import android.content.Context
import com.zzzh.akhalteke.mvp.contract.LookAgreementContract
import com.zzzh.akhalteke.mvp.view.RequestResultInterface
import com.zzzh.akhalteke.mvp.view.StringResultInterface
import com.zzzh.akhalteke.net.UserNet
import com.zzzh.akhalteke.utils.Utils

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.mvp.model
 * @Email : yufeilong92@163.com
 * @Time :2019/3/20 15:11
 * @Purpose :司机端查看协议详情
 */
class LookAgreementModel : LookAgreementContract.Model {
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

    override fun requestLookAgreement(context: Context, order: String, request: RequestResultInterface) {
        val net = UserNet.getInstance
        net.requestAgreementInfom(context, order, object : StringResultInterface {
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