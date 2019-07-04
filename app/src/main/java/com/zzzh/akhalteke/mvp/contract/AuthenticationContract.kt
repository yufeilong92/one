package com.zzzh.akhalteke.mvp.contract

import android.content.Context
import com.zzzh.akhalteke.mvp.view.RequestResultInterface
import com.zzzh.akhalteke.mvp.view.StringResultInterface
import java.io.File

interface AuthenticationContract {
    interface Model {
        fun submitUserInfom(
            context: Context, iDnumber: String, map: HashMap<Int, String>,
            name: String,  m: RequestResultInterface
        )
        fun submitUserIdCarFace( context: Context, filr:File,m:RequestResultInterface)
        fun submitUserIdCarBack( context: Context, filr:File,m:RequestResultInterface)
    }

    interface View {
        fun SubmitSuccess(t: Any?)
        fun SubmitError(ex: Throwable)
        fun SubmitCompter()

        fun FileFaceSuccess(t: Any?)
        fun FileFaceError(ex: Throwable)
        fun FileFaceCompter()

        fun FileBackSuccess(t: Any?)
        fun FileBackError(ex: Throwable)
        fun FileBackCompter()
    }

    interface Presenter {
        fun initMvp(v:View,m:Model)
        fun submitUserInfom(
            context: Context,  iDnumber: String, map: HashMap<Int, String>,
            name: String
        )
        fun submitUserIdCarFace( context: Context, filr:File)
        fun submitUserIdCarBack( context: Context, filr:File)

    }
}