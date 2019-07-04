package com.zzzh.akhalteke.ui.waybill

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.yanzhenjie.permission.Permission
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.bean.DataMessageVo
import com.zzzh.akhalteke.bean.vo.OrderAgreementVo
import com.zzzh.akhalteke.mvp.contract.AgreementContract
import com.zzzh.akhalteke.mvp.model.AgreementModel
import com.zzzh.akhalteke.mvp.presenter.AgreementPresenter
import com.zzzh.akhalteke.ui.BaseActivity
import com.zzzh.akhalteke.utils.*
import kotlinx.android.synthetic.main.gm_order_new_infom.*
import kotlinx.android.synthetic.main.gm_rl_title_title.*

/**
 * @Author : YFL  is Creating a porject in com.zzzh.akhalteke.ui.waybill
 * @Package com.zzzh.akhalteke.ui.waybill
 * @Email : yufeilong92@163.com
 * @Time :2019/6/4 13:55
 * @Purpose :已完成，已取消
 */
class OrderInfomNewNoActivity : BaseActivity(), AgreementContract.View {

    companion object {
        val ORDER_ID = "orderid"
        val TYPE = "type"
        val TYPE_ONE = "ONE"
        val TYPE_TWO = "two"
        val TYPE_THREE = "three"
    }

    private var mOrderId: String? = null
    private var mType: String? = null
    private var mPresenter: AgreementPresenter? = null
    private var mUpdata: Boolean = true//上传汇单
    private var mShowLook: Boolean = true//是否上传回单
    override fun setContentView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_order_infom_new_no)
        if (intent != null) {
            mOrderId = intent.getStringExtra(ORDER_ID)
            mType = intent.getStringExtra(TYPE)
        }
        tv_gm_right_title.text = getString(R.string.contact_service)
        initRequest()
        initView()
        initEvent()
    }
    fun initView(){
        if (mType==TYPE_THREE){
            rl_order_detail_freight.visibility= View.GONE
            rl_order_detail_receipt.visibility= View.GONE
//            line_view.visibility= View.GONE
        }else{
            rl_order_detail_freight.visibility= View.VISIBLE
            rl_order_detail_receipt.visibility= View.VISIBLE
//            line_view.visibility= View.VISIBLE
        }
    }

    fun initEvent() {

        tv_gm_right_title.setOnClickListener {
            Utils.playPhone(mContext, DataMessageVo.KEFU_PHONE)
        }
        //回单
        rl_order_detail_receipt.setOnClickListener {
            if (!mUpdata) {
                showToast(getString(R.string.no_agreement))
                return@setOnClickListener
            }
            if (!mShowLook) {
//                showToast("您未上传回单")
                PermissionUtils.showPermission(mContext, "",
                        arrayOf(Permission.WRITE_EXTERNAL_STORAGE,
                                Permission.ACCESS_COARSE_LOCATION,
                                Permission.READ_EXTERNAL_STORAGE)) {
                    val bundle = Bundle()
                    bundle.putString(UpdataReciptActvity.ORDER_ID, mOrderId)
                    bundle.putString(UpdataReciptActvity.TYPE, UpdataReciptActvity.NO_TYPE)
                    jumpTo(UpdataReciptActvity::class.java, bundle, getString(R.string.update_receipt))
                }
                return@setOnClickListener
            }
            PermissionUtils.showPermission(mContext, "",
                    arrayOf(Permission.WRITE_EXTERNAL_STORAGE,
                            Permission.ACCESS_COARSE_LOCATION,
                            Permission.READ_EXTERNAL_STORAGE)) {
                val bundle = Bundle()
                bundle.putString(UpdataReciptActvity.ORDER_ID, mOrderId)
                bundle.putString(UpdataReciptActvity.TYPE, UpdataReciptActvity.LOOK_TYPE)
                jumpTo(UpdataReciptActvity::class.java, bundle, getString(R.string.look_receipt))
            }

        }
    }

    fun initRequest() {
        mPresenter = AgreementPresenter()
        mPresenter!!.initMvp(this, model = AgreementModel())
        showProgress()
        mPresenter!!.requestOrderInfo(mContext, mOrderId!!)
    }

    override fun InfoSuccess(t: Any?) {
        if (t == null) {
            nsv_root.visibility = View.GONE
            return
        } else {
            nsv_root.visibility = View.VISIBLE
        }
        val data: OrderAgreementVo = t as OrderAgreementVo
        bindViewData(data)

    }

    fun bindViewData(data: OrderAgreementVo) {
        val timeUtil = TimeUtil.getInstance()!!
        tv_order_detail_city.text = data.loadAreaName
        TextUtil.setViewShow(tv_order_detail_city_address,data.loadAddress)
        if (data.loadTime != null && data.loadTime != ""&&data.loadTime!="0") {
            tv_order_detail_time.text = timeUtil.getMDhmTime(data.loadTime.toLong())
        } else {
            tv_order_detail_time.text = "无"
            ll_order_detail_loadd.visibility = View.GONE
        }
        tv_order_detail_uncity.text = data.unloadAreaName
        TextUtil.setViewShow(tv_order_detail_uncity_unaddress,data.unloadAddress)
        if (data.unloadTime != null && data.unloadTime != ""&&data.loadTime!="0") {
            tv_order_detail_untime.text = timeUtil.getMDhmTime(data.unloadTime.toLong())
        } else {
            tv_order_detail_untime.text = "无"
            ll_order_detail_unload.visibility = View.GONE
        }
        val mLists = mutableListOf<String>()

        if (!ToolUtils.isEmpty(data.name)) {
            mLists.add(data.name!!)
        }
        if (!ToolUtils.isEmpty(data.weightVolume)) {
            mLists.add(data.weightVolume!!)
        }
        addDetailTag(mLists)
        if (!StringUtil.isEmpty(data.cost)) {
            tv_order_detail_money.visibility = View.VISIBLE
        } else {
            tv_order_detail_money.visibility = View.GONE
        }
        //费用
        tv_order_detail_money.text="￥${data.cost}"
        //姓名
        tv_order_detail_name.text = data.shipperName
        //公司
        tv_order_detail_plate.text = data.corporateName
        //电话
        iv_order_detail_phone.setOnClickListener {
            Utils.playPhone(mContext, data.shipperPhone!!)
        }
        //协议
        rl_order_detail_freight.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(FreightNewAgreementActivity.ORDER_ID, mOrderId)
            when (data.ifAgreement) {
                "1" -> {
                    jumpTo(FreightNewAgreementActivity::class.java, bundle, getString(R.string.yunshugood_agreement))
                }
                "2" -> {
                    showToast(getString(R.string.shipper_no_sendagreement))
                }
                else -> {
                    jumpTo(CompletaNewAgreementActivity::class.java, bundle, getString(R.string.yunshugood_agreement))
                }
            }

        }
        when (data.ifAgreement) {
            "1" -> {
                tv_order_detail_agreement_status.text = getString(R.string.wait_sure)
                mUpdata = true
            }
            "2" -> {
                tv_order_detail_agreement_status.text = getString(R.string.no_send)
                mUpdata = false
            }
            "3" -> {
                tv_order_detail_agreement_status.text = getString(R.string.agree)
                mUpdata = true
            }
            "4" -> {
                tv_order_detail_agreement_status.text = getString(R.string.after_reject)
                mUpdata = false
            }
            else -> {
                tv_order_detail_agreement_status.text = getString(R.string.no_send)
                mUpdata = false
            }
        }
        when (data.receipt) {
            "1" -> {
//                tv_gm_order_upStatus.text = getString(R.string.after_updata)
                tv_order_detail_receipt_status.text = getString(R.string.look_receipt)
                mShowLook = true
            }
            "2" -> {
//                tv_gm_order_upStatus.text = getString(R.string.no_update)
                tv_order_detail_receipt_status.text = getString(R.string.update_receipt)
                mShowLook = false
            }
            else -> {
//                tv_gm_order_upStatus.text = getString(R.string.no_update)
                tv_order_detail_receipt_status.text = getString(R.string.update_receipt)
                mShowLook = false
            }
        }
        Utils.showNetImager(sdv_order_inofom_shipper_hear, data.shipperPortrait)
        tv_order_detail_orderid.text = data.orderId
        tv_order_detail_ordertime.text = timeUtil.getYMDTime(data.createdTime!!.toLong())
        tv_order_detail_copy.setOnClickListener {
            val trim = tv_order_detail_orderid.text.toString().trim()
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText(null, trim)
            clipboard.primaryClip = clipData
            showToast(getString(R.string.cope_success))
        }

    }
    private fun addDetailTag(datas: MutableList<String>?) {
        flw_order_detail_tag.removeAllViews()
        if (datas == null || datas.isEmpty()) {
            flw_order_detail_tag.visibility = View.GONE
            return
        }
        for (i in datas!!.indices) {
            val key = datas!![i]
            val itemvo =
                    LayoutInflater.from(mContext).inflate(R.layout.item_order_detail_tag, flw_order_detail_tag, false)
            val tv = itemvo.findViewById(R.id.tv_order_detail_tag) as TextView
            tv.setBackgroundResource(ToolUtils.getImageDrawer(mContext))
            tv.text = key
            flw_order_detail_tag.addView(itemvo)
        }
    }

    override fun onRestart() {
        super.onRestart()
        showProgress()
        mPresenter!!.requestOrderInfo(mContext, mOrderId!!)
    }

    override fun InfoError(ex: Throwable) {
        this.onError(ex)
    }

    override fun InfoComplise() {
        this.onComplise()
    }

    override fun SubmitSuccess(t: Any?) {

    }

    override fun SubmitError(ex: Throwable) {
        this.onError(ex)
    }

    override fun SubmitComplise() {
        this.onComplise()
    }
}
