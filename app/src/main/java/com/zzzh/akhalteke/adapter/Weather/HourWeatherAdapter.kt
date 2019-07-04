package com.zzzh.akhalteke.adapter.Weather

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.bean.vo.WeatherVo
import com.zzzh.akhalteke.utils.GlideUtil

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.adapter.Weather
 * @Email : yufeilong92@163.com
 * @Time :2019/5/14 17:33
 * @Purpose :天气小时
 */
class HourWeatherAdapter(var context: Context, var infoList: MutableList<WeatherVo.`_$3hourForcastBean`>) :
        BaseQuickAdapter<WeatherVo.`_$3hourForcastBean`, BaseViewHolder>(R.layout.item_hour_weather, infoList) {
    override fun convert(helper: BaseViewHolder?, item:WeatherVo.`_$3hourForcastBean`?) {
        val mTvWeatherhour = helper!!.getView<TextView>(R.id.tv_item_weather_hour)
        val mImageView = helper!!.getView<ImageView>(R.id.iv_item_weather_wea)
        val mTvWeatherT = helper.getView<TextView>(R.id.tv_item_weather_t)
        mTvWeatherT.text = item!!.temperature
        mTvWeatherhour.text = item!!.hour
       GlideUtil.LoadImager(context,mImageView,item.weather_pic)
    }
}