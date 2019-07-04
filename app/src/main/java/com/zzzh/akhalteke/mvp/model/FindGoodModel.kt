package com.zzzh.akhalteke.mvp.model

import android.content.Context
import com.zzzh.akhalteke.mvp.contract.FindGoodContract
import com.zzzh.akhalteke.mvp.view.RequestResultInterface
import com.zzzh.akhalteke.mvp.view.StringResultInterface
import com.zzzh.akhalteke.net.UserNet

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.mvp.model
 * @Email : yufeilong92@163.com
 * @Time :2019/3/15 15:46
 * @Purpose :货源
 */
class FindGoodModel:FindGoodContract.Model {
    override fun requestFindGoodList(
        context: Context,
        loadAreaCode: String,
        unloadAreaCode: String,
        sortType: String,
        type: String,
        loadTime: String,
        carLength: String,
        carType: String,
        page: Int,
        request: RequestResultInterface
    ) {
     val net =UserNet.getInstance
        net.requestFindGoodList(context,loadAreaCode,unloadAreaCode,sortType,type,loadTime,carLength,carType,page,object :StringResultInterface{
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