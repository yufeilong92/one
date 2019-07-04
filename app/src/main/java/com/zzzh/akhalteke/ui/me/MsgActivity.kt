package com.zzzh.akhalteke.ui.me

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.adapter.MsgAdapter
import com.zzzh.akhalteke.adapter.OrderAdapter
import com.zzzh.akhalteke.bean.DataMessageVo
import com.zzzh.akhalteke.bean.vo.OrderInfom
import com.zzzh.akhalteke.mvp.contract.MsgContract
import com.zzzh.akhalteke.mvp.model.MsgModel
import com.zzzh.akhalteke.mvp.presenter.MsgPresenter
import com.zzzh.akhalteke.ui.BaseActivity
import kotlinx.android.synthetic.main.gm_refresh.*

/**
 * @Author : YFL  is Creating a porject in com.zzzh.akhalteke.ui.me
 * @Package com.zzzh.akhalteke.ui.me
 * @Email : yufeilong92@163.com
 * @Time :2019/3/22 16:45
 * @Purpose :我的消息
 */
class MsgActivity : BaseActivity(), MsgContract.View {

    private var isRefrsh: Boolean = false;
    private var mArrayList: MutableList<String>? = null
    private var mPage: Int = 0
    private var mAdapter: MsgAdapter? = null
    private var mPresenter:MsgPresenter?=null
    override fun setContentView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_msg)
        initRefresh()
        clearList()
        initAdapter()
        initRequest()
        gm_SmartRefreshLayout.autoRefresh()
    }

    fun initAdapter(){
        setMangager(gm_rlv_content)
        mAdapter= MsgAdapter(mContext,mArrayList!!)
        gm_rlv_content.adapter=mAdapter
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
        mPresenter!!.requestMsgListOne(mContext, DataMessageVo.REFRESH_STARTPAGE)
    }

    private fun loadMoreData() {
        if (isRefrsh) {
            return
        }
        isRefrsh = true
        mPresenter!!.requestMsgListTwo(mContext,  mPage)
    }

    fun initRequest(){
        mPresenter= MsgPresenter()
        mPresenter!!.initMvp(this,model = MsgModel())


    }
    private fun clearList() {
        if (mArrayList == null) {
            mArrayList = mutableListOf()
        } else {
            mArrayList!!.clear()
        }
    }

    private fun addList(list: MutableList<String>) {
        if (list == null || list.isEmpty()) return
        if (mArrayList == null) {
            clearList()
        }
        mArrayList!!.addAll(list)

    }
    override fun OneSuccess(t: Any?) {
        isRefrsh=false
        gm_SmartRefreshLayout.finishRefresh()
        gm_SmartRefreshLayout.setEnableLoadMore(false)
        mAdapter!!.setEmptyView(R.layout.gm_refresh_empty, gm_rlv_content)
        mAdapter!!.notifyDataSetChanged()
    }

    override fun OneError(ex: Throwable) {
        isRefrsh=false
        gm_SmartRefreshLayout.finishRefresh()
//        this.onError(ex)
    }

    override fun OneComplise() {
        isRefrsh=false
        gm_SmartRefreshLayout.finishRefresh()
        this.onComplise()
    }

    override fun TwoSuccess(t: Any?) {
        isRefrsh=false
        gm_SmartRefreshLayout.finishLoadMore()
        mAdapter!!.setEmptyView(R.layout.gm_refresh_empty, gm_rlv_content)
        mAdapter!!.notifyDataSetChanged()
    }

    override fun TwoError(ex: Throwable) {
        isRefrsh=false
        gm_SmartRefreshLayout.finishLoadMore()

//        this.onError(ex)
    }

    override fun TwoComplist() {
        isRefrsh=false
        gm_SmartRefreshLayout.finishLoadMore()
        this.onComplise()
    }

}
