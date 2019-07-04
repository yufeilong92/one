package com.zzzh.akhalteke.ui.verify

import android.content.Intent
import android.os.Bundle
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.adapter.CarTypeAdapter
import com.zzzh.akhalteke.bean.vo.CarTypeVo
import com.zzzh.akhalteke.mvp.contract.CarTypeContract
import com.zzzh.akhalteke.mvp.model.CarTypeModel
import com.zzzh.akhalteke.mvp.presenter.CarTypePresenter
import com.zzzh.akhalteke.ui.BaseActivity
import com.zzzh.akhalteke.ui.verify.newverify.CarNewActivity
import kotlinx.android.synthetic.main.activity_cartype.*

/**
 * @Author : YFL  is Creating a porject in Lenovo
 * @Email : yufeilong92@163.com
 * @Time :2019/3/6 17:11
 * @Purpose :车型
 */

class CarTypeActivity : BaseActivity(), CarTypeContract.View {


    override fun setContentView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_cartype)
        initRequest()
        initView()
    }

    fun initView() {
        iv_cartype_back.setOnClickListener {
            finishBase()
        }
    }

    fun initRequest() {
        var mPresenter = CarTypePresenter()
        mPresenter.initMvp(this, m = CarTypeModel())
        showProgress()
        mPresenter.getCarType(mContext)
    }

    override fun CarTypeSuccess(t: Any?) {
        var data: MutableList<CarTypeVo> = t as MutableList<CarTypeVo>
        var carTypeAdapter = CarTypeAdapter(mContext, data)
//        var layout=GridLayoutManager(mContext,1)
//        layout.orientation=GridLayoutManager.VERTICAL
//        rlv_car_type.layoutManager=layout
        setMangager(rlv_car_type)
        rlv_car_type.adapter = carTypeAdapter
        carTypeAdapter.setOnItemClickListener { adapter, view, position ->
            val vo = data.get(position)
            var intent = Intent(this@CarTypeActivity, CarNewActivity::class.java)
            intent.putExtra(CarNewActivity.CARTYPE_ID, vo.id)
            intent.putExtra(CarNewActivity.CARTYPE_NAME, vo.name)
            setResult(CarNewActivity.CARTYPENUMBER, intent)
            finishBase()
        }
    }

    override fun CarTypeError(ex: Throwable) {
        this.onError(ex)
    }

    override fun CarTypeComplise() {
        this.onComplise()
    }

}