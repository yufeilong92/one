package com.zzzh.akhalteke.mvp.model

import android.content.Context
import com.zzzh.akhalteke.bean.vo.SelectPath
import com.zzzh.akhalteke.mvp.contract.UpLoadReceiptContract
import com.zzzh.akhalteke.mvp.view.RequestResultInterface
import com.zzzh.akhalteke.mvp.view.StringResultInterface
import com.zzzh.akhalteke.net.UserNet

/**
 * @Author : YFL  is Creating a porject in com.zzzh.akhalteke.mvp.model
 * @Package com.zzzh.akhalteke.mvp.model
 * @Email : yufeilong92@163.com
 * @Time :2019/3/29 15:24
 * @Purpose :查看回单
 */
class UpLoadReceiptModel : UpLoadReceiptContract.Model {
    override fun requestRecepit(context: Context, orderId: String, request: RequestResultInterface) {
        val net = UserNet.getInstance
        net.requestLookReceipt(context, orderId, object : StringResultInterface {
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

    override fun submitUpLoadRecepit(context: Context, orderId: String, map: MutableList<SelectPath>,
                                     request: RequestResultInterface) {
        val net = UserNet.getInstance
        net.submitUploadReceiptOne(context, orderId, map, object : StringResultInterface {
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