package com.zzzh.akhalteke.mvp.presenter

import android.content.Context
import com.zzzh.akhalteke.mvp.contract.CodeContract
import com.zzzh.akhalteke.mvp.view.RequestResultInterface
import com.zzzh.akhalteke.mvp.view.StringResultInterface

/**
 * @Author : YFL  is Creating a porject in Lenovo
 * @Email : yufeilong92@163.com
 * @Time :2019/3/6 18:43
 * @Purpose :验证码
 */

class CodePresenter : CodeContract.Presenter {
    override fun checkCode(context: Context, phone: String, code: String) {
        m!!.checkCode(context, phone, code, object : RequestResultInterface {
            override fun onError(ex: Throwable) {
                v!!.CheckCodeError(ex)
            }

            override fun onComplise() {
                v!!.CodeCompter()
            }

            override fun <T> Success(t: T) {
                v!!.CheckCodeSuccess(t)
            }

        })
    }

    var v: CodeContract.View? = null;
    var m: CodeContract.Model? = null;
    override fun initMvp(v: CodeContract.View, m: CodeContract.Model) {
        this.v = v;
        this.m = m;
    }

    override fun getCode(context: Context, phone: String) {
        m!!.getCode(context, phone, object : RequestResultInterface {
            override fun onError(ex: Throwable) {
                v!!.CodeError(ex)
            }

            override fun onComplise() {
                v!!.CodeCompter()
            }

            override fun <T> Success(t: T) {
                v!!.CodeSuccess(t)
            }

        })
    }


}
