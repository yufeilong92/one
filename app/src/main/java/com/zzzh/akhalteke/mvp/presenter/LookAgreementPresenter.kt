package com.zzzh.akhalteke.mvp.presenter

import android.content.Context
import com.zzzh.akhalteke.mvp.contract.LookAgreementContract
import com.zzzh.akhalteke.mvp.view.RequestResultInterface

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.mvp.presenter
 * @Email : yufeilong92@163.com
 * @Time :2019/3/20 15:12
 * @Purpose :司机端查看协议详情
 */
class LookAgreementPresenter : LookAgreementContract.Presenter {
    override fun submtAgreement(context: Context, orderId: String, agree: Boolean) {

        model!!.submtAgreement(context, orderId, agree, object : RequestResultInterface {
            override fun onError(ex: Throwable) {
                view!!.SubmitAgreementError(ex)
            }

            override fun onComplise() {
                view!!.SubmitAgreementComplise()
            }

            override fun <T> Success(t: T) {
                view!!.SubmitAgreementSucces(t)
            }
        })
    }

    var view: LookAgreementContract.View? = null
    var model: LookAgreementContract.Model? = null
    override fun initMvp(view: LookAgreementContract.View, model: LookAgreementContract.Model) {
        this.view = view
        this.model = model

    }

    override fun requestLookAgreement(context: Context, order: String) {
        model!!.requestLookAgreement(context, order, object : RequestResultInterface {
            override fun onError(ex: Throwable) {
                view!!.LookAgreementError(ex)
            }

            override fun onComplise() {
                view!!.LookAgreementComplise()
            }

            override fun <T> Success(t: T) {
                view!!.LookAgreementSuccess(t)
            }
        })
    }
}