package com.zzzh.akhalteke.ui.me

import android.os.Bundle
import com.yanzhenjie.permission.Permission
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.adapter.MyMoneyAdapter
import com.zzzh.akhalteke.bean.DataMessageVo
import com.zzzh.akhalteke.bean.vo.GetUserInfom
import com.zzzh.akhalteke.bean.vo.MyMoneyTiXianData
import com.zzzh.akhalteke.bean.vo.MyMoneyTiXianVo
import com.zzzh.akhalteke.dbHelp.UserDbHelp
import com.zzzh.akhalteke.mvp.contract.CheckInfomContract
import com.zzzh.akhalteke.mvp.contract.MyMoneyContract
import com.zzzh.akhalteke.mvp.model.CheckInfomModel
import com.zzzh.akhalteke.mvp.model.MyMoneyModel
import com.zzzh.akhalteke.mvp.presenter.CheckInfomPresenter
import com.zzzh.akhalteke.mvp.presenter.MyMoneyPresenter
import com.zzzh.akhalteke.ui.BaseActivity
import com.zzzh.akhalteke.utils.PermissionUtils
import com.zzzh.akhalteke.utils.StringUtil
import com.zzzh.akhalteke.utils.Utils
import kotlinx.android.synthetic.main.activity_my_money.*

/**
 * @Author : YFL  is Creating a porject in com.zzzh.akhalteke.ui.user
 * @Package com.zzzh.akhalteke.ui.user
 * @Email : yufeilong92@163.com
 * @Time :2019/3/14 17:00
 * @Purpose :我的钱包
 */

class MyMoneyActivity : BaseActivity(), MyMoneyContract.View, CheckInfomContract.View {


    companion object {
        val MONEY_NUMBER = "money_number"

    }

    private var mArrayList: MutableList<MyMoneyTiXianData>? = null
    private var mPage: Int = 0
    private var mMoney: String = ""
    private var mAdapter: MyMoneyAdapter? = null
    private var mPresenter: MyMoneyPresenter? = null
    private var mCheckInfomPresenter: CheckInfomPresenter? = null
    override fun setContentView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_my_money)
//        if (intent != null) {
//            mMoney = intent.getStringExtra(MONEY_NUMBER)
//        }
        bindViewData()
        initEvent()
        initRefresh()
        clearList()
        initAdapter()
        initRequest()
    }

    fun initRequest() {
        mPresenter = MyMoneyPresenter()
        mPresenter!!.initMvp(this, model = MyMoneyModel())
        showProgress()
        mPresenter!!.requestCenterAccountOne(mContext, DataMessageVo.REFRESH_STARTPAGE)
        mCheckInfomPresenter = CheckInfomPresenter()
        mCheckInfomPresenter!!.initMvp(this, CheckInfomModel())

    }

    fun bindViewData() {
        tv_timoney_number.text = Utils.addMoneyComma(mMoney)
    }

    override fun onRestart() {
        super.onRestart()
        showProgress()
        mPresenter!!.requestCenterAccountOne(mContext, DataMessageVo.REFRESH_STARTPAGE)
    }

    fun initRefresh() {
        mymoney_gm_SmartRefreshLayout.setEnableLoadMore(true)
        mymoney_gm_SmartRefreshLayout.setEnableRefresh(false)
        mymoney_gm_SmartRefreshLayout.apply {
            setOnLoadMoreListener {
                loadMoreData()
            }
        }

    }

    private fun loadMoreData() {
        mPresenter!!.requestCenterAccountTwo(mContext, mPage)
    }

    fun initAdapter() {
        setMangager(rlv_mymoneygm_content)
        mAdapter = MyMoneyAdapter(mContext, mArrayList!!)
        rlv_mymoneygm_content.adapter = mAdapter
    }

    fun initEvent() {

        tv_money_bank.setOnClickListener {
            //银行卡管理
            val bundle = Bundle()
            jumpTo(BankListActivity::class.java, bundle, getString(R.string.bank_manager))
        }

        rl_mymoney_tixian.setOnClickListener {
            //提现
            if (StringUtil.isEmpty(mMoney)) {
                showToast(getString(R.string.please_agren_loadd))
                return@setOnClickListener
            }
            PermissionUtils.showPermission(mContext, "",
                    arrayOf(Permission.ACCESS_COARSE_LOCATION)) {
                showProgress()
                mCheckInfomPresenter!!.requestCheckInfom(mContext)
            }
//            val bundle = Bundle()
//            bundle.putString(TiMoneyActivity.MONEY_NUMBER, mMoney)
//            jumpTo(TiMoneyActivity::class.java, bundle, getString(R.string.tixian))
        }
    }

    override fun OneSuccess(t: Any?) {
        mymoney_gm_SmartRefreshLayout.finishLoadMore()
        if (t == null) return
        val data: MyMoneyTiXianVo = t as MyMoneyTiXianVo
        tv_timoney_number.text = data.usableAmount
        mMoney = data.usableAmount
        clearList()
        if (data.list == null || data.list.isEmpty()) {
            mymoney_gm_SmartRefreshLayout.setEnableLoadMore(false)
            mAdapter!!.setEmptyView(R.layout.gm_refresh_empty, mymoney_gm_SmartRefreshLayout)
            mAdapter!!.notifyDataSetChanged()
            return
        }
        mPage = data.pageInfo!!.page + 1
        addList(data.list)
        if (mArrayList!!.size == data.pageInfo.total) {
            mymoney_gm_SmartRefreshLayout.setEnableLoadMore(false)
        } else {
            mymoney_gm_SmartRefreshLayout.setEnableLoadMore(true)
        }
        mAdapter!!.notifyDataSetChanged()

    }

    override fun OneError(ex: Throwable) {
        this.onError(ex)
    }

    override fun OneComplise() {
        this.onComplise()
    }

    override fun TwoSuccess(t: Any?) {
        if (t == null) return
        mymoney_gm_SmartRefreshLayout.finishLoadMore()
        val data: MyMoneyTiXianVo = t as MyMoneyTiXianVo
        if (data.list == null || data.list.isEmpty()) {
            mymoney_gm_SmartRefreshLayout.setEnableLoadMore(false)
            mAdapter!!.notifyDataSetChanged()
            return
        }
        addList(data.list!!)
        if (mArrayList!!.size == data.pageInfo!!.total) {
            mymoney_gm_SmartRefreshLayout.setEnableLoadMore(false)
        } else {
            mymoney_gm_SmartRefreshLayout.setEnableLoadMore(true)
        }
        mAdapter!!.notifyDataSetChanged()

    }

    override fun TwoError(ex: Throwable) {
        this.onError(ex)
    }

    override fun TwoComplise() {
        this.onComplise()
    }

    private fun clearList() {
        if (mArrayList == null) {
            mArrayList = mutableListOf()
        } else {
            mArrayList!!.clear()
        }
    }

    private fun addList(list: MutableList<MyMoneyTiXianData>) {
        if (list == null || list.isEmpty()) return
        if (mArrayList == null) {
            clearList()
        }
        mArrayList!!.addAll(list)

    }

    override fun CheckSuccess(t: Any?) {
        if (t == null) return
        var userinfom: GetUserInfom = t as GetUserInfom
        updateUserInfom(userinfom)
        showGoRenZhen(userinfom, object : BaseActivity.ContinueDoEvent {
            override fun doEvent() {
                initBaidu()
                val bundle = Bundle()
                bundle.putString(TiMoneyActivity.MONEY_NUMBER, mMoney)
                jumpTo(TiMoneyActivity::class.java, bundle, getString(R.string.tixian))
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
