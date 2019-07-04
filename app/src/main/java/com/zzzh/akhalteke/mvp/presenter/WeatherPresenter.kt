package com.zzzh.akhalteke.mvp.presenter

import android.content.Context
import com.zzzh.akhalteke.mvp.contract.WeatherContract
import com.zzzh.akhalteke.mvp.view.RequestResultInterface

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.mvp.presenter
 * @Email : yufeilong92@163.com
 * @Time :2019/5/10 11:16
 * @Purpose :天气
 */
class WeatherPresenter : WeatherContract.Presenter {


    var view: WeatherContract.View? = null
    var model: WeatherContract.Model? = null
    override fun initMvp(view: WeatherContract.View, model: WeatherContract.Model) {
        this.view = view
        this.model = model
    }

    override fun requestWeather(context: Context, lat: String, lng: String, need3: Boolean, needAlarm: Boolean, needHourData: Boolean, needIndex: Boolean, neewMoreDay: Boolean) {
        model!!.requestWeather(context, "5", lat, lng, need3, needAlarm, needHourData
                , needIndex, neewMoreDay, object : RequestResultInterface {
            override fun onError(ex: Throwable) {
                view!!.WeatherError(ex)
            }

            override fun onComplise() {
                view!!.WeatherComplise()
            }

            override fun <T> Success(t: T) {
                view!!.WeatherSuccess(t)
            }
        })


    }

}