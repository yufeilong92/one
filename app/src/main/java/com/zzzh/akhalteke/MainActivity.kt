package com.zzzh.akhalteke

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.KeyEvent
import android.view.View
import com.google.gson.Gson
import com.vector.appupdatedemo.ext.cancelProgressDialog
import com.vector.appupdatedemo.ext.showProgressDialog
import com.vector.update_app.UpdateAppBean
import com.vector.update_app.UpdateAppManager
import com.vector.update_app.UpdateCallback
import com.vector.update_app_kotlin.check
import com.vector.update_app_kotlin.updateApp
import com.yanzhenjie.permission.Permission
import com.zzzh.akhalteke.bean.DataMessageVo
import com.zzzh.akhalteke.bean.vo.GetUserInfom
import com.zzzh.akhalteke.bean.vo.GmContentVo
import com.zzzh.akhalteke.bean.vo.UpDataVo
import com.zzzh.akhalteke.bean.vo.WeatherVo
import com.zzzh.akhalteke.dbHelp.UserDbHelp
import com.zzzh.akhalteke.dbHelp.Vo.UserDbVo
import com.zzzh.akhalteke.fragment.home.*
import com.zzzh.akhalteke.mvp.contract.CheckInfomContract
import com.zzzh.akhalteke.mvp.contract.MainContract
import com.zzzh.akhalteke.mvp.model.CheckInfomModel
import com.zzzh.akhalteke.mvp.presenter.CheckInfomPresenter
import com.zzzh.akhalteke.net.updateapp.UpdateAppHttpUtil
import com.zzzh.akhalteke.net.util.CProgressDialogUtils
import com.zzzh.akhalteke.ui.BaseActivity
import com.zzzh.akhalteke.ui.login.CodeActivity
import com.zzzh.akhalteke.ui.verify.DrivingMemberActivity
import com.zzzh.akhalteke.ui.verify.newverify.AuthenticationNewActivity
import com.zzzh.akhalteke.ui.verify.newverify.CarNewActivity
import com.zzzh.akhalteke.ui.verify.newverify.DrivingNewMemberActivity
import com.zzzh.akhalteke.utils.*
import com.zzzh.akhalteke.weather.Observer.WeatherObserver
import com.zzzh.akhalteke.xunfei.MyIntentService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.gm_weather_title_root.*
import org.json.JSONObject
import java.io.File


/**
 * @Author : YFL  is Creating a porject in Lenovo
 * @Email : yufeilong92@163.com
 * @Time :2019/3/12 10:42
 * @Purpose :首页
 */

class MainActivity : BaseActivity(), CheckInfomContract.View, WeatherObserver{



    private var mCheckInfomPresenter: CheckInfomPresenter? = null
    override fun setContentView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_main)
        initFragmentList()
        initJgPush()
        mCheckInfomPresenter = CheckInfomPresenter()
        mCheckInfomPresenter!!.initMvp(this, CheckInfomModel())
        initUpdata()
//        MyIntentService.startActionFoo(mContext,"你已经超速，你已经超速，你已经超速，你已经超速，你已经超速，你已经超速，你已经超速，你已经超速，你已经超速","")
    }

    override fun onStart() {
        super.onStart()
        PermissionUtils.showPermission(
            mContext, "",
            arrayOf(Permission.ACCESS_COARSE_LOCATION)
        ) {
            if (GmContentVo.getWeatherVo() == null) {
                getWeatherload(this)
            }
        }


    }

    override fun onResume() {
        super.onResume()
        if (GmContentVo.getWeatherVo() != null) {
            BaseApplication.toInstance().WeatherNotifyObjserver()
        }
    }


    fun initJgPush() {
        val dbHelp = UserDbHelp.get_Instance(mContext)
        val userInfom = dbHelp!!.getUserInfom()
        if (userInfom == null || StringUtil.isEmpty(userInfom.userId)) return
        val util = JgPushUtil.getInstance(mContext)
        val beab = JgPushUtil.TagAliasBean()
        beab.isAliasAction = true
        beab.alias = userInfom.userId
        beab.action = JgPushUtil.ACTION_SET
        util!!.handleAction(mContext, DataMessageVo.JGRQUEST, beab)
    }

    private fun checkInfom() {
        val dbHelp = UserDbHelp.get_Instance(mContext)
        val userInfom = dbHelp!!.getUserInfom()
        if (userInfom!!.ifReal == "2") {
            showAuthentication(userInfom)
        } else if (userInfom!!.ifDriver == "2") {
            showDriverDialog(userInfom)
        } else if (userInfom.ifCar == "2") {
            showCarDialog(userInfom)
        }
    }

    fun showCarDialog(userInfom: UserDbVo?) {
        if (StringUtil.isEmpty(userInfom!!.ifCar) || userInfom!!.ifCar != "1") {
            DialogUtil.showDialogContinueAuthentication(mContext, getString(R.string.you_authentication_car), 1, true,
                object : DialogUtil.ContinueInterface {
                    override fun dialogDismiss(type: Int) {

                    }

                    override fun continuer() {
                        val bundle = Bundle()
                        bundle.putString(CarNewActivity.TYPE, "")
                        jumpTo(CarNewActivity::class.java, bundle)
                    }
                })
        }
    }

    fun showDriverDialog(userInfom: UserDbVo?) {
        if (StringUtil.isEmpty(userInfom!!.ifDriver) || userInfom!!.ifDriver != "1") {
            DialogUtil.showDialogContinueAuthentication(mContext,
                getString(R.string.you_authentication_driver),
                1,
                true,
                object : DialogUtil.ContinueInterface {
                    override fun dialogDismiss(type: Int) {

                    }

                    override fun continuer() {
                        val bundle = Bundle()
                        bundle.putString(DrivingNewMemberActivity.TYPE, DrivingNewMemberActivity.TYPE_NO_FINISH)
                        jumpTo(DrivingNewMemberActivity::class.java, bundle)
                    }

                })
        }
    }

    fun showAuthentication(userInfom: UserDbVo?) {
        if (StringUtil.isEmpty(userInfom!!.ifReal) || userInfom!!.ifReal != "1") {
            DialogUtil.showDialogContinueAuthentication(mContext, getString(R.string.you_authentication_us), 1, true,
                object : DialogUtil.ContinueInterface {
                    override fun dialogDismiss(type: Int) {
                    }

                    override fun continuer() {
                        val bundle = Bundle()
                        bundle.putString(AuthenticationNewActivity.TYPE, AuthenticationNewActivity.TYPE_NO_FINISH)
                        bundle.putString(AuthenticationNewActivity.USERPHONE, userInfom.phone)
                        jumpTo(AuthenticationNewActivity::class.java, bundle)

                    }
                })
        }

    }

    fun initUpdata() {
        //下载路径
        updateDiy1()
    }

    fun updateDiy1() {
        //下载路径
        val path = mContext.externalCacheDir.absolutePath + File.separator
        //自定义参数
        updateApp(DataMessageVo.mUpdateUrl, UpdateAppHttpUtil())
        //自定义配置
        {
            //以下设置，都是可选
            //设置请求方式，默认get
            isPost = false
            //添加自定义参数，默认version=1.0.0（app的versionName）；apkKey=唯一表示（在AndroidManifest.xml配置）
//            setParams(params)
            //设置点击升级后，消失对话框，默认点击升级后，对话框显示下载进度
            hideDialogOnDownloading()
            //设置头部，不设置显示默认的图片，设置图片后自动识别主色调，然后为按钮，进度条设置颜色
            topPic = R.mipmap.top_8

            //为按钮，进度条设置颜色。
            themeColor = 0xffffac5d.toInt()
            //设置apk下砸路径，默认是在下载到sd卡下/Download/1.0.0/test.apk
            targetPath = path
            //设置appKey，默认从AndroidManifest.xml获取，如果，使用自定义参数，则此项无效
//                setAppKey("ab55ce55Ac4bcP408cPb8c1Aaeac179c5f6f")

        }
            .check {
                onBefore {
                    showProgressDialog(this@MainActivity)
                }
                //自定义解析
                parseJson {
                    val data = Gson().fromJson<UpDataVo>(it, UpDataVo::class.java)
                    var isUpdate = false
                    if (data.type == 1) {
                        isUpdate = true
                    }
                    val code = Utils.getAppVersionCode(mContext)
                    var isUpdataApp = "No"
                    if (code < data.versionCode!!) {
                        isUpdataApp = "Yes"
                    }
                    val jsonObject = JSONObject(it)
                    UpdateAppBean()
                        //（必须）是否更新Yes,No
                        .setUpdate(isUpdataApp)
                        //（必须）新版本号，
                        .setNewVersion(data.version)
                        //（必须）下载地址
                        .setApkFileUrl(data.path)
                        //（必须）更新内容
                        .setUpdateLog(data.updateLog)
                        //大小，不设置不显示大小，可以不设置
                        .setTargetSize(data.appSize)
                        //是否强制更新，可以不设置
                        .setConstraint(isUpdate)
                    //设置md5，可以不设置
//                                .setNewMd5(jsonObject.optString("new_md5"))
                }
                noNewApp {
                    mCheckInfomPresenter!!.requestCheckInfom(mContext)
                }
                onAfter {
                    cancelProgressDialog(this@MainActivity)
                }
            }
    }

    fun initFragmentList() {
        val listFragment = mutableListOf<Fragment>()
        val home = HomeHomeFragment.newInstance("", "")
        listFragment.add(home)
        BaseApplication.toInstance().addObserver(home);
        val waybill = HomeWayBillFragment.newInstance("", "")
        listFragment.add(waybill)
        BaseApplication.toInstance().addObserver(waybill);
        val find = HomeDiscoverFragment.newInstance("", "")
        listFragment.add(find)
        BaseApplication.toInstance().addObserver(find)
        listFragment.add(HomeMeFragment.newInstance("", ""))

        val mSfm = supportFragmentManager
        for (fragment in listFragment) {
            val transaction = mSfm.beginTransaction()
            transaction.add(R.id.fl_content, fragment).hide(fragment).commit()
        }
        mSfm.beginTransaction().show(listFragment[0]).commit()
        rdg_layout.setOnCheckedChangeListener { group, checkedId ->
            val msfm = supportFragmentManager
            when (checkedId) {
                R.id.rdb_select_home -> {
                    showSelectFragment(msfm, listFragment, R.id.fl_content, 0)
                }
                R.id.rdb_select_good -> {
                    showSelectFragment(msfm, listFragment, R.id.fl_content, 1)
                }
                R.id.rdb_select_odd -> {
                    showSelectFragment(msfm, listFragment, R.id.fl_content, 2)
                }
                R.id.rdb_select_me -> {
                    showSelectFragment(msfm, listFragment, R.id.fl_content, 3)
                }
            }
        }

    }

    /**
     * @param sfm    fragment管理器
     * @param list   fragment集合
     * @param layout 显示fragment 布局
     * @param id     要显示的集合的fragment 的几个
     * @return
     */
    private fun showSelectFragment(
        sfm: FragmentManager?,
        list: MutableList<Fragment>,
        layout: Int,
        id: Int
    ) {
        var sfm = sfm
        if (id + 1 > list.size) {
            throw IndexOutOfBoundsException("超出集合长度") as Throwable
        }
        if (sfm == null) {
            sfm = supportFragmentManager
        }
        val transaction = sfm!!.beginTransaction()
        val fragment = list[id]
        if (!fragment.isVisible) {
            if (!fragment.isAdded) {
                transaction.add(layout, fragment)
            } else {
                for (item in list) {
                    sfm.beginTransaction().hide(item).commit()
                }
                transaction.show(fragment)
            }
            transaction.commit()
        }
    }

    override fun CheckSuccess(t: Any?) {
        if (t == null) return
        val data: GetUserInfom = t as GetUserInfom
        updateUserInfom(data)
        checkInfom()
    }

    override fun CheckError(ex: Throwable) {
        this.onError(ex)
    }

    override fun CheckComplise() {
        this.onComplise()
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.keyCode === KeyEvent.KEYCODE_BACK && event.action === KeyEvent.ACTION_UP) {
            showToast(getString(R.string.out_app))
            if (Utils.handleOnDoubleClick()) {
                BaseApplication.toInstance().removerAllObserver()
                finish()
                appManager.finishAllActivity()
            }
            return true
        }
        return super.dispatchKeyEvent(event)
    }

    /**
     * 更新天气
     */
    override fun upDataWeather() {
//        val vo = GmContentVo.getWeatherVo() ?: return
//        bindViewData(vo)
    }

    fun bindViewData(data: WeatherVo) {
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

}
