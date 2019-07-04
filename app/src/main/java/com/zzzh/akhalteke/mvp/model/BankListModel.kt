package com.zzzh.akhalteke.mvp.model

import android.content.Context
import com.zzzh.akhalteke.mvp.contract.BankListContract
import com.zzzh.akhalteke.mvp.view.RequestResultInterface
import com.zzzh.akhalteke.mvp.view.StringResultInterface
import com.zzzh.akhalteke.net.UserNet

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.mvp.model
 * @Email : yufeilong92@163.com
 * @Time :2019/3/21 10:45
 * @Purpose :银行卡列表
 */
class BankListModel : BankListContract.Model {
    override fun requestBankList(context: Context, request: RequestResultInterface) {
        val net = UserNet.getInstance
        net.requestBankCardList(context, object : StringResultInterface {
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

    override fun submitDeleteBankCar(context: Context, cardId: String, request: RequestResultInterface) {
        val net = UserNet.getInstance
        net.submitBankCardDelete(context, cardId, object : StringResultInterface {
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