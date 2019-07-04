package com.zzzh.akhalteke.mvp.model

import android.content.Context
import com.zzzh.akhalteke.BaseApplication
import com.zzzh.akhalteke.mvp.contract.CarLengthContract
import com.zzzh.akhalteke.mvp.view.RequestResultInterface
import com.zzzh.akhalteke.mvp.view.StringResultInterface
import com.zzzh.akhalteke.net.UserNet

class CarLengthModel:CarLengthContract.Model{
    override fun getCarLength(context: Context, request: RequestResultInterface) {
        var net=UserNet.getInstance
        net!!.getCarLength(context ,object :StringResultInterface{
            override fun <T> Success(t: T) {
                request.Success(t)
            }

            override fun onError(ex: Throwable) {
                request.onError(ex)
            }

            override fun onComplise() {
                request.onComplise()
            }

        })
    }

}