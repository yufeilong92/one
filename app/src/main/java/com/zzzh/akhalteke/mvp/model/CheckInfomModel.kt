package com.zzzh.akhalteke.mvp.model

import android.content.Context
import com.zzzh.akhalteke.mvp.contract.CheckInfomContract

import com.zzzh.akhalteke.mvp.view.RequestResultInterface
import com.zzzh.akhalteke.mvp.view.StringResultInterface
import com.zzzh.akhalteke.net.UserNet

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.mvp.model
 * @Email : yufeilong92@163.com
 * @Time :2019/4/9 9:25
 * @Purpose :检车司机信息
 */
class CheckInfomModel : CheckInfomContract.Model {
    override fun requestCheckInfom(context: Context, request: RequestResultInterface) {
        val net = UserNet.getInstance
        net.requestInfomCheck(context, object : StringResultInterface {
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