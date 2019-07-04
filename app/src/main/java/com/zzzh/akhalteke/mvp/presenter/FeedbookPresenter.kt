package com.zzzh.akhalteke.mvp.presenter

import android.content.Context
import com.zzzh.akhalteke.mvp.contract.FeedbookContract
import com.zzzh.akhalteke.mvp.view.RequestResultInterface

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.mvp.presenter
 * @Email : yufeilong92@163.com
 * @Time :2019/3/22 16:18
 * @Purpose :意见反馈
 */
class FeedbookPresenter : FeedbookContract.Presenter {
    var view: FeedbookContract.View? = null
    var model: FeedbookContract.Model? = null
    override fun initMvp(view: FeedbookContract.View, model: FeedbookContract.Model) {
        this.view = view
        this.model = model

    }

    override fun submitFeedbook(context: Context, content: String) {
        model!!.submitFeedbook(context, content, object : RequestResultInterface {
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