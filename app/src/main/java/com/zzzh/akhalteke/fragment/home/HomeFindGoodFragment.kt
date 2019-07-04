package com.zzzh.akhalteke.fragment.home

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.widget.ImageView
import com.zzzh.akhalteke.MainActivity
import com.zzzh.akhalteke.R

import com.zzzh.akhalteke.adapter.FilterAdapter
import com.zzzh.akhalteke.adapter.FindGoodListAdapter
import com.zzzh.akhalteke.adapter.Home.FindNewGoodListAdapter
import com.zzzh.akhalteke.bean.DataMessageVo
import com.zzzh.akhalteke.ui.BaseFragment
import com.zzzh.akhalteke.ui.home.find.FiltrateActivity
import com.zzzh.akhalteke.ui.home.find.SelectCityActivity
import com.zzzh.akhalteke.utils.StringUtil
import kotlinx.android.synthetic.main.fragment_find_good.*
import kotlinx.android.synthetic.main.gm_refresh.*
import com.zzzh.akhalteke.bean.vo.FindGoodVo
import com.zzzh.akhalteke.bean.vo.GetUserInfom
import com.zzzh.akhalteke.bean.vo.GoodItem
import com.zzzh.akhalteke.bean.vo.RlvSelectVo
import com.zzzh.akhalteke.dbHelp.UserDbHelp
import com.zzzh.akhalteke.mvp.contract.CheckInfomContract
import com.zzzh.akhalteke.mvp.contract.FindGoodContract
import com.zzzh.akhalteke.mvp.model.CheckInfomModel
import com.zzzh.akhalteke.mvp.model.FindGoodModel
import com.zzzh.akhalteke.mvp.presenter.CheckInfomPresenter
import com.zzzh.akhalteke.mvp.presenter.FindGoodPresenter
import com.zzzh.akhalteke.ui.home.find.InfomActivity
import com.zzzh.akhalteke.ui.home.find.InfomNewActivity
import com.zzzh.akhalteke.utils.Utils
import kotlinx.android.synthetic.main.gm_title.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * @Author : YFL  is Creating a porject in Lenovo
 * @Email : yufeilong92@163.com
 * @Time :2019/3/12 17:39
 * @Purpose :找货
 */

class HomeFindGoodFragment : BaseFragment(), FindGoodContract.View, CheckInfomContract.View {


    private var param1: String? = null
    private var param2: String? = null
    private var mFilterList: ArrayList<RlvSelectVo>? = arrayListOf()
    private var mSelectAdapter: FilterAdapter? = null
    private var mFilterAdpater: FindNewGoodListAdapter? = null

    private var mLoadAreaCodeParam: String = ""//卸货地址
    private var mUnloadAreaCodeParam: String = ""//卸货时间
    private var mSortTypeParam: String = "1"//排序类型
    private var mTypeParam: String = ""//类型
    private var mLoadTimeParam: String = ""//装货时间
    private var mCarLengthParam: String = ""//车长参数
    private var mCarTypeParam: String = ""//车型参数
    private var mPageParam: Int = 0
    private var mPresenter: FindGoodPresenter? = null
    private var mCheckInfomPresenter: CheckInfomPresenter? = null
    private var isRefrsh: Boolean = false;
    private var mArrayList: MutableList<GoodItem>? = null
    private var mCheckInfomType: Int = 0//默认item 点击
    private var mCheckInfomParame: String = ""//默认传递出参数

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFindGoodFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

        val REQUESTCODE = 1000//请求码
        val FAHUO_RESULT: Int = 1011//发货
        val SHOUHUO_RESULT: Int = 1012//收货
        val MOUREN_RESULT: Int = 1013//排序
        val SHAIXUAN_RESULT: Int = 1014//筛选
        val KEY_ID: String = "key"
        val VALUES_NAME: String = "values"

        val TYPE = "type"//	用车类型
        val TYPENAME = "typename"//	用车类型
        val LOADTIME = "loadTime"//装货时间
        val LOADTIMENAME = "loadTimename"//装货时间
        val CARLENGTH = "carLength"//车长
        val CARLENGTHNAME = "carLengthname"//车长
        val CARTYPE = "carType"//车型
        val CARTYPENAME = "carTypename"//车型

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun setContentView(): Int {
        return R.layout.fragment_find_good
    }

    private var mActivity: MainActivity? = null
    override fun setCreatedContentView(view: View, savedInstanceState: Bundle?) {
        tv_activity_title.text = getText(R.string.goods)
        if (activity is MainActivity) {
            mActivity = activity as MainActivity
        }
        initEvent()
        initRefresh()
        clearList()
        initAdapter()
        initRequest()
        gm_SmartRefreshLayout.autoRefresh()

    }

    fun initEvent() {
        line_one.setOnClickListener {
            //发货
            jumpToFoResulBU(SelectCityActivity::class.java, FAHUO_RESULT)
        }
        line_two.setOnClickListener {
            //收货
            jumpToFoResulBU(SelectCityActivity::class.java, SHOUHUO_RESULT)
        }
        line_three.setOnClickListener {
            //默认
        }
        line_four.setOnClickListener {
            //筛选
            jumpToFoResulBU(
                FiltrateActivity::class.java, SHAIXUAN_RESULT, FiltrateActivity.SELECTLIST
                , mFilterList!!, "筛选"
            )
        }
        tv_clear_select.setOnClickListener {
            mFilterList!!.clear()
            clearSelect()
            filterShow(false)
            gm_SmartRefreshLayout.autoRefresh()
        }
    }

    fun clearSelect() {
//        mLoadAreaCodeParam=""
//        mUnloadAreaCodeParam=""
        mSortTypeParam = "1"
        mTypeParam = ""
        mLoadTimeParam = ""
        mCarLengthParam = ""
        mCarTypeParam = ""
    }

    fun initAdapter() {
        setMangager(gm_rlv_content)
        mFilterAdpater = FindNewGoodListAdapter(mContext, mArrayList!!)
        gm_rlv_content.adapter = mFilterAdpater
        mFilterAdpater!!.setOnClikcListener(object : FindNewGoodListAdapter.OnClickItemListener {
            override fun onClickItemLisener(postion: Int, id: String) {
                if (mActivity != null) {
                    val dbHelp = UserDbHelp.get_Instance(mContext)
                    val userInfom = dbHelp!!.getUserInfom()
                    if (userInfom != null) {
                        if (userInfom.ifReal == "2") {
                            mActivity!!.showAuthentication(userInfom)
                            return
                        }
                        if (userInfom.ifDriver == "2") {
                            mActivity!!.showDriverDialog(userInfom)
                            return
                        }
                        if (userInfom.ifCar == "2") {
                            mActivity!!.showCarDialog(userInfom)
                            return
                        }
                    }
                }
                mCheckInfomType = 0
                mCheckInfomParame = id
                showProgress()
                mCheckInfomPresenter!!.requestCheckInfom(mContext)
//                val bundle = Bundle()
//                bundle.putString(InfomActivity.INFOM_ID, id)
//                jumpTo(InfomActivity::class.java, bundle, getString(R.string.infom))
            }
        })
        mFilterAdpater!!.setPhoneOnClikcListener(object : FindNewGoodListAdapter.OnPhoneClickItemListener {
            override fun onPhoneClickItemLisener(postion: Int, str: String) {
                if (mActivity != null) {
                    val dbHelp = UserDbHelp.get_Instance(mContext)
                    val userInfom = dbHelp!!.getUserInfom()
                    if (userInfom != null) {
                        if (userInfom.ifReal == "2") {
                            mActivity!!.showAuthentication(userInfom)
                            return
                        }
                        if (userInfom.ifDriver == "2") {
                            mActivity!!.showDriverDialog(userInfom)
                            return
                        }
                        if (userInfom.ifCar == "2") {
                            mActivity!!.showCarDialog(userInfom)
                            return
                        }
                    }
                }
                mCheckInfomType = 1
                mCheckInfomParame = str
                showProgress()
                mCheckInfomPresenter!!.requestCheckInfom(mContext)
//                Utils.playPhone(mContext, str)

            }

        })
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

    fun loadNewData() {
        if (isRefrsh) {
            return
        }
        isRefrsh = true
        mPageParam = 0;
        mPresenter!!.requestFindGoodListOne(
            mContext, mLoadAreaCodeParam, mUnloadAreaCodeParam, mSortTypeParam,
            mTypeParam, mLoadTimeParam, mCarLengthParam, mCarTypeParam, DataMessageVo.REFRESH_STARTPAGE
        )
    }

    fun loadMoreData() {
        if (isRefrsh) {
            return
        }
        isRefrsh = true
        mPresenter!!.requestFindGoodListTwo(
            mContext, mLoadAreaCodeParam, mUnloadAreaCodeParam, mSortTypeParam,
            mTypeParam, mLoadTimeParam, mCarLengthParam, mCarTypeParam, mPageParam
        )

    }

    fun initRequest() {
        mPresenter = FindGoodPresenter()
        mPresenter!!.initMvp(this, model = FindGoodModel())
        mCheckInfomPresenter = CheckInfomPresenter()
        mCheckInfomPresenter!!.initMvp(this, CheckInfomModel())


    }


    fun setImager(img: ImageView, id: Int) {
        when (id) {
            0 -> img.setImageResource(R.mipmap.ic_good_up)
            1 -> img.setImageResource(R.mipmap.ic_good_down)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data == null) return
        if (requestCode == FAHUO_RESULT && resultCode == REQUESTCODE) {//发货
            mLoadAreaCodeParam = data.getStringExtra(KEY_ID)
            val name = data.getStringExtra(VALUES_NAME)
            tv_good_one.text = name
            if (name == getString(R.string.nation))
                setImager(iv_good_two, 1)
            else
                setImager(iv_good_two, 0)
            gm_SmartRefreshLayout.autoRefresh()
        } else if (requestCode == SHOUHUO_RESULT && resultCode == REQUESTCODE) {//收货
            mUnloadAreaCodeParam = data.getStringExtra(KEY_ID)
            val name = data.getStringExtra(VALUES_NAME)
            tv_good_two.text = name
            if (name == getString(R.string.nation))
                setImager(iv_good_two, 1)
            else
                setImager(iv_good_two, 0)
            gm_SmartRefreshLayout.autoRefresh()
        } else if (requestCode == SHAIXUAN_RESULT && resultCode == REQUESTCODE) {//筛选
            if (mFilterList == null)
                mFilterList = arrayListOf()
            else {
                mFilterList!!.clear()
            }
            mTypeParam = data.getStringExtra(TYPE)
            mLoadTimeParam = data.getStringExtra(LOADTIME)
            mCarLengthParam = data.getStringExtra(CARLENGTH)
            mCarTypeParam = data.getStringExtra(CARTYPE)

            val typeName = data.getStringExtra(TYPENAME)
            val loadtimeName = data.getStringExtra(LOADTIMENAME)
            val carlengthName = data.getStringExtra(CARLENGTHNAME)
            val carTypeName = data.getStringExtra(CARTYPENAME)

            if (!StringUtil.isEmpty(typeName)) {
                val vo = RlvSelectVo()
                vo.name = typeName
                vo.id = mTypeParam
                vo.type = TYPE
                mFilterList!!.add(vo)
            }
            if (!StringUtil.isEmpty(loadtimeName)) {
                val vo = RlvSelectVo()
                vo.name = loadtimeName
                vo.id = mLoadTimeParam
                vo.type = LOADTIME
                mFilterList!!.add(vo)
            }
            if (!StringUtil.isEmpty(carlengthName)) {
                val vo = RlvSelectVo()
                vo.name = carlengthName
                vo.id = mCarLengthParam
                vo.type = CARLENGTH
                mFilterList!!.add(vo)
            }
            if (!StringUtil.isEmpty(carTypeName)) {
                val vo = RlvSelectVo()
                vo.name = carTypeName
                vo.id = mCarTypeParam
                vo.type = CARTYPE
                mFilterList!!.add(vo)
            }
            if (mFilterList == null || mFilterList!!.isEmpty()) {
                filterShow(false)
            } else {
                filterShow(true)
            }
            setFilterAdapter()
            gm_SmartRefreshLayout.autoRefresh()
        }

    }

    //筛选条件
    fun setFilterAdapter() {
        if (mSelectAdapter == null) {
            mSelectAdapter = FilterAdapter(mContext, mFilterList!!)
            setMangager(rlv_shaixun, 1, GridLayoutManager.HORIZONTAL)
            rlv_shaixun.adapter = mSelectAdapter
        } else {
            mSelectAdapter!!.refreshData(mFilterList!!)
            mSelectAdapter!!.notifyDataSetChanged()
        }
        mSelectAdapter!!.setOnImagerDeleteListener(object : FilterAdapter.setImagerDeleteListener {
            override fun setItemdeleteListener(item: String) {
                var index: Int = -1
                var type: String = ""
                for (id in 0 until mFilterList!!.size) {
                    val name = mFilterList!![id].name
                    if (name.equals(item)) {
                        index = id
                        type = mFilterList!![id].type!!
                        break
                    }
                }
                if (index == -1) {
                    return
                }
                mFilterList!!.removeAt(index)
                if (mFilterList == null || mFilterList!!.isEmpty()) {
                    filterShow(false)
                }
                mSelectAdapter!!.refreshData(mFilterList!!)
                mSelectAdapter!!.notifyDataSetChanged()
                when (type) {
                    CARTYPE -> {
                        mCarTypeParam = ""
                        gm_SmartRefreshLayout.autoRefresh()
                    }
                    LOADTIME -> {
                        mLoadTimeParam = ""
                        gm_SmartRefreshLayout.autoRefresh()
                    }
                    TYPE -> {
                        mTypeParam = ""
                        gm_SmartRefreshLayout.autoRefresh()
                    }
                    CARLENGTH -> {
                        mCarLengthParam = ""
                        gm_SmartRefreshLayout.autoRefresh()
                    }
                    else -> {
                        gm_SmartRefreshLayout.autoRefresh()
                    }
                }

            }

        })
    }

    fun filterShow(boolean: Boolean) {
        if (boolean) {
            ll_filter_layout.visibility = View.VISIBLE
        } else {
            ll_filter_layout.visibility = View.GONE
        }

    }

    override fun FindGoodListOneSuccess(t: Any?) {
        isRefrsh = false
        if (gm_SmartRefreshLayout != null)
            gm_SmartRefreshLayout.finishRefresh()
        val data: FindGoodVo = t as FindGoodVo
        clearList()
        if (data.list == null || data.list.isEmpty()) {
            gm_SmartRefreshLayout.setEnableLoadMore(false)
            gm_SmartRefreshLayout.setEnableRefresh(true)
            mFilterAdpater!!.setEmptyView(R.layout.gm_refresh_empty, gm_rlv_content)
            mFilterAdpater!!.notifyDataSetChanged()
            return
        }
        mPageParam = data.pageInfo!!.page + 1
        addList(data.list)
        if (mArrayList!!.size == data.pageInfo!!.total) {
            if (gm_SmartRefreshLayout != null) {
                gm_SmartRefreshLayout.setEnableLoadMore(false)
                gm_SmartRefreshLayout.setEnableRefresh(true)
            }
        } else {
            if (gm_SmartRefreshLayout != null) {
                gm_SmartRefreshLayout.setEnableLoadMore(true)
                gm_SmartRefreshLayout.setEnableRefresh(true)
            }
        }
        mFilterAdpater!!.notifyDataSetChanged()

    }

    private fun clearList() {
        if (mArrayList == null) {
            mArrayList = mutableListOf()
        } else {
            mArrayList!!.clear()
        }
    }

    private fun addList(list: MutableList<GoodItem>) {
        if (list == null || list.isEmpty()) return
        if (mArrayList == null) {
            clearList()
        }
        mArrayList!!.addAll(list)

    }

    override fun FindGoodListOneError(ex: Throwable) {
        isRefrsh = false
        if (gm_SmartRefreshLayout != null)
            gm_SmartRefreshLayout.finishRefresh()
        this.onError(ex)
    }

    override fun FindGoodListOneComplise() {
        isRefrsh = false
        if (gm_SmartRefreshLayout != null)
            gm_SmartRefreshLayout.finishRefresh()
        this.onComplise()
    }

    override fun FindGoodListTwoSuccess(t: Any?) {
        isRefrsh = false
        if (gm_SmartRefreshLayout != null)
            gm_SmartRefreshLayout.finishLoadMore()
        val data: FindGoodVo = t as FindGoodVo
        if (data.list == null || data.list.isEmpty()) {
            gm_SmartRefreshLayout.setEnableLoadMore(false)
            gm_SmartRefreshLayout.setEnableRefresh(true)
            mFilterAdpater!!.notifyDataSetChanged()
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
        mFilterAdpater!!.notifyDataSetChanged()

    }

    override fun FindGoodListTwoError(ex: Throwable) {
        isRefrsh = false
        if (gm_SmartRefreshLayout != null)
            gm_SmartRefreshLayout.finishLoadMore()
        this.onError(ex)
    }

    override fun FindGoodListTwoComplise() {
        isRefrsh = false
        if (gm_SmartRefreshLayout != null)
            gm_SmartRefreshLayout.finishLoadMore()
        this.onComplise()
    }

    override fun CheckSuccess(t: Any?) {
        if (t == null) return
        val data: GetUserInfom = t as GetUserInfom
        updateUserInfom(data)
        showGoRenZhen(data, object : BaseFragment.ContinueDoEvent {
            override fun doEvent() {
                when (mCheckInfomType) {
                    0 -> {
                        val bundle = Bundle()
                        bundle.putString(InfomNewActivity.INFOM_ID, mCheckInfomParame)
                        jumpTo(InfomNewActivity::class.java, bundle, getString(R.string.infom))
                    }
                    1 -> {
                        Utils.playPhone(mContext, mCheckInfomParame)
                    }
                }
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
