package com.zzzh.akhalteke.mvp.presenter

import android.content.Context
import com.zzzh.akhalteke.mvp.contract.ShipperbyIdContract
import com.zzzh.akhalteke.mvp.view.RequestResultInterface

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.mvp.presenter
 * @Email : yufeilong92@163.com
 * @Time :2019/3/18 17:10
 * @Purpose :查看货主信息简介
 */
class ShipperbyidPresenter : ShipperbyIdContract.Presenter {


    var view: ShipperbyIdContract.View? = null
    var model: ShipperbyIdContract.Model? = null
    override fun initMvp(view: ShipperbyIdContract.View, model: ShipperbyIdContract.Model) {
        this.view = view
        this.model = model
    }

    override fun requestShipperId(context: Context, shipperId: String) {
        model!!.requestShipperId(context, shipperId, object : RequestResultInterface {
            override fun onError(ex: Throwable) {
                view!!.Success(ex)
            }

            override fun onComplise() {
                view!!.Complise()
            }

            override fun <T> Success(t: T) {
                view!!.Success(t)
            }

        })
    }

//    override fun requestShipperList(context: Context, shipperid: String, page: Int) {
//        model!!.requestShipperList(context, shipperid, page, object : RequestResultInterface {
//            override fun onError(ex: Throwable) {
//                view!!.ListError(ex)
//            }
//
//            override fun onComplise() {
//                view!!.ListComplise()
//            }
//
//            override fun <T> Success(t: T) {
//                view!!.ListSuccess(t)
//            }
//
//        })
//    }
}