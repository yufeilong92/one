package com.zzzh.akhalteke.ui.home.find

import android.os.Bundle
import android.view.View
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.bean.vo.GetUserInfom
import com.zzzh.akhalteke.bean.vo.InfomVo
import com.zzzh.akhalteke.mvp.contract.CheckInfomContract
import com.zzzh.akhalteke.mvp.contract.InfomContract
import com.zzzh.akhalteke.mvp.model.CheckInfomModel
import com.zzzh.akhalteke.mvp.model.InfomModel
import com.zzzh.akhalteke.mvp.presenter.CheckInfomPresenter
import com.zzzh.akhalteke.mvp.presenter.InfomPresneter
import com.zzzh.akhalteke.ui.BaseActivity
import com.zzzh.akhalteke.utils.DialogUtil
import com.zzzh.akhalteke.utils.StringUtil
import com.zzzh.akhalteke.utils.TimeUtil
import com.zzzh.akhalteke.utils.Utils
import kotlinx.android.synthetic.main.activity_infom.*

/**
 * @Author : YFL  is Creating a porject in com.zzzh.akhalteke.ui.home
 * @Package com.zzzh.akhalteke.ui.home
 * @Email : yufeilong92@163.com
 * @Time :2019/3/14 11:49
 * @Purpose :订单详情页
 */
class InfomActivity : BaseActivity(), InfomContract.View, CheckInfomContract.View {


    companion object {
        val INFOM_ID: String = "id"
    }

    private var mCheckInfomPresenter = CheckInfomPresenter()
    private var mCheckInfomParame: String? = ""
    private var mGoodInfomId: String = ""
    override fun setContentView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_infom)
        if (intent != null) {
            mGoodInfomId = intent.getStringExtra(INFOM_ID)
        }
        initEvent()
        initRequest()
    }

    fun initEvent() {

    }

    fun initRequest() {
        val mPresenter = InfomPresneter()
        mPresenter.initMvp(this, model = InfomModel())
        showProgress()
        mPresenter.requestInfom(mContext, mGoodInfomId)
        mCheckInfomPresenter = CheckInfomPresenter()
        mCheckInfomPresenter.initMvp(this, CheckInfomModel())

    }

    override fun InfomSuccess(t: Any?) {
        if (t == null) {
            return
        }
        ll_infom_root.visibility = View.VISIBLE
        val data: InfomVo = t as InfomVo
        bindViewData(data)
    }

    fun bindViewData(data: InfomVo) {

        tv_infom_start_address.text = Utils.showAddress(mContext, data.loadAreaCode, data.unloadAreaCode)
        if (!StringUtil.isEmpty(data.createdTime)) {
            tv_infom_create_time.text = TimeUtil().getYMDTime(data!!.createdTime!!.toLong()) + " 注册"
        }
        val content = StringBuffer()
        if (!StringUtil.isEmpty(data.carLength)) {
            content.append(data.carLength)
            content.append("  ")
        }
        if (!StringUtil.isEmpty(data.carType)) {
            content.append(data.carType)
        }
        tv_infom_car.text = content.toString()
//        tv_infom_car.text = data.carLength + "米  " + data.carType
        val contentOne = StringBuffer()
        if (!StringUtil.isEmpty(data.goodsName)) {
            contentOne.append(data.goodsName)
            contentOne.append("  ")
        }
        if (!StringUtil.isEmpty(data.weightVolume)) {
            contentOne.append(data.weightVolume)
        }

//        tv_infom_goods.text = data.goodsName + ",${data.weightVolume}"
        tv_infom_goods.text = contentOne.toString()
        val type = Utils.getArrayString(mContext, R.array.car_type)
//        val timeList = Utils.getArrayString(mContext, R.array.car_time)
        val loadtype = Utils.getArrayString(mContext, R.array.infom_loadType)
        val contentTwo = StringBuffer()
        var laod = ""
        var time = ""
        var car_type = ""
        if (!StringUtil.isEmpty(data.goodsType) && data.goodsType != "0") {
            car_type = type[data.goodsType!!.toInt() - 1]
        }
        tv_infom_car_type.text = car_type
        if (!StringUtil.isEmpty(data.loadTime) && data.loadTime != "0") {
            time = TimeUtil.getInstance()!!.getTime(data.loadTime!!.toLong())
        }
        if (!StringUtil.isEmpty(data.loadType) && data.loadType != "0") {
            laod = loadtype[data.loadType!!.toInt() - 1]
        }
        if (!StringUtil.isEmpty(time)) {
            if (StringUtil.isEmpty(laod)) {
                contentTwo.append(time)
            } else {
                contentTwo.append(time)
                contentTwo.append(",  ")
            }
        }
        if (!StringUtil.isEmpty(laod)) {
            contentTwo.append(laod)
        }
        tv_infom_content.text = contentTwo.toString()
        if (!StringUtil.isEmpty(data.loadAddress!!)) {
            ll_infom_supply_address.visibility = View.VISIBLE
            tv_infom_supply_address.text = "货源地址：" + data.loadAddress
        } else {
            ll_infom_supply_address.visibility = View.GONE
        }
        if (StringUtil.isEmpty(data.unloadAddress!!)) {
            ll_unloading_address.visibility = View.GONE
        } else {
            ll_unloading_address.visibility = View.VISIBLE
            tv_infom_unloading_address.text = "卸货地址：" + data.unloadAddress
        }
        if (!StringUtil.isEmpty(data.cost)) {
            tv_infom_other.text = data.cost + "/趟"
        }
        tv_infom_note.text = data.comments
        tv_infom_name.text = data.shipperName
        tv_infom_cargo.text = data.corporateName
        btn_infom_phone.setOnClickListener {
            mCheckInfomParame = data.shipperPhone!!
            showProgress()
            mCheckInfomPresenter.requestCheckInfom(mContext)
            //电话咨询
//            DialogUtil.showPlayPhone(this@InfomActivity, data.shipperPhone!!, object : DialogUtil.ShowPlayPhoneListener {
//                override fun onClickPlayListener(str: String) {
//                    Utils.playPhone(this@InfomActivity, str)
//                }
//            })
        }
        ll_infom_go_infom.setOnClickListener {
            //货主简介
            val bunder = Bundle()
            bunder.putString(ShipperNewActivity.SHIPPER_ID, data.shipperId)
            jumpTo(ShipperNewActivity::class.java, bunder, getString(R.string.shipper_infom))
        }
        Utils.showNetImager(iv_infom_user_hear, data.shipperPortrait)
    }

    override fun InfomError(ex: Throwable) {
        this.onError(ex)
    }

    override fun InfomComplise() {
        this.onComplise()
    }

    override fun CheckSuccess(t: Any?) {
        if (t == null) return
        val data: GetUserInfom = t as GetUserInfom
        updateUserInfom(data)
        showGoRenZhen(data, object : BaseActivity.ContinueDoEvent {
            override fun doEvent() {
                DialogUtil.showPlayPhone(this@InfomActivity, 1,mCheckInfomParame!!, object : DialogUtil.ShowPlayPhoneListener {
                    override fun onClickPlayListener(str: String) {
                        Utils.playPhone(this@InfomActivity, str)
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
