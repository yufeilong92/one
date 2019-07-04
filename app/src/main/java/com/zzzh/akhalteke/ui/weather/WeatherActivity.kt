package com.zzzh.akhalteke.ui.weather

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.baidu.location.d.j.L
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.adapter.Weather.HourWeatherAdapter
import com.zzzh.akhalteke.adapter.Weather.WeekWeatherAdapter
import com.zzzh.akhalteke.bean.vo.WeatherVo
import com.zzzh.akhalteke.ui.BaseActivity
import com.zzzh.akhalteke.utils.TimeSampUtil
import kotlinx.android.synthetic.main.activity_weather.*

/**
 * @Author : YFL  is Creating a porject in com.zzzh.akhalteke.ui
 * @Package com.zzzh.akhalteke.ui
 * @Email : yufeilong92@163.com
 * @Time :2019/5/6 14:52
 * @Purpose :天气
 */
class WeatherActivity : BaseActivity() {
    companion object {
        val TYPE: String = "type";
    }

    private var mWeatherData: WeatherVo? = null
    override fun setContentView(savedInstanceState: Bundle?) {
         setContentView(R.layout.activity_weather)
        if (intent != null) {
            mWeatherData = intent.getParcelableExtra(TYPE) as WeatherVo
        }
        bindViewData(mWeatherData)
        ic_weather_back.setOnClickListener {
            onFinishWeather()
        }
    }

    private fun bindViewData(mWeatherData: WeatherVo?) {
        initHourAdapter(mWeatherData)
        initWeekAdapter(mWeatherData)
        val now = mWeatherData!!.now
        val cityInfo = mWeatherData.cityInfo
        val f1 = mWeatherData.f1
        weather_view_weather.setDrawerType(SwtWeatherType(now.weather))
        tv_weather_wea.text = now.weather
        tv_weather_city.text = cityInfo.c3
        tv_weather_temperature.text = now.temperature
        tv_weahter_week.text = TimeSampUtil.getWeek(f1.day)
        tv_weather_max_time.text = f1.day_air_temperature
        tv_weather_min_time.text = f1.night_air_temperature


    }

    fun initHourAdapter(mWeatherData: WeatherVo?) {
        var mList = mWeatherData!!.f1!!.`_$3hourForcast`
        setHorizontalMangager(rlv_weather_hour_wea)
        val adapter = HourWeatherAdapter(mContext, mList)
        rlv_weather_hour_wea.adapter = adapter

    }

    fun initWeekAdapter(mWeatherData: WeatherVo?) {
        val mWeekList = mutableListOf<WeatherVo.WeatherBean>()
        mWeekList.add(mWeatherData!!.f1)
        mWeekList.add(mWeatherData.f2)
        mWeekList.add(mWeatherData.f3)
        mWeekList.add(mWeatherData.f4)
        mWeekList.add(mWeatherData.f5)
        mWeekList.add(mWeatherData.f6)
        mWeekList.add(mWeatherData.f7)
        setMangager(rlv_weather_day_wea)
        val adapter = WeekWeatherAdapter(mContext, mWeekList)
        rlv_weather_day_wea.adapter = adapter

    }

    override fun onResume() {
        super.onResume()
        weather_view_weather.onResume()
    }

    override fun onPause() {
        super.onPause()
        weather_view_weather.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        weather_view_weather.onDestroy()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        onFinishWeather()
    }

}
