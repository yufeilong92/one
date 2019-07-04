package com.zzzh.akhalteke.adapter

import android.content.Context
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.bean.StringInfo
import com.zzzh.akhalteke.bean.vo.BankListVo
import com.zzzh.akhalteke.utils.BankLogUtil
import com.zzzh.akhalteke.utils.BankStatus

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.adapter
 * @Email : yufeilong92@163.com
 * @Time :2019/3/21 9:53
 * @Purpose :银行卡列表
 */
class BankListAdpater(var context: Context, var infoList: MutableList<BankListVo>) :
    BaseQuickAdapter<BankListVo, BaseViewHolder>(R.layout.item_bank_list, infoList) {
    override fun convert(helper: BaseViewHolder?, item: BankListVo?) {
        val position = helper!!.layoutPosition
        val llRootView = helper.getView<LinearLayout>(R.id.li_item_bank_bg)
        val iv_item_bank_samllimag = helper.getView<ImageView>(R.id.iv_item_bank_samll_img)
        val tv_item_bankcard_hangtype = helper.getView<TextView>(R.id.tv_item_bankcard_hangtype)
        val tv_item_bankcard_romver = helper.getView<TextView>(R.id.tv_item_bankcard_romver)
        val tv_item_bankcard_type = helper.getView<TextView>(R.id.tv_item_bankcard_cardtype)
        val tv_item_bankcard_number = helper.getView<TextView>(R.id.tv_item_bankcard_number)
        tv_item_bankcard_romver.setOnClickListener {
            if (OnClikcDeleteBankItem != null) {
                OnClikcDeleteBankItem!!.onClickDeleteBankItemLisener(
                    position,
                    item!!.id!!, item.cardNumber!!
                )
            }
        }
        setCardBg(mContext, llRootView, iv_item_bank_samllimag, tv_item_bankcard_hangtype, item!!.bank!!)
        val mStr = StringBuffer()
        mStr.append("★★★★ ★★★★ ★★★★ ")
        mStr.append(item.cardNumber)
        tv_item_bankcard_number.text = mStr.toString()


    }

    fun addempty(str: String): String {
        val i = str.length % 4
        if (i == 0) {
            return str
        }
        var stringbuffer = StringBuffer()
        for (id in 0 until i) {
            val content = subString(str, id)
            stringbuffer.append(content)
            stringbuffer.append(" ")
        }
        return stringbuffer.toString()

    }

    fun subString(str: String, number: Int): String {
        val mgs = str.substring(number, number + 4)
        return mgs
    }

    var OnClikcDeleteBankItem: OnClickDeleteBankItemListener? = null

    interface OnClickDeleteBankItemListener {
        fun onClickDeleteBankItemLisener(postion: Int, id: String, str: String)
    }

    fun setOnClikcDeleteBankListener(lisenter: OnClickDeleteBankItemListener) {
        this.OnClikcDeleteBankItem = lisenter
    }

    fun setCardBg(context: Context, linearLayout: LinearLayout, imageView: ImageView, tv: TextView, status: String) {
        val util = BankLogUtil.getInstance(context)
        when (status) {
            "0105" -> {//建设
//                linearLayout.background = context.resources.getDrawable(R.mipmap.jiansheyinhang)
//                imageView.setImageResource(R.mipmap.jianshetubiao)
//                tv.text = "建设银行"
                util!!.selecBankLogo(BankStatus.JIANSHE)

            }
            "0102" -> {//工商银行
//                linearLayout.background = context.resources.getDrawable(R.mipmap.gongshangyinhang)
//                imageView.setImageResource(R.mipmap.gongshangtubiao)
//                tv.text = "工商银行"
                util!!.selecBankLogo(BankStatus.GONGSHANG)

            }
            "0100" -> {//邮政银行
//                linearLayout.background = context.resources.getDrawable(R.mipmap.youzhengyinhan)
//                imageView.setImageResource(R.mipmap.youzhengtubiao)
//                tv.text = "邮政银行"
                util!!.selecBankLogo(BankStatus.YOUZHENG)
            }
            "0303" -> {//光大银行
//                linearLayout.background = context.resources.getDrawable(R.mipmap.guangdayinhang)
//                imageView.setImageResource(R.mipmap.guangdatubiao)
//                tv.text = "光大银行"
                util!!.selecBankLogo(BankStatus.GUANGDA)
            }
            "0309" -> {//兴业银行
//                linearLayout.background = context.resources.getDrawable(R.mipmap.xingyeyinhang)
//                imageView.setImageResource(R.mipmap.xingyetubiao)
//                tv.text = "兴业银行"
                util!!.selecBankLogo(BankStatus.XINGYE)

            }
            "0104" -> {//中国银行
//                linearLayout.background = context.resources.getDrawable(R.mipmap.zhongguoyinhang)
//                imageView.setImageResource(R.mipmap.zhongguotubiao)
//                tv.text = "中国银行"
                util!!.selecBankLogo(BankStatus.ZHOGNGUO)
            }
            "0308" -> {//招商银行
//                linearLayout.background = context.resources.getDrawable(R.mipmap.zhaoshangyinhang)
//                imageView.setImageResource(R.mipmap.zhaoshangtubiao)
//                tv.text = "招商银行"
                util!!.selecBankLogo(BankStatus.ZHAOSHANG)
            }
            "0103" -> {//农业银行
//                linearLayout.background = context.resources.getDrawable(R.mipmap.nongyeyinhang)
//                imageView.setImageResource(R.mipmap.nongyeyinhangtuboao)
//                tv.text = "农业银行"
                util!!.selecBankLogo(BankStatus.NOGNYE)
            }
            "0306" -> {//,广发银行
//                linearLayout.background = context.resources.getDrawable(R.mipmap.guangfayinhang)
//                imageView.setImageResource(R.mipmap.guangfayinhangtubio)
//                tv.text = "广发银行"
                util!!.selecBankLogo(BankStatus.GUANGFA)
            }
            "0307" -> {//平安银行
//                linearLayout.background = context.resources.getDrawable(R.mipmap.pinganyinhang)
//                imageView.setImageResource(R.mipmap.pinganyinhangtubiao)
//                tv.text = "平安银行"
                util!!.selecBankLogo(BankStatus.PINGAN)
            }
            "0301" -> {//交通银行
//                linearLayout.background = context.resources.getDrawable(R.mipmap.jiaotongyinhang)
//                imageView.setImageResource(R.mipmap.jiaotongyinhangtubio)
//                tv.text = "交通银行"
                util!!.selecBankLogo(BankStatus.JIAOTONG)
            }
            "0302" -> {//中信银行
//                linearLayout.background = context.resources.getDrawable(R.mipmap.zhongxinyinhang)
//                imageView.setImageResource(R.mipmap.zhongxinyinhangtubio)
//                tv.text = "中信银行"
                util!!.selecBankLogo(BankStatus.ZHONGXIN)
            }
            "0304" -> {//华夏银行
//                linearLayout.background = context.resources.getDrawable(R.mipmap.huaxiayinhang)
//                imageView.setImageResource(R.mipmap.huaxiayinhangtubio)
//                tv.text = "华夏银行"
                util!!.selecBankLogo(BankStatus.HUAXIA)
            }
            "0305" -> {//民生银行
//                linearLayout.background = context.resources.getDrawable(R.mipmap.minshengyinhang)
//                imageView.setImageResource(R.mipmap.mingshengyinhangtubiao)
//                tv.text = "民生银行"
                util!!.selecBankLogo(BankStatus.MINSHENG)
            }
        }

        tv.text = util!!.title
        imageView.setImageResource(util.smallLogo!!)
        linearLayout.background = util.BgLogo
    }
}