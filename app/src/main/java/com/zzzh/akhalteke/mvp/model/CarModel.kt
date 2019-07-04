package com.zzzh.akhalteke.mvp.model

import android.content.Context
import com.zzzh.akhalteke.mvp.contract.CarContract
import com.zzzh.akhalteke.mvp.view.RequestResultInterface
import com.zzzh.akhalteke.mvp.view.StringResultInterface
import com.zzzh.akhalteke.net.UserNet
import java.io.File

class CarModel : CarContract.Model {


    override fun submitCarInfom(
            context: Context,

            plateNumber: String,
            carPlateColour: String,
            carType: String,
            carLength: String,
            load: String,
            map: HashMap<Int, String>,

            n: RequestResultInterface
    ) {
        var net = UserNet.getInstance
        net!!.submitAuthCarInfom(context, map, plateNumber, carPlateColour, carType,
                carLength, load,
                object : StringResultInterface {
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

    override fun submitDrivingLicense(m: Context, file: File, request: RequestResultInterface) {
        val net = UserNet.getInstance
        net.submitDriveringLicense(m, file, object : StringResultInterface {
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