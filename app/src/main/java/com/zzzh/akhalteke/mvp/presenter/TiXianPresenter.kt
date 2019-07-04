package com.zzzh.akhalteke.mvp.presenter

import android.content.Context
import com.zzzh.akhalteke.mvp.contract.TiXianContract
import com.zzzh.akhalteke.mvp.view.RequestResultInterface
import com.zzzh.akhalteke.mvp.view.StringResultInterface
import com.zzzh.akhalteke.net.UserNet

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.mvp.presenter
 * @Email : yufeilong92@163.com
 * @Time :2019/3/25 17:52
 * @Purpose :体现
 */
class TiXianPresenter : TiXianContract.Presenter {


    var view: TiXianContract.View? = null
    var model: TiXianContract.Model? = null
    override fun initMvp(view: TiXianContract.View, model: TiXianContract.Model) {
        this.view = view
        this.model = model

    }

    override fun submiteTiXian(context: Context, sum: String, bankId: String) {
        model!!.submiteTiXian(context, sum, bankId, object : RequestResultInterface {
            override fun onError(ex: Throwable) {
                view!!.Error(ex)
            }

            override fun onComplise() {
                view!!.Complise()
            }

            override fun <T> Success(t: T) {
                view!!.Success(t)
            }
        })
    }

    override fun requestBanlList(context: Context) {
        model!!.requestBanlList(context, object : RequestResultInterface {
            override fun onError(ex: Throwable) {
                view!!.BankError(ex)
            }

            override fun onComplise() {
                view!!.BankComplise()
            }

            override fun <T> Success(t: T) {
                view!!.BankSuccess(t)
            }
        })
    }

}