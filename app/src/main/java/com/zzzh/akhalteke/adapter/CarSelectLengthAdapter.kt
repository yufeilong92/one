package com.zzzh.akhalteke.adapter

import android.content.Context
import android.widget.CheckBox
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.bean.vo.CarLengthVo
import com.zzzh.akhalteke.bean.vo.RlvSelectVo

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.adapter
 * @Email : yufeilong92@163.com
 * @Time :2019/3/13 17:43
 * @Purpose :用车车长类型
 */
class CarSelectLengthAdapter(
    var context: Context,
    var infoList: MutableList<CarLengthVo>,
    var list: MutableList<RlvSelectVo>
) :
    BaseQuickAdapter<CarLengthVo, BaseViewHolder>(R.layout.item_select, infoList) {
    override fun convert(helper: BaseViewHolder?, item: CarLengthVo?) {
        val position = helper!!.layoutPosition
        val checkBox = helper.getView<CheckBox>(R.id.chb_select)
        checkBox.text = item!!.length
        checkBox.isChecked = list[position].select
        checkBox.setOnClickListener {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(this, helper.itemView, position)
            }
        }
    }

    fun refhresh(list: MutableList<RlvSelectVo>) {
        if (list == null||list.isEmpty()) return
        this.list = list
        notifyDataSetChanged()
    }
}