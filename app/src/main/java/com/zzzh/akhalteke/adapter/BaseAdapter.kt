package com.zzzh.akhalteke.adapter

import android.content.Context
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.bean.StringInfo
import com.zzzh.akhalteke.utils.ToolUtils

class BaseAdapter(var context: Context, var infoList: MutableList<StringInfo>) :
    BaseQuickAdapter<StringInfo, BaseViewHolder>(R.layout.item_test, infoList) {

    private var dpspace = 0

    init {
        dpspace = ToolUtils.dpTopx(context, 6f)
    }

    override fun convert(helper: BaseViewHolder?, item: StringInfo?) {
        val position = helper!!.layoutPosition
    }

}