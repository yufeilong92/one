package com.zzzh.akhalteke.ui.verify

import android.content.Intent
import android.os.Bundle
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.adapter.CarLengthAdapter
import com.zzzh.akhalteke.bean.vo.CarLengthVo
import com.zzzh.akhalteke.mvp.contract.CarLengthContract
import com.zzzh.akhalteke.mvp.model.CarLengthModel
import com.zzzh.akhalteke.mvp.presenter.CarLengthPresenter
import com.zzzh.akhalteke.ui.BaseActivity
import com.zzzh.akhalteke.ui.verify.newverify.CarNewActivity
import kotlinx.android.synthetic.main.activity_car_length.*

/**
 * @Author : YFL  is Creating a porject in Lenovo
 * @Email : yufeilong92@163.com
 * @Time :2019/3/11 19:26
 * @Purpose :车长
 */

class CarLengthActivity : BaseActivity(), CarLengthContract.View {


    override fun setContentView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_car_length)
        initRequest()

    }

    fun initRequest() {
        var mPresenter = CarLengthPresenter()
        mPresenter.initMvp(this, model = CarLengthModel())
        mPresenter.getCarLength(mContext)
    }

    override fun Success(t: Any?) {
        var data = t as MutableList<CarLengthVo>;
        var carTypeAdapter = CarLengthAdapter(mContext, data)
        setMangager(rlv_car_length)
        rlv_car_length.adapter = carTypeAdapter
        carTypeAdapter.setOnItemClickListener { adapter, view, position ->
            val vo = data.get(position)
            var intent = Intent(this@CarLengthActivity, CarNewActivity::class.java)
            intent.putExtra(CarNewActivity.CARTYPE_ID, vo.id)
            intent.putExtra(CarNewActivity.CARTYPE_NAME, vo.length)
            setResult(CarNewActivity.CARLENGTH_RESULT, intent)
            finishBase()
        }
    }

    override fun Complise() {
        this.onComplise()
    }

    override fun Error(ex: Throwable) {
        this.onError(ex)
    }
}