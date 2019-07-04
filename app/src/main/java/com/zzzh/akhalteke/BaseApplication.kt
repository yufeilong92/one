package com.zzzh.akhalteke

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import cn.jpush.android.api.JPushInterface
import com.baidu.location.LocationClient
import com.facebook.drawee.backends.pipeline.Fresco
import com.iflytek.cloud.SpeechConstant
import com.iflytek.cloud.SpeechUtility
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.tencent.bugly.crashreport.CrashReport
import com.zhy.http.okhttp.OkHttpUtils
import com.zzzh.akhalteke.dbHelp.Vo.UserDbVo
import com.zzzh.akhalteke.bean.vo.GmContentVo
import com.zzzh.akhalteke.ui.login.LoginActivity
import com.zzzh.akhalteke.utils.AudioUtils
import com.zzzh.akhalteke.utils.Constant
import com.zzzh.akhalteke.utils.PreferencesUtils
import com.zzzh.akhalteke.utils.ToolUtils
import com.zzzh.akhalteke.weather.Observer.WeatherManger
import com.zzzh.akhalteke.weather.Observer.WeatherObserver

class BaseApplication : Application() {

    private var count = 0
    private var isStop = false
    private var isExitOrHome = false
    private val weatherManger = WeatherManger()
    var mLocationClien: LocationClient? = null

    companion object {
        private var instance: BaseApplication? = null
        fun toInstance(): BaseApplication {
            return instance!!
        }

        init {
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
                layout.setPrimaryColorsId(R.color.main_bg, R.color.main_text9)
                return@setDefaultRefreshHeaderCreator ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate)
            }

            SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
                return@setDefaultRefreshFooterCreator ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        initOKUtils()
        initBaidu()
        initImagerUtils()
        initLifecyler()
        initBugler()
        initJGPush()
        SpeechUtility.createUtility(this,SpeechConstant.APPID +"=5d1431bf")
        AudioUtils.getInstance(applicationContext)!!.initVideo()
    }

    fun initBaidu() {
        mLocationClien = LocationClient(applicationContext)
    }

    fun initLifecyler() {
        if (applicationInfo.packageName == getCurProcessName(this)) {
            initData()
            activityLifecycle()
        }
    }

    fun initOKUtils() {
        OkHttpUtils.getInstance()
                .init(this)
                .debug(true, "okHttp")
                .timeout((20 * 1000).toLong())
    }

    fun initImagerUtils() {
        Fresco.initialize(this)
    }

    fun initBugler() {
        //        CrashReport.initCrashReport(getApplicationContext(), "2b7d577c75", true);
        CrashReport.initCrashReport(applicationContext);
    }

    fun initJGPush() {
        JPushInterface.setDebugMode(true)
        JPushInterface.init(this)
    }

    private fun activityLifecycle() {
        registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

            }

            override fun onActivityStarted(activity: Activity) {
                isExitOrHome = false
                if (count === 0 && isStop) {

                    isStop = false
                }
                count++
            }

            override fun onActivityResumed(activity: Activity) {

            }

            override fun onActivityPaused(activity: Activity) {

            }

            override fun onActivityStopped(activity: Activity) {
                count--
                if (count === 0) {
                    isStop = true
                }
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

            }

            override fun onActivityDestroyed(activity: Activity) {

            }
        })
    }

    private fun getCurProcessName(context: Context): String? {
        val pid = android.os.Process.myPid()
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (appProcess in activityManager.runningAppProcesses) {
            if (appProcess.pid == pid) {
                return appProcess.processName
            }
        }
        return null
    }

    private fun initData() {
        Constant.isFristApp = PreferencesUtils().isFristApp()
//        Constant.userInfo = PreferencesUtils().toGetUserInfo()
//        Constant.token = Constant.userInfo?.userToken?:""
        ToolUtils.obtainScreenWH(this)
    }

    fun startActivty(context: Context) {
        GmContentVo.setUserInfom(UserDbVo())
        val intent = Intent(context, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
    }

    /**
     * 添加天气观察者并通知被观察
     */
    fun addAndRefresh(o: WeatherObserver?) {
        weatherManger.registerOberver(o)
        weatherManger.notifyObserver()
    }

    //删除订阅者
    fun removerAllObserver() {
        weatherManger.removerAll()
    }

    /**
     * 添加天气观察者
     */
    fun addObserver(o: WeatherObserver?) {
        weatherManger.registerOberver(o)
    }

    fun WeatherNotifyObjserver() {
        weatherManger.notifyObserver()
    }
}