package com.zzzh.akhalteke.ui.me

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.WebViewActivity
import com.zzzh.akhalteke.bean.vo.BankListVo
import com.zzzh.akhalteke.mvp.contract.TiXianContract
import com.zzzh.akhalteke.mvp.model.TiXianModel
import com.zzzh.akhalteke.mvp.presenter.TiXianPresenter
import com.zzzh.akhalteke.ui.BaseActivity
import com.zzzh.akhalteke.utils.*
import kotlinx.android.synthetic.main.activity_ti_money.*
import kotlinx.android.synthetic.main.gm_rl_title_title.*

/**
 * @Author : YFL  is Creating a porject in com.zzzh.akhalteke.ui.money
 * @Package com.zzzh.akhalteke.ui.money
 * @Email : yufeilong92@163.com
 * @Time :2019/3/14 17:37
 * @Purpose :提现界面
 */
class TiMoneyActivity : BaseActivity(), TiXianContract.View {

    companion object {
        var MONEY_NUMBER = "money_number"
        var SELECT_KEY = "select_key"
        var REQUEST_CODE = 1000
        var RESULT_CODE = 10001
        var MONEY: String = "money"
    }

    private var mMoney: String = "0"
    private var mBankID: String = ""

    private var mPresenter: TiXianPresenter? = null
    override fun setContentView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_ti_money)
        if (intent != null) {
            mMoney = intent.getStringExtra(MONEY_NUMBER)
        }
        tv_activity_title.text = getString(R.string.tixian)
        tv_gm_right_title.text = getString(R.string.tixian_log)
        bingViewData()
        initEvent()
        initReqeust()
    }

    fun initReqeust() {
        mPresenter = TiXianPresenter()
        mPresenter!!.initMvp(this, model = TiXianModel())
        mPresenter!!.requestBanlList(mContext)

    }

    fun bingViewData() {
        tv_tixian_money_number.text = Utils.addMoneyComma(mMoney)
    }

    fun initEvent() {
        tv_tixian_agreement.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(WebViewActivity.TYPE, WebViewActivity.NO_HEAR_HTTP_TYPE)
            bundle.putString(WebViewActivity.URL, getString(R.string.agreement_tixaincash_html))
            jumpTo(WebViewActivity::class.java, bundle, "提现条约")
        }
        tv_gm_right_title.setOnClickListener {
            jumpTo(TiMoneyListActivity::class.java, getString(R.string.tixian_log))
        }
        btn_ti_money_sure.setOnClickListener {
            submiteTiXian()
        }
        ll_tixian_bank.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(BankListActivity.TYPE, BankListActivity.SELECT_TYPE)
            jumpToFoResulBU(BankListActivity::class.java, bundle, getString(R.string.please_select_bank), REQUEST_CODE)
        }
        tv_ti_money_select.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(BankListActivity.TYPE, BankListActivity.SELECT_TYPE)
            jumpToFoResulBU(BankListActivity::class.java, bundle, getString(R.string.please_select_bank), REQUEST_CODE)

        }
        tv_tixian_all.setOnClickListener {
            val money = Utils.getObjToStr(tv_tixian_money_number)
            et_timoney_input_number.setText(money)
        }
        et_timoney_input_number.addTextChangedListener(MoneyTextWatcher(et_timoney_input_number))
    }

    fun submiteTiXian() {
        if (StringUtil.isEmpty(mBankID)) {
            showToast(getString(R.string.please_select_bank))
            return
        }
        val text = et_timoney_input_number.text.toString().trim()
        val checked = chb_tixina_agreement.isChecked
        if (StringUtil.isEmpty(text)) {
            showToast(getString(R.string.tixian_is_null))
            return
        }
        val replace = Utils.replaceDou(text)
        val toDouble = replace.toDouble()
        if (toDouble == 0.0) {
            showToast(getString(R.string.tixian_is_zone))
            return
        }
        var yiyou = tv_tixian_money_number.text.toString().trim()
        yiyou = Utils.replaceDou(yiyou)
        val cha = yiyou.toDouble() - replace.toDouble()
        if (cha < 0) {
            showToast(getString(R.string.balance))
            return
        }
        val presenter = TiXianPresenter()
        presenter.initMvp(this, model = TiXianModel())

        if (checked) {
            showProgress()
            mPresenter!!.submiteTiXian(mContext, replace, mBankID)
        } else {
            showToast(getString(R.string.please_select_tiixian))
        }
    }

    override fun Success(t: Any?) {
        showToast(getString(R.string.submit_success_wait))
        finishBase()
    }


    override fun Error(ex: Throwable) {
        this.onError(ex)
    }

    override fun Complise() {
        this.onComplise()
    }

    override fun BankSuccess(t: Any?) {
        if (t == null) {
            tv_ti_money_select.visibility = View.VISIBLE
            return
        }
        val data = t as MutableList<BankListVo>
        if (data == null || data.isEmpty()) return
        val bank = data[0]
        bindBankViewData(bank)
    }

    private fun bindBankViewData(bank: BankListVo) {
        tv_ti_money_select.visibility = View.GONE
        mBankID = bank.id!!
        val util = BankLogUtil.getInstance(mContext)
        when (bank.bank) {
            "0105" -> {//建设
                util!!.selecBankLogo(BankStatus.JIANSHE)

            }
            "0102" -> {//工商银行
                util!!.selecBankLogo(BankStatus.GONGSHANG)

            }
            "0100" -> {//邮政银行
                util!!.selecBankLogo(BankStatus.YOUZHENG)
            }
            "0303" -> {//光大银行
                util!!.selecBankLogo(BankStatus.GUANGDA)
            }
            "0309" -> {//兴业银行
                util!!.selecBankLogo(BankStatus.XINGYE)

            }
            "0104" -> {//中国银行
                util!!.selecBankLogo(BankStatus.ZHOGNGUO)
            }
            "0308" -> {//招商银行
                util!!.selecBankLogo(BankStatus.ZHAOSHANG)
            }
            "0103" -> {//农业银行
                util!!.selecBankLogo(BankStatus.NOGNYE)
            }
            "0306" -> {//,广发银行
                util!!.selecBankLogo(BankStatus.GUANGFA)
            }
            "0307" -> {//平安银行
                util!!.selecBankLogo(BankStatus.PINGAN)
            }
            "0301" -> {//交通银行
                util!!.selecBankLogo(BankStatus.JIAOTONG)
            }
            "0302" -> {//中信银行
                util!!.selecBankLogo(BankStatus.ZHONGXIN)
            }
            "0304" -> {//华夏银行
                util!!.selecBankLogo(BankStatus.HUAXIA)
            }
            "0305" -> {//民生银行
                util!!.selecBankLogo(BankStatus.MINSHENG)
            }
        }
        iv_tixian_bank_image.setImageResource(util!!.smallLogo!!)
        tv_tixain_bankname.text = util.title!!
        val buf = StringBuffer()
        buf.append(getString(R.string.tail_number))
        buf.append(bank.cardNumber)
        buf.append(getString(R.string.deposit_card))
        tv_tixain_banknuber.text = buf.toString()
    }

    override fun BankError(ex: Throwable) {
        this.onError(ex)
    }

    override fun BankComplise() {
        this.onComplise()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) return
        if (requestCode == REQUEST_CODE && RESULT_CODE == resultCode) {
            val data = data.getSerializableExtra(SELECT_KEY) as BankListVo ?: return
            bindBankViewData(data)

        }
    }
}
