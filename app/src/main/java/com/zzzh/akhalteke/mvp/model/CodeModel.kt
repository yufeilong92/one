package com.zzzh.akhalteke.mvp.model

import android.content.Context
import com.zzzh.akhalteke.mvp.contract.CodeContract
import com.zzzh.akhalteke.mvp.view.RequestResultInterface
import com.zzzh.akhalteke.mvp.view.StringResultInterface
import com.zzzh.akhalteke.net.UserNet
import com.zzzh.akhalteke.retrofit.RetrofitFactory
import com.zzzh.akhalteke.utils.ToolUtils.onError
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

class CodeModel : CodeContract.Model {
    override fun checkCode(context: Context, phone: String, code: String, m: RequestResultInterface) {
        var net = UserNet.getInstance
        net!!.checkCode(context, phone, code, object : StringResultInterface {
            override fun onError(ex: Throwable) {
                m.onError(ex)
            }

            override fun onComplise() {
                m.onComplise()
            }

            override fun <T> Success(t: T) {
                m.Success(t)
            }

        })
    }

    override fun getCode(context: Context, phone: String, m: RequestResultInterface) {
        var net = UserNet.getInstance
        net!!.getCode(context, phone, object : StringResultInterface {
            override fun <T> Success(t: T) {
                m.Success(t)
            }

            override fun onError(ex: Throwable) {
                m.onError(ex)
            }

            override fun onComplise() {
                m.onComplise()
            }

        })

    }


}
