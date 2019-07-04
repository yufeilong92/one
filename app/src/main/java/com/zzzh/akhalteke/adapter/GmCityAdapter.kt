package com.zzzh.akhalteke.adapter

import android.content.Context
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.bean.vo.City
import com.zzzh.akhalteke.bean.vo.RlvSelectVo

/**
 * @Author : YFL  is Creating a porject in com.zzzh.akhalteke.adapter
 * @Package com.zzzh.akhalteke.adapter
 * @Email : yufeilong92@163.com
 * @Time :2019/3/13 14:24
 * @Purpose :通用地区适配器
 */

class GmCityAdapter(
    var context: Context,
    var infoList: MutableList<City>,
    var mSelectVo: MutableList<RlvSelectVo>
) :
    BaseQuickAdapter<City, BaseViewHolder>(R.layout.item_province, infoList) {
    override fun convert(helper: BaseViewHolder?, item: City?) {
        val position = helper!!.layoutPosition
        val imgaView = helper.getView<View>(R.id.iv_select_color)
        val textview = helper.getView<TextView>(R.id.tv_area_name)
        textview.text = item!!.area_name
        val vo = mSelectVo[position]
        if (vo.select) {
            imgaView.visibility = View.VISIBLE
            textview.setTextColor(context.resources.getColor(R.color.gm_color))
        } else {
            imgaView.visibility = View.INVISIBLE
            textview.setTextColor(context.resources.getColor(R.color.text_title_color))
        }
    }
    fun Refresh(mSelectVo: MutableList<RlvSelectVo>) {
        this.mSelectVo = mSelectVo
        notifyDataSetChanged()

    }

}