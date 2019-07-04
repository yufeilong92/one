package com.zzzh.akhalteke.mvp.contract

import android.content.Context
import com.zzzh.akhalteke.mvp.view.RequestResultInterface

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.mvp.contract
 * @Email : yufeilong92@163.com
 * @Time :2019/3/16 15:37
 * @Purpose :订单详情
 */
interface InfomContract {
    interface View {
        fun InfomSuccess(t: Any?)
        fun InfomError(ex: Throwable)
        fun InfomComplise()

    }

    interface Model {
        fun requestGoodInfom(context: Context, goodId: String, request: RequestResultInterface)
    }

    interface Presenter {
        fun initMvp(view: View, model: Model)
        fun requestInfom(context: Context, goodId: String)
    }
}