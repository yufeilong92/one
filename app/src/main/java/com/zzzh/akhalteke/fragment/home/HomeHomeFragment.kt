package com.zzzh.akhalteke.fragment.home

import android.os.Bundle
import android.view.View
import com.yanzhenjie.permission.Permission
import com.zzzh.akhalteke.MainActivity
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.WebViewActivity
import com.zzzh.akhalteke.adapter.Home.MainHomeAdaptervar
import com.zzzh.akhalteke.bean.DataMessageVo
import com.zzzh.akhalteke.bean.vo.*
import com.zzzh.akhalteke.dbHelp.UserDbHelp
import com.zzzh.akhalteke.dbHelp.UserDbHelp.mContext
import com.zzzh.akhalteke.mvp.contract.MainContract
import com.zzzh.akhalteke.mvp.contract.WeatherContract
import com.zzzh.akhalteke.mvp.model.MainModel
import com.zzzh.akhalteke.mvp.model.WeatherModel
import com.zzzh.akhalteke.mvp.presenter.MainPresenter
import com.zzzh.akhalteke.mvp.presenter.WeatherPresenter
import com.zzzh.akhalteke.ui.BaseFragment
import com.zzzh.akhalteke.ui.login.CodeActivity
import com.zzzh.akhalteke.utils.L
import com.zzzh.akhalteke.utils.PermissionUtils
import com.zzzh.akhalteke.utils.StringUtil
import com.zzzh.akhalteke.weather.BaseDrawer
import com.zzzh.akhalteke.weather.Observer.WeatherObserver
import kotlinx.android.synthetic.main.fragment_home_home.*
import kotlinx.android.synthetic.main.gm_refresh.*
import kotlinx.android.synthetic.main.gm_weather_title_root.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * @Author : YFL  is Creating a porject in com.zzzh.akhalteke.fragment.home
 * @Package com.zzzh.akhalteke.fragment.home
 * @Email : yufeilong92@163.com
 * @Time :2019/5/17 9:16
 * @Purpose :首页碎片
 */
class HomeHomeFragment : BaseFragment(), WeatherObserver, MainContract.View {


    private var param1: String? = null
    private var param2: String? = null
    private var mPresenter: MainPresenter? = null

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeHomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun setContentView(): Int {
        return R.layout.fragment_home_home
    }

    override fun upDataWeather() {
        val vo = GmContentVo.getWeatherVo() ?: return
        if (vo.cityInfo == null) return
        GmContentVo.setWeatherVo(vo)
        bindViewData(vo)
    }

    fun bindViewData(data: WeatherVo) {
        if (ll_weather_root == null) return
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

    fun setWeatherShow(show: Boolean) {
        ll_weather_root.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun setCreatedContentView(view: View, savedInstanceState: Bundle?) {
        setMangager(gm_rlv_content)
        tv_gm_weather_title.text = getString(R.string.main_home)
        initRequest()
        initOnClick()
        gm_SmartRefreshLayout.setEnableLoadMore(false)
        gm_SmartRefreshLayout.setEnableAutoLoadMore(false)
        initRefresh()
        gm_SmartRefreshLayout.autoRefresh()
    }

    fun initRefresh() {
        gm_SmartRefreshLayout.apply {
            setOnRefreshListener {
                loadNewData()
            }
            setOnLoadMoreListener {
            }
        }
    }

    private fun loadNewData() {
        mPresenter!!.requestMainContent(mContext)
    }

    fun initRequest() {
        mPresenter = MainPresenter()
        mPresenter!!.initMvp(this, MainModel())

    }

    fun initOnClick() {
        setWeatherOnClick(rl_gm_weather_root)
    }

    override fun onMainSuccess(t: Any?) {
        gm_SmartRefreshLayout.finishRefresh()
        val data = t as MainVo ?: return
        initAdapter(data)

    }

    private fun initAdapter(data: MainVo) {
        val userInfom = UserDbHelp.get_Instance(mContext)!!.getUserInfom()
        var userPhone = ""
        var userid= ""
        if (userInfom != null && !StringUtil.isEmpty(userInfom!!.userId)) {
            userPhone = userInfom.phone
            userid = userInfom.userId
        }
        val homeAdapter = MainHomeAdaptervar(mContext, data)
        gm_rlv_content.adapter = homeAdapter
        homeAdapter.setOnBannerClikcListener(object : MainHomeAdaptervar.OnBannerClickItemListener {
            override fun onBannerClickItemLisener(postion: Int, item: BannerVo) {
                val bundle = Bundle()
                bundle.putString(WebViewActivity.TYPE, WebViewActivity.NEW_TYPE)
                bundle.putString(WebViewActivity.URL, item.url + "?user_id=$userid&username=$userPhone")
                jumpTo(WebViewActivity::class.java, bundle, "活动")
            }
        })
        homeAdapter.setOnActivityClikcListener(object : MainHomeAdaptervar.OnActiviyClickItemListener {
            override fun onActivityClickItemLisener(postion: Int, item: ActivityVo) {
                val bundle = Bundle()
                bundle.putString(WebViewActivity.TYPE, WebViewActivity.NEW_TYPE)
                bundle.putString(WebViewActivity.URL, item.url + "?user_id=$userid&username=$userPhone")
                jumpTo(WebViewActivity::class.java, bundle, "活动")
            }
        })
        homeAdapter.setOnUrlClikcListener(object : MainHomeAdaptervar.OnUrlClickItemListener {
            override fun onUrlClickItemLisener(postion: Int, str: String) {
                when (postion) {
                    2 -> {
                        PermissionUtils.showPermission(
                            mContext, "",
                            arrayOf(
                                Permission.CAMERA,
                                Permission.READ_EXTERNAL_STORAGE,
                                Permission.READ_EXTERNAL_STORAGE
                            )
                        ) {
                            val bundle = Bundle()
                            bundle.putString(WebViewActivity.TYPE, WebViewActivity.NEW_TYPE)
                            bundle.putString(WebViewActivity.URL, "${DataMessageVo.ETC_HTTP}?channel=10030116&username=$userPhone")
                            jumpTo(WebViewActivity::class.java, bundle, "ETC")
                        }
                    }
                    else -> {
                        val bundle = Bundle()
                        bundle.putString(WebViewActivity.TYPE, WebViewActivity.NEW_TYPE)
                        bundle.putString(WebViewActivity.URL, "$str?user_id=$userid&username=$userPhone")
                        jumpTo(WebViewActivity::class.java, bundle, "活动")
                    }
                }
            }
        })
    }

    override fun onMainError(ex: Throwable) {
        this.onError(ex)
        if (gm_SmartRefreshLayout != null)
            gm_SmartRefreshLayout.finishRefresh()
    }

    override fun onMainComplise() {
        this.onComplise()
        if (gm_SmartRefreshLayout != null)
            gm_SmartRefreshLayout.finishRefresh()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            if (GmContentVo.mWeatherVo == null || GmContentVo.mWeatherVo!!.cityInfo == null) return
            bindViewData(GmContentVo.mWeatherVo!!)
        }
    }

}
