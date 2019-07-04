package com.zzzh.akhalteke.adapter

import android.content.Context
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.bean.StringInfo
import com.zzzh.akhalteke.bean.vo.TiXianItemVo
import com.zzzh.akhalteke.dbHelp.UserDbHelp
import com.zzzh.akhalteke.net.UserNet
import com.zzzh.akhalteke.utils.BankLogUtil
import com.zzzh.akhalteke.utils.BankStatus
import com.zzzh.akhalteke.utils.TimeUtil

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.adapter
 * @Email : yufeilong92@163.com
 * @Time :2019/3/22 18:16
 * @Purpose :提现记录适配器
 */
class TiMoneyListAdapter(var context: Context, var infoList: MutableList<TiXianItemVo>) :
        BaseQuickAdapter<TiXianItemVo, BaseViewHolder>(R.layout.item_timoney_log_list, infoList) {
    override fun convert(helper: BaseViewHolder?, item: TiXianItemVo?) {
        val iv = helper!!.getView<ImageView>(R.id.iv_item_timoney_list)
        val tvCarType = helper!!.getView<TextView>(R.id.tv_item_timoney_hang_type)
        val tvTimoneyNumber = helper!!.getView<TextView>(R.id.tv_item_timoney_number)
        val tvTiMoneyStatus = helper!!.getView<TextView>(R.id.tv_item_timoney_status)
        val tvTiMoneyNumber = helper!!.getView<TextView>(R.id.tv_item_timoney_to_number)
        val tvTiMoneyName = helper!!.getView<TextView>(R.id.tv_item_timoeny_to_name)
        val tvTiMoneyCreateName = helper!!.getView<TextView>(R.id.tv_item_timoney_create_time)
        val bankLogUtil = BankLogUtil.getInstance(mContext)
        setSelectBank(item!!.bank!!, bankLogUtil!!)
        iv.setImageResource(bankLogUtil.smallLogo!!)
        tvCarType.text=bankLogUtil.title!!
        setTvSatus(mContext,tvTiMoneyStatus,item.status!!)
        tvTimoneyNumber.text=item.sum
        tvTiMoneyNumber.text=item.bankNumber
        val dbHelp = UserDbHelp.get_Instance(mContext)
        val userInfom = dbHelp!!.getUserInfom()
        tvTiMoneyName.text=userInfom!!.name

        val timeUtil = TimeUtil.getInstance()
        val ymdTime = timeUtil!!.getYMDTime(item!!.createdTime!!.toLong())
        tvTiMoneyCreateName.text = ymdTime


    }

    fun setTvSatus(context: Context, tv: TextView, status: String) {
        when (status) {
            "1" -> {
                tv.setTextColor(context.resources.getColor(R.color.tv_tixian_success_bg))
                tv.setBackgroundResource(R.drawable.bg_tixian_success)
            }
            "2" -> {
                tv.setTextColor(context.resources.getColor(R.color.tv_tixian_check_bg))
                tv.setBackgroundResource(R.drawable.bg_tixian_wait)
            }
            "3" -> {
                tv.setTextColor(context.resources.getColor(R.color.tv_tixian_failure_bg))
                tv.setBackgroundResource(R.drawable.bg_tixian_defeated)
            }
        }
    }

    fun setSelectBank(status: String, util: BankLogUtil) {

        when (status) {
            "0105" -> {//建设
                util!!.selecBankLogo(BankStatus.JIANSHE)

            }
            "0102" -> {//工商银行
                util!!.selecBankLogo(BankStatus.GONGSHANG)

            }
            "0100" -> {//邮政银行
                util!!.selecBankLogo(BankStatus.YOUZHENG)
            }
            "0303" -> {//光大银行
                util!!.selecBankLogo(BankStatus.GUANGDA)
            }
            "0309" -> {//兴业银行
                util!!.selecBankLogo(BankStatus.XINGYE)

            }
            "0104" -> {//中国银行
                util!!.selecBankLogo(BankStatus.ZHOGNGUO)
            }
            "0308" -> {//招商银行
                util!!.selecBankLogo(BankStatus.ZHAOSHANG)
            }
            "0103" -> {//农业银行
                util!!.selecBankLogo(BankStatus.NOGNYE)
            }
            "0306" -> {//,广发银行
                util!!.selecBankLogo(BankStatus.GUANGFA)
            }
            "0307" -> {//平安银行
                util!!.selecBankLogo(BankStatus.PINGAN)
            }
            "0301" -> {//交通银行
                util!!.selecBankLogo(BankStatus.JIAOTONG)
            }
            "0302" -> {//中信银行
                util!!.selecBankLogo(BankStatus.ZHONGXIN)
            }
            "0304" -> {//华夏银行
                util!!.selecBankLogo(BankStatus.HUAXIA)
            }
            "0305" -> {//民生银行
                util!!.selecBankLogo(BankStatus.MINSHENG)
            }
        }
    }
}