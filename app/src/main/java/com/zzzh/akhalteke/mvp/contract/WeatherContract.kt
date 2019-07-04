package com.zzzh.akhalteke.mvp.contract

import android.content.Context
import com.zzzh.akhalteke.mvp.view.RequestResultInterface

/**
 * @Author : YFL  is Creating a porject in com.zzzh.akhalteke.mvp.contract
 * @Package com.zzzh.akhalteke.mvp.contract
 * @Email : yufeilong92@163.com
 * @Time :2019/5/10 10:53
 * @Purpose :天气接口
 */
interface WeatherContract {
    interface View {
        fun WeatherSuccess(t:Any?)
        fun WeatherComplise()
        fun WeatherError(ex: Throwable)

    }

    interface Model {
        fun requestWeather( context: Context, fromType: String, lat: String, lng: String,
                            need3: Boolean, needAlarm: Boolean, needHourData: Boolean
                            , needIndex: Boolean, neewMoreDay: Boolean,request: RequestResultInterface)
    }

    interface Presenter {
        fun initMvp(view: View, model: Model)
        fun requestWeather( context: Context, lat: String, lng: String,
                            need3: Boolean, needAlarm: Boolean, needHourData: Boolean
                            , needIndex: Boolean, neewMoreDay: Boolean)
    }
}