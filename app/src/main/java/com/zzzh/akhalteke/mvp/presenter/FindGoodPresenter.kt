package com.zzzh.akhalteke.mvp.presenter

import android.content.Context
import com.zzzh.akhalteke.mvp.contract.FindGoodContract
import com.zzzh.akhalteke.mvp.view.RequestResultInterface

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.mvp.presenter
 * @Email : yufeilong92@163.com
 * @Time :2019/3/15 15:55
 * @Purpose :货源列表
 */
class FindGoodPresenter : FindGoodContract.Presenter {
    var view: FindGoodContract.View? = null
    var model: FindGoodContract.Model? = null
    override fun initMvp(view: FindGoodContract.View, model: FindGoodContract.Model) {
        this.view = view
        this.model = model

    }

    override fun requestFindGoodListOne(
        context: Context,
        loadAreaCode: String,
        unloadAreaCode: String,
        sortType: String,
        type: String,
        loadTime: String,
        carLength: String,
        carType: String,
        page: Int
    ) {
        model!!.requestFindGoodList(
            context,
            loadAreaCode,
            unloadAreaCode,
            sortType,
            type,
            loadTime,
            carLength,
            carType,
            page,
            object : RequestResultInterface {
                override fun <T> Success(t: T) {
                    view!!.FindGoodListOneSuccess(t)
                }

                override fun onError(ex: Throwable) {
                    view!!.FindGoodListOneError(ex)
                }

                override fun onComplise() {
                    view!!.FindGoodListOneComplise()
                }
            })
    }

    override fun requestFindGoodListTwo(
        context: Context,
        loadAreaCode: String,
        unloadAreaCode: String,
        sortType: String,
        type: String,
        loadTime: String,
        carLength: String,
        carType: String,
        page: Int
    ) {
        model!!.requestFindGoodList(
            context,
            loadAreaCode,
            unloadAreaCode,
            sortType,
            type,
            loadTime,
            carLength,
            carType,
            page,
            object : RequestResultInterface {
                override fun <T> Success(t: T) {
                    view!!.FindGoodListTwoSuccess(t)
                }

                override fun onError(ex: Throwable) {
                    view!!.FindGoodListTwoError(ex)
                }

                override fun onComplise() {
                    view!!.FindGoodListTwoComplise()
                }

            })
    }
}