package com.zzzh.akhalteke.mvp.presenter

import android.content.Context
import com.zzzh.akhalteke.mvp.contract.MyMoneyContract
import com.zzzh.akhalteke.mvp.view.RequestResultInterface

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.mvp.presenter
 * @Email : yufeilong92@163.com
 * @Time :2019/3/30 15:18
 * @Purpose :我的钱包
 */
class MyMoneyPresenter : MyMoneyContract.Presenter {
    var view: MyMoneyContract.View? = null
    var model: MyMoneyContract.Model? = null
    override fun initMvp(view: MyMoneyContract.View, model: MyMoneyContract.Model) {
        this.view = view
        this.model = model

    }

    override fun requestCenterAccountOne(context: Context, page: Int) {
        model!!.requestCenterAccount(context, page, object : RequestResultInterface {
            override fun onError(ex: Throwable) {
                view!!.OneError(ex)
            }

            override fun onComplise() {
                view!!.OneComplise()
            }

            override fun <T> Success(t: T) {
                view!!.OneSuccess(t)
            }
        })
    }

    override fun requestCenterAccountTwo(context: Context, page: Int) {
        model!!.requestCenterAccount(context, page, object : RequestResultInterface {
            override fun onError(ex: Throwable) {
                view!!.TwoError(ex)
            }

            override fun onComplise() {
                view!!.TwoComplise()
            }

            override fun <T> Success(t: T) {
                view!!.TwoSuccess(t)
            }
        })
    }
}