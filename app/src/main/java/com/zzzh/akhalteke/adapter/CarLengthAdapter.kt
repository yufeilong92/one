package com.zzzh.akhalteke.adapter

import android.content.Context
import android.view.View
import android.widget.Space
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.bean.vo.CarLengthVo

class CarLengthAdapter(var context: Context, var infoList: MutableList<CarLengthVo>) :
    BaseQuickAdapter<CarLengthVo, BaseViewHolder>(R.layout.item_car_type, infoList) {

    override fun convert(helper: BaseViewHolder?, item: CarLengthVo?) {
        val position = helper!!.layoutPosition
        val tvCarType = helper.getView<TextView>(R.id.tv_car_type_content)
        val sc = helper.getView<Space>(R.id.sc_bottom)
        tvCarType.text = item!!.length
        if (position==infoList.size-1){
            sc.visibility= View.VISIBLE
        }

    }

}