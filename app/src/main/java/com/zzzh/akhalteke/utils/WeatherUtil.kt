package com.zzzh.akhalteke.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zzzh.akhalteke.MainActivity
import com.zzzh.akhalteke.bean.vo.WeatherBean
import com.zzzh.akhalteke.weather.BaseDrawer
import com.zzzh.akhalteke.weather.Observer.WeatherManger
import com.zzzh.akhalteke.weather.Observer.WeatherObserver
import java.sql.Time
import java.util.*

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.utils
 * @Email : yufeilong92@163.com
 * @Time :2019/5/10 16:03
 * @Purpose :天气工具
 */
class WeatherUtil {
    companion object {//被companion object包裹的语句都是private的

        private var singletonInstance: WeatherUtil? = null

        @Synchronized
        fun getInstance(): WeatherUtil? {
            if (singletonInstance == null) {
                singletonInstance = WeatherUtil()
            }
            return singletonInstance
        }
    }

    fun getWeatherType(str: String): BaseDrawer.Type {
        val night = !TimeUtil.getInstance()!!.isNight(Date())
        var mType = BaseDrawer.Type.UNKNOWN_N
        when (str) {
            "晴", "晴转阴", "晴转多云", "晴转雷阵雨", "晴转大雪", "晴转小雨",
            "晴转雨夹雪", "晴转小雪", "晴转中雪", "晴转扬沙", "晴转霾", "晴转阵雪" -> {
                mType = if (night) BaseDrawer.Type.values()[1] else BaseDrawer.Type.values()[2]
            }

            "阴", "阴转多云", "阴转晴", "阴转中雨", "阴转阵雪", "阴转大雪",
            "阴转小雪", "阴转雨夹雪", "阴转中雪", "阴转雾" -> {
                mType = if (night) BaseDrawer.Type.values()[9] else BaseDrawer.Type.values()[10]

            }

            "多云", "多云转晴", "多云转阴", "多云转霾", "多云转雨夹雪", "多云转小雨", "多云转中雪", "多云转小到中雪",
            "多云转阵雨", "多云转大雪", "多云转雾", "多云转阵雪", "多云转中雨", "多云转小到中雨", "多云转小雪" -> {
                mType = if (night) BaseDrawer.Type.values()[7] else BaseDrawer.Type.values()[8]

            }

            "雨夹雪", "雨夹雪转晴", "雨夹雪转多云", "小雪转雨夹雪"
                , "阵雨转雨夹雪", "小雨转阵雪", "中雪转多云", "雨夹雪转小雪", "雨夹雪转中雪",
            "雨夹雪转大雪", "雨夹雪转阴", "雨夹雪转小雨" -> {
                mType = if (night) BaseDrawer.Type.values()[18] else BaseDrawer.Type.values()[19]

            }

            "小雨", "中雨", "小雨转晴", "中雨转多云", "阵雨转晴", "阴转小雨",
            "小雨转多云", "阵雨转多云", "小雨转中雨", "中雨转小雨","雨",
            "小雨转大雨", "阵雨转中雨", "阵雨转大雨", "阴转大雨", "阵雨转小雨", "中雨转阴",
            "多云转大雨", "小雨转暴雨", "阵雨转中到大雨", "小雨转阵雨","中到大雨","大到暴雨","暴雨到大暴雨",
            "阴转阵雨", "小雨转小到中雨", "小到中雨转小雨", "晴转阵雨", "中雨转阵雨",
            "阵雨转雷阵雨", "中雨转中雪", "中雨转小雪", "小雨转雨夹雪","大暴雨到特大暴雨",
            "大雨转阴", "小雨转阴", "阵雨转阴", "小到中雨转阴",
            "中雨转大雨", "小到中雨", "小到中雨转阵雨", "雷阵雨转多云", "雷阵雨转阵雨",
            "小雨转小雪", "小雨转大雪", "阵雨转小雪","雷阵雨伴有冰雹","特大暴雨","冻雨",
            "阵雨", "大雨", "暴雨", "雷阵雨", "大暴雨" -> {
                mType = if (night) BaseDrawer.Type.values()[3] else BaseDrawer.Type.values()[4]

            }

            "小雪", "中雪", "小雪转晴", "大雪", "阵雪", "小雪转多云", "阵雪转阴",
            "阵雪转小雪", "小雪转阴", "阵雪转多云","雪",
            "小雪转阵雪", "小雪转中雪","小到中雪","中到大雪","大到暴雪",
            "中雪转小雪", "大雪转小雪", "小到中雪转多云", "大雪转多云", "小雨转中雪",
            "暴雪", "阵雪转晴" -> {
                mType = if (night) BaseDrawer.Type.values()[5] else BaseDrawer.Type.values()[6]
            }

            "雾", "浮尘", "扬沙转晴", "浮尘转晴", "浮尘转多云",
            "晴转雾", "雾转多云", "浮尘转霾" -> {
                mType = if (night) BaseDrawer.Type.values()[11] else BaseDrawer.Type.values()[12]

            }
            "霾", "霾转多云", "霾转阴", "霾转晴" -> {
                mType = if (night) BaseDrawer.Type.values()[13] else BaseDrawer.Type.values()[14]
            }
            "扬沙", "多云转扬沙", "扬沙转阴", "扬沙转多云","沙尘暴","强沙尘暴" -> {
                mType = if (night) BaseDrawer.Type.values()[15] else BaseDrawer.Type.values()[16]

            }
        }
        return mType
    }

    fun getMuslTalble(mContext: Context): MutableList<WeatherBean>? {
        val mlist = mutableListOf<WeatherBean>()
        val gson = Gson()
        val assets = readFromAssets(mContext)
        val list = gson.fromJson<MutableList<WeatherBean>>(assets,
                object : TypeToken<MutableList<WeatherBean>>() {
                }.type)
        for (item in list) {
            mlist.add(item)
        }
        return mlist

    }

    private fun readFromAssets(mContext: Context): String? {
        try {
            val `is` = mContext.assets.open("")
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            return String(buffer)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }


}