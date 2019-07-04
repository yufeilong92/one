package com.zzzh.akhalteke.adapter.Home

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.facebook.drawee.view.SimpleDraweeView
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.bean.vo.GoodItem
import com.zzzh.akhalteke.utils.StringUtil
import com.zzzh.akhalteke.utils.TimeSampUtil
import com.zzzh.akhalteke.utils.Utils

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.adapter
 * @Email : yufeilong92@163.com
 * @Time :2019/3/16 10:34
 * @Purpose :找货适配器
 */
class FindNewGoodListAdapter(var context: Context, var infoList: MutableList<GoodItem>) :
        BaseQuickAdapter<GoodItem, BaseViewHolder>(R.layout.item_find_new_good, infoList) {
    var setOnPhoneClikcItem: OnPhoneClickItemListener? = null

    interface OnPhoneClickItemListener {
        fun onPhoneClickItemLisener(postion: Int, str: String)
    }

    fun setPhoneOnClikcListener(lisenter: OnPhoneClickItemListener) {
        this.setOnPhoneClikcItem = lisenter
    }

    var OnClikcItem: OnClickItemListener? = null

    interface OnClickItemListener {
        fun onClickItemLisener(postion: Int, id: String)
    }

    fun setOnClikcListener(lisenter: OnClickItemListener) {
        this.OnClikcItem = lisenter
    }

    override fun convert(helper: BaseViewHolder?, item: GoodItem?) {
        val position = helper!!.layoutPosition
        val simpleDraweeView = helper!!.getView<SimpleDraweeView>(R.id.iv_item_new_order_hear)
        //找货
        val tvfindgoodstartaddress = helper.getView<TextView>(R.id.tv_item_findgood_city)
        val tvaddress = helper!!.getView<TextView>(R.id.tv_item_findgood_address)
        //卸货
        val tvfindgoodendaddress = helper.getView<TextView>(R.id.tv_item_findgood_loadcity)
        val tvundaddress = helper.getView<TextView>(R.id.tv_item_findgood_loadAddress)

        val tvfindgoodtime = helper!!.getView<TextView>(R.id.tv_item_findgood_time)
        //车型
        val tvfindgood_dun = helper!!.getView<TextView>(R.id.tv_item_findgood_dun)
        val tvfindgoodlength = helper!!.getView<TextView>(R.id.tv_item_findgood_car)
        //名称
        val tvfindgoodcailiao = helper!!.getView<TextView>(R.id.tv_item_findgood_cargo)
        val ivfindgoodphone = helper!!.getView<TextView>(R.id.tv_item_findgood_phone)
        val itfindgoodreadname = helper!!.getView<ImageView>(R.id.iv_item_findgood_readname)
        val tvfindgoodName = helper!!.getView<TextView>(R.id.tv_item_findgood_name)
        tvfindgoodstartaddress.text = item!!.loadAreaCode
        tvfindgoodendaddress.text = item.unloadAreaCode
        val content = StringBuffer()
        if (!StringUtil.isEmpty(item.carLength)) {
            content.append(item.carLength)
            content.append("  ")
        }
        if (!StringUtil.isEmpty(item.carType)) {
            content.append(item.carType)
            content.append("  ")
        }
//        if (!StringUtil.isEmpty(item.weightVolume)) {
//            content.append(item.weightVolume)
//        }
        Utils.showNetImager(simpleDraweeView, item.shipperPortrait)
        tvfindgoodlength.text = content.toString()
        tvfindgood_dun.text = item.weightVolume
        tvaddress.text=item.loadAddress
        tvundaddress.text=item.unloadAddress
        tvfindgoodtime.text = TimeSampUtil.getStringTimeStamp(item.createdTime)
        tvfindgoodName.text = item.shipperName
        tvfindgoodcailiao.text = item.goodsName
        itfindgoodreadname.visibility=if (item.ifRealCertification=="1") View.VISIBLE else View.GONE
        ivfindgoodphone.setOnClickListener {
            if (setOnPhoneClikcItem != null) {
                setOnPhoneClikcItem!!.onPhoneClickItemLisener(position, item.shipperPhone!!);
            }
        }
        helper.itemView.setOnClickListener {
            if (OnClikcItem != null) {
                OnClikcItem!!.onClickItemLisener(position, item.goodsId!!)
            }
        }
    }

}