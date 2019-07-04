package com.zzzh.akhalteke.mvp.presenter

import android.content.Context
import com.zzzh.akhalteke.mvp.contract.MeInfomContract
import com.zzzh.akhalteke.mvp.view.RequestResultInterface
import java.io.File

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.mvp.presenter
 * @Email : yufeilong92@163.com
 * @Time :2019/5/7 10:48
 * @Purpose :个人信息页
 */
class MeInfomPresenter : MeInfomContract.Presenter {

    var view: MeInfomContract.View? = null
    var model: MeInfomContract.Model? = null
    override fun initMvp(view: MeInfomContract.View, model: MeInfomContract.Model) {
        this.view = view
        this.model = model
    }

    override fun submitHear(context: Context, file: File) {
        model!!.submitHear(context, file, object : RequestResultInterface {
            override fun onError(ex: Throwable) {
                view!!.UpdataHearError(ex)
            }

            override fun onComplise() {
                view!!.UpdataHearComplise()
            }

            override fun <T> Success(t: T) {
                view!!.UpdataHearSuccess(t)
            }
        })
    }
}