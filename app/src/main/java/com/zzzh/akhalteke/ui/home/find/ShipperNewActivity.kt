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
import kotlinx.android.synthetic.main.activity_shipper_new.*

/**
 * @Author : YFL  is Creating a porject in com.zzzh.akhalteke.ui.home.find
 * @Package com.zzzh.akhalteke.ui.home.find
 * @Email : yufeilong92@163.com
 * @Time :2019/6/4 14:17
 * @Purpose :货主简介
 */
class ShipperNewActivity : BaseActivity(), ShipperbyIdContract.View, CheckInfomContract.View{
    private var mShipperId: String = ""
    private var mCheckInfomParamer: String? = "";
    private var mCheckInfomPresenter: CheckInfomPresenter? = null

    companion object {
        //货主id
        var SHIPPER_ID: String = "shipperId"
    }

    override fun setContentView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_shipper_new)
        if (intent != null) {
            mShipperId = intent.getStringExtra(ShipperActivity.SHIPPER_ID)
        }
        initEvent()
        initRequest()
    }

    fun initEvent() {

//        tv_shipper_look_more.setOnClickListener {
//            val bundle = Bundle()
//            bundle.putString(DayGoodActivity.SHIPPER_ID, mShipperId)
//            jumpTo(DayGoodActivity::class.java, bundle, getString(R.string.please_day_goods))
//        }
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
        //姓名
        tv_ship_detail_name.text = vo.name
        val ymdTime = TimeUtil.getInstance()!!.getYMDTime(vo.registerTime!!.toLong())
        //注册时间
        tv_ship_detail_create_time.text = ymdTime
        iv_ship_detail_realname.visibility =if (vo.ifRealCertification == "1") View.VISIBLE else View.GONE
        iv_ship_detail_dao.visibility =if (vo.ifCompanyCertification == "1")  View.VISIBLE else View.GONE
        //公司名称
        tv_ship_detail_company.text = vo.corporateName
        //公司地址
        tv_ship_detail_address.text = vo.areaAddress
        tv_ship_detail_send_number.text = vo.goodsCount
        tv_ship_detail_deal_number.text = vo.orderCount
        if (vo.goodsList==null||vo.goodsList.isEmpty()){
            tv_ship_detail_title3.text ="0"
        }else{
            tv_ship_detail_title3.text = "${vo.goodsList.size}"
        }
        setMangager(rlv_ship_detail_recycle)
        Utils.showNetImager(sdv_iv_shupper_hear, vo.portrait)
        var adapter = ShipperGoodinfomAdapter(mContext, vo.goodsList!!)
        rlv_ship_detail_recycle.adapter = adapter
        if (vo.goodsList!!.size > 2) {
//            tv_shipper_look_more.visibility = View.VISIBLE
        } else {
//            tv_shipper_look_more.visibility = View.GONE
        }
        tv_ship_detail_it.setOnClickListener {
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
                DialogUtil.showPlayPhone(this@ShipperNewActivity,1, mCheckInfomParamer!!, object : DialogUtil.ShowPlayPhoneListener {
                    override fun onClickPlayListener(str: String) {
                        Utils.playPhone(this@ShipperNewActivity, str)
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
