package com.zzzh.akhalteke.mvp.contract

import android.content.Context
import com.zzzh.akhalteke.mvp.view.RequestResultInterface

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.mvp.contract
 * @Email : yufeilong92@163.com
 * @Time :2019/4/9 10:23
 * @Purpose :上传用户地址
 */
interface UpdateAddressContract {
    interface View {
        fun Success(t:Any?)
        fun Error(ex:Throwable)
        fun Complise()

    }

    interface Model {
        fun submitPointAdd(context: Context,latitude:String,longitude:String,
                           loc_time:String ,ciird_type_input:String,request: RequestResultInterface)
    }

    interface Presenter {
        fun initMvp(view: View, model: Model)
        fun submitPointAdd(context: Context,latitude:String,longitude:String,
                           loc_time:String ,ciird_type_input:String)
    }
}