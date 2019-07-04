package com.zzzh.akhalteke.fragment.home

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.tencent.bugly.crashreport.CrashReport
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.BitmapCallback
import com.zzzh.akhalteke.BaseApplication
import com.zzzh.akhalteke.MainActivity

import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.WebViewActivity
import com.zzzh.akhalteke.bean.DataMessageVo
import com.zzzh.akhalteke.bean.vo.GetUserInfom
import com.zzzh.akhalteke.dbHelp.UserDbHelp
import com.zzzh.akhalteke.mvp.contract.CheckInfomContract
import com.zzzh.akhalteke.mvp.contract.MeContract
import com.zzzh.akhalteke.mvp.model.CheckInfomModel
import com.zzzh.akhalteke.mvp.model.MeModel
import com.zzzh.akhalteke.mvp.presenter.CheckInfomPresenter
import com.zzzh.akhalteke.mvp.presenter.MePresenter
import com.zzzh.akhalteke.ui.BaseFragment
import com.zzzh.akhalteke.ui.SelectorImageFragment
import com.zzzh.akhalteke.ui.me.*
import com.zzzh.akhalteke.utils.StringUtil
import com.zzzh.akhalteke.utils.Utils
import kotlinx.android.synthetic.main.fragment_home_me.*
import okhttp3.Call
import okhttp3.Response
import java.lang.Exception

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * @Author : YFL  is Creating a porject in Lenovo
 * @Email : yufeilong92@163.com
 * @Time :2019/3/12 10:42
 * @Purpose :我的
 */

class HomeMeFragment : SelectorImageFragment(), View.OnClickListener, CheckInfomContract.View {

    private var param1: String? = null
    private var param2: String? = null
    private var isFisetRquest = true
    private var mCheckPresenter: CheckInfomPresenter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                HomeMeFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    override fun setContentViewImg(): Int {
        return R.layout.fragment_home_me
    }

    private var mActivity: MainActivity? = null
    override fun setCreatedContentImgView(view: View, savedInstanceState: Bundle?) {
        if (activity is MainActivity) {
            mActivity = activity as MainActivity
        }
        initRequest()
        initEvent()
        bindView()
    }

    override fun onResume() {
        super.onResume()
            bindView()
    }

    fun initRequest() {
        mCheckPresenter = CheckInfomPresenter()
        mCheckPresenter!!.initMvp(this, CheckInfomModel())
        mCheckPresenter!!.requestCheckInfom(mContext)
        isFisetRquest = true
    }

    fun bindView() {
        val userInfom = UserDbHelp.get_Instance(mContext)!!.getUserInfom()
        if (userInfom == null) {
            BaseApplication.toInstance().startActivty(mContext)
            return
        }
        Utils.showNetImager(iv_me_hear, userInfom.hear)
        var name = getString(R.string.madao_driver)
        if (!StringUtil.isEmpty(userInfom.name)) {
            name = userInfom.name
        }
        tv_m_name.text = name
        tv_m_plateNumber.text = userInfom.plateNumber
        showZF(tv_m_drvier_type, userInfom.role)
        iv_me_real_name.visibility=if (userInfom.ifReal=="1") View.VISIBLE else View.GONE
        iv_me_olddriver.visibility=if (userInfom.ifDriver=="1") View.VISIBLE else View.GONE
        iv_me_five_starts.visibility=if (userInfom.ifCar=="1") View.VISIBLE else View.GONE
        iv_me_809.visibility=if (userInfom.extend=="1") View.VISIBLE else View.GONE
    }


    fun showZF(tv: TextView, status: String) {
        when (status) {
            "1" -> {
                tv.text = getString(R.string.z_driving)
            }
            "2" -> {
                tv.text = getString(R.string.f_driving)
            }
            else -> tv.text = ""
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
       R.id.ll_me_driver_paizhao,  R.id.tv_m_name,   R.id.iv_me_hear -> {//个人资料
                jumpTo(MeInfomActivity::class.java, getString(R.string.me_infom))
            }
        }
    }

    fun initEvent() {
        iv_me_hear.setOnClickListener(this)
        tv_m_name.setOnClickListener(this)
        ll_me_driver_paizhao.setOnClickListener(this)
        ll_m_kefu.setOnClickListener {
            //客服
            Utils.playPhone(mContext, DataMessageVo.KEFU_PHONE)
        }
        ll_m_setting.setOnClickListener {
            //设置
            jumpTo(SettingActivity::class.java, getString(R.string.setting))
        }
        ll_m_sms.setOnClickListener {
            //我的消息
            jumpTo(MsgActivity::class.java, getString(R.string.my_msg))
        }
        tv_m_partner.setOnClickListener {
            //城市合伙人
            val bundle = Bundle()
            bundle.putString(WebViewActivity.TYPE, WebViewActivity.NO_HEAR_HTTP_TYPE)
            bundle.putString(WebViewActivity.URL, getString(R.string.city_hmtl))
            jumpTo(WebViewActivity::class.java, bundle, getString(R.string.city_partner))
        }
        tv_m_about_us.setOnClickListener {
            //关于我们

            val bundle = Bundle()
            bundle.putString(WebViewActivity.TYPE, WebViewActivity.NO_HEAR_HTTP_TYPE)
            bundle.putString(WebViewActivity.URL, getString(R.string.about_us_hmtl))
            jumpTo(WebViewActivity::class.java, bundle, getString(R.string.about_us))

        }
        rlv_me_my_money.setOnClickListener {
            val dbHelp = UserDbHelp.get_Instance(mContext)
            val userInfom = dbHelp!!.getUserInfom()
            if (userInfom != null) {
                if (userInfom.ifReal == "2") {
                    mActivity!!.showAuthentication(userInfom)
                    return@setOnClickListener
                }
                if (userInfom.ifDriver == "2") {
                    mActivity!!.showDriverDialog(userInfom)
                    return@setOnClickListener
                }
                if (userInfom.ifCar == "2") {
                    mActivity!!.showCarDialog(userInfom)
                    return@setOnClickListener
                }
            }
            showProgress()
            mCheckPresenter!!.requestCheckInfom(mContext)
            isFisetRquest = false
        }
    }


    override fun CheckSuccess(t: Any?) {
        if (t == null) return
        val data: GetUserInfom = t as GetUserInfom
        updateUserInfom(data)
        if (isFisetRquest) return
        showGoRenZhen(data, object : BaseFragment.ContinueDoEvent {
            override fun doEvent() {
                val bunder = Bundle()
                jumpTo(MyNewMoneyActivity::class.java, bunder, getString(R.string.my_wallet))
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
