package com.zzzh.akhalteke.adapter.wailly

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.facebook.drawee.view.SimpleDraweeView
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.bean.DataMessageVo
import com.zzzh.akhalteke.bean.StringInfo
import com.zzzh.akhalteke.bean.vo.OrderInfom
import com.zzzh.akhalteke.utils.StringUtil
import com.zzzh.akhalteke.utils.TimeUtil
import com.zzzh.akhalteke.utils.Utils
import org.w3c.dom.Text

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.adapter
 * @Email : yufeilong92@163.com
 * @Time :2019/3/19 16:05
 * @Purpose :我的运输单适配器
 */
class OrderNewAdapter(var context: Context, var status: String, var infoList: MutableList<OrderInfom>) :
        BaseQuickAdapter<OrderInfom, BaseViewHolder>(R.layout.item_order_new, infoList) {

    var OnClikcPlayPhoneItem: OnClickPlayPhoneItemListener? = null

    interface OnClickPlayPhoneItemListener {
        fun onClickPlayPhoneItemLisener(postion: Int, str: String)
    }

    fun setOnClikcPlayPhoneListener(lisenter: OnClickPlayPhoneItemListener) {
        this.OnClikcPlayPhoneItem = lisenter
    }


    var OnAgreementClikcItem: OnAgreementClickItemListener? = null

    interface OnAgreementClickItemListener {
        /**
         * @param postion
         * @param id 订单id
         * @param status 那个模块
         * @param   ifagreement 是否签订协议
         */
        fun onAgreementClickItemLisener(postion: Int, id: String, status: String, ifagreement: Boolean)
    }

    fun setOnAgreementClikcListener(lisenter: OnAgreementClickItemListener) {
        this.OnAgreementClikcItem = lisenter
    }


    override fun convert(helper: BaseViewHolder?, item: OrderInfom?) {
        val position = helper!!.layoutPosition
        //订单状态
        val tv_status = helper.getView<TextView>(R.id.tv_item_new_order_xieyi)
        //订单时间
        val tv_time = helper.getView<TextView>(R.id.tv_item_new_order_time)
        //开始地址
        val tv_start_address = helper.getView<TextView>(R.id.tv_item_new_order_city)
        //开始地址详情
        val tv_start_infomaddress = helper.getView<TextView>(R.id.tv_item_new_order_address)
        //结束地址
        val tv_end_address = helper.getView<TextView>(R.id.tv_item_new_order_loadcity)
        //结束地址详情
        val tv_end_infomaddress = helper.getView<TextView>(R.id.tv_item_new_order_loadAddress)
        //头像
        val iv_order_hear = helper.getView<SimpleDraweeView>(R.id.sdv_item_new_order_hear)
        //姓名
        val iv_order_name = helper.getView<TextView>(R.id.tv_item_new_order_name)
        //状态
        val btn_order_paly_status = helper.getView<Button>(R.id.btn_item_new_order_paly_status)
//        val view_line = helper.getView<View>(R.id.view_line)
//        tv_start_address.text = Utils.showAddress(mContext, item.loadAreaName, item.unloadAreaName)
        tv_start_address.text= item!!.loadAreaName
        tv_start_infomaddress.text=item.loadAddress
        tv_end_address.text=item.unloadAreaName
        tv_end_infomaddress.text=item.unloadAddress
        statusEvent(context, tv_status, btn_order_paly_status, item!!.ifAgreement!!, status)
        tv_time.text = TimeUtil.getInstance()!!.getYMDWithOuthmsTime(item!!.createdTime!!.toLong())

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
            content.append("  ")
        }
        if (!StringUtil.isEmpty(item.cost)) {
            content.append("¥")
            content.append(item.cost)
            content.append("/每台")
        }
//        tv_order_infom.text = content.toString()
        iv_order_name.text = item.shipperName
//        tv_order_paizhao.text = item.corporateName
        Utils.showNetImager(iv_order_hear, item.shipperPortrait)

//        iv_order_phone.setOnClickListener {
//            if (OnClikcPlayPhoneItem != null) {
//                OnClikcPlayPhoneItem!!.onClickPlayPhoneItemLisener(position, item.shipperPhone!!)
//            }
//        }

        btn_order_paly_status.setOnClickListener {
            if (OnAgreementClikcItem != null) {
                OnAgreementClikcItem!!.onAgreementClickItemLisener(position, item.orderId!!,
                        status, item.ifAgreement == "3" || item.ifAgreement == "4")
            }
        }


    }

    /**
     * 按钮状态显示隐藏、
     * 	协议状态1待确认，2未发起，3同意，4拒绝
     */
    fun agreementShowStatus(btn: Button,status: String) {
        when (status) {
            "1", "3", "4" -> {
                btn.visibility = View.VISIBLE
            }
            "2" -> {
                btn.visibility = View.GONE
            }
        }
    }

    /**
     * @param tv   状态
     * @param btn  签订协议
     * @param view//分割线
     * @param str//不同界面 运输，完成 取消
     * @param status// 运输，协议按钮状态
     */
    fun statusEvent(m: Context, tv: TextView, btn: Button,  ifagreemnet: String,
                    str: String) {
        when (str) {
            DataMessageVo.ORDER_STATUS_One -> {//运输中
                tv.text = context.getString(R.string.in_transit)
                btnStuataText(btn, ifagreemnet)
                btn.setBackgroundResource(R.drawable.gm_bg_bule_radiu)
                btn.setTextColor(m.resources.getColor(R.color.white))
                btn.visibility = View.VISIBLE
                agreementShowStatus(btn, ifagreemnet)
            }
            DataMessageVo.ORDER_STATUS_tWO -> {//已完成
                tv.text = context.getString(R.string.after_completa)
                btn.visibility = View.GONE
            }
            DataMessageVo.ORDER_STATUS_Three -> {//已取消
                tv.text = context.getString(R.string.after_cancle)
                btn.visibility = View.GONE

            }
        }
    }

    /**
     * 按钮文字显示
     */
    fun btnStuataText(btn: Button, status: String) {
        when (status) {
            "1", "2" -> {
                btn.text = "签订协议"
            }
            "3", "4" -> {
                btn.text = "查看协议"
            }
        }
    }
}