package com.zzzh.akhalteke.ui.me

import android.os.Bundle
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.adapter.MsgAdapter
import com.zzzh.akhalteke.adapter.TiMoneyListAdapter
import com.zzzh.akhalteke.bean.DataMessageVo
import com.zzzh.akhalteke.bean.vo.OrderInfomVo
import com.zzzh.akhalteke.bean.vo.TiXianItemVo
import com.zzzh.akhalteke.bean.vo.TiXianListVo
import com.zzzh.akhalteke.mvp.contract.TiMoneyListContract
import com.zzzh.akhalteke.mvp.model.TiMoneyListModel
import com.zzzh.akhalteke.mvp.presenter.MsgPresenter
import com.zzzh.akhalteke.mvp.presenter.TiMoneyListPresenter
import com.zzzh.akhalteke.ui.BaseActivity
import com.zzzh.akhalteke.utils.Utils
import kotlinx.android.synthetic.main.gm_refresh.*
import kotlinx.android.synthetic.main.gm_rl_title_title.*

/**
 * @Author : YFL  is Creating a porject in com.zzzh.akhalteke.ui.me
 * @Package com.zzzh.akhalteke.ui.me
 * @Email : yufeilong92@163.com
 * @Time :2019/3/22 17:57
 * @Purpose :提现详情
 */
class TiMoneyListActivity : BaseActivity(), TiMoneyListContract.View {


    var mPresenter: TiMoneyListPresenter? = null
    private var isRefrsh: Boolean = false;
    private var mArrayList: MutableList<TiXianItemVo>? = null
    private var mPage: Int = 0
    private var mAdapter: TiMoneyListAdapter? = null
    override fun setContentView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_ti_money_list)
        tv_gm_right_title.text = getString(R.string.contact_service)
        initEvent()
        initRefresh()
        clearList()
        initAdapter()
        initRequest()
        gm_SmartRefreshLayout.autoRefresh()
    }
    fun initAdapter(){
        setMangager(gm_rlv_content)
        mAdapter= TiMoneyListAdapter(mContext,mArrayList!!)
        gm_rlv_content.adapter=mAdapter
    }
    fun initRequest() {
        mPresenter = TiMoneyListPresenter()
        mPresenter!!.initMvp(this, model = TiMoneyListModel())
    }
    fun initRefresh() {
        gm_SmartRefreshLayout.apply {
            setOnRefreshListener {
                loadNewData()
            }
            setOnLoadMoreListener {
                loadMoreData()
            }
        }

    }
    private fun loadNewData() {
        if (isRefrsh) {
            return
        }
        isRefrsh = true
        mPresenter!!.requestTiMoneyListOne(mContext, DataMessageVo.REFRESH_STARTPAGE)
    }

    private fun loadMoreData() {
        if (isRefrsh) {
            return
        }
        isRefrsh = true
        mPresenter!!.requestTiMoneyListTwo(mContext,  mPage)
    }
    private fun clearList() {
        if (mArrayList == null) {
            mArrayList = mutableListOf()
        } else {
            mArrayList!!.clear()
        }
    }

    private fun addList(list: MutableList<TiXianItemVo>) {
        if (list == null || list.isEmpty()) return
        if (mArrayList == null) {
            clearList()
        }
        mArrayList!!.addAll(list)

    }
    fun initEvent() {
        tv_gm_right_title.setOnClickListener {
            Utils.playPhone(mContext, DataMessageVo.KEFU_PHONE)
        }
    }

    override fun OneSuccess(t: Any?) {
        isRefrsh = false
        gm_SmartRefreshLayout.finishRefresh()
        if (t==null)return
        val data: TiXianListVo = t as TiXianListVo
        clearList()
        if (data.list == null || data.list.isEmpty()) {
            gm_SmartRefreshLayout.setEnableLoadMore(false)
            gm_SmartRefreshLayout.setEnableRefresh(true)
            mAdapter!!.setEmptyView(R.layout.gm_refresh_empty, gm_rlv_content)
            mAdapter!!.notifyDataSetChanged()
            return
        }
        mPage = data.pageInfo!!.page + 1
        addList(data.list)
        if (mArrayList!!.size == data.pageInfo.total) {
            gm_SmartRefreshLayout.setEnableLoadMore(false)
            gm_SmartRefreshLayout.setEnableRefresh(true)
        } else {
            gm_SmartRefreshLayout.setEnableLoadMore(true)
            gm_SmartRefreshLayout.setEnableRefresh(true)
        }
        mAdapter!!.notifyDataSetChanged()

    }

    override fun OneError(ex: Throwable) {
        isRefrsh = false
        gm_SmartRefreshLayout.finishRefresh()
        this.onError(ex)
    }

    override fun OneComplise() {
        isRefrsh = false
        gm_SmartRefreshLayout.finishRefresh()
        this.onComplise()
    }

    override fun TwoSuccess(t: Any?) {
        isRefrsh = false
        gm_SmartRefreshLayout.finishLoadMore()
        if (t==null)return
        val data: TiXianListVo = t as TiXianListVo
        if (data.list == null || data.list.isEmpty()) {
            gm_SmartRefreshLayout.setEnableLoadMore(false)
            gm_SmartRefreshLayout.setEnableRefresh(true)
            mAdapter!!.notifyDataSetChanged()
            return
        }
        addList(data.list)
        if (mArrayList!!.size == data.pageInfo!!.total) {
            gm_SmartRefreshLayout.setEnableLoadMore(false)
            gm_SmartRefreshLayout.setEnableRefresh(true)
        } else {
            gm_SmartRefreshLayout.setEnableLoadMore(true)
            gm_SmartRefreshLayout.setEnableRefresh(true)
        }
        mAdapter!!.notifyDataSetChanged()
    }

    override fun TwoError(ex: Throwable) {
        isRefrsh = false
        gm_SmartRefreshLayout.finishLoadMore()
        this.onError(ex)
    }

    override fun TwoComplise() {
        isRefrsh = false
        gm_SmartRefreshLayout.finishLoadMore()
        this.onComplise()
    }

}
