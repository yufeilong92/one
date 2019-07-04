package com.zzzh.akhalteke.mvp.presenter

import android.content.Context
import com.zzzh.akhalteke.mvp.contract.CarTypeContract
import com.zzzh.akhalteke.mvp.view.RequestResultInterface

/**
 * @Author : YFL  is Creating a porject in Lenovo
 * @Email : yufeilong92@163.com
 * @Time :2019/3/11 11:11
 * @Purpose :车类型
 */

class CarTypePresenter : CarTypeContract.Presenter {
    var view: CarTypeContract.View? = null
    var modle: CarTypeContract.Model? = null
    override fun initMvp(v: CarTypeContract.View, m: CarTypeContract.Model) {
        this.view = v
        this.modle = m
    }

    override fun getCarType(context: Context) {
        modle!!.getCarType(context, object : RequestResultInterface {
            override fun <T> Success(t: T) {
                view!!.CarTypeSuccess(t)
            }

            override fun onError(ex: Throwable) {
                view!!.CarTypeError(ex)
            }

            override fun onComplise() {
                view!!.CarTypeComplise()
            }

        })
    }

}