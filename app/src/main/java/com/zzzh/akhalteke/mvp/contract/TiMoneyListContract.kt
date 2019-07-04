package com.zzzh.akhalteke.mvp.contract

import android.content.Context
import com.zzzh.akhalteke.mvp.view.RequestResultInterface

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.mvp.contract
 * @Email : yufeilong92@163.com
 * @Time :2019/3/22 18:05
 * @Purpose :提现列表
 */
interface TiMoneyListContract {
    interface View {
        fun OneSuccess(t: Any?)
        fun OneError(ex: Throwable)
        fun OneComplise()
        fun TwoSuccess(t: Any?)
        fun TwoError(ex: Throwable)
        fun TwoComplise()
    }

    interface Model {
        fun requestTiMoneyList(context: Context,page:Int, request: RequestResultInterface)
    }

    interface Presenter {
        fun initMvp(view: View, model: Model)
        fun requestTiMoneyListOne(context: Context,page:Int)
        fun requestTiMoneyListTwo(context: Context,page:Int)
    }
}