package com.zzzh.akhalteke.ui.home.find

import android.os.Bundle
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.adapter.ShipperGoodinfomAdapter
import com.zzzh.akhalteke.bean.vo.DayGoodListVo
import com.zzzh.akhalteke.bean.vo.DayInfomVo
import com.zzzh.akhalteke.mvp.contract.DayGoodContract
import com.zzzh.akhalteke.mvp.model.DayGoodModel
import com.zzzh.akhalteke.mvp.presenter.DayGoodPresenter
import com.zzzh.akhalteke.ui.BaseActivity
import kotlinx.android.synthetic.main.gm_refresh.*

/**
 * @Author : YFL  is Creating a porject in com.zzzh.akhalteke.ui.findgood
 * @Package com.zzzh.akhalteke.ui.findgood
 * @Email : yufeilong92@163.com
 * @Time :2019/3/19 10:22
 * @Purpose :当天获取源
 */
class DayGoodActivity : BaseActivity(), DayGoodContract.View {

    private var isRefrsh: Boolean = false;
    private var mArrayList: MutableList<DayInfomVo>? = null
    private var mPage: Int = 0
    private var mPresenter: DayGoodPresenter? = null
    private var mShipperId: String? = null
    private var mAdapter: ShipperGoodinfomAdapter? = null

    companion object {
        val SHIPPER_ID: String = "Shipperid"
    }

    override fun setContentView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_day_good)
        if (intent != null) {
            mShipperId = intent.getStringExtra(SHIPPER_ID)
        }
        initRefresh()
        clearList()
        initAdapter()
        initRequest()
        gm_SmartRefreshLayout.autoRefresh()
    }

    private fun initRequest() {
        mPresenter = DayGoodPresenter()
        mPresenter!!.initMvp(this, model = DayGoodModel())
    }

    fun initAdapter() {
        setMangager(gm_rlv_content)
        mAdapter = ShipperGoodinfomAdapter(mContext, mArrayList!!)
        gm_rlv_content.adapter = mAdapter

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

    private fun loadMoreData() {
        if (isRefrsh) {
            return
        }
        isRefrsh = true
        mPresenter!!.requestShipperGoodTwo(mContext, mShipperId!!, mPage)
    }

    private fun loadNewData() {
        if (isRefrsh) {
            return
        }
        isRefrsh = true
        mPresenter!!.requestShipperGoodOne(mContext, mShipperId!!, mPage)
    }


    private fun clearList() {
        if (mArrayList == null) {
            mArrayList = mutableListOf()
        } else {
            mArrayList!!.clear()
        }
    }

    private fun addList(list: MutableList<DayInfomVo>) {
        if (list == null || list.isEmpty()) return
        if (mArrayList == null) {
            clearList()
        }
        mArrayList!!.addAll(list)

    }

    override fun ShipperGoodOneSuccess(t: Any?) {
        isRefrsh = false
        gm_SmartRefreshLayout.finishRefresh()
        val data: DayGoodListVo = t as DayGoodListVo
        if (data.list == null || data.list.isEmpty()) {
            gm_SmartRefreshLayout.setEnableLoadMore(false)
            gm_SmartRefreshLayout.setEnableRefresh(true)
            mAdapter!!.setEmptyView(R.layout.gm_refresh_empty, gm_rlv_content)
            mAdapter!!.notifyDataSetChanged()
            return
        }
        mPage = data.pageInfo!!.page+ 1
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

    override fun ShipperGoodOneError(ex: Throwable) {
        isRefrsh = false
        gm_SmartRefreshLayout.finishRefresh()
        this.onError(ex)
    }

    override fun ShipperGoodOneComplise() {
        isRefrsh = false
        gm_SmartRefreshLayout.finishRefresh()
        this.onComplise()
    }

    override fun ShipperGoodTwoSuccess(t: Any?) {
        isRefrsh = false
        gm_SmartRefreshLayout.finishLoadMore()
        val data: DayGoodListVo = t as DayGoodListVo
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

    override fun ShipperGoodTwoError(ex: Throwable) {
        isRefrsh = false
        gm_SmartRefreshLayout.finishLoadMore()
        this.onError(ex)
    }

    override fun ShipperGoodTwoComplise() {
        isRefrsh = false
        gm_SmartRefreshLayout.finishLoadMore()
        this.onComplise()
    }


}
