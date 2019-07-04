package com.zzzh.akhalteke.service

import android.app.IntentService
import android.content.Context
import android.content.Intent
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClientOption
import com.zzzh.akhalteke.BaseApplication
import com.zzzh.akhalteke.mvp.contract.UpdateAddressContract
import com.zzzh.akhalteke.mvp.model.UpdateAddressModel
import com.zzzh.akhalteke.mvp.presenter.UpdateAddressPresenter
import com.zzzh.akhalteke.utils.L
import java.util.*

private const val ACTION_FOO = "com.zzzh.akhalteke.service.action.FOO"

private const val EXTRA_PARAM1 = "com.zzzh.akhalteke.service.extra.PARAM1"
private const val EXTRA_PARAM2 = "com.zzzh.akhalteke.service.extra.PARAM2"
private const val EXTRA_PARAM3 = "com.zzzh.akhalteke.service.extra.PARAM3"

/**
 * @Author : YFL  is Creating a porject in com.zzzh.akhalteke.service
 * @Package com.zzzh.akhalteke.service
 * @Email : yufeilong92@163.com
 * @Time :2019/4/9 10:20
 * @Purpose :上传用户地址
 */
class UpdateAddressIntentService : IntentService("UpdateAddressIntentService"), UpdateAddressContract.View {


    override fun onHandleIntent(intent: Intent?) {
        when (intent?.action) {
            ACTION_FOO -> {
                handleActionFoo()
            }

        }
    }

    private var mPresenter: UpdateAddressPresenter? = null
    private var mContext: Context? = null
    private fun handleActionFoo() {
        mPresenter = UpdateAddressPresenter()
        mPresenter!!.initMvp(this, UpdateAddressModel())
        mContext = applicationContext
        ininBaidu()
    }

    fun ininBaidu() {
        val option = LocationClientOption()
        option.locationMode = LocationClientOption.LocationMode.Hight_Accuracy
        option.setCoorType("bd09ll")
        option.setScanSpan(0)
        option.isOpenGps = true
        option.isLocationNotify = false
        option.setIgnoreKillProcess(false)
        option.SetIgnoreCacheException(false)
        option.setWifiCacheTimeOut(5 * 60 * 1000)
        option.setEnableSimulateGps(false)
        option.setIsNeedAddress(true)
        BaseApplication.toInstance().mLocationClien!!.locOption = option
        BaseApplication.toInstance().mLocationClien!!.registerLocationListener(locationListener())
        BaseApplication.toInstance().mLocationClien!!.start()
    }

    inner class locationListener : BDAbstractLocationListener() {
        override fun onReceiveLocation(location: BDLocation?) {
            val latitude = location!!.latitude    //获取纬度信息
            val longitude = location.longitude    //获取经度信息
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
//            获取城市编码
            val coorType = location.coorType
            val city = location.city
            val street = location.street
            val district = location.district
            val cityCode = location.cityCode
            val province = location.province
//            获取城市adcode
            val adCode = location.adCode
            L.e(
                "=city=" + city + "=street=" + street + "=district=" + district +
                        "=cityCode=" + cityCode + "=province=" + province + "=adCode=" + adCode
            )
            val time = Date().time / 1000
            mPresenter!!.submitPointAdd(
                mContext!!, latitude.toString(),
                longitude.toString(), time.toString(), coorType
            )

        }

    }

    companion object {
        @JvmStatic
        fun startActionFoo(context: Context) {
            val intent = Intent(context, UpdateAddressIntentService::class.java).apply {
                action = ACTION_FOO
            }
            context.startService(intent)
        }
    }

    override fun Error(ex: Throwable) {
        stopSelf()
    }

    override fun Complise() {
        stopSelf()
    }

    override fun Success(t: Any?) {
        stopSelf()
    }
}
