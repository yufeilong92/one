package com.zzzh.akhalteke.mvp.presenter

import android.content.Context
import com.zzzh.akhalteke.mvp.contract.AgreementContract
import com.zzzh.akhalteke.mvp.view.RequestResultInterface

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.mvp.presenter
 * @Email : yufeilong92@163.com
 * @Time :2019/3/20 10:05
 * @Purpose :查看司机端订单详情
 */
class AgreementPresenter : AgreementContract.Presenter {


    var view: AgreementContract.View? = null
    var model: AgreementContract.Model? = null
    override fun initMvp(view: AgreementContract.View, model: AgreementContract.Model) {
        this.view = view
        this.model = model
    }

    override fun requestOrderInfo(context: Context, orderId: String) {
        model!!.requestOrderInfo(context, orderId, object : RequestResultInterface {
            override fun onError(ex: Throwable) {
                view!!.InfoError(ex)
            }

            override fun onComplise() {
                view!!.InfoComplise()
            }

            override fun <T> Success(t: T) {
                view!!.InfoSuccess(t)
            }

        })
    }

    override fun submtAgreement(context: Context, orderId: String, agree: Boolean) {
        model!!.submtAgreement(context, orderId, agree, object : RequestResultInterface {
            override fun onError(ex: Throwable) {
                view!!.SubmitError(ex)
            }

            override fun onComplise() {
                view!!.SubmitComplise()
            }

            override fun <T> Success(t: T) {
                view!!.SubmitSuccess(t)
            }
        })
    }
}