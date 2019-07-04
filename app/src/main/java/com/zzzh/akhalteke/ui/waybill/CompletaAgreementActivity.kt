package com.zzzh.akhalteke.ui.waybill

import android.os.Bundle
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.ui.BaseActivity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.zzzh.akhalteke.WebViewActivity
import com.zzzh.akhalteke.bean.DataMessageVo
import com.zzzh.akhalteke.bean.vo.LookAgreementVo
import com.zzzh.akhalteke.dbHelp.UserDbHelp
import com.zzzh.akhalteke.mvp.contract.LookAgreementContract
import com.zzzh.akhalteke.mvp.model.LookAgreementModel
import com.zzzh.akhalteke.mvp.presenter.LookAgreementPresenter
import com.zzzh.akhalteke.utils.StringUtil
import com.zzzh.akhalteke.utils.TimeUtil
import com.zzzh.akhalteke.utils.Utils
import kotlinx.android.synthetic.main.activity_completa.*
import kotlinx.android.synthetic.main.activity_freight_agreement.*
import kotlinx.android.synthetic.main.gm_agreement.*
import kotlinx.android.synthetic.main.gm_order_infom.*


/**
 * @Author : YFL  is Creating a porject in com.zzzh.akhalteke.ui.waybill
 * @Package com.zzzh.akhalteke.ui.waybill
 * @Email : yufeilong92@163.com
 * @Time :2019/3/15 10:28
 * @Purpose :已完成协议
 */
class CompletaAgreementActivity : BaseActivity() , LookAgreementContract.View {
    companion object {
        var ORDER_ID: String = "order_id"
    }

    private var mOrder_Id: String? = null
    private var mPresenter: LookAgreementPresenter? = null
    private var mType:String? = ""
    override fun setContentView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_completa)
        if (intent != null) {
            mOrder_Id = intent.getStringExtra(CompletaAgreementActivity.ORDER_ID)
        }
        initReqeust()
        initEvent()
    }

    fun initEvent() {
        web_view.loadUrl(DataMessageVo.HTTP_HEAR + getString(R.string.agreement_yunshu_html))
    }

    fun initView() {
        val content = getString(R.string.i_see_madao)
        val mSpanck = SpannableString(content)
        mSpanck.setSpan(Spank(mContext), 7, 15, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        mSpanck.setSpan(SpankOne(mContext), 16, content.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        tv_freight_agreement.text = mSpanck
        tv_freight_agreement.movementMethod = LinkMovementMethod.getInstance()
    }

    class Spank(var context: Context) : ClickableSpan() {
        override fun onClick(widget: View) {
            val bundle=Bundle()
            bundle.putString(WebViewActivity.TYPE,WebViewActivity.NO_HEAR_HTTP_TYPE)
            bundle.putString(BaseActivity.CNT_PARAMETE_TITLE,"用户服务协议")
            bundle.putString(WebViewActivity.URL,context.getString(R.string.agreement_server_html))
            val intent=Intent(context,WebViewActivity::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.color = context.resources.getColor(R.color.gm_color)
            ds.isUnderlineText = false
            ds.clearShadowLayer()
        }

    }


    class SpankOne(var context: Context) : ClickableSpan() {
        override fun onClick(widget: View) {
            val bundle=Bundle()
            bundle.putString(WebViewActivity.TYPE,WebViewActivity.NO_HEAR_HTTP_TYPE)
            bundle.putString(BaseActivity.CNT_PARAMETE_TITLE,"货物运输协议")
            bundle.putString(WebViewActivity.URL,context.getString(R.string.agreement_yunshu_html))
            val intent=Intent(context,WebViewActivity::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.color = context.resources.getColor(R.color.gm_color)
            ds.isUnderlineText = false
            ds.clearShadowLayer()
        }

    }

    fun initReqeust() {
        mPresenter = LookAgreementPresenter()
        mPresenter!!.initMvp(this, model = LookAgreementModel())
        showProgress()
        mPresenter!!.requestLookAgreement(mContext, mOrder_Id!!)
    }

    override fun LookAgreementSuccess(t: Any?) {
        if (t==null){
            scrollView_completa_root_view.visibility=View.GONE
            return
        }else{
            scrollView_completa_root_view.visibility=View.VISIBLE
        }
        val data: LookAgreementVo = t as LookAgreementVo
        bindViewData(data)
    }

    fun bindViewData(data: LookAgreementVo) {
        val timeUtil = TimeUtil.getInstance()
        tv_agree_ordernumber.text = data.orderId
        tv_lookagreement_start_address.text =  Utils.showAddress(mContext,data.loadAddress,data.unloadAddress)
        tv_agreement_volume_weight.text = data.weightVolume
        tv_agreement_car_type.text = data.carTypeLength
        iv_agreement_pay_moenry.text = data.companyAmount
        tv_agreement_day_number.text = data.payTime
        tv_agree_good_type.text = data.goodsName
        var loadtime: String = "无"
        if (!StringUtil.isEmpty(data.loadTime)) {
            val outms = timeUtil!!.getYMDTimeWithOutms(data.loadTime!!.toLong())
            loadtime = outms + "前"
        }
        tv_agree_good_time.text = loadtime
        var unloadtime: String = "无"
        if (!StringUtil.isEmpty(data.loadTime)) {
            val outms = timeUtil!!.getYMDTimeWithOutms(data.unloadTime!!.toLong())
            unloadtime = outms + "前"
        }
        tv_agree_good_end_time.text = unloadtime
        tv_agreement_end_adree.text = data.loadAddress
        tv_agreement_end_address.text = data.unloadAddress
        tv_agreement_other_appoint.text = data.appointment
        showImageStatus(iv_agreement_status, data.status!!)
        val vo = UserDbHelp.get_Instance(mContext)!!.getUserInfom()
        setUserInfom(data.shipperName, data.shipperPhone, data.shipperAddress, data, timeUtil,
                data.shipperPortrait, true)
        ll_agreement_consigner.setOnClickListener {
            //承运司机
            showShipperOrDriver(true)
            setUserInfom(data.shipperName, data.shipperPhone, data.shipperAddress,
                    data, timeUtil, data.shipperPortrait, true)
        }
        ll_agreement_carriage_driver.setOnClickListener {
            //发货人
            showShipperOrDriver(false)
            setUserInfom(
                    data.ownerName, data.ownerPhone,
                    vo!!.plateNumber, data, timeUtil, data.driverPortrait, false
            )
        }
    }

    fun showShipperOrDriver(show: Boolean) {
        if (show) {
            tv_agreemnet_send_shipper.setTextColor(mContext.resources.getColor(R.color.gm_color))
            tv_agreemnet_send_drivier.setTextColor(mContext.resources.getColor(R.color.text_title_color))
            tv_agreemnet_send_view_drivier.setBackgroundColor(mContext.resources.getColor(R.color.gm_bg_color))
            tv_agreemnet_send_view_shipper.setBackgroundColor(mContext.resources.getColor(R.color.gm_color))
        } else {
            tv_agreemnet_send_shipper.setTextColor(mContext.resources.getColor(R.color.text_title_color))
            tv_agreemnet_send_drivier.setTextColor(mContext.resources.getColor(R.color.gm_color))
            tv_agreemnet_send_view_drivier.setBackgroundColor(mContext.resources.getColor(R.color.gm_color))
            tv_agreemnet_send_view_shipper.setBackgroundColor(mContext.resources.getColor(R.color.gm_bg_color))
        }

    }

    /**
     * @param name 姓名
     * @param phone 电话
     * @param addree 地址
     *
     */
    private fun setUserInfom(
            name: String?, phone: String?, addree: String?, data: LookAgreementVo?,
            timeUtil: TimeUtil?, hear: String?, isShipper: Boolean
    ) {
        tv_agreement_name.text = name
        tv_agreement_phone.text = phone
        tv_agreement_addree.text = addree
        showTitle(tv_agreement_time_title, data!!.status!!)
        showTime(tv_agreement_time, data.status!!, timeUtil!!, data)
        showAddressTitle(tv_agreement_isdriver, isShipper)
        Utils.showNetImager(iv_freight_agreement_hear, hear)
    }


    fun showTitle(tv: TextView, status: String) {
        when (status) {
            "2" -> {
                tv.text = getString(R.string.send_start_time)
            }
            else -> {
                tv.text = getString(R.string.send_sure_time)
            }
        }
    }

    fun showAddressTitle(tv: TextView, status: Boolean) {
        if (status) {
            tv.text = getString(R.string.company_address)
        } else {
            tv.text = getString(R.string.plate_number)
        }
    }

    fun showTime(tv: TextView, status: String, timeUtil: TimeUtil, data: LookAgreementVo) {
        when (status) {
            "2" -> {
                var loadtime: String = ""
                if (!StringUtil.isEmpty(data.launchTime)) {
                    loadtime = timeUtil.getYMDTime(data.launchTime!!.toLong())
                }
                tv.text = loadtime
            }
            else -> {
                var loadtime: String = ""
                if (!StringUtil.isEmpty(data.confirmTime)) {
                    loadtime = timeUtil.getYMDTime(data.confirmTime!!.toLong())
                }
                tv.text = loadtime
            }
        }
    }

    fun showImageStatus(imgage: ImageView, status: String) {
        when (status) {
            "1" -> {//成功
                imgage.setImageResource(R.mipmap.ic_agreement_success)
            }
            "2" -> {//等待
                imgage.setImageResource(R.mipmap.ic_agreement_wait)
            }
            "3" -> {//失败
                imgage.setImageResource(R.mipmap.ic_agreement_reject)
            }
            else -> {//等待
                imgage.setImageResource(R.mipmap.ic_agreement_wait)
            }
        }
    }

    override fun LookAgreementError(ex: Throwable) {
        this.onError(ex)
    }

    override fun LookAgreementComplise() {
        this.onComplise()
    }

    override fun SubmitAgreementSucces(t: Any?) {
    }

    override fun SubmitAgreementError(ex: Throwable) {
    }

    override fun SubmitAgreementComplise() {
    }

}
