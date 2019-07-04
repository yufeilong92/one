package com.zzzh.akhalteke.mvp.presenter

import android.content.Context
import com.zzzh.akhalteke.mvp.contract.OrderContract
import com.zzzh.akhalteke.mvp.view.RequestResultInterface
import com.zzzh.akhalteke.mvp.view.StringResultInterface

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.mvp.presenter
 * @Email : yufeilong92@163.com
 * @Time :2019/3/19 15:46
 * @Purpose :司机端获取订单列表
 */
class OrderPresenter : OrderContract.Presenter {
    override fun requestOrderListTwo(context: Context, status: String, page: Int) {
        model!!.requestOrderList(context, status, page, object : RequestResultInterface {
            override fun onError(ex: Throwable) {
                view!!.TwoError(ex)
            }

            override fun onComplise() {
                view!!.TwoComplise()
            }

            override fun <T> Success(t: T) {
                view!!.TwoSuccess(t)
            }
        })
    }

    var view: OrderContract.View? = null
    var model: OrderContract.Model? = null
    override fun initMvp(view: OrderContract.View, model: OrderContract.Model) {
        this.view = view
        this.model = model

    }

    override fun requestOrderListOne(context: Context, status: String, page: Int) {
        model!!.requestOrderList(context, status, page, object : RequestResultInterface {
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
}