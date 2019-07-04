package com.zzzh.akhalteke.ui.me

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.zzzh.akhalteke.BaseApplication
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.bean.DataMessageVo
import com.zzzh.akhalteke.dbHelp.UserDbHelp
import com.zzzh.akhalteke.dbHelp.Vo.UserDbVo
import com.zzzh.akhalteke.mvp.contract.MeInfomContract
import com.zzzh.akhalteke.mvp.model.MeInfomModel
import com.zzzh.akhalteke.mvp.presenter.MeInfomPresenter
import com.zzzh.akhalteke.ui.SelectorImageActivity


import com.zzzh.akhalteke.ui.verify.DrivingMemberActivity
import com.zzzh.akhalteke.ui.verify.newverify.AuthenticationNewActivity
import com.zzzh.akhalteke.ui.verify.newverify.CarNewActivity
import com.zzzh.akhalteke.ui.verify.newverify.DrivingNewMemberActivity
import com.zzzh.akhalteke.utils.CarColorSelextUtil
import com.zzzh.akhalteke.utils.Utils
import kotlinx.android.synthetic.main.activity_me_infom.*
import java.io.File

/**
 * @Author : YFL  is Creating a porject in com.zzzh.akhalteke.ui.me
 * @Package com.zzzh.akhalteke.ui.me
 * @Email : yufeilong92@163.com
 * @Time :2019/4/1 16:02
 * @Purpose :个人信息页
 */
class MeInfomActivity : SelectorImageActivity(), View.OnClickListener, MeInfomContract.View {

    private var mUserInfom: UserDbVo? = null
    private var mPresenter: MeInfomPresenter? = null
    override fun setContentView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_me_infom)
        val help = UserDbHelp.get_Instance(mContext)
        mUserInfom = help!!.getUserInfom()
        if (mUserInfom == null) {
            BaseApplication.toInstance().startActivty(mContext)
            return
        }
        initRequest()
        bindViewData()
        initEvent()
        initUpdataHear()

    }

    fun initRequest() {
        mPresenter = MeInfomPresenter()
        mPresenter!!.initMvp(this, MeInfomModel())

    }

    fun initUpdataHear() {
        ll_updata_hear.setOnClickListener {
            //行驶证
            toShowDialog(DataMessageVo.Hear)
        }
    }

    override fun onBaseNext(base: String, id: Int, img: String) {
        showProgress()
        Utils.showLoadmager(sdv_me_hear, img)
        mPresenter!!.submitHear(mContext, File(img))
    }

    override fun onRestart() {
        super.onRestart()
        val help = UserDbHelp.get_Instance(mContext)
        mUserInfom = help!!.getUserInfom()
        bindViewData()
    }

    fun bindViewData() {
        Utils.showNetImager(sdv_me_hear, mUserInfom!!.hear)
        tv_me_login_phone.text = mUserInfom!!.phone
        tv_me_id_card.text = if (mUserInfom!!.ifReal == "1") {
            getString(R.string.authenticated)
        } else {
            getString(R.string.unauthorized)
        }

        tv_me_driver_authentication_status.text = if (mUserInfom!!.ifDriver == "1") {
            getString(R.string.authenticated)
        } else {
            getString(R.string.unauthorized)
        }
        tv_me_car_authentication_status.text = if (mUserInfom!!.ifCar == "1") {
            showCarColor(iv_me_card_number_status, mUserInfom!!.carPlateColourId)
            mUserInfom!!.plateNumber
        } else {
            iv_me_card_number_status.visibility = View.GONE
            getString(R.string.unauthorized)
        }

    }

    fun showCarColor(iv: ImageView, status: String) {
        iv_me_card_number_status.visibility = View.VISIBLE
        val mCarColorSelext = CarColorSelextUtil.getInstance(mContext)
        when (status) {
            "1" -> {
                mCarColorSelext!!.setSelectCar(CarColorSelextUtil.CarColor.BLUE)
            }
            "2" -> {
                mCarColorSelext!!.setSelectCar(CarColorSelextUtil.CarColor.YELLOW)
            }
            "3" -> {
                mCarColorSelext!!.setSelectCar(CarColorSelextUtil.CarColor.Green)
            }
            "4" -> {
                mCarColorSelext!!.setSelectCar(CarColorSelextUtil.CarColor.Hun)
            }
            else -> {
                iv_me_card_number_status.visibility = View.GONE
                mCarColorSelext!!.setSelectCar(CarColorSelextUtil.CarColor.BLUE)
            }
        }
        iv.setImageDrawable(mCarColorSelext.mCarColor!!)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.ll_me_id_card, R.id.ll_me_phone -> {
                if (mUserInfom!!.ifReal == "1") {
                    showToas()
                    return
                }
                val bundle = Bundle()
                bundle.putString(AuthenticationNewActivity.USERPHONE, mUserInfom!!.phone)
                bundle.putString(AuthenticationNewActivity.TYPE, AuthenticationNewActivity.TYPE_FINISH)
                jumpTo(AuthenticationNewActivity::class.java, bundle)
            }
            R.id.ll_me_driver -> {//驾驶员认证
                if (mUserInfom!!.ifDriver == "1") {
                    showToas()
                    return
                }
                val bundle = Bundle()
                bundle.putString(DrivingNewMemberActivity.TYPE, DrivingNewMemberActivity.TYPE_NO_FINISH)
                jumpTo(DrivingNewMemberActivity::class.java, bundle)
            }
            R.id.ll_me_card -> {//车辆认证
                if (mUserInfom!!.ifCar == "1") {
                    showToas()
                    return
                }
                val bundle = Bundle()
                bundle.putString(CarNewActivity.TYPE, CarNewActivity.ME_INFOM_TYPE)
                jumpTo(CarNewActivity::class.java, bundle)
            }
        }
    }

    fun showToas() {
        showToast(getString(R.string.infom_over_authorized))
    }

    fun initEvent() {
        ll_me_id_card.setOnClickListener(this)
        ll_me_phone.setOnClickListener(this)
        ll_me_driver.setOnClickListener(this)
        ll_me_card.setOnClickListener(this)
    }

    override fun UpdataHearSuccess(t: Any?) {
        val instance = UserDbHelp.get_Instance(mContext)
        instance!!.upHearInfom(t.toString())
    }

    override fun UpdataHearError(ex: Throwable) {
        this.onError(ex)
    }

    override fun UpdataHearComplise() {
        this.onComplise()
    }

}
