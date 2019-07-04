package com.zzzh.akhalteke.ui

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.lipo.views.MyProgreeDialog
import com.lipo.views.ToastView
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.retrofit.gsonFactory.ResultException
import com.zzzh.akhalteke.utils.*
import kotlinx.android.synthetic.main.gm_title.*
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.RelativeLayout
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClientOption
import com.google.gson.Gson
import com.yanzhenjie.permission.Permission
import com.zzzh.akhalteke.BaseApplication
import com.zzzh.akhalteke.bean.vo.GetUserInfom
import com.zzzh.akhalteke.bean.vo.GmContentVo
import com.zzzh.akhalteke.bean.vo.WeatherVo
import com.zzzh.akhalteke.dbHelp.UserDbHelp
import com.zzzh.akhalteke.dbHelp.Vo.UserDbVo
import com.zzzh.akhalteke.mvp.contract.UpdateAddressContract
import com.zzzh.akhalteke.mvp.contract.WeatherContract
import com.zzzh.akhalteke.mvp.model.UpdateAddressModel
import com.zzzh.akhalteke.mvp.model.WeatherModel
import com.zzzh.akhalteke.mvp.presenter.UpdateAddressPresenter
import com.zzzh.akhalteke.mvp.presenter.WeatherPresenter
import com.zzzh.akhalteke.service.UpdateAddressIntentService
import com.zzzh.akhalteke.ui.verify.newverify.AuthenticationNewActivity
import com.zzzh.akhalteke.ui.verify.newverify.CarNewActivity
import com.zzzh.akhalteke.ui.verify.newverify.DrivingNewMemberActivity
import com.zzzh.akhalteke.ui.weather.WeatherActivity
import com.zzzh.akhalteke.weather.BaseDrawer
import com.zzzh.akhalteke.weather.Observer.WeatherObserver
import kotlinx.android.synthetic.main.gm_rl_title_right_transparency.*
import java.util.*


abstract class BaseActivity : AppCompatActivity(), UpdateAddressContract.View, WeatherContract.View {


    lateinit var mContext: BaseActivity
    lateinit var progressDialog: MyProgreeDialog
    val appManager: AppManager = AppManager.appManager

    companion object {
        val CNT_PARAMETE_TITLE: String = "param_title"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT//竖屏
        mContext = this
        progressDialog = MyProgreeDialog(this)
        appManager.addActivity(this)
        initTrack()
        setContentView(savedInstanceState);
        if (intent != null) {
            val title = intent.getStringExtra(CNT_PARAMETE_TITLE)
            if (title != null) {
                setTitle(title)
            }
        }
    }

    /**
     * 更新地理位置
     */
    private var mPresenter: UpdateAddressPresenter? = null
    /**
     * 天气控制者
     */
    private var mWeatherPresenter: WeatherPresenter? = null

    /**
     * 初始化设置
     */
    fun initTrack() {
        mPresenter = UpdateAddressPresenter()
        mPresenter!!.initMvp(this, UpdateAddressModel())
        mWeatherPresenter = WeatherPresenter()
        mWeatherPresenter!!.initMvp(this, WeatherModel())
    }

    override fun Success(t: Any?) {
    }

    override fun Error(ex: Throwable) {
    }

    override fun Complise() {
    }

    fun setTitle(str: String) {
        if (StringUtil.isEmpty(str)) return
        if (tv_activity_title != null) {
            tv_activity_title.text = str
        }
        if (tv_activity_transparency_title != null) {
            tv_activity_transparency_title.text = str
        }
    }

    open fun onHomeBack(v: View) {
        finishBase()
    }

    abstract fun setContentView(savedInstanceState: Bundle?);

    open fun showToast(toastStr: String) {
        ToastView.setToasd(mContext, toastStr)
    }

    fun test() {
        PermissionUtils.showPermission(mContext, "", arrayOf("")) {

        }
    }
//    fun getData() {
//        if(RetrofitFactory.judgmentNetWork(mContext)){
//            showProgress()
//            RetrofitFactory.createMainRetrofit().shopGetAll()
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(this::onNext,this::onError,this::onComplise)
//        }
//    }
//    fun onNext(entry: BaseEntity<UserInfo>) {
//        showToast(JSON.toJSONString(entry))
//    }

    fun onError(ex: Throwable) {
        dismissProgress()
        onHttpFinish()
        try {
            val error = if (ex is SocketTimeoutException) {
                "网络连接超时，请稍后再试..."
            } else if (ex is ConnectException) {
                "网络连接超时，请稍后再试..."
            } else if (ex is UnknownHostException) {
                "网络连接超时，请稍后再试..."
            } else {
                if (ex is ResultException) {
                    when (ex.status) {

                    }
                    (ex as ResultException).msg   //抛出异常，抓取数据
                } else {
                    "网络连接异常，请稍候重试"
                }
            }
            ToastView.setToasd(mContext, error)
            ex.printStackTrace()
        } catch (e1: IOException) {
            e1.printStackTrace()
        } finally {

        }
    }

    open fun onComplise() {
        dismissProgress()
        onHttpFinish()
    }

    open fun onHttpFinish() {

    }


    fun jumpTo(clazz: Class<*>) {
        val intentB = Intent()
        intentB.setClass(mContext, clazz)
        mContext.startActivity(intentB)
        mContext.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out)
    }

    fun jumpTo(clazz: Class<*>, title: String) {
        val intentB = Intent()
        intentB.setClass(mContext, clazz)
        intentB.putExtra(BaseActivity.CNT_PARAMETE_TITLE, title)
        mContext.startActivity(intentB)
        mContext.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out)
    }

    fun jumpTo(clazz: Class<*>, bundle: Bundle) {
        val intentB = Intent()
        intentB.setClass(mContext, clazz)
        intentB.putExtras(bundle)
        mContext.startActivity(intentB)
        mContext.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out)
    }

    fun jumpTo(clazz: Class<*>, bundle: Bundle, title: String) {
        val intentB = Intent()
        intentB.setClass(mContext, clazz)
        intentB.putExtras(bundle)
        intentB.putExtra(BaseActivity.CNT_PARAMETE_TITLE, title)
        mContext.startActivity(intentB)
        mContext.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out)
    }

    fun jumpWeatherTo(clazz: Class<*>, bundle: Bundle, title: String) {
        val intentB = Intent()
        intentB.setClass(mContext, clazz)
        intentB.putExtras(bundle)
        intentB.putExtra(BaseActivity.CNT_PARAMETE_TITLE, title)
        mContext.startActivity(intentB)
        mContext.overridePendingTransition(R.anim.weather_up, R.anim.weather_down)
    }

    fun jumpToWeather(clazz: Class<*>, bundle: Bundle, title: String) {
        val intentB = Intent()
        intentB.setClass(mContext, clazz)
        intentB.putExtras(bundle)
        intentB.putExtra(BaseActivity.CNT_PARAMETE_TITLE, title)
        mContext.startActivity(intentB)
        mContext.overridePendingTransition(R.anim.weather_up, R.anim.weather_down)
    }

    fun jumpToFoResult(clazz: Class<*>, resultCode: Int) {
        val intentB = Intent()
        intentB.setClass(mContext, clazz)
        mContext.startActivityForResult(intentB, resultCode)
        mContext.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out)
    }

    fun jumpToFoResult(clazz: Class<*>, bundle: Bundle, resultCode: Int) {
        val intentB = Intent()
        intentB.setClass(mContext, clazz)
        intentB.putExtras(bundle)
        mContext.startActivityForResult(intentB, resultCode)
        mContext.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out)
    }

    fun jumpToBU(clazz: Class<*>) {
        val intentB = Intent()
        intentB.setClass(mContext, clazz)
        mContext.startActivity(intentB)
        mContext.overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out)
    }

    fun jumpToBU(clazz: Class<*>, bundle: Bundle) {
        val intentB = Intent()
        intentB.setClass(mContext, clazz)
        intentB.putExtras(bundle)
        mContext.startActivity(intentB)
        mContext.overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out)
    }

    fun jumpToFoResulBU(clazz: Class<*>, resultCode: Int) {
        val intentB = Intent()
        intentB.setClass(mContext, clazz)
        mContext.startActivityForResult(intentB, resultCode)
        mContext.overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out)
    }

    fun jumpToFoResultBU(clazz: Class<*>, bundle: Bundle, resultCode: Int) {
        val intentB = Intent()
        intentB.setClass(mContext, clazz)
        intentB.putExtras(bundle)
        mContext.startActivityForResult(intentB, resultCode)
        mContext.overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out)
    }

    fun jumpToFoResulBU(clazz: Class<*>, bundle: Bundle, title: String, resultCode: Int) {
        val intentB = Intent()
        intentB.setClass(mContext, clazz)
        intentB.putExtras(bundle)
        intentB.putExtra(BaseActivity.CNT_PARAMETE_TITLE, title)
        mContext.startActivityForResult(intentB, resultCode)
        mContext.overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out)
    }

    fun finishBase() {
        finish()
        overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
    }

    fun onFinishWeather() {
        finish()
        overridePendingTransition(R.anim.weather_finish_up, R.anim.weather_finish_down)
    }

    fun finishBaseWeather() {
        finish()
        overridePendingTransition(R.anim.weather_up, R.anim.weather_down)
    }

    fun finishBaseBU() {
        finish()
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out)
    }

    fun showProgress() {
        ToolUtils.showDialog(progressDialog)
    }

    fun dismissProgress() {
        ToolUtils.dismissDialog(progressDialog)
    }

    override fun onDestroy() {
        super.onDestroy()
        ToolUtils.dismissDialog(progressDialog)
        appManager.finishActivity(this)
    }

    /**
     * @METHOD 设置竖向管理器
     * @param rlv
     */
    open fun setMangager(rlv: RecyclerView) {
        var layout = GridLayoutManager(mContext, 1)
        layout.orientation = GridLayoutManager.VERTICAL
        rlv.layoutManager = layout
    }

    /**
     * 设置横向管理器
     */
    open fun setHorizontalMangager(rlv: RecyclerView) {
        var layout = GridLayoutManager(mContext, 1)
        layout.orientation = GridLayoutManager.HORIZONTAL
        rlv.layoutManager = layout
    }

    /**
     * 设置竖向管理器
     */
    open fun setMangager(rlv: RecyclerView, number: Int, orientai: Int) {
        var layout = GridLayoutManager(mContext, number)
        layout.orientation = orientai
        rlv.layoutManager = layout
    }

    /**
     * 设置web
     */
    protected fun setWebVIewSetting(webView: WebView) {
        val settings = webView.settings
        settings.javaScriptEnabled = true
        // 设置可以支持缩放
        settings.setSupportZoom(true)
        //加载网络数据，不读取本地
        settings.cacheMode = WebSettings.LOAD_NO_CACHE;
        // 设置出现缩放工具
        settings.builtInZoomControls = true
        /*       //开启缓存
        settings.setDomStorageEnabled(true);*/
        //自适应屏幕
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        //设置自适应屏幕，两者合用
        settings.useWideViewPort = true //将图片调整到适合webview的大小
        settings.loadWithOverviewMode = true // 缩放至屏幕的大小
        settings.displayZoomControls = false //隐藏原生的缩放控件
        settings.loadsImagesAutomatically = true //支持自动加载图片
        settings.defaultTextEncodingName = "utf-8"//设置编码格式
    }

    /**
     * 请求权限
     */
    fun showPresnmissAddress(doEvent: ContinueDoEvent) {
        PermissionUtils.showPermission(mContext, "",
                arrayOf(Permission.ACCESS_COARSE_LOCATION)) {
            UpdateAddressIntentService.startActionFoo(mContext)
            doEvent.doEvent()
        }

    }

    /**
     * 初始化百度
     */
    fun initBaidu() {
        UpdateAddressIntentService.startActionFoo(mContext)

    }

    /**
     * 天气观察者
     */
    private var mVo: WeatherObserver? = null

    /**
     * 天气加载
     */
    fun getWeatherload(o: WeatherObserver) {
        this.mVo = o;
        val option = LocationClientOption()
        option.locationMode = LocationClientOption.LocationMode.Hight_Accuracy
        option.setCoorType("bd09ll")
        option.setScanSpan(0)
        option.isOpenGps = true
        option.isLocationNotify = false
        option.setIgnoreKillProcess(false)
        option.SetIgnoreCacheException(false)
        option.setWifiCacheTimeOut(5 * 60 * 1000)
        option.setEnableSimulateGps(true)
        option.setIsNeedAddress(true)
        BaseApplication.toInstance().mLocationClien!!.locOption = option
        BaseApplication.toInstance().mLocationClien!!.registerLocationListener(
                object : BDAbstractLocationListener() {
                    override fun onReceiveLocation(location: BDLocation?) {
                        val latitude = location!!.latitude    //获取纬度信息
                        val longitude = location.longitude    //获取经度信息
                        val time = Date().time / 1000
                        //            获取城市编码
//                        BaseApplication.toInstance().mLocationClien!!.stop()
                        val coorType = location.coorType
                        //提交轨迹
                        mPresenter!!.submitPointAdd(mContext!!, latitude.toString(),
                                longitude.toString(), time.toString(), coorType)
                        //提交天气
                        mWeatherPresenter!!.requestWeather(this@BaseActivity,
                                latitude.toString(), longitude.toString(),
                                true, false, true, true, true)
                    }

                })
        BaseApplication.toInstance().mLocationClien!!.start()
    }

    /**
     * 更新用户信息
     */
    fun updateUserInfom(userinfom: GetUserInfom) {
        val dbHelp = UserDbHelp.get_Instance(mContext)
        dbHelp!!.updateUserInfom(
            userinfom.id, userinfom.phone, userinfom.ifRealCertification,
            userinfom.ifDriver, userinfom.ifCar, userinfom.name, userinfom.number, userinfom.portrait,
            userinfom.plateNumber, userinfom.role, userinfom.carPlateColourId, userinfom.ifVert
        )
    }

    interface ContinueDoEvent {
        fun doEvent()
    }

    /**
     * 实名认证
     */
    fun showGoRenZhen(userinfom: GetUserInfom, doEvent: BaseActivity.ContinueDoEvent) {
        if (userinfom.ifRealCertification == "3") {
            showDialogContinue(1, userinfom.phone!!)
        } else if (userinfom.ifCar == "3") {
            showDialogContinue(2, userinfom.phone!!)
        } else if (userinfom.ifDriver == "3") {
            showDialogContinue(3, userinfom.phone!!)
        } else {
            doEvent.doEvent()
        }
    }

    /**
     * 实名认证
     */
    fun showGoRenZhen(userinfom: UserDbVo?) {
        if (userinfom == null) return
        if (userinfom.ifReal == "3") {
            showDialogContinue(1, userinfom.phone)
            return
        } else if (userinfom.ifCar == "3") {
            showDialogContinue(2, userinfom.phone)
            return
        } else if (userinfom.ifDriver == "3") {
            showDialogContinue(3, userinfom.phone)
            return
        }
    }

    /**
     * 设置 通用对话框
     */
    private fun showDialogContinue(i: Int, phone: String) {
        var title = ""
        when (i) {
            1 -> {
                title = getString(R.string.us_authenticatio_again)
            }
            2 -> {
                title = getString(R.string.car_authenticatio_again)
            }
            3 -> {
                title = getString(R.string.driver_authenticatio_again)
            }

        }
        DialogUtil.showDialogContinueAuthentication(mContext, title, 2, false, object : DialogUtil.ContinueInterface {
            override fun dialogDismiss(type: Int) {
            }

            override fun continuer() {
                when (i) {
                    1 -> {//实名
                        val bundle = Bundle()
                        bundle.putString(AuthenticationNewActivity.USERPHONE, phone)
                        bundle.putString(AuthenticationNewActivity.TYPE, AuthenticationNewActivity.TYPE_FINISH)
                        jumpTo(AuthenticationNewActivity::class.java, bundle)
                    }
                    2 -> {//车辆
                        val bundle = Bundle()
                        bundle.putString(CarNewActivity.TYPE, CarNewActivity.ME_INFOM_TYPE)
                        jumpTo(CarNewActivity::class.java, bundle)
                    }
                    3 -> {//驾驶员
                        val bundle = Bundle()
                        bundle.putString(DrivingNewMemberActivity.TYPE, DrivingNewMemberActivity.TYPE_Finish)
                        jumpTo(DrivingNewMemberActivity::class.java, bundle)
                    }
                }
            }
        })
    }

    /**
     * 设置天气动画
     */
    open fun SwtWeatherType(weather: String): BaseDrawer.Type {
        return WeatherUtil.getInstance()!!.getWeatherType(weather)
    }

    /**
     * 天气结果
     */
    override fun WeatherSuccess(t: Any?) {
        FileUtil.saveText(this@BaseActivity, t.toString())
        val gson = Gson()
        val data = gson.fromJson<WeatherVo>(t.toString(), WeatherVo::class.java)
        GmContentVo.setWeatherVo(data)
        BaseApplication.toInstance().addAndRefresh(mVo)
    }

    /**
     * 天气结果
     */
    override fun WeatherComplise() {
        this.onComplise()
    }

    /**
     * 天气结果
     */
    override fun WeatherError(ex: Throwable) {
        this.onError(ex)
    }

    /**
     * 设置天监听
     */
    open fun setWeatherOnClick(rlv: RelativeLayout) {
        rlv.setOnClickListener {
            if (GmContentVo.getWeatherVo() == null) {
                showToast("暂无天气信息")
                return@setOnClickListener
            }
            val vo = GmContentVo.getWeatherVo()
            val bunlde = Bundle()
            bunlde.putParcelable(WeatherActivity.TYPE, vo)
            jumpWeatherTo(WeatherActivity::class.java, bunlde, "马道天气")
        }
    }

}