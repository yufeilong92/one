package com.zzzh.akhalteke.mvp.presenter

import android.content.Context
import com.zzzh.akhalteke.mvp.contract.CheckInfomContract

import com.zzzh.akhalteke.mvp.view.RequestResultInterface

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.mvp.presenter
 * @Email : yufeilong92@163.com
 * @Time :2019/4/9 9:26
 * @Purpose :主界面
 */
class CheckInfomPresenter : CheckInfomContract.Presenter {
    var view: CheckInfomContract.View? = null
    var model: CheckInfomContract.Model? = null
    override fun initMvp(view: CheckInfomContract.View, model: CheckInfomContract.Model) {
        this.view = view
        this.model = model

    }

    override fun requestCheckInfom(context: Context) {
        model!!.requestCheckInfom(context, object : RequestResultInterface {
            override fun onError(ex: Throwable) {
                view!!.CheckError(ex)
            }

            override fun onComplise() {
                view!!.CheckComplise()
            }

            override fun <T> Success(t: T) {
                view!!.CheckSuccess(t)
            }
        })
    }
}