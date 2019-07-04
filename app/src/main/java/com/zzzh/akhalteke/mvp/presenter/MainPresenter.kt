package com.zzzh.akhalteke.mvp.presenter

import android.content.Context
import com.zzzh.akhalteke.mvp.contract.MainContract
import com.zzzh.akhalteke.mvp.view.RequestResultInterface
import com.zzzh.akhalteke.ui.home.EmptyActivity

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.mvp.presenter
 * @Email : yufeilong92@163.com
 * @Time :2019/6/14 16:00
 * @Purpose :首页
 */
class MainPresenter : MainContract.Presenter {
    var view: MainContract.View? = null
    var model: MainContract.Model? = null
    override fun initMvp(view: MainContract.View, model: MainContract.Model) {
        this.view = view
        this.model = model
    }

    override fun requestMainContent(context: Context) {
        model!!.requestMainContent(context, object : RequestResultInterface {
            override fun onError(ex: Throwable) {
                view!!.onMainError(ex)
            }

            override fun onComplise() {
                view!!.onMainComplise()
            }

            override fun <T> Success(t: T)
            {
                view!!.onMainSuccess(t)
            }
        })
    }
}