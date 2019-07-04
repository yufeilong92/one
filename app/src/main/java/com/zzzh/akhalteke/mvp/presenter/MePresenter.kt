package com.zzzh.akhalteke.mvp.presenter

import android.content.Context
import com.zzzh.akhalteke.mvp.contract.MeContract
import com.zzzh.akhalteke.mvp.view.RequestResultInterface

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.mvp.presenter
 * @Email : yufeilong92@163.com
 * @Time :2019/3/26 19:43
 * @Purpose :个人中心
 */
class MePresenter : MeContract.Presenter {
    var view: MeContract.View? = null
    var model: MeContract.Model? = null
    override fun initMvp(view: MeContract.View, model: MeContract.Model) {
        this.view = view
        this.model = model

    }

    override fun requestDriverInfom(context: Context) {
        model!!.requestDriverInfom(context, object : RequestResultInterface {
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
}