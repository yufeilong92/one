package com.zzzh.akhalteke.mvp.contract

import android.content.Context
import com.zzzh.akhalteke.mvp.view.RequestResultInterface
import java.io.File
import java.lang.Exception

interface DrivingContract {
    interface Model {
        fun submitDivingInfom(
                m: Context,
                map: HashMap<Int, String>, role: String
                , r: RequestResultInterface
        )

        fun submitDriverLicense(m: Context, file: File, request: RequestResultInterface)

    }

    interface View {
        fun SubmitSuccess(t: Any?)
        fun SubmitError(ex: Throwable)
        fun SubmitComplise()
        fun DriverLicenseSuccess(t: Any?)
        fun DriverLicenseError(ex: Throwable)
        fun DriverLicenseComplise()

    }

    interface Presenter {
        fun initMvp(v: View, m: Model)
        fun submitDivingInfom(
                m: Context,
                map: HashMap<Int, String>, role: String)

        fun submitDriverLicense(m: Context, file: File)
    }
}