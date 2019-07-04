package com.zzzh.akhalteke.ui.home.find

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import com.zzzh.akhalteke.MainActivity

import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.adapter.CarSelectLengthAdapter
import com.zzzh.akhalteke.adapter.CarSelectTypeAdapter
import com.zzzh.akhalteke.adapter.CarSelectTypeOneAdapter

import com.zzzh.akhalteke.bean.vo.CarLengthVo
import com.zzzh.akhalteke.bean.vo.CarTypeVo
import com.zzzh.akhalteke.bean.vo.RlvSelectVo
import com.zzzh.akhalteke.fragment.home.HomeFindGoodFragment
import com.zzzh.akhalteke.mvp.contract.CarLengthContract
import com.zzzh.akhalteke.mvp.contract.CarTypeContract
import com.zzzh.akhalteke.mvp.model.CarLengthModel
import com.zzzh.akhalteke.mvp.model.CarTypeModel
import com.zzzh.akhalteke.mvp.presenter.CarLengthPresenter
import com.zzzh.akhalteke.mvp.presenter.CarTypePresenter
import com.zzzh.akhalteke.ui.BaseActivity
import com.zzzh.akhalteke.utils.StringUtil
import com.zzzh.akhalteke.utils.Utils
import kotlinx.android.synthetic.main.activity_filtrate.*

/**
 * @Author : YFL  is Creating a porject in com.zzzh.akhalteke.ui.home
 * @Package com.zzzh.akhalteke.ui.home
 * @Email : yufeilong92@163.com
 * @Time :2019/3/13 16:53
 * @Purpose :筛选条件
 */
class FiltrateActivity : BaseActivity(), CarTypeContract.View, CarLengthContract.View {
    private var mSelectUseCarTypList: MutableList<RlvSelectVo>? = null
    private var mSelectCarLengthList: MutableList<RlvSelectVo>? = null
    private var mSelectTimeList: MutableList<RlvSelectVo>? = null
    private var mSelectCarTyep: MutableList<RlvSelectVo>? = null
    //用车类型 整单，零单
    private var mUseCarTypeAdapter: CarSelectTypeAdapter? = null
    private var mCarTypeOneAdapter: CarSelectTypeOneAdapter? = null
    private var mCarLengthAdapter: CarSelectLengthAdapter? = null
    private var mCarTimeAdapter: CarSelectTypeAdapter? = null

    companion object {
        var SELECTLIST = "select_list"
    }

    //用户选择结果
    private var mFilterList: ArrayList<RlvSelectVo>? = null

    override fun setContentView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_filtrate)
        if (intent != null) {
            mFilterList = intent.getSerializableExtra(SELECTLIST) as ArrayList<RlvSelectVo>?
        }
        initRequest()
        initCarTypeData()
        initZhangTimeData()
        initEvent()
    }


    fun initRequest() {
        showProgress()
        val mCarTypePresenter = CarTypePresenter()
        mCarTypePresenter.initMvp(this, m = CarTypeModel())
        val mCarLengthPresenter = CarLengthPresenter()
        mCarLengthPresenter.initMvp(this, model = CarLengthModel())
        mCarTypePresenter.getCarType(mContext)
        mCarLengthPresenter.getCarLength(mContext)
    }

    /**
     * 处理用车类型
     */
    fun initCarTypeData() {
        val list = Utils.getMutablistString(mContext, R.array.car_type)
        mSelectUseCarTypList = Utils.getRlvSelctVoUserCar(list)
        setMangager(rlv_sx_car_type, 4, GridLayoutManager.VERTICAL)
        mUseCarTypeAdapter = CarSelectTypeAdapter(mContext, list, mSelectUseCarTypList!!)
        rlv_sx_car_type.adapter = mUseCarTypeAdapter
        mUseCarTypeAdapter!!.setOnItemClickListener { adapter, view, position ->
            for (id in 0 until list.size) {
                mSelectUseCarTypList!![id].select = false
                mSelectUseCarTypList!![id].name = ""
            }
            mSelectUseCarTypList!![position].select = true
            mSelectUseCarTypList!![position].name = list[position]
            mSelectUseCarTypList!![position].id = (position + 1).toString()
            mUseCarTypeAdapter!!.refhresh(mSelectUseCarTypList!!)
        }
        ecahData(HomeFindGoodFragment.TYPE)
    }

    /**
     * 处理用户时间
     */
    fun initZhangTimeData() {
        val list = Utils.getMutablistString(mContext, R.array.car_time)
        mSelectTimeList = Utils.getRlvSelctVoTime(list)
        setMangager(rlv_sx_huo_time, 4, GridLayoutManager.VERTICAL)
        mCarTimeAdapter = CarSelectTypeAdapter(mContext, list, mSelectTimeList!!)
        rlv_sx_huo_time.adapter = mCarTimeAdapter
        mCarTimeAdapter!!.setOnItemClickListener { adapter, view, position ->
            for (id in 0 until list.size) {
                mSelectTimeList!![id].select = false
                mSelectTimeList!![position].name = ""
            }
            mSelectTimeList!![position].select = true
            mSelectTimeList!![position].name = list[position]
            mSelectTimeList!![position].id = (position + 1).toString()
            mCarTimeAdapter!!.refhresh(mSelectTimeList!!)
        }
        ecahData(HomeFindGoodFragment.LOADTIME)
    }

    fun initEvent() {
        btn_sx_clear_select.setOnClickListener {
            Utils.clearSelect(mSelectTimeList!!)
            Utils.clearSelect(mSelectUseCarTypList!!)
            Utils.clearSelect(mSelectCarTyep!!)
            Utils.clearSelect(mSelectCarLengthList!!)
            mCarLengthAdapter.let { mCarLengthAdapter!!.refhresh(mSelectCarLengthList!!) }
            mCarTimeAdapter.let { mCarTimeAdapter!!.refhresh(mSelectTimeList!!) }
            mCarTypeOneAdapter.let { mCarTypeOneAdapter!!.refhresh(mSelectCarTyep!!) }
            mUseCarTypeAdapter.let { mUseCarTypeAdapter!!.refhresh(mSelectUseCarTypList!!) }
        }
        btn_sx_sure.setOnClickListener {
            //用户使用车型
            val selectUseCarType = Utils.getUserSelectToStr(mSelectUseCarTypList!!)
            val selectUseCarTypeName = Utils.getUserSelectToStrOne(mSelectUseCarTypList!!)
            //用户车长
            val selectCarLength = Utils.getUserSelectToStr(mSelectCarLengthList!!)
            val selectCarLengthName = Utils.getUserSelectToStrOne(mSelectCarLengthList!!)
            //用户装货时间
            val selectTime = Utils.getUserSelectToStr(mSelectTimeList!!)
            val selectTimeName = Utils.getUserSelectToStrOne(mSelectTimeList!!)
            //车辆类型
            val selectType = Utils.getUserSelectToStr(mSelectCarTyep!!)
            val selectTypeName = Utils.getUserSelectToStrOne(mSelectCarTyep!!)
            val intent = Intent(this@FiltrateActivity, MainActivity::class.java)
            intent.putExtra(HomeFindGoodFragment.TYPE, selectUseCarType)
            intent.putExtra(HomeFindGoodFragment.TYPENAME, selectUseCarTypeName)
            intent.putExtra(HomeFindGoodFragment.LOADTIME, selectTime)
            intent.putExtra(HomeFindGoodFragment.LOADTIMENAME, selectTimeName)
            intent.putExtra(HomeFindGoodFragment.CARLENGTH, selectCarLength)
            intent.putExtra(HomeFindGoodFragment.CARLENGTHNAME, selectCarLengthName)
            intent.putExtra(HomeFindGoodFragment.CARTYPE, selectType)
            intent.putExtra(HomeFindGoodFragment.CARTYPENAME, selectTypeName)
            setResult(HomeFindGoodFragment.REQUESTCODE, intent)
            finishBase()
        }
    }

    //用车车长
    override fun Success(t: Any?) {
        val data = t as MutableList<CarLengthVo>;
        setMangager(rlv_sx_car_length, 4, GridLayoutManager.VERTICAL)
        mSelectCarLengthList = Utils.getRlvSelctVoCarLength(data)
        mCarLengthAdapter = CarSelectLengthAdapter(mContext, data, mSelectCarLengthList!!)
        rlv_sx_car_length.adapter = mCarLengthAdapter
        mCarLengthAdapter!!.setOnItemClickListener { adapter, view, position ->
            mSelectCarLengthList!![position].select = !mSelectCarLengthList!![position].select
            mSelectCarLengthList!![position].id = data[position].id
            mSelectCarLengthList!![position].name = data[position].length
            mCarLengthAdapter!!.refhresh(mSelectCarLengthList!!)
        }
        ecahData(HomeFindGoodFragment.CARLENGTH)
    }

    override fun Error(ex: Throwable) {
        this.onError(ex)
    }

    override fun Complise() {
        this.onComplise()
    }

    //车辆类型
    override fun CarTypeSuccess(t: Any?) {
        val data: MutableList<CarTypeVo> = t as MutableList<CarTypeVo>
        setMangager(rlv_sx_car_type_one, 4, GridLayoutManager.VERTICAL)
        mSelectCarTyep = Utils.getRlvSelctVoCarType(data)
        mCarTypeOneAdapter = CarSelectTypeOneAdapter(mContext, data, mSelectCarTyep!!)
        rlv_sx_car_type_one.adapter = mCarTypeOneAdapter
        mCarTypeOneAdapter!!.setOnItemClickListener { adapter, view, position ->
            if (isSelectThree(mSelectCarTyep!!)) {
                showToast(getString(R.string.select_more_three))
                mSelectCarTyep!![position].select = false
                mCarTypeOneAdapter!!.refhresh(mSelectCarTyep!!)
                return@setOnItemClickListener
            }
            mSelectCarTyep!![position].select = !mSelectCarTyep!![position].select
            mSelectCarTyep!![position].id = data[position].id
            mSelectCarTyep!![position].name = data[position].name
            mCarTypeOneAdapter!!.refhresh(mSelectCarTyep!!)
        }
        ecahData(HomeFindGoodFragment.CARTYPE)
    }

    fun isSelectThree(rlvSelctVo: MutableList<RlvSelectVo>): Boolean {
        var number: Int = 0
        for (item in rlvSelctVo) {
            if (number > 2) {
                return true
            }
            if (item.select) {
                ++number;
            }
        }
        return false
    }

    override fun CarTypeError(ex: Throwable) {
        this.onError(ex)
    }

    override fun CarTypeComplise() {
        this.onComplise()
    }


    /***
     * @param type 数据类型
     *  //回显数据
     */
    fun ecahData(type: String) {
        if (mFilterList == null) return
        for (item in mFilterList!!) {
            if (item.type == type) {
                if (StringUtil.isEmpty(item.id!!)) return
                val selectList = Utils.getUserSelectToList(item.id!!)
                if (selectList == null || selectList.isEmpty()) return
                when (type) {
                    HomeFindGoodFragment.TYPE -> {//车辆类型（整单，零担）
                        if (mSelectUseCarTypList == null) return
                        for (itemOne in  mSelectUseCarTypList!!) {
                            for (itemTwo in selectList) {
                                if (itemOne.id == itemTwo) {
                                    itemOne.select = true
                                    itemOne.id=itemTwo
                                }
                            }
                        }
                        mUseCarTypeAdapter!!.refhresh(mSelectUseCarTypList!!)
                        mUseCarTypeAdapter!!.notifyDataSetChanged()

                    }
                    HomeFindGoodFragment.LOADTIME -> {//装货时间
                        if (mSelectTimeList == null) return
                        for (itemOne in mSelectTimeList!!) {
                            for (itemTwo in selectList) {
                                if (itemOne.id == itemTwo) {
                                    itemOne.select = true
                                }
                            }
                        }
                        mCarTimeAdapter!!.refhresh(mSelectTimeList!!)
                        mCarTimeAdapter!!.notifyDataSetChanged()
                    }
                    HomeFindGoodFragment.CARLENGTH -> {//车长
                        if (mSelectCarLengthList == null) return
                        for (itemOne in mSelectCarLengthList!!) {
                            for (itemTwo in selectList) {
                                if (itemOne.id == itemTwo) {
                                    itemOne.select = true
                                }
                            }
                        }
                        mCarLengthAdapter!!.refhresh(mSelectCarLengthList!!)
                        mCarLengthAdapter!!.notifyDataSetChanged()
                    }
                    HomeFindGoodFragment.CARTYPE -> {//车型
                        if (mSelectCarTyep == null) return
                        for (itemOne in mSelectCarTyep!!) {
                            for (itemTwo in selectList) {
                                if (itemOne.id == itemTwo) {
                                    itemOne.select = true
                                }
                            }
                        }
                        mCarTypeOneAdapter!!.refhresh(mSelectCarTyep!!)
                        mCarTypeOneAdapter!!.notifyDataSetChanged()
                    }
                }
            }
        }

    }
}

