package com.zzzh.akhalteke.ui.home.find

import android.os.Bundle
import android.view.View
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.adapter.ShipperGoodinfomAdapter
import com.zzzh.akhalteke.bean.vo.GetUserInfom
import com.zzzh.akhalteke.bean.vo.ShipperJIanjieVo
import com.zzzh.akhalteke.mvp.contract.CheckInfomContract
import com.zzzh.akhalteke.mvp.contract.ShipperbyIdContract
import com.zzzh.akhalteke.mvp.model.CheckInfomModel
import com.zzzh.akhalteke.mvp.model.ShipperbyidModel
import com.zzzh.akhalteke.mvp.presenter.CheckInfomPresenter
import com.zzzh.akhalteke.mvp.presenter.ShipperbyidPresenter
import com.zzzh.akhalteke.ui.BaseActivity
import com.zzzh.akhalteke.utils.DialogUtil
import com.zzzh.akhalteke.utils.TimeUtil
import com.zzzh.akhalteke.utils.Utils
import kotlinx.android.synthetic.main.activity_shipper.*

/**
 * @Author : YFL  is Creating a porject in com.zzzh.akhalteke.ui.user
 * @Package com.zzzh.akhalteke.ui.user
 * @Email : yufeilong92@163.com
 * @Time :2019/3/14 14:28
 * @Purpose :货主简介
 */
class ShipperActivity : BaseActivity(), ShipperbyIdContract.View, CheckInfomContract.View {


    private var mShipperId: String = ""
    private var mCheckInfomParamer: String? = "";
    private var mCheckInfomPresenter: CheckInfomPresenter? = null

    companion object {
        //货主id
        var SHIPPER_ID: String = "shipperId"
    }

    override fun setContentView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_shipper)
        if (intent != null) {
            mShipperId = intent.getStringExtra(SHIPPER_ID)
        }
        initEvent()
        initRequest()
    }

    fun initEvent() {

        tv_shipper_look_more.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(DayGoodActivity.SHIPPER_ID, mShipperId)
            jumpTo(DayGoodActivity::class.java, bundle, getString(R.string.please_day_goods))
        }
    }

    fun initRequest() {
        val mPresenter = ShipperbyidPresenter()
        mPresenter.initMvp(this, model = ShipperbyidModel())
        showProgress()
        mPresenter.requestShipperId(mContext, mShipperId)
        mCheckInfomPresenter = CheckInfomPresenter()
        mCheckInfomPresenter!!.initMvp(this, CheckInfomModel())

    }

    override fun Success(t: Any?) {
        val data: ShipperJIanjieVo = t as ShipperJIanjieVo
        bindViewData(data)
    }

    fun bindViewData(vo: ShipperJIanjieVo) {
        tv_shipper_name.text = vo.name
        val ymdTime = TimeUtil.getInstance()!!.getYMDTime(vo.registerTime!!.toLong())
        tv_shipper_register.text = ymdTime
        chb_shipper_shiming.isChecked = vo.ifRealCertification == "1"
        chb_shipper_gongsi.isChecked = vo.ifCompanyCertification == "1"
        tv_shipper_company_name.text = vo.corporateName
        tv_shipper_company_address.text = vo.areaAddress
        tv_shipper_goodcount.text = vo.goodsCount
        tv_shipper_jiayicount.text = vo.orderCount
        tv_shipper_goodnumber.text = "当天货源(" + vo.goodsList!!.size + ")"
        setMangager(rlv_shipper_good_list)
        Utils.showNetImager(iv_shupper_hear, vo.portrait)
        var adapter = ShipperGoodinfomAdapter(mContext, vo.goodsList!!)
        rlv_shipper_good_list.adapter = adapter
        if (vo.goodsList!!.size > 2) {
            tv_shipper_look_more.visibility = View.VISIBLE
        } else {
            tv_shipper_look_more.visibility = View.GONE
        }
        btn_shipper_phone.setOnClickListener {
            showProgress()
            mCheckInfomParamer = vo.phone
            mCheckInfomPresenter!!.requestCheckInfom(mContext)
//            DialogUtil.showPlayPhone(this@ShipperActivity, vo.phone!!, object : DialogUtil.ShowPlayPhoneListener {
//                override fun onClickPlayListener(str: String) {
//                    Utils.playPhone(this@ShipperActivity, str)
//                }
//            })
        }
    }

    override fun Error(ex: Throwable) {
        this.onError(ex)
    }

    override fun Complise() {
        this.onComplise()
    }

    override fun CheckSuccess(t: Any?) {
        if (t == null) return
        val data: GetUserInfom = t as GetUserInfom
        updateUserInfom(data)
        showGoRenZhen(data, object : BaseActivity.ContinueDoEvent {
            override fun doEvent() {
                DialogUtil.showPlayPhone(this@ShipperActivity,1, mCheckInfomParamer!!, object : DialogUtil.ShowPlayPhoneListener {
                    override fun onClickPlayListener(str: String) {
                        Utils.playPhone(this@ShipperActivity, str)
                    }
                })
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
