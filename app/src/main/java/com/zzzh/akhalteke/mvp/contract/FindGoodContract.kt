package com.zzzh.akhalteke.mvp.contract

import android.content.Context
import com.zzzh.akhalteke.mvp.view.RequestResultInterface

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.mvp.contract
 * @Email : yufeilong92@163.com
 * @Time :2019/3/15 15:42
 * @Purpose :获取货源列表
 */
interface FindGoodContract {
    interface View {
        fun FindGoodListOneSuccess(t:Any?)
        fun FindGoodListOneError(ex:Throwable)
        fun FindGoodListOneComplise()
        fun FindGoodListTwoSuccess(t:Any?)
        fun FindGoodListTwoError(ex:Throwable)
        fun FindGoodListTwoComplise()
    }

    interface Model {
        fun requestFindGoodList(context: Context,loadAreaCode:String , unloadAreaCode:String,
                                sortType:String,type:String,loadTime:String,
                                carLength:String,carType:String,page:Int,
                                request: RequestResultInterface)
    }

    interface Presenter {
        fun initMvp(view: View, model: Model)
        fun requestFindGoodListOne(context: Context,loadAreaCode:String , unloadAreaCode:String,
                                sortType:String,type:String,loadTime:String,
                                carLength:String,carType:String,page:Int)
        fun requestFindGoodListTwo(context: Context,loadAreaCode:String , unloadAreaCode:String,
                                sortType:String,type:String,loadTime:String,
                                carLength:String,carType:String,page:Int)
    }
}