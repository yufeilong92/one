package com.zzzh.akhalteke.mvp.presenter

import android.content.Context
import com.zzzh.akhalteke.mvp.contract.TiMoneyListContract
import com.zzzh.akhalteke.mvp.view.RequestResultInterface

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.mvp.presenter
 * @Email : yufeilong92@163.com
 * @Time :2019/3/22 18:10
 * @Purpose :
 */
class TiMoneyListPresenter : TiMoneyListContract.Presenter {
    var view: TiMoneyListContract.View? = null
    var model: TiMoneyListContract.Model? = null
    override fun initMvp(view: TiMoneyListContract.View, model: TiMoneyListContract.Model) {
        this.view = view
        this.model = model

    }

    override fun requestTiMoneyListOne(context: Context, page: Int) {
        model!!.requestTiMoneyList(context, page, object : RequestResultInterface {
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

    override fun requestTiMoneyListTwo(context: Context, page: Int) {
        model!!.requestTiMoneyList(context, page, object : RequestResultInterface {
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