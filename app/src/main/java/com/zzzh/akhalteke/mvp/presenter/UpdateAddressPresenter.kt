package com.zzzh.akhalteke.mvp.presenter

import android.content.Context
import com.zzzh.akhalteke.mvp.contract.UpdateAddressContract
import com.zzzh.akhalteke.mvp.view.RequestResultInterface
import com.zzzh.akhalteke.net.UserNet

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.mvp.presenter
 * @Email : yufeilong92@163.com
 * @Time :2019/4/9 10:24
 * @Purpose :上传用户地址
 */
class UpdateAddressPresenter : UpdateAddressContract.Presenter {


    var view: UpdateAddressContract.View? = null
    var model: UpdateAddressContract.Model? = null
    override fun initMvp(view: UpdateAddressContract.View, model: UpdateAddressContract.Model) {
        this.view = view
        this.model = model

    }

    override fun submitPointAdd(context: Context, latitude: String, longitude: String, loc_time: String, ciird_type_input: String) {
        model!!.submitPointAdd(context, latitude, longitude, loc_time, ciird_type_input, object : RequestResultInterface {
            override fun onError(ex: Throwable) {
                view!!.Error(ex)
            }

            override fun onComplise() {
                view!!.Complise()
            }

            override fun <T> Success(t: T) {
                view!!.Success(t)
            }
        })
    }

}