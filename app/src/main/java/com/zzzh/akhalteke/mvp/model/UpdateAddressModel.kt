package com.zzzh.akhalteke.mvp.model

import android.content.Context
import com.zzzh.akhalteke.mvp.contract.UpdateAddressContract
import com.zzzh.akhalteke.mvp.view.RequestResultInterface
import com.zzzh.akhalteke.mvp.view.StringResultInterface
import com.zzzh.akhalteke.net.UserNet

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.mvp.model
 * @Email : yufeilong92@163.com
 * @Time :2019/4/9 10:23
 * @Purpose :上传用户地址
 */
class UpdateAddressModel : UpdateAddressContract.Model {
    override fun submitPointAdd(context: Context, latitude: String, longitude: String, loc_time: String, ciird_type_input: String, request: RequestResultInterface) {
        val net = UserNet.getInstance
        net.submitPointAdd(context, latitude, longitude, loc_time, ciird_type_input, object : StringResultInterface {
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