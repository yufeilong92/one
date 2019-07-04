package com.zzzh.akhalteke.fragment.waybill

import android.os.Bundle
import android.view.View
import com.zzzh.akhalteke.Event.RefreshWayBillTitleEvent

import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.adapter.OrderAdapter
import com.zzzh.akhalteke.adapter.wailly.OrderNewAdapter
import com.zzzh.akhalteke.bean.DataMessageVo
import com.zzzh.akhalteke.bean.vo.MyOrderTitleVo
import com.zzzh.akhalteke.bean.vo.OrderInfom
import com.zzzh.akhalteke.bean.vo.OrderInfomVo
import com.zzzh.akhalteke.mvp.contract.OrderContract
import com.zzzh.akhalteke.mvp.model.OrderModel
import com.zzzh.akhalteke.mvp.presenter.OrderPresenter
import com.zzzh.akhalteke.ui.BaseFragment
import com.zzzh.akhalteke.ui.waybill.*
import com.zzzh.akhalteke.utils.Utils
import kotlinx.android.synthetic.main.gm_refresh.*
import org.greenrobot.eventbus.EventBus

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * @Author : YFL  is Creating a porject in com.zzzh.akhalteke.fragment.odd
 * @Package com.zzzh.akhalteke.fragment.odd
 * @Email : yufeilong92@163.com
 * @Time :2019/3/15 9:58
 * @Purpose :运输中
 */
class WaybillOneFragment : BaseFragment(), OrderContract.View {


    private var param1: String? = null
    private var param2: String? = null
    private var mPresenter: OrderPresenter? = null
    private var isRefrsh: Boolean = false;
    private var mArrayList: MutableList<OrderInfom>? = null
    private var mPage: Int = 0
    private var mAdapter: OrderNewAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun setContentView(): Int {
        return R.layout.fragment_waybill_one
    }

    override fun setCreatedContentView(view: View, savedInstanceState: Bundle?) {
        initRefresh()
        clearList()
        initAdapter()
        initRequest()
//        gm_SmartRefreshLayout.autoRefresh()
    }

    override fun onResume() {
        super.onResume()
        if (gm_SmartRefreshLayout != null) {
            gm_SmartRefreshLayout.autoRefresh()
        }
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

    fun initAdapter() {
        setMangager(gm_rlv_content)
        mAdapter = OrderNewAdapter(mContext, DataMessageVo.ORDER_STATUS_One, mArrayList!!)
        gm_rlv_content.adapter = mAdapter
        mAdapter!!.setOnClikcPlayPhoneListener(object : OrderNewAdapter.OnClickPlayPhoneItemListener {
            override fun onClickPlayPhoneItemLisener(postion: Int, str: String) {
                Utils.playPhone(mContext, str)
            }

        })
        mAdapter!!.setOnAgreementClikcListener(object : OrderNewAdapter.OnAgreementClickItemListener {
            override fun onAgreementClickItemLisener(postion: Int, id: String, status: String, ifagrement: Boolean) {
                val bundle = Bundle()
                bundle.putString(FreightNewAgreementActivity.ORDER_ID, id)
                if (ifagrement) {
                    jumpTo(CompletaNewAgreementActivity::class.java, bundle, getString(R.string.good_agreement))
                    return
                }
                jumpTo(FreightNewAgreementActivity::class.java, bundle, getString(R.string.good_agreement))
            }

        })
        mAdapter!!.setOnItemClickListener { adapter, view, position ->
            val infom = mArrayList!!.get(position)
            val bundle = Bundle()
            bundle.putString(OrderNewInfomActivity.ORDER_ID, infom.orderId)
            bundle.putString(OrderNewInfomActivity.TYPE, OrderInfomNoActivity.TYPE_ONE)
            if (infom.ifAgreement == "2") {
                jumpTo(OrderInfomNewNoActivity::class.java, bundle, getString(R.string.order_infom))
                return@setOnItemClickListener
            }
            jumpTo(OrderNewInfomActivity::class.java, bundle, getString(R.string.order_infom))

        }
    }

    private fun clearList() {
        if (mArrayList == null) {
            mArrayList = mutableListOf()
        } else {
            mArrayList!!.clear()
        }
    }

    private fun addList(list: MutableList<OrderInfom>) {
        if (list == null || list.isEmpty()) return
        if (mArrayList == null) {
            clearList()
        }
        mArrayList!!.addAll(list)

    }

    private fun loadMoreData() {
        if (isRefrsh) {
            return
        }
        isRefrsh = true
        mPresenter!!.requestOrderListTwo(mContext, DataMessageVo.ORDER_STATUS_One, mPage)
    }

    private fun loadNewData() {
        if (isRefrsh) {
            return
        }
        isRefrsh = true
        mPresenter!!.requestOrderListOne(mContext, DataMessageVo.ORDER_STATUS_One, DataMessageVo.REFRESH_STARTPAGE)
    }

    fun initRequest() {
        mPresenter = OrderPresenter()
        mPresenter!!.initMvp(this, model = OrderModel())
    }

    fun refreshTitle(str: String) {
        MyOrderTitleVo.setYunNumber(str)
        EventBus.getDefault().postSticky(RefreshWayBillTitleEvent(0))
    }

    override fun OneSuccess(t: Any?) {
        isRefrsh = false
        gm_SmartRefreshLayout.finishRefresh()
        val data: OrderInfomVo = t as OrderInfomVo
        refreshTitle(data.pageInfo!!.total.toString())
        clearList()
        if (data.list == null || data.list.isEmpty()) {
            gm_SmartRefreshLayout.setEnableLoadMore(false)
            gm_SmartRefreshLayout.setEnableRefresh(true)
            mAdapter!!.setEmptyView(R.layout.gm_refresh_empty, gm_rlv_content)
            mAdapter!!.notifyDataSetChanged()
            return
        }
        mPage = data.pageInfo.page + 1
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
        if (gm_SmartRefreshLayout != null)
            gm_SmartRefreshLayout.finishRefresh()
        this.onError(ex)
    }

    override fun OneComplise() {
        isRefrsh = false
        if (gm_SmartRefreshLayout != null)
            gm_SmartRefreshLayout.finishRefresh()
        this.onComplise()
    }

    override fun TwoSuccess(t: Any?) {
        isRefrsh = false
        gm_SmartRefreshLayout.finishLoadMore()
        val data: OrderInfomVo = t as OrderInfomVo
        refreshTitle(data.pageInfo!!.total.toString())
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
        if (gm_SmartRefreshLayout != null)
            gm_SmartRefreshLayout.finishLoadMore()
        this.onError(ex)
    }

    override fun TwoComplise() {
        isRefrsh = false
        if (gm_SmartRefreshLayout != null)
            gm_SmartRefreshLayout.finishLoadMore()
        this.onComplise()
    }

}
