package com.zzzh.akhalteke.ui.login

import android.os.Bundle
import android.os.Handler
import com.yanzhenjie.permission.Permission
import com.zzzh.akhalteke.dbHelp.UserDbHelp
import com.zzzh.akhalteke.dbHelp.Vo.UserDbVo
import com.zzzh.akhalteke.MainActivity
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.bean.vo.GetUserInfom
import com.zzzh.akhalteke.bean.vo.GmContentVo
import com.zzzh.akhalteke.mvp.contract.CodeContract
import com.zzzh.akhalteke.mvp.model.CodeModel
import com.zzzh.akhalteke.mvp.presenter.CodePresenter
import com.zzzh.akhalteke.service.UpdateAddressIntentService
import com.zzzh.akhalteke.ui.BaseActivity
import com.zzzh.akhalteke.ui.verify.AuthenticationActivity
import com.zzzh.akhalteke.utils.AppManager
import com.zzzh.akhalteke.utils.PermissionUtils
import com.zzzh.akhalteke.utils.Utils
import com.zzzh.akhalteke.utils.helper.GetCodeHelper
import kotlinx.android.synthetic.main.activity_code.*

/**
 * @Author : YFL  is Creating a porject in Lenovo
 * @Email : yufeilong92@163.com
 * @Time :2019/3/6 12:05
 * @Purpose :验证码
 */

class CodeActivity : BaseActivity(), CodeContract.View {

    lateinit var inputHelp: GetCodeHelper
    var mPhone = "";
    var mPresenter: CodePresenter? = null;

    override fun setContentView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_code)
        if (intent != null) {
            mPhone = intent.getStringExtra(PHONEKEY)
        }

        initView()
        initRequest()
        bindViewData()
        initEvent()

    }

    fun initRequest() {
        mPresenter = CodePresenter()
        mPresenter!!.initMvp(this, m = CodeModel())
        showProgress()
        mPresenter!!.getCode(mContext, mPhone)
//        var han = Handler()
//        han.postDelayed({
//            yanzheng("123456")
//        }, 2000)
    }


    companion object {
        val PHONEKEY: String = "phone_key";
    }

    fun initEvent() {
        inputHelp = GetCodeHelper(mContext, input_code_number, object : GetCodeHelper.InputCompterInterface {
            override fun competer(str: String) {
                Utils.hideInputMethod(this@CodeActivity)
                yanzheng(str)
            }
        })
    }

    fun bindViewData() {
        tv_phone_title.setText(mPhone)
    }

    fun initView() {
        iv_back.setOnClickListener {
            finishBase()
        }
    }


    //验证验证码
    fun yanzheng(str: String) {
        showProgress()
        mPresenter!!.checkCode(mContext, mPhone, str)

    }

    override fun CodeError(ex: Throwable) {
        this.onError(ex)
    }

    override fun CodeCompter() {
        this.onComplise()
    }

    override fun CodeSuccess(t: Any?) {
        showToast(getString(R.string.request_code))
    }

    override fun CheckCodeSuccess(t: Any?) {
        var userinfom: GetUserInfom = t as GetUserInfom
        addDbEvent(userinfom)
        var userVo: UserDbVo = UserDbVo()
        userVo.phone = mPhone
        userVo.userId = userinfom.id!!
        userVo.token = userinfom.token!!
        GmContentVo.setUserInfom(userVo)
//        if (userinfom.ifRealCertification != "1") {//未实名认证
//            jumpTo(AuthenticationActivity::class.java)
//            return
//        }
//        if (userinfom.ifDriver != "1") {//未驾驶员认证
//            jumpTo(DrivingMemberActivity::class.java)
//            return
//        }
//        if (userinfom.ifCar != "1") {
//            jumpTo(CarActivity::class.java)
//            return
//        }
        jumpTo(MainActivity::class.java)
        val app: AppManager = AppManager.appManager
        app.finishActivity(LoginActivity::class.java)
        app.finishActivity(CodeActivity::class.java)
    }

    private fun addDbEvent(userinfom: GetUserInfom) {
        val mUserHelp = UserDbHelp.get_Instance(mContext)
        mUserHelp!!.addUseInfom(
                userinfom.id, userinfom.token,
                userinfom.phone, userinfom.ifRealCertification,
                userinfom.ifDriver,
                userinfom.ifCar, userinfom.name,
                userinfom.number, userinfom.portrait, userinfom.plateNumber, userinfom.role
                , userinfom.carPlateColourId
        )
    }

    override fun CheckCodeError(ex: Throwable) {
        this.onError(ex)
    }

    override fun CheckCodeCompter() {
        this.onComplise()
    }

}