package com.zzzh.akhalteke.mvp.model

import android.content.Context
import com.zzzh.akhalteke.mvp.contract.MsgContract
import com.zzzh.akhalteke.mvp.view.RequestResultInterface

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.mvp.model
 * @Email : yufeilong92@163.com
 * @Time :2019/3/22 16:48
 * @Purpose :我的消息
 */
class MsgModel : MsgContract.Model {
    override fun requestMsgList(context: Context, page: Int, request: RequestResultInterface) {
        request.Success(null)
        request.onError(Throwable())
        request.onComplise()
    }

}