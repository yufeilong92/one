package com.zzzh.akhalteke.mvp.model

import android.content.Context
import com.zzzh.akhalteke.mvp.contract.AuthenticationContract
import com.zzzh.akhalteke.mvp.view.RequestResultInterface
import com.zzzh.akhalteke.mvp.view.StringResultInterface
import com.zzzh.akhalteke.net.UserNet
import java.io.File

/**
 * @Author : YFL  is Creating a porject in Lenovo
 * @Email : yufeilong92@163.com
 * @Time :2019/3/11 11:05
 * @Purpose :车主实名认证
 */

class AuthenticationModel : AuthenticationContract.Model {


    override fun submitUserInfom(
            context: Context,
            iDnumber: String,
            map: HashMap<Int, String>,
            name: String,
            m: RequestResultInterface
    ) {
        var net = UserNet.getInstance
        net!!.shubmitUserInfom(context, iDnumber, map, name, object : StringResultInterface {
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

    override fun submitUserIdCarFace(context: Context, filr: File, m: RequestResultInterface) {
        val net = UserNet.getInstance
        net.submitIdCardFace(context, filr, object : StringResultInterface {
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

    override fun submitUserIdCarBack(context: Context, filr: File, m: RequestResultInterface) {
        val data = UserNet.getInstance
        data.submitIdCardBack(context, filr, object : StringResultInterface {
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
}