package com.zzzh.akhalteke.mvp.contract

import android.content.Context
import com.zzzh.akhalteke.mvp.view.RequestResultInterface
import com.zzzh.akhalteke.mvp.view.StringResultInterface

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.mvp.contract
 * @Email : yufeilong92@163.com
 * @Time :2019/3/21 11:25
 * @Purpose :添加银行卡
 */
interface BankAddContract {
    interface View {
        fun Success(t: Any?)
        fun Error(ex: Throwable)
        fun Complise()
        fun CarTypeError(ex: Throwable)
        fun CarTypeSuccess(t: Any?)
        fun CarTypeComplise()

    }

    interface Model {
        fun submitBankAddd(
            context: Context, name: String, idCardNumber: String, cardNumber: String,
            phone: String, bank: String, request: RequestResultInterface
        )
        fun submitCarType(context: Context,cardNumber: String,request: RequestResultInterface)
    }

    interface Presenter {
        fun initMvp(view: View, model: Model)
        fun submitBankAddd(
            context: Context, name: String, idCardNumber: String, cardNumber: String,
            phone: String, bank: String)
        fun submitCarType(context: Context,cardNumber: String)
    }
}