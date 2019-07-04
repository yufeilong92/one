package com.zzzh.akhalteke.fragment.home

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.zzzh.akhalteke.Event.RefreshWayBillTitleEvent
import com.zzzh.akhalteke.MainActivity

import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.adapter.ViewPagerAdaper
import com.zzzh.akhalteke.bean.vo.GmContentVo
import com.zzzh.akhalteke.bean.vo.MyOrderTitleVo
import com.zzzh.akhalteke.bean.vo.WeatherVo
import com.zzzh.akhalteke.fragment.waybill.WaybillOneFragment
import com.zzzh.akhalteke.fragment.waybill.WaybillThreeFragment
import com.zzzh.akhalteke.fragment.waybill.WaybillTwoFragment
import com.zzzh.akhalteke.ui.BaseFragment
import com.zzzh.akhalteke.utils.Utils
import com.zzzh.akhalteke.weather.Observer.WeatherObserver
import kotlinx.android.synthetic.main.fragment_homewaybill.*
import kotlinx.android.synthetic.main.gm_title.*
import kotlinx.android.synthetic.main.gm_weather_title_root.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * @Author : YFL  is Creating a porject in com.zzzh.akhalteke.fragment
 * @Package com.zzzh.akhalteke.fragment
 * @Email : yufeilong92@163.com
 * @Time :2019/3/15 9:44
 * @Purpose :运单首页
 */
class HomeWayBillFragment : BaseFragment(), WeatherObserver {


    private var param1: String? = null
    private var param2: String? = null
    private var mTitleAdapter: ViewPagerAdaper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                HomeWayBillFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().removeAllStickyEvents()
        EventBus.getDefault().unregister(this)
    }

    override fun setContentView(): Int {
        return R.layout.fragment_homewaybill
    }

    override fun upDataWeather() {
        val vo = GmContentVo.getWeatherVo() ?: return
        if (vo.cityInfo==null)return
        GmContentVo.setWeatherVo(vo)
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
    override fun setCreatedContentView(view: View, savedInstanceState: Bundle?) {
        tv_gm_weather_title.text=getString(R.string.odd)
        val titles = Utils.getMutablistString(mContext, R.array.waybill)
        var listFragment = mutableListOf<Fragment>()
        listFragment.add(WaybillOneFragment())
        listFragment.add(WaybillTwoFragment())
        listFragment.add(WaybillThreeFragment())
        mTitleAdapter = ViewPagerAdaper(fragmentManager, listFragment, titles)
        viewparger_waybill.adapter = mTitleAdapter
        tab_layout_waybill.setupWithViewPager(viewparger_waybill)
        viewparger_waybill.offscreenPageLimit = 3
        initOnClick()
    }

    fun initOnClick() {
        setWeatherOnClick(rl_gm_weather_root)
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun RefhreshTtitleEvent(event: RefreshWayBillTitleEvent) {
        mTitleAdapter.let {
            val titles = mutableListOf<String>()
            titles.add("运输中(" + MyOrderTitleVo.getYunNumber() + ")")
            titles.add("已完成(" + MyOrderTitleVo.getCompletaNumber() + ")")
            titles.add("已取消(" + MyOrderTitleVo.getCancleNumber() + ")")
            mTitleAdapter!!.refreshTitle(titles)
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            if (GmContentVo.mWeatherVo == null || GmContentVo.mWeatherVo!!.cityInfo == null) return
            bindViewData(GmContentVo.mWeatherVo!!)
        }
    }
}
