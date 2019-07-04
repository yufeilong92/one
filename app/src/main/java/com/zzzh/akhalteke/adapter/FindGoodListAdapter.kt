package com.zzzh.akhalteke.adapter

import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
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
import com.zzzh.akhalteke.view.customview.CenterAlignImageSpan

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.adapter
 * @Email : yufeilong92@163.com
 * @Time :2019/3/16 10:34
 * @Purpose :找货适配器
 */
class FindGoodListAdapter(var context: Context, var infoList: MutableList<GoodItem>) :
        BaseQuickAdapter<GoodItem, BaseViewHolder>(R.layout.item_find_good, infoList) {
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
        val simpleDraweeView = helper!!.getView<SimpleDraweeView>(R.id.img_show_hear)
        val tvfindgoodstartaddress = helper.getView<TextView>(R.id.tv_findGood_start_address)
        val tvfindgoodtime = helper!!.getView<TextView>(R.id.tv_findGood_time)
        val tvfindgoodlength = helper!!.getView<TextView>(R.id.tv_find_good_length)
        val tvfindgoodcailiao = helper!!.getView<TextView>(R.id.tv_findGood_cailiao)
        val tvfindgoodzhtime = helper!!.getView<TextView>(R.id.tv_find_good_zhtime)
        val ivfindgoodphone = helper!!.getView<ImageView>(R.id.iv_findgood_phone)
        val tvfindgoodplaytype = helper!!.getView<TextView>(R.id.tv_findGood_play_type)
        val tvfindgoodName = helper!!.getView<TextView>(R.id.tv_findGood_name)
        tvfindgoodstartaddress.text = Utils.showAddress(context, item!!.loadAreaCode, item.unloadAreaCode)
        val content = StringBuffer()
        if (!StringUtil.isEmpty(item.carLength)) {
            content.append(item.carLength)
            content.append("  ")
        }
        if (!StringUtil.isEmpty(item.carType)) {
            content.append(item.carType)
            content.append("  ")
        }
        if (!StringUtil.isEmpty(item.weightVolume)) {
            content.append(item.weightVolume)
        }
        Utils.showNetImager(simpleDraweeView, item.shipperPortrait)
        tvfindgoodlength.text = content.toString()
        tvfindgoodtime.text = TimeSampUtil.getStringTimeStamp(item.createdTime)
        tvfindgoodzhtime.text = item.loadTime
        tvfindgoodplaytype.text = item.payType
        tvfindgoodName.text = item.shipperName
        tvfindgoodcailiao.text = item.goodsName
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