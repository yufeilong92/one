package com.zzzh.akhalteke.adapter

import android.content.Context
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.bean.StringInfo

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.adapter
 * @Email : yufeilong92@163.com
 * @Time :2019/3/22 16:54
 * @Purpose :我的消息
 */
class MsgAdapter (var context: Context, var infoList: MutableList<String>) :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_msg_layout, infoList) {
    override fun convert(helper: BaseViewHolder?, item: String?) {

    }
}