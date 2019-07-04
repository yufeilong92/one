package com.zzzh.akhalteke.mvp.presenter

import android.content.Context
import com.zzzh.akhalteke.mvp.contract.MsgContract
import com.zzzh.akhalteke.mvp.view.RequestResultInterface

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.mvp.presenter
 * @Email : yufeilong92@163.com
 * @Time :2019/3/22 16:49
 * @Purpose :我的消息
 */
class MsgPresenter : MsgContract.Presenter {
    var view: MsgContract.View? = null
    var model: MsgContract.Model? = null
    override fun initMvp(view: MsgContract.View, model: MsgContract.Model) {
        this.view = view
        this.model = model

    }

    override fun requestMsgListOne(context: Context,page:Int) {
        model!!.requestMsgList(context, page,object : RequestResultInterface {
            override fun onError(ex: Throwable) {
                view!!.OneError(ex)
            }

            override fun onComplise() {
                view!!.OneComplise()
            }

            override fun <T> Success(t: T) {
                view!!.OneSuccess(t)
            }
        })
    }

    override fun requestMsgListTwo(context: Context,page:Int) {
        model!!.requestMsgList(context, page,object : RequestResultInterface {
            override fun onError(ex: Throwable) {
                view!!.TwoError(ex)
            }

            override fun onComplise() {
                view!!.TwoComplist()
            }

            override fun <T> Success(t: T) {
                view!!.TwoSuccess(t)
            }
        })
    }
}