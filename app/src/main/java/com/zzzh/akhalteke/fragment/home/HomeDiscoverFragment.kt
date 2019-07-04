package com.zzzh.akhalteke.fragment.home

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.bean.vo.GmContentVo
import com.zzzh.akhalteke.bean.vo.WeatherVo
import com.zzzh.akhalteke.ui.BaseFragment
import com.zzzh.akhalteke.weather.Observer.WeatherObserver
import kotlinx.android.synthetic.main.gm_weather_title_root.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
/**
 * @Author : YFL  is Creating a porject in com.zzzh.akhalteke.fragment.home
 * @Package com.zzzh.akhalteke.fragment.home
 * @Email : yufeilong92@163.com
 * @Time :2019/6/4 10:47
 * @Purpose :发现
 */
class HomeDiscoverFragment : BaseFragment(), WeatherObserver {


    override fun setContentView(): Int {
        return R.layout.fragment_home_discover
    }


    private var param1: String? = null
    private var param2: String? = null

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                HomeDiscoverFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun setCreatedContentView(view: View, savedInstanceState: Bundle?) {
        tv_gm_weather_title.text=getString(R.string.main_find)
        initOnClick()
    }

    fun initOnClick() {
        setWeatherOnClick(rl_gm_weather_root)
    }

    override fun upDataWeather() {
        val vo = GmContentVo.getWeatherVo() ?: return
        if (vo.cityInfo==null)return
        bindViewData(vo)
    }

    fun bindViewData(data: WeatherVo) {
        if (ll_weather_root==null)return
        setWeatherShow(true)
        val info = data.cityInfo
        val vo = data.now
        val f1 = data.f1
        tv_gm_waether_max_t.text = f1.day_air_temperature
        tv_gm_waether_min_t.text = f1.night_air_temperature
        if (info.c3 == info.c5) {
            tv_gm_weather_city.visibility = View.GONE
        } else {
            tv_gm_weather_city.visibility = View.VISIBLE
        }
        tv_gm_weather_city.text = info.c5
        tv_gm_weather_couny.text = info.c3
        tv_gm_weather_wea.text = vo.weather
    }

    fun setWeatherShow(show:Boolean){
        ll_weather_root.visibility=if (show) View.VISIBLE else View.GONE
    }
}
