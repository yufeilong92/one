package com.zzzh.akhalteke.mvp.presenter

import android.content.Context
import com.zzzh.akhalteke.mvp.contract.InfomContract
import com.zzzh.akhalteke.mvp.view.RequestResultInterface

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.mvp.presenter
 * @Email : yufeilong92@163.com
 * @Time :2019/3/16 15:41
 * @Purpose :货源详情
 */
class InfomPresneter : InfomContract.Presenter {
    var view: InfomContract.View? = null
    var model: InfomContract.Model? = null
    override fun initMvp(view: InfomContract.View, model: InfomContract.Model) {
        this.view = view
        this.model = model

    }
    override fun requestInfom(context: Context, goodId: String) {
        model!!.requestGoodInfom(context,goodId,object :RequestResultInterface{
            override fun onError(ex: Throwable) {
                view!!.InfomError(ex)
            }

            override fun onComplise() {
                view!!.InfomComplise()
            }

            override fun <T> Success(t: T) {
                view!!.InfomSuccess(t)
            }

        })
    }
}