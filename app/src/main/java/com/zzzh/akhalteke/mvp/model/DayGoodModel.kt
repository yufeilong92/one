package com.zzzh.akhalteke.mvp.model

import android.content.Context
import com.zzzh.akhalteke.mvp.contract.DayGoodContract
import com.zzzh.akhalteke.mvp.view.RequestResultInterface
import com.zzzh.akhalteke.mvp.view.StringResultInterface
import com.zzzh.akhalteke.net.UserNet

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.mvp.model
 * @Email : yufeilong92@163.com
 * @Time :2019/3/19 10:36
 * @Purpose :当天货源
 */
class DayGoodModel : DayGoodContract.Model {
    override fun requestShipperGood(context: Context, shipperId: String, page: Int, request: RequestResultInterface) {
        val net = UserNet.getInstance
        net.requestShipperGoodList(context, shipperId, page, object : StringResultInterface {
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