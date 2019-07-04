package com.zzzh.akhalteke.ui.me

import android.content.Context
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Toast
import com.zzzh.akhalteke.BaseApplication
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.bean.vo.CheckCarTypeVo
import com.zzzh.akhalteke.dbHelp.UserDbHelp
import com.zzzh.akhalteke.mvp.contract.BankAddContract
import com.zzzh.akhalteke.mvp.model.BankAddModel
import com.zzzh.akhalteke.mvp.presenter.BankAddPresenter
import com.zzzh.akhalteke.ui.BaseActivity
import com.zzzh.akhalteke.utils.DialogUtil
import com.zzzh.akhalteke.utils.StringUtil
import com.zzzh.akhalteke.utils.Utils
import kotlinx.android.synthetic.main.activity_bank_add.*

/**
 * @Author : YFL  is Creating a porject in com.zzzh.akhalteke.ui.me
 * @Package com.zzzh.akhalteke.ui.me
 * @Email : yufeilong92@163.com
 * @Time :2019/3/15 14:01
 * @Purpose :银行添加界面
 */
class BankAddActivity : BaseActivity(), BankAddContract.View {

    private var mPresenter: BankAddPresenter? = null
    private var mDialogUtil: DialogUtil? = null
    private var mName: String? = ""
    private var mIdnumber: String? = ""
    private var mPhone: String? = ""
    private var mCarnumber: String? = ""
    override fun setContentView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_bank_add)
        initEvent()
        initUtils()
        initRequest()
        bindViewData()

    }

    fun initUtils() {
        mDialogUtil = DialogUtil
    }

    fun initRequest() {
        mPresenter = BankAddPresenter()
        mPresenter!!.initMvp(this, model = BankAddModel())
    }

    fun initEvent() {
        ll_addbank_showUserInfon.setOnClickListener {
            ll_addbank_showUserInfon.visibility = View.GONE
            et_addbank_carnumber.visibility = View.VISIBLE
            Utils.showSoftInputFromWindow(this@BankAddActivity, et_addbank_carnumber)
        }

        tv_addbank_waring.setOnClickListener {
            DialogUtil.showAddWaringDialog(mContext, getString(R.string.chikairen), getString(R.string.addbank_warning))
        }
        et_addbank_carnumber.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {//聚焦
            } else {//失焦
                val trim = et_addbank_carnumber.text.trim().toString().trim()
                if (StringUtil.isEmpty(trim)) {
                    et_addbank_carnumber.isFocusable = false
                    et_addbank_carnumber.isFocusableInTouchMode = false
                    ll_addbank_showUserInfon.visibility = View.VISIBLE
                    et_addbank_carnumber.visibility = View.GONE
                    return@setOnFocusChangeListener
                }
            }
        }

    }

    fun bindViewData() {
        val vo = UserDbHelp.get_Instance(mContext)!!.getUserInfom()
        if (vo == null) {
            BaseApplication.toInstance().startActivty(mContext)
            return
        }
        tv_addbank_username.text = vo.name
        tv_addbank_name.text = vo.name
        tv_addbank_idnumber.text = vo.idnumber
        val content = getString(R.string.agreement_content)
        val spankString = SpannableString(content)
        spankString.setSpan(Spank(mContext), 12, content.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        tv_addbank_xieyi.text = spankString
        tv_addbank_xieyi.movementMethod = LinkMovementMethod.getInstance()
        btn_add_bank_next.setOnClickListener {
            if (!Utils.handleOnDoubleClick()) {
                submitBankInfom(vo.name, vo.idnumber)
            }
        }
    }


    fun submitBankInfom(name: String, idnumber: String) {
        mName = name
        mIdnumber = idnumber
        var carnumber = Utils.getObjToStr(et_addbank_carnumber)

        if (StringUtil.isEmpty(carnumber)) {
            showToast(getString(R.string.please_bank_number))
            return
        }

        mCarnumber = carnumber.replace(" ", "", true)
        mPhone = Utils.getObjToStr(et_addbank_phone)
        if (StringUtil.isEmpty(mPhone!!)) {
            showToast(getString(R.string.please_yuliu_phone))
            return
        }
        if (!Utils.isPhoneNum(mPhone!!)) {
            showToast(getString(R.string.please_right_phone))
            return
        }

//        val checked = chb_addbank_yudu.isChecked
//        if (!checked) {
//            showToast(getString(R.string.please_agreement))
//            return
//        }

        showProgress()
        val trim = et_addbank_carnumber.text.toString().trim()
        val last = trim.replace(" ", "", true)
        mPresenter!!.submitCarType(mContext, last)


    }


    class Spank(var context: Context) : ClickableSpan() {
        override fun onClick(widget: View) {
            Toast.makeText(context, "22", Toast.LENGTH_SHORT).show()
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.color = context.resources.getColor(R.color.gm_color)
            ds.isUnderlineText = false
            ds.clearShadowLayer()
        }
    }

    override fun Success(t: Any?) {
        mDialogUtil!!.showAddBankSuccesse(mContext, true, object : DialogUtil.addBankAgainInterface {
            override fun bankAgainListener() {
                finishBase()
            }
        })

    }

    override fun Error(ex: Throwable) {
        this.onError(ex)
//        mDialogUtil!!.showAddBankSuccesse(mContext, false, object : DialogUtil.addBankAgainInterface {
//            override fun bankAgainListener() {
//                et_addbank_carnumber.text = null
//                et_addbank_phone.text = null
//                ll_addbank_showUserInfon.visibility = View.VISIBLE
//                et_addbank_carnumber.visibility = View.GONE
//            }
//        })
    }
    override fun Complise() {
        this.onComplise()
    }

    override fun CarTypeError(ex: Throwable) {
        this.onError(ex)
    }
    override fun CarTypeSuccess(t: Any?) {
        val data: String = t as String
        mPresenter!!.submitBankAddd(mContext, mName!!, mIdnumber!!, mCarnumber!!, mPhone!!, data)

    }
    override fun CarTypeComplise() {
//        this.Complise()
    }

}
