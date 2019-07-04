package com.zzzh.akhalteke.adapter.Weather

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.bean.StringInfo
import com.zzzh.akhalteke.bean.vo.WeatherVo
import com.zzzh.akhalteke.utils.GlideUtil
import com.zzzh.akhalteke.utils.TimeSampUtil
import com.zzzh.akhalteke.utils.TimeUtil
import java.util.*

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.adapter.Weather
 * @Email : yufeilong92@163.com
 * @Time :2019/5/14 17:36
 * @Purpose :7天天气
 */
class WeekWeatherAdapter(var context: Context, var infoList: MutableList<WeatherVo.WeatherBean>) :
        BaseQuickAdapter<WeatherVo.WeatherBean, BaseViewHolder>(R.layout.item_week_weather, infoList) {
    override fun convert(helper: BaseViewHolder?, item: WeatherVo.WeatherBean?) {
        val tvItemWeatherWeek = helper!!.getView<TextView>(R.id.tv_item_weather_week)
        val ivItemWeatherWea = helper.getView<ImageView>(R.id.iv_item_weather_wea)
        val tvItemWeatherMaxT = helper.getView<TextView>(R.id.tv_item_weather_max_t)
        val tvItemWeatherMinT = helper.getView<TextView>(R.id.tv_item_weather_min_t)
        tvItemWeatherMaxT.text = item!!.day_air_temperature
        tvItemWeatherMinT.text = item.night_air_temperature
        val night = TimeUtil.getInstance()!!.isNight(Date())
        tvItemWeatherWeek.text=TimeSampUtil.getWeek(item.day)
        GlideUtil.LoadImager(context, ivItemWeatherWea,if (!night) item.day_weather_pic else item.night_weather_pic)
    }
}