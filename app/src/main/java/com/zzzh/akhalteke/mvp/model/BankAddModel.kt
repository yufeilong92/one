package com.zzzh.akhalteke.mvp.model

import android.content.Context
import com.zzzh.akhalteke.mvp.contract.BankAddContract
import com.zzzh.akhalteke.mvp.view.RequestResultInterface
import com.zzzh.akhalteke.mvp.view.StringResultInterface
import com.zzzh.akhalteke.net.UserNet

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.mvp.model
 * @Email : yufeilong92@163.com
 * @Time :2019/3/21 11:26
 * @Purpose :添加银行卡
 */
class BankAddModel : BankAddContract.Model {
    override fun submitBankAddd(
        context: Context,
        name: String,
        idCardNumber: String,
        cardNumber: String,
        phone: String,
        bank: String,
        request: RequestResultInterface
    ) {

        val net = UserNet.getInstance
        net.submitBankCardAdd(context, name, idCardNumber, cardNumber, phone, bank, object : StringResultInterface {
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

    override fun submitCarType(context: Context, cardNumber: String, request: RequestResultInterface) {
        val net = UserNet.getInstance
        net.requestBankTyep(context, cardNumber, object : StringResultInterface {
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