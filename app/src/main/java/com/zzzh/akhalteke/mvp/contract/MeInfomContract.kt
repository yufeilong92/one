package com.zzzh.akhalteke.mvp.contract

import android.content.Context
import com.zzzh.akhalteke.mvp.view.RequestResultInterface
import java.io.File

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.mvp.contract
 * @Email : yufeilong92@163.com
 * @Time :2019/5/7 10:46
 * @Purpose :个人修改信息页
 */
interface MeInfomContract {
    interface View {
        fun UpdataHearSuccess(t:Any?)
        fun UpdataHearError(ex:Throwable)
        fun UpdataHearComplise()

    }

    interface Model {
        fun submitHear(context: Context,file:File, request: RequestResultInterface)
    }

    interface Presenter {
        fun initMvp(view: View, model: Model)
        fun submitHear(context:Context,file:File)
    }
}