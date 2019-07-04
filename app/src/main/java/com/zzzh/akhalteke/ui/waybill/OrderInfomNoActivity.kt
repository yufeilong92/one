package com.zzzh.akhalteke.ui.waybill

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import com.yanzhenjie.permission.Permission
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.bean.DataMessageVo
import com.zzzh.akhalteke.bean.vo.OrderAgreementVo
import com.zzzh.akhalteke.mvp.contract.AgreementContract
import com.zzzh.akhalteke.mvp.model.AgreementModel
import com.zzzh.akhalteke.mvp.presenter.AgreementPresenter
import com.zzzh.akhalteke.ui.BaseActivity
import com.zzzh.akhalteke.utils.PermissionUtils
import com.zzzh.akhalteke.utils.StringUtil
import com.zzzh.akhalteke.utils.TimeUtil
import com.zzzh.akhalteke.utils.Utils
import kotlinx.android.synthetic.main.gm_order_infom.*
import kotlinx.android.synthetic.main.gm_rl_title_title.*

/**
 * @Author : YFL  is Creating a porject in com.zzzh.akhalteke.ui.waybill
 * @Package com.zzzh.akhalteke.ui.waybill
 * @Email : yufeilong92@163.com
 * @Time :2019/3/30 14:24
 * @Purpose :订单协议未发起签订
 */
class OrderInfomNoActivity : BaseActivity(), AgreementContract.View {
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
        setContentView(R.layout.activity_order_infom_no)
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
        if (mType==OrderInfomNoActivity.TYPE_THREE){
            li_gm_order_yunshu_agressment.visibility=View.GONE
            ll_gm_order_receipt.visibility=View.GONE
            line_view.visibility=View.GONE
        }else{
            li_gm_order_yunshu_agressment.visibility=View.VISIBLE
            ll_gm_order_receipt.visibility=View.VISIBLE
            line_view.visibility=View.VISIBLE
        }
    }

    fun initEvent() {

        tv_gm_right_title.setOnClickListener {
            Utils.playPhone(mContext, DataMessageVo.KEFU_PHONE)
        }
        ll_gm_order_receipt.setOnClickListener {
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
                jumpTo(UpdataReciptActvity::class.java, bundle, getString(R.string.update_receipt))
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
            scrollView.visibility = View.GONE
            return
        } else {
            scrollView.visibility = View.VISIBLE
        }
        val data: OrderAgreementVo = t as OrderAgreementVo
        bindViewData(data)

    }

    fun bindViewData(data: OrderAgreementVo) {
        val timeUtil = TimeUtil.getInstance()!!
        tv_gm_order_start_address.text = data.loadAreaName
        if (data.loadTime != null && data.loadTime != ""&&data.loadTime!="0") {
            tv_gm_order_time.text = timeUtil.getMDhmTime(data.loadTime.toLong())
        } else {
            tv_gm_order_time.text = "无"
            ll_gm_load_time.visibility = View.GONE
        }
        tv_gm_order_end_address.text = data.unloadAreaName
        if (data.unloadTime != null && data.unloadTime != ""&&data.loadTime!="0") {
            tv_gm_order_end_time.text = timeUtil.getMDhmTime(data.unloadTime.toLong())
        } else {
            tv_gm_order_end_time.text = "无"
            ll_gm_unload_time.visibility = View.GONE
        }
        if (!StringUtil.isEmpty(data.name)) {
            tv_gm_order_good_name.text = data.name
            tv_gm_order_good_name.visibility = View.VISIBLE
        } else {
            tv_gm_order_good_name.visibility = View.GONE
        }
        if (!StringUtil.isEmpty(data.weightVolume!!)) {
            tv_gm_order_good_dunwei.text = data.weightVolume
            tv_gm_order_good_dunwei.visibility = View.VISIBLE
        } else {
            tv_gm_order_good_dunwei.visibility = View.GONE
        }
        var cost=""
        if (!StringUtil.isEmpty(data.cost)){
            cost= data.cost!!;
            tv_infom_order_y.visibility=View.VISIBLE
        }else{
            tv_infom_order_y.visibility=View.GONE
        }
        tv_gm_order_yunfeimoney.text = cost
        tv_gm_order_name.text = data.shipperName
        tv_gm_order_company.text = data.corporateName
        iv_gm_order_phone.setOnClickListener {
            Utils.playPhone(mContext, data.shipperPhone!!)
        }
        li_gm_order_yunshu_agressment.setOnClickListener {
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
                tv_gm_order_yunshu_status.text = getString(R.string.wait_sure)
                mUpdata = true
            }
            "2" -> {
                tv_gm_order_yunshu_status.text = getString(R.string.no_send)
                mUpdata = false
            }
            "3" -> {
                tv_gm_order_yunshu_status.text = getString(R.string.agree)
                mUpdata = true
            }
            "4" -> {
                tv_gm_order_yunshu_status.text = getString(R.string.after_reject)
                mUpdata = false
            }
            else -> {
                tv_gm_order_yunshu_status.text = getString(R.string.no_send)
                mUpdata = false
            }
        }
        when (data.receipt) {
            "1" -> {
                tv_gm_order_upStatus.text = getString(R.string.after_updata)
                tv_gm_order_huidan_status.text = getString(R.string.look_receipt)
                mShowLook = true
            }
            "2" -> {
                tv_gm_order_upStatus.text = getString(R.string.no_update)
                tv_gm_order_huidan_status.text = getString(R.string.update_receipt)
                mShowLook = false
            }
            else -> {
                tv_gm_order_upStatus.text = getString(R.string.no_update)
                tv_gm_order_huidan_status.text = getString(R.string.update_receipt)
                mShowLook = false
            }
        }
        Utils.showNetImager(iv_order_inofom_shipper_hear, data.shipperPortrait)
        tv_gm_order_order_number.text = data.orderId
        tv_gm_order_order_time.text = timeUtil.getYMDTime(data.createdTime!!.toLong())
        tv_gm_order_copy.setOnClickListener {
            val trim = tv_gm_order_order_number.text.toString().trim()
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText(null, trim)
            clipboard.primaryClip = clipData
            showToast(getString(R.string.cope_success))
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
