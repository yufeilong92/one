package com.zzzh.akhalteke.mvp.contract

import android.content.Context
import com.zzzh.akhalteke.mvp.view.RequestResultInterface
import java.io.File

interface CarContract {
    interface View {
        fun SubmitSuccess(t: Any?)
        fun SubmitError(ex: Throwable)
        fun SubmitComplise()
        fun DrivingLicenseSuccess(t: Any?)
        fun DrivingLicenseError(ex: Throwable)
        fun DrivingLicenseComplise()
    }

    interface Model {
        fun submitCarInfom(
            context: Context,
            plateNumber: String, carPlateColour: String, carType: String
            , carLength: String, load: String,map:HashMap<Int,String>,  n: RequestResultInterface
        )
        fun submitDrivingLicense(m: Context, file: File, request: RequestResultInterface)

    }

    interface Presenter {
        fun initMvp(view: View, model: Model)
        fun submitCarInfom(
            m:Context,
             plateNumber: String, carPlateColour: String, carType: String
            , carLength: String, load: String,map:HashMap<Int,String>)
        fun submitDrivingLicense(m: Context, file: File)
    }
}