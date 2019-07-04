package com.zzzh.akhalteke.adapter

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.bean.vo.MyMoneyTiXianData
import com.zzzh.akhalteke.utils.TimeUtil

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.adapter
 * @Email : yufeilong92@163.com
 * @Time :2019/3/22 17:30
 * @Purpose :我的钱包
 */
class MyMoneyAdapter(var context: Context, var infoList: MutableList<MyMoneyTiXianData>) :
        BaseQuickAdapter<MyMoneyTiXianData, BaseViewHolder>(R.layout.item_my_money, infoList) {
    private  var mShowStatus="-"
    override fun convert(helper: BaseViewHolder?, item: MyMoneyTiXianData?) {
        val tvTime = helper!!.getView<TextView>(R.id.tv_item_mymoney_time)
        val tvMoney = helper!!.getView<TextView>(R.id.tv_item_mymoney_money)
        val tvType = helper!!.getView<TextView>(R.id.tv_item_mymoney_topup)
        val tv_stutas = helper!!.getView<TextView>(R.id.tv_my_money_status)
        val iv_stutas = helper!!.getView<ImageView>(R.id.iv_money_status)
        tvMoney.text = item!!.sum
        tvTime.text = TimeUtil.getInstance()!!.getYMDTime(item.createdTime.toLong())
        tvType.text=setMoneyStatus(iv_stutas,item.eventType)
        tv_stutas.text=mShowStatus
    }

    fun setMoneyStatus(iv:ImageView,string: String): String {
        val a = when (string) {
            "1" -> {
                mShowStatus="-"
                iv.setImageResource(R.mipmap.ic_money_pay)
                "冻结"
            }
            "2" -> {
                mShowStatus="+"
                iv.setImageResource(R.mipmap.ic_money_add)
                "解冻"
            }
            "3" -> {
                mShowStatus=""
                iv.setImageResource(R.mipmap.ic_money_pay)
                "抵消"
            }
            "4" -> {
                mShowStatus="+"
                iv.setImageResource(R.mipmap.ic_money_add)
                "收入"
            }
            "5" -> {
                mShowStatus="-"
                iv.setImageResource(R.mipmap.ic_money_pay)
                "提现"
            }
            else -> {
                mShowStatus="-"
                iv.setImageResource(R.mipmap.ic_money_add)
                ""
            }
        }
        return a
    }
}