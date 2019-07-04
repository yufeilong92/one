package com.zzzh.akhalteke.mvp.presenter

import android.content.Context
import com.zzzh.akhalteke.mvp.contract.BankListContract
import com.zzzh.akhalteke.mvp.view.RequestResultInterface

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.mvp.presenter
 * @Email : yufeilong92@163.com
 * @Time :2019/3/21 10:46
 * @Purpose :银行卡管理
 */
class BankListPresenter : BankListContract.Presenter {
    var view: BankListContract.View? = null
    var model: BankListContract.Model? = null
    override fun initMvp(view: BankListContract.View, model: BankListContract.Model) {
        this.view = view
        this.model = model

    }

    override fun requestBankList(context: Context) {
        model!!.requestBankList(context, object : RequestResultInterface {
            override fun onError(ex: Throwable) {
                view!!.BankListError(ex)
            }

            override fun onComplise() {
                view!!.BankListComplise()
            }

            override fun <T> Success(t: T) {
                view!!.BankListSuccess(t)
            }
        })
    }

    override fun submitDeleteBankCar(context: Context, cardId: String) {
        model!!.submitDeleteBankCar(context, cardId, object : RequestResultInterface {
            override fun onError(ex: Throwable) {
                view!!.DeleteBankError(ex)
            }

            override fun onComplise() {
                view!!.DeleteBankComplise()
            }

            override fun <T> Success(t: T) {
                view!!.DeleteBankSuccess(t)
            }
        })

    }
}