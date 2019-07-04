package com.zzzh.akhalteke.mvp.presenter

import android.content.Context
import com.zzzh.akhalteke.mvp.contract.BankAddContract
import com.zzzh.akhalteke.mvp.view.RequestResultInterface

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.mvp.presenter
 * @Email : yufeilong92@163.com
 * @Time :2019/3/21 11:29
 * @Purpose :
 */
class BankAddPresenter : BankAddContract.Presenter {
    var view: BankAddContract.View? = null
    var model: BankAddContract.Model? = null
    override fun initMvp(view: BankAddContract.View, model: BankAddContract.Model) {
        this.view = view
        this.model = model

    }

    override fun submitBankAddd(
        context: Context,
        name: String,
        idCardNumber: String,
        cardNumber: String,
        phone: String,
        bank: String
    ) {
        model!!.submitBankAddd(context, name, idCardNumber, cardNumber, phone, bank, object : RequestResultInterface {
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

    override fun submitCarType(context: Context, cardNumber: String) {
        model!!.submitCarType(context, cardNumber, object : RequestResultInterface {
            override fun onError(ex: Throwable) {
                view!!.CarTypeError(ex)
            }

            override fun onComplise() {
                view!!.CarTypeComplise()
            }

            override fun <T> Success(t: T) {
                view!!.CarTypeSuccess(t)
            }
        })

    }
}