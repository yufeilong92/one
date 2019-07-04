package com.zzzh.akhalteke.ui.login

import android.os.Bundle
import com.yanzhenjie.permission.Permission
import com.zzzh.akhalteke.dbHelp.GmDBHelp
import com.zzzh.akhalteke.dbHelp.UserDbHelp
import com.zzzh.akhalteke.MainActivity
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.WebViewActivity
import com.zzzh.akhalteke.bean.vo.GmContentVo
import com.zzzh.akhalteke.ui.BaseActivity
import com.zzzh.akhalteke.utils.AppManager
import com.zzzh.akhalteke.utils.PermissionUtils
import com.zzzh.akhalteke.utils.StringUtil
import com.zzzh.akhalteke.utils.Utils
import kotlinx.android.synthetic.main.activity_login.*

/**
 * @Author : YFL  is Creating a porject in Lenovo
 * @Email : yufeilong92@163.com
 * @Time :2019/3/6 12:05
 * @Purpose :登陆
 */

class LoginActivity : BaseActivity() {
    override fun setContentView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_login)
        GmDBHelp.getInstance(mContext)
        isStart()
        bindEvent()
    }

    fun isStart() {
        val userDbHelp = UserDbHelp.get_Instance(mContext)
        val vo = userDbHelp!!.getUserInfom()
        //是否能登陆
        if (vo != null && !StringUtil.isEmpty(vo.token) && !StringUtil.isEmpty(vo.userId)) {
            GmContentVo.setUserInfom(vo)
//            if (StringUtil.isEmpty(vo.ifReal) || vo.ifReal != "1") {
//                jumpTo(AuthenticationActivity::class.java)
//                return
//            }
            val app: AppManager = AppManager.appManager
            app.finishActivity(LoginActivity::class.java)
            jumpTo(MainActivity::class.java)

        }

    }

    fun bindEvent() {
        tv_get_code.setOnClickListener {
            val checked = chb_shipper_shiming.isChecked
            if (!checked) {
                showToast("请阅读并同意马道隐私协议")
                return@setOnClickListener
            }
            //获取验证码
            val phone = Utils.getObjToStr(et_login_phone)
            if (StringUtil.isEmpty(phone)) {
                showToast(getString(R.string.please_phone))
                return@setOnClickListener
            }
            val num = Utils.isPhoneNum(phone)
            if (!num) {
                showToast(getString(R.string.please_right_phone))
                return@setOnClickListener
            }

            PermissionUtils.showPermission(mContext, "",
                    arrayOf(Permission.ACCESS_COARSE_LOCATION)) {
                val bundle = Bundle();
                bundle.putString(CodeActivity.PHONEKEY, phone)
                jumpTo(CodeActivity::class.java, bundle)
            }
        }

        tv_logo_agreement.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(WebViewActivity.TYPE, WebViewActivity.NO_HEAR_HTTP_TYPE)
            bundle.putString(WebViewActivity.URL, getString(R.string.agreement_yinsi_html))
            jumpTo(WebViewActivity::class.java, bundle, "隐私协议")
        }
    }

}