package com.zzzh.akhalteke.adapter

import android.content.Context
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.bean.vo.DayInfomVo
import com.zzzh.akhalteke.utils.StringUtil
import com.zzzh.akhalteke.utils.TimeUtil
import com.zzzh.akhalteke.utils.Utils

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.adapter
 * @Email : yufeilong92@163.com
 * @Time :2019/3/19 9:05
 * @Purpose :
 */
class ShipperGoodinfomAdapter(var context: Context, var infoList: MutableList<DayInfomVo>) :
        BaseQuickAdapter<DayInfomVo, BaseViewHolder>(R.layout.item_shipper_good_infom, infoList) {
    override fun convert(helper: BaseViewHolder?, item: DayInfomVo?) {
        val position = helper!!.layoutPosition
        val tv_start = helper.getView<TextView>(R.id.tv_shipper_start_address)
        val tv_infom = helper.getView<TextView>(R.id.tv_shipper_infom)
        val tv_time = helper.getView<TextView>(R.id.tv_shipper_time)
        tv_start.text = Utils.showAddress(mContext, item!!.loadAreaName, item!!.unloadAreaName)
        var str = StringBuffer()
        if (!StringUtil.isEmpty(item.carLength)) {
            str.append(item.carLength)
            str.append(" ")
        }
        if (!StringUtil.isEmpty(item.carType)) {
            str.append(item.carType)
            str.append(" ")
        }
        if (!StringUtil.isEmpty(item.weightVolume)) {
            str.append(item.weightVolume)
        }
        tv_infom.text = str.toString()
        if (item.loadTime == "0") {
            tv_time.visibility = View.GONE
        } else{
            tv_time.visibility = View.VISIBLE
            tv_time.text = TimeUtil.getInstance()!!.getHourAndMin(item.loadTime!!.toLong())
        }

    }
}