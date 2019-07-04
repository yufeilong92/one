package com.zzzh.akhalteke.mvp.presenter

import android.content.Context
import com.zzzh.akhalteke.mvp.contract.CarContract
import com.zzzh.akhalteke.mvp.view.RequestResultInterface
import java.io.File

class CarPresenter : CarContract.Presenter {


    var view: CarContract.View? = null;
    var model: CarContract.Model? = null;
    override fun initMvp(view: CarContract.View, model: CarContract.Model) {
        this.view = view;
        this.model = model
    }

    override fun submitCarInfom(
            m: Context,

            plateNumber: String,
            carPlateColour: String,
            carType: String,
            carLength: String,
            load: String,
            map: HashMap<Int, String>
    ) {
        model!!.submitCarInfom(m, plateNumber, carPlateColour, carType, carLength, load, map, object : RequestResultInterface {
            override fun <T> Success(t: T) {
                view!!.SubmitSuccess(t)
            }

            override fun onError(ex: Throwable) {
                view!!.SubmitError(ex)
            }

            override fun onComplise() {
                view!!.SubmitComplise()
            }

        })

    }

    override fun submitDrivingLicense(m: Context, file: File) {
        model!!.submitDrivingLicense(m, file, object : RequestResultInterface {
            override fun onError(ex: Throwable) {
                view!!.DrivingLicenseError(ex)
            }

            override fun onComplise() {
                view!!.DrivingLicenseComplise()
            }

            override fun <T> Success(t: T) {
                view!!.DrivingLicenseSuccess(t)
            }
        })
    }

}