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
import com.zzzh.akhalteke.bean.vo.GetUserInfom
import com.zzzh.akhalteke.bean.vo.OrderAgreementVo
import com.zzzh.akhalteke.mvp.contract.AgreementContract
import com.zzzh.akhalteke.mvp.contract.CheckInfomContract
import com.zzzh.akhalteke.mvp.model.AgreementModel
import com.zzzh.akhalteke.mvp.model.CheckInfomModel
import com.zzzh.akhalteke.mvp.presenter.AgreementPresenter
import com.zzzh.akhalteke.mvp.presenter.CheckInfomPresenter
import com.zzzh.akhalteke.ui.BaseActivity
import com.zzzh.akhalteke.utils.*
import kotlinx.android.synthetic.main.activity_open_video.*
import kotlinx.android.synthetic.main.activity_order_infom.*
import kotlinx.android.synthetic.main.activity_order_new_infom.*
import kotlinx.android.synthetic.main.gm_order_infom.*
import kotlinx.android.synthetic.main.gm_order_new_infom.*
import kotlinx.android.synthetic.main.gm_rl_title_title.*

/**
 * @Author : YFL  is Creating a porject in com.zzzh.akhalteke.ui.waybill
 * @Package com.zzzh.akhalteke.ui.waybill
 * @Email : yufeilong92@163.com
 * @Time :2019/3/19 18:29
 * @Purpose :新运输中(已发起协议)
 */

class OrderNewInfomActivity : BaseActivity(), AgreementContract.View, CheckInfomContract.View {

    companion object {
        val ORDER_ID = "orderid"
        val TYPE = "type"
        val TYPE_ONE = "ONE"
        val TYPE_TWO = "two"
        val TYPE_THREE = "three"
    }

    private var mOrderId: String? = null
    private var mPresenter: AgreementPresenter? = null
    private var mCheckInfomPresenter: CheckInfomPresenter? = null
    private var mUpdateHuiDan: Boolean = true//上传汇单
    private var mType: String? = null
    override fun setContentView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_order_new_infom)
        if (intent != null) {
            mOrderId = intent.getStringExtra(ORDER_ID)
            mType = intent.getStringExtra(TYPE)
        }
//        tv_gm_right_title.text = getString(R.string.contact_service)
        initRequest()
        initEvent()

    }

    fun initEvent() {

        tv_order_new_infom_kefu.setOnClickListener {
            Utils.playPhone(mContext, DataMessageVo.KEFU_PHONE)
        }
        rl_order_detail_receipt.setOnClickListener {
            if (!mUpdateHuiDan) {
                showToast(getString(R.string.no_agreement))
                return@setOnClickListener
            }
            PermissionUtils.showPermission(mContext, "",
                    arrayOf(Permission.WRITE_EXTERNAL_STORAGE,
                            Permission.ACCESS_COARSE_LOCATION,
                            Permission.READ_EXTERNAL_STORAGE)) {
                showProgress()
                mCheckInfomPresenter!!.requestCheckInfom(mContext)

            }

        }

    }

    fun initRequest() {
        mPresenter = AgreementPresenter()
        mPresenter!!.initMvp(this, model = AgreementModel())
        showProgress()
        mPresenter!!.requestOrderInfo(mContext, mOrderId!!)
        mCheckInfomPresenter = CheckInfomPresenter()
        mCheckInfomPresenter!!.initMvp(this, CheckInfomModel())

    }

    override fun InfoSuccess(t: Any?) {
        if (t == null) {
            rl_new_root_view.visibility = View.GONE
            return
        } else {
            rl_new_root_view.visibility = View.VISIBLE
            nsv_root.visibility = View.VISIBLE
        }
        val data: OrderAgreementVo = t as OrderAgreementVo
        bindViewData(data)

    }

    override fun onRestart() {
        super.onRestart()
        showProgress()
        mPresenter!!.requestOrderInfo(mContext, mOrderId!!)
    }

    fun bindViewData(data: OrderAgreementVo) {
        val timeUtil = TimeUtil.getInstance()!!
        tv_order_detail_city.text = data.loadAreaName
        TextUtil.setViewShow(tv_order_detail_city_address, data.loadAddress)
        if (data.loadTime != null && data.loadTime != "" && data.loadTime != "0") {
            tv_order_detail_time.text = timeUtil.getMDhmTime(data.loadTime.toLong())
        } else {
            tv_order_detail_time.text = "无"
            ll_order_detail_loadd.visibility = View.GONE
        }
        tv_order_detail_uncity.text = data.unloadAreaName
        TextUtil.setViewShow(tv_order_detail_uncity_unaddress, data.unloadAddress)
        if (data.unloadTime != null && data.unloadTime != "" && data.loadTime != "0") {
            tv_order_detail_untime.text = timeUtil.getMDhmTime(data.unloadTime.toLong())
        } else {
            ll_order_detail_unload.visibility = View.GONE
            tv_order_detail_untime.text = "无"
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
        tv_order_detail_money.text = "￥${data.cost}"
        //姓名
        tv_order_detail_name.text = data.shipperName
        //公司
        tv_order_detail_plate.text = data.corporateName
        //电话
        iv_order_detail_phone.setOnClickListener {
            Utils.playPhone(mContext, data.shipperPhone!!)
        }
        //货源协议
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

        btn_transpeor_new_qianxieyi.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(FreightNewAgreementActivity.ORDER_ID, mOrderId)
            when (data.ifAgreement) {
                "1" -> {
                    PermissionUtils.showPermission(mContext, "",
                            arrayOf(Permission.ACCESS_COARSE_LOCATION)) {
                        jumpTo(FreightNewAgreementActivity::class.java, bundle, getString(R.string.yunshugood_agreement))
                    }
                }
                "2" -> {
                    showToast(getString(R.string.shipper_no_sendagreement))
                }
                else -> {
                    PermissionUtils.showPermission(mContext, "",
                            arrayOf(Permission.ACCESS_COARSE_LOCATION)) {
                        jumpTo(CompletaNewAgreementActivity::class.java, bundle, getString(R.string.yunshugood_agreement))
                    }
                }
            }
        }
        when (data.ifAgreement) {
            "1" -> {
                tv_order_detail_agreement_status.text = getString(R.string.wait_sure)
                mUpdateHuiDan = false
            }
            "2" -> {
                tv_order_detail_agreement_status.text = getString(R.string.no_send)
                mUpdateHuiDan = false
            }
            "3" -> {
                tv_order_detail_agreement_status.text = getString(R.string.agree)
                mUpdateHuiDan = true
                btn_transpeor_new_qianxieyi.text = getString(R.string.look_agreement)
            }
            "4" -> {
                tv_order_detail_agreement_status.text = getString(R.string.after_reject)
                mUpdateHuiDan = false
            }
            else -> {
                tv_order_detail_agreement_status.text = getString(R.string.no_send)
                mUpdateHuiDan = false
            }
        }
        when (data.receipt) {
            "1" -> {
//                tv_gm_order_upStatus.text = getString(R.string.after_updata)
                tv_order_detail_receipt_status.text = getString(R.string.look_receipt)
            }
            "2" -> {
//                tv_gm_order_upStatus.text = getString(R.string.no_update)
                tv_order_detail_receipt_status.text = getString(R.string.update_receipt)
            }
            else -> {
//                tv_gm_order_upStatus.text = getString(R.string.no_update)
                tv_order_detail_receipt_status.text = getString(R.string.update_receipt)
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
        if (datas == null || datas.isEmpty()) {
            flw_order_detail_tag.visibility = View.GONE
            return
        }
        flw_order_detail_tag.removeAllViews()
        for (i in datas!!.indices) {
            val key = datas!![i]
            val itemvo = LayoutInflater.from(mContext).inflate(R.layout.item_order_detail_tag, flw_order_detail_tag, false)
            val tv = itemvo.findViewById(R.id.tv_order_detail_tag) as TextView
            tv.setBackgroundResource(ToolUtils.getImageDrawer(mContext))
            tv.text = key
            flw_order_detail_tag.addView(itemvo)
        }
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

    override fun CheckSuccess(t: Any?) {
        if (t == null) return
        val data: GetUserInfom = t as GetUserInfom
        updateUserInfom(data)
        showGoRenZhen(data, object : BaseActivity.ContinueDoEvent {
            override fun doEvent() {
                val bundle = Bundle()
                bundle.putString(UpdataReciptActvity.ORDER_ID, mOrderId)
                bundle.putString(UpdataReciptActvity.TYPE, UpdataReciptActvity.NO_TYPE)
                jumpTo(UpdataReciptActvity::class.java, bundle, getString(R.string.update_receipt))
            }
        })
    }

    override fun CheckError(ex: Throwable) {
        this.onError(ex)
    }

    override fun CheckComplise() {
        this.onComplise()
    }


}
