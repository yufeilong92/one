package com.zzzh.akhalteke.mvp.contract

import android.content.Context
import com.zzzh.akhalteke.mvp.view.RequestResultInterface

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.mvp.contract
 * @Email : yufeilong92@163.com
 * @Time :2019/3/26 19:41
 * @Purpose :个人中心
 */
interface MeContract {
    interface View {
        fun Success(t: Any?)
        fun Error(ex: Throwable)
        fun Complise()

    }

    interface Model {
        fun requestDriverInfom(context: Context, request: RequestResultInterface)
    }

    interface Presenter {
        fun initMvp(view: View, model: Model)
        fun requestDriverInfom(context: Context)
    }
}