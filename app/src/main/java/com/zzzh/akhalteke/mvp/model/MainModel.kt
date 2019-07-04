package com.zzzh.akhalteke.mvp.model

import android.content.Context
import com.zzzh.akhalteke.mvp.contract.MainContract
import com.zzzh.akhalteke.mvp.view.RequestResultInterface
import com.zzzh.akhalteke.mvp.view.StringResultInterface
import com.zzzh.akhalteke.net.UserNet

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.mvp.model
 * @Email : yufeilong92@163.com
 * @Time :2019/6/14 15:56
 * @Purpose :首页内容
 */
class MainModel : MainContract.Model {
    override fun requestMainContent(context: Context, request: RequestResultInterface) {
        val net = UserNet.getInstance
        net.requestMainContent(context, object : StringResultInterface {
            override fun <T> Success(t: T) {
                request.Success(t)
            }

            override fun onError(ex: Throwable) {
                request.onError(ex)
            }

            override fun onComplise() {
                request.onComplise()
            }

        })

    }
}