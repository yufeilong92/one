package com.zzzh.akhalteke.ui.me

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.adapter.BankListAdpater
import com.zzzh.akhalteke.bean.vo.BankListVo
import com.zzzh.akhalteke.mvp.contract.BankListContract
import com.zzzh.akhalteke.mvp.model.BankListModel
import com.zzzh.akhalteke.mvp.presenter.BankListPresenter
import com.zzzh.akhalteke.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_bank_list.*
import kotlinx.android.synthetic.main.gm_refresh.*
import kotlinx.android.synthetic.main.gm_rl_title_title.*

/**
 * @Author : YFL  is Creating a porject in com.zzzh.akhalteke.ui.me
 * @Package com.zzzh.akhalteke.ui.me
 * @Email : yufeilong92@163.com
 * @Time :2019/3/15 14:00
 * @Purpose :银行卡管理界面
 */
class BankListActivity : BaseActivity(), BankListContract.View {


    private var mPresenter: BankListPresenter? = null

    companion object {
        val TYPE: String = "type"
        val SHOW_TYPE: String = "showtype"
        val SELECT_TYPE: String = "selecttype"
    }

    private var mType: String? = ""
    override fun setContentView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_bank_list)
        if (intent != null) {
            mType = intent.getStringExtra(TYPE)
        }
        tv_gm_right_title.text = getString(R.string.add_bank)
        initRequest()
        initEvent()

    }

    private fun setShowAdd(isShow: Boolean) {
        tv_banklist_add_list.visibility = if (isShow) View.GONE else View.VISIBLE
        tv_gm_right_title.visibility = if (isShow) View.VISIBLE else View.GONE
    }

    private fun initEvent() {
        tv_banklist_add_list.setOnClickListener {
            val bunder = Bundle()
            jumpTo(BankAddActivity::class.java, bunder, getString(R.string.add_bank))
        }
        tv_gm_right_title.setOnClickListener {
            val bunder = Bundle()
            jumpTo(BankAddActivity::class.java, bunder, getString(R.string.add_bank))
        }
    }

    fun initRequest() {
        mPresenter = BankListPresenter()
        mPresenter!!.initMvp(this, model = BankListModel())
        showProgress()
        mPresenter!!.requestBankList(mContext)

    }

    override fun BankListSuccess(t: Any?) {
        val data = t as MutableList<BankListVo>
        if (data == null || data.isEmpty()) {
            setShowAdd(true)
        } else {
            setShowAdd(false)
        }
        setMangager(rlv_banklist_content)
        val adapter = BankListAdpater(mContext, data)
        rlv_banklist_content.adapter = adapter
        adapter!!.setEmptyView(R.layout.gm_refresh_bankempty, rlv_banklist_content)
        adapter.setOnClikcDeleteBankListener(object : BankListAdpater.OnClickDeleteBankItemListener {
            override fun onClickDeleteBankItemLisener(postion: Int, id: String, str: String) {
                showProgress()
                mPresenter!!.submitDeleteBankCar(mContext, id)
            }

        })
        adapter.setOnItemClickListener { adapter, view, position ->
            when (mType) {
                SHOW_TYPE -> {

                }
                SELECT_TYPE -> {
                    val intent = Intent()
                    intent.putExtra(TiMoneyActivity.SELECT_KEY, data[position])
                    setResult(TiMoneyActivity.RESULT_CODE, intent)
                    finishBase()
                }
            }

        }
    }

    override fun onRestart() {
        super.onRestart()
        showProgress()
        mPresenter!!.requestBankList(mContext)
    }

    override fun BankListError(ex: Throwable) {
        this.onError(ex)
    }

    override fun BankListComplise() {
        this.onComplise()
    }

    override fun DeleteBankComplise() {
        this.onComplise()
    }

    override fun DeleteBankError(ex: Throwable) {
        this.onError(ex)
    }

    override fun DeleteBankSuccess(t: Any?) {
        showToast(getString(R.string.remover_success))
        mPresenter!!.requestBankList(mContext)
    }
}
