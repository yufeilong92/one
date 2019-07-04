package com.zzzh.akhalteke.mvp.contract

import android.content.Context
import com.zzzh.akhalteke.mvp.view.RequestResultInterface

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.mvp.contract
 * @Email : yufeilong92@163.com
 * @Time :2019/6/14 15:51
 * @Purpose :首頁控制器
 */
interface MainContract {
    interface View {
        fun onMainSuccess(t: Any?)
        fun onMainError(ex: Throwable)
        fun onMainComplise()
    }

    interface Model {
        fun requestMainContent(context: Context, request: RequestResultInterface)
    }

    interface Presenter {
        fun initMvp(view: View, model: Model)
        fun requestMainContent(context: Context)
    }
}