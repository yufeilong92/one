package com.zzzh.akhalteke.mvp.model

import android.content.Context
import com.zzzh.akhalteke.mvp.contract.CarTypeContract
import com.zzzh.akhalteke.mvp.view.RequestResultInterface
import com.zzzh.akhalteke.mvp.view.StringResultInterface
import com.zzzh.akhalteke.net.UserNet

class CarTypeModel : CarTypeContract.Model {
    override fun getCarType(context: Context, n: RequestResultInterface) {
        var net = UserNet.getInstance
        net.getCarType(context, object : StringResultInterface {
            override fun <T> Success(t: T) {
                n.Success(t)
            }

            override fun onError(ex: Throwable) {
                n.onError(ex)
            }

            override fun onComplise() {
                n.onComplise()
            }

        })
    }

}