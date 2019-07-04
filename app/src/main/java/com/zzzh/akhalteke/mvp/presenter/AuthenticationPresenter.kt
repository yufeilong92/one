package com.zzzh.akhalteke.mvp.presenter

import android.content.Context
import com.zzzh.akhalteke.mvp.contract.AuthenticationContract
import com.zzzh.akhalteke.mvp.view.RequestResultInterface
import java.io.File

/**
 * @Author : YFL  is Creating a porject in Lenovo
 * @Email : yufeilong92@163.com
 * @Time :2019/3/11 11:11
 * @Purpose :车主实名认证
 */

class AuthenticationPresenter : AuthenticationContract.Presenter {


    var view: AuthenticationContract.View? = null
    var model: AuthenticationContract.Model? = null
    override fun initMvp(v: AuthenticationContract.View, m: AuthenticationContract.Model) {
        this.view = v
        this.model = m
    }

    override fun submitUserInfom(
            context: Context,
            iDnumber: String,
            map: HashMap<Int, String>,
            name: String
    ) {
        model!!.submitUserInfom(context, iDnumber, map, name, object : RequestResultInterface {
            override fun <T> Success(t: T) {
                view!!.SubmitSuccess(t)

            }

            override fun onError(ex: Throwable) {
                view!!.SubmitError(ex)
            }

            override fun onComplise() {
                view!!.SubmitCompter()
            }

        })
    }

    override fun submitUserIdCarFace(context: Context, filr: File) {
        model!!.submitUserIdCarFace(context, filr, object : RequestResultInterface {
            override fun onError(ex: Throwable) {
                view!!.FileFaceError(ex)
            }

            override fun onComplise() {
                view!!.FileFaceCompter()
            }

            override fun <T> Success(t: T) {
                view!!.FileFaceSuccess(t)
            }
        })
    }

    override fun submitUserIdCarBack(context: Context, filr: File) {
        model!!.submitUserIdCarBack(context, filr, object : RequestResultInterface {
            override fun onError(ex: Throwable) {
                view!!.FileBackError(ex)
            }

            override fun onComplise() {
                view!!.FileBackCompter()
            }

            override fun <T> Success(t: T) {
                view!!.FileBackSuccess(t)
            }
        })

    }
}
