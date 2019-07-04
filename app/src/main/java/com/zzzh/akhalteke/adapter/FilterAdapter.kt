package com.zzzh.akhalteke.adapter

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.bean.vo.RlvSelectVo

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.adapter
 * @Email : yufeilong92@163.com
 * @Time :2019/3/15 11:44
 * @Purpose :筛选适配器
 */
class FilterAdapter(
        var context: Context,
        var infoList: MutableList<RlvSelectVo>
) : BaseQuickAdapter<RlvSelectVo, BaseViewHolder>(R.layout.item_filter, infoList) {
    override fun convert(helper: BaseViewHolder?, item: RlvSelectVo?) {
        val layoutPosition = helper!!.layoutPosition
        val textView = helper!!.getView<TextView>(R.id.tv_filter_title)
        val imageView = helper.getView<ImageView>(R.id.iv_filter_delete)
        val lineView = helper.getView<View>(R.id.view_line)
        textView.text = item!!.name
        lineView.visibility = if (layoutPosition == infoList.size - 1) View.INVISIBLE else View.VISIBLE
        imageView.setOnClickListener {
            if (setOnDeleteListener != null) {
                setOnDeleteListener!!.setItemdeleteListener(item.name!!)
            }
        }
    }

    open fun refreshData(infoList: MutableList<RlvSelectVo>) {
        this.infoList = infoList
        notifyDataSetChanged()
    }

    interface setImagerDeleteListener {
        fun setItemdeleteListener(item: String)
    }

    var setOnDeleteListener: setImagerDeleteListener? = null
    fun setOnImagerDeleteListener(listener: setImagerDeleteListener) {
        this.setOnDeleteListener = listener
    }
}