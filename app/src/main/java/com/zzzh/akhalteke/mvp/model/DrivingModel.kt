package com.zzzh.akhalteke.mvp.model

import android.content.Context
import com.zzzh.akhalteke.mvp.contract.DrivingContract
import com.zzzh.akhalteke.mvp.view.RequestResultInterface
import com.zzzh.akhalteke.mvp.view.StringResultInterface
import com.zzzh.akhalteke.net.UserNet
import java.io.File

class DrivingModel : DrivingContract.Model {

    override fun submitDivingInfom(
            m: Context,
            map: HashMap<Int, String>,
            role: String,
            r: RequestResultInterface
    ) {
        var net = UserNet.getInstance
        net!!.submitDrivieInfom(m, map, role, object : StringResultInterface {
            override fun <T> Success(t: T) {
                r.Success(t)
            }

            override fun onError(ex: Throwable) {
                r.onError(ex)
            }

            override fun onComplise() {
                r.onComplise()
            }

        })

    }

    override fun submitDriverLicense(m: Context, file: File, request: RequestResultInterface) {
        val net = UserNet.getInstance
        net.submitDriversLicense(m, file, object : StringResultInterface {
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