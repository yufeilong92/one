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
import com.zzzh.akhalteke.utils.*
import kotlinx.android.synthetic.main.activity_infom_new.*

/**
 * @Author : YFL  is Creating a porject in com.zzzh.akhalteke.ui.home.find
 * @Package com.zzzh.akhalteke.ui.home.find
 * @Email : yufeilong92@163.com
 * @Time :2019/6/3 17:53
 * @Purpose :找货详情
 */
class InfomNewActivity : BaseActivity(), InfomContract.View, CheckInfomContract.View {

    companion object {
        val INFOM_ID: String = "id"
    }

    private var mCheckInfomPresenter = CheckInfomPresenter()
    private var mCheckInfomParame: String? = ""
    private var mGoodInfomId: String = ""

    override fun setContentView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_infom_new)
        if (intent != null) {
            mGoodInfomId = intent.getStringExtra(InfomActivity.INFOM_ID)
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
            ll_infom_detail_root.visibility = View.GONE
            return
        }
        ll_infom_detail_root.visibility = View.VISIBLE
        val data: InfomVo = t as InfomVo
        bindViewData(data)
    }

    fun bindViewData(data: InfomVo) {
        //起始地址
        TextUtil.setViewShow(tv_infom_detail_city, data.loadAreaCode)
        TextUtil.setViewShow(tv_infom_detail_city_address, data.loadAddress)
        //终止地址
        TextUtil.setViewShow(tv_infom_detail_uncity, data.unloadAreaCode)
        TextUtil.setViewShow(tv_infom_detail_uncity_unaddress, data.unloadAddress)
        //注册时间
        if (!StringUtil.isEmpty(data.createdTime)) {
            tv_infom_detail_create_time.text = TimeUtil().getYMDTime(data!!.createdTime!!.toLong()) + " 注册"
        }
        val content = StringBuffer()
        if (!StringUtil.isEmpty(data.carLength)) {
            content.append(data.carLength)
            content.append("  ")
        }
        if (!StringUtil.isEmpty(data.carType)) {
            content.append(data.carType)
        }
        //车辆
        tv_infom_detail_car.text = content.toString()
        val contentOne = StringBuffer()
        if (!StringUtil.isEmpty(data.goodsName)) {
            contentOne.append(data.goodsName)
            contentOne.append("  ")
        }
        if (!StringUtil.isEmpty(data.weightVolume)) {
            contentOne.append(data.weightVolume)
        }
        //货物
        tv_infom_detail_goods.text = contentOne.toString()
//        val type = Utils.getArrayString(mContext, R.array.car_type)
        val loadtype = Utils.getArrayString(mContext, R.array.infom_loadType)
        val contentTwo = StringBuffer()
        var laod = ""
        var time = ""
        var car_type = ""
//        if (!StringUtil.isEmpty(data.goodsType) && data.goodsType != "0") {
//            car_type = type[data.goodsType!!.toInt() - 1]
//        }
//        tv_infom_car_type.text = car_type
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
        //装卸
        tv_infom_detail_loadorunload.text = contentTwo.toString()
        if (!StringUtil.isEmpty(data.cost)) {
            //其他
            tv_infom_detail_other.text = data.cost + "/趟"
        }
        //备注
        tv_infom_detail_beizhu.text = data.comments
        tv_infom_detail_name.text = data.shipperName
        btn_infom_detail__play_phone.setOnClickListener {
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
        rl_infom_detail_driver.setOnClickListener {
            //货主简介
            val bunder = Bundle()
            bunder.putString(ShipperNewActivity.SHIPPER_ID, data.shipperId)
            jumpTo(ShipperNewActivity::class.java, bunder, getString(R.string.shipper_infom))
        }
        Utils.showNetImager(iv_sdv_infom_user_hear, data.shipperPortrait)
        //实名认证
        iv_source_detail_readname.visibility = if (data.ifRealCertification == "1") View.VISIBLE else View.GONE
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
                DialogUtil.showPlayPhone(this@InfomNewActivity, 1,mCheckInfomParame!!, object : DialogUtil.ShowPlayPhoneListener {
                    override fun onClickPlayListener(str: String) {
                        Utils.playPhone(this@InfomNewActivity, str)
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
