package com.zzzh.akhalteke.mvp.presenter

import android.content.Context
import com.zzzh.akhalteke.mvp.contract.CarLengthContract
import com.zzzh.akhalteke.mvp.view.RequestResultInterface
/**
 * @Author : YFL  is Creating a porject in Lenovo
 * @Email : yufeilong92@163.com
 * @Time :2019/3/11 11:11
 * @Purpose :车长
 */

class CarLengthPresenter : CarLengthContract.Presenter {
    var view: CarLengthContract.View? = null;
    var model: CarLengthContract.Model? = null;
    override fun initMvp(view: CarLengthContract.View, model: CarLengthContract.Model) {
        this.view = view
        this.model = model
    }

    override fun getCarLength(context: Context) {
        model!!.getCarLength(context, object : RequestResultInterface {
            override fun <T> Success(t: T) {
                view!!.Success(t)
            }

            override fun onError(ex: Throwable) {
                view!!.Error(ex)
            }

            override fun onComplise() {
                view!!.Complise()
            }

        })
    }

}