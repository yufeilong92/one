package com.zzzh.akhalteke.mvp.contract

import android.content.Context
import com.zzzh.akhalteke.mvp.view.RequestResultInterface

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.mvp.contract
 * @Email : yufeilong92@163.com
 * @Time :2019/3/21 10:43
 * @Purpose :银行卡列表
 */
interface BankListContract {
    interface View {
        fun BankListSuccess(t: Any?)
        fun BankListError(ex: Throwable)
        fun BankListComplise()
        fun DeleteBankSuccess(t: Any?)
        fun DeleteBankError(ex: Throwable)
        fun DeleteBankComplise()

    }

    interface Model {
        fun requestBankList(context: Context, request: RequestResultInterface)
        fun submitDeleteBankCar(context: Context, cardId: String, request: RequestResultInterface)
    }

    interface Presenter {
        fun initMvp(view: View, model: Model)
        fun requestBankList(context: Context)
        fun submitDeleteBankCar(context: Context, cardId: String)

    }
}