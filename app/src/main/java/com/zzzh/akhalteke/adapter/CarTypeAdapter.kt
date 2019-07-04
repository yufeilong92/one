package com.zzzh.akhalteke.adapter

import android.content.Context
import android.view.View
import android.widget.Space
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.bean.vo.CarTypeVo

class CarTypeAdapter(var context: Context, var infoList: MutableList<CarTypeVo>) :
    BaseQuickAdapter<CarTypeVo, BaseViewHolder>(R.layout.item_car_type, infoList) {

    override fun convert(helper: BaseViewHolder?, item: CarTypeVo?) {
        val position = helper!!.layoutPosition
        val tvCarType = helper.getView<TextView>(R.id.tv_car_type_content)
        val sc = helper.getView<Space>(R.id.sc_bottom)
        tvCarType.text = item!!.name
        if (position==infoList.size-1){
            sc.visibility= View.VISIBLE
        }

    }

}