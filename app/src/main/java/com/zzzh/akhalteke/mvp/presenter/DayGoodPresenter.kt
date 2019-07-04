package com.zzzh.akhalteke.mvp.presenter

import android.content.Context
import com.zzzh.akhalteke.mvp.contract.DayGoodContract
import com.zzzh.akhalteke.mvp.view.RequestResultInterface

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.mvp.presenter
 * @Email : yufeilong92@163.com
 * @Time :2019/3/19 10:42
 * @Purpose :
 */
class DayGoodPresenter : DayGoodContract.Presenter {
    var view: DayGoodContract.View? = null
    var model: DayGoodContract.Model? = null
    override fun initMvp(view: DayGoodContract.View, model: DayGoodContract.Model) {
        this.view = view
        this.model = model

    }

    override fun requestShipperGoodOne(context: Context, shipperId: String, page: Int) {
        model!!.requestShipperGood(context, shipperId, page, object : RequestResultInterface {
            override fun onError(ex: Throwable) {
                view!!.ShipperGoodOneError(ex)
            }

            override fun onComplise() {
                view!!.ShipperGoodOneComplise()
            }

            override fun <T> Success(t: T) {
                view!!.ShipperGoodOneSuccess(t)
            }
        })
    }

    override fun requestShipperGoodTwo(context: Context, shipperId: String, page: Int) {
        model!!.requestShipperGood(context, shipperId, page, object : RequestResultInterface {
            override fun onError(ex: Throwable) {
                view!!.ShipperGoodTwoError(ex)
            }

            override fun onComplise() {
                view!!.ShipperGoodTwoComplise()
            }

            override fun <T> Success(t: T) {
                view!!.ShipperGoodTwoSuccess(t)
            }
        })
    }
}