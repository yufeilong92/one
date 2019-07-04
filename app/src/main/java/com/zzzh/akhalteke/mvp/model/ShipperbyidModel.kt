package com.zzzh.akhalteke.mvp.model

import android.content.Context
import com.zzzh.akhalteke.mvp.contract.ShipperbyIdContract
import com.zzzh.akhalteke.mvp.view.RequestResultInterface
import com.zzzh.akhalteke.mvp.view.StringResultInterface
import com.zzzh.akhalteke.net.UserNet

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.mvp.model
 * @Email : yufeilong92@163.com
 * @Time :2019/3/18 17:04
 * @Purpose :
 */
class ShipperbyidModel : ShipperbyIdContract.Model {
    override fun requestShipperList(context: Context, shipperid: String, page: Int, request: RequestResultInterface) {
        val net = UserNet.getInstance
        net.requestShipperGoodList(context, shipperid, page, object : StringResultInterface {
            override fun onError(ex: Throwable) {
                request.onError(ex)
            }

            override fun onComplise() {
                request.onComplise()
            }

            override fun <T> Success(t: T) {
                request.Success(t)
            }

        })
    }

    override fun requestShipperId(
        context: Context,
        shipperId: String,
        request: RequestResultInterface
    ) {
        val net = UserNet.getInstance
        net.requestShipperById(context, shipperId, object : StringResultInterface {
            override fun onError(ex: Throwable) {
                request.onError(ex)
            }

            override fun onComplise() {
                request.onComplise()
            }

            override fun <T> Success(t: T) {
                request.Success(t)
            }

        })


    }
}