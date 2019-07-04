package com.zzzh.akhalteke.mvp.presenter

import android.content.Context
import com.zzzh.akhalteke.bean.vo.SelectPath
import com.zzzh.akhalteke.mvp.contract.UpLoadReceiptContract
import com.zzzh.akhalteke.mvp.view.RequestResultInterface

/**
 * @Author : YFL  is Creating a porject in Lenovo
 * @Email : yufeilong92@163.com
 * @Time :2019/3/11 11:12
 * @Purpose :查看回单
 */

class UpLoadReceiptPresenter : UpLoadReceiptContract.Presenter {

    var view: UpLoadReceiptContract.View? = null
    var model: UpLoadReceiptContract.Model? = null
    override fun initMvp(view: UpLoadReceiptContract.View, model: UpLoadReceiptContract.Model) {
        this.view = view
        this.model = model

    }

    override fun submitUpLoadRecepit(context: Context, orderId: String, map: MutableList<SelectPath>) {

        model!!.submitUpLoadRecepit(context, orderId, map, object : RequestResultInterface {
            override fun onError(ex: Throwable) {
                view!!.UpLoadRecepitError(ex)
            }

            override fun onComplise() {
                view!!.UpLoadRecepitCompter()
            }

            override fun <T> Success(t: T) {
                view!!.UpLoadRecepitSuccess(t)
            }
        })
    }

    override fun requestRecepit(context: Context, orderId: String) {
        model!!.requestRecepit(context, orderId, object : RequestResultInterface {
            override fun onError(ex: Throwable) {
                view!!.LookRecepitError(ex)
            }

            override fun onComplise() {
                view!!.LookRecepitCompter()
            }

            override fun <T> Success(t: T) {
                view!!.LookRecepitSuccess(t)
            }
        })
    }


}