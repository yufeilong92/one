package com.zzzh.akhalteke.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
//import com.igexin.sdk.PushManager
import com.lipo.views.MyProgreeDialog
import com.lipo.views.ToastView
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.bean.vo.GetUserInfom
import com.zzzh.akhalteke.bean.vo.GmContentVo
import com.zzzh.akhalteke.bean.vo.RlvSelectVo
import com.zzzh.akhalteke.dbHelp.UserDbHelp
import com.zzzh.akhalteke.dbHelp.Vo.UserDbVo
import com.zzzh.akhalteke.retrofit.gsonFactory.ResultException
import com.zzzh.akhalteke.ui.verify.newverify.AuthenticationNewActivity
import com.zzzh.akhalteke.ui.verify.newverify.CarNewActivity
import com.zzzh.akhalteke.ui.verify.newverify.DrivingNewMemberActivity
import com.zzzh.akhalteke.ui.weather.WeatherActivity
import com.zzzh.akhalteke.utils.*
import com.zzzh.akhalteke.utils.ExampleUtil.showToast

import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

abstract class BaseFragment : Fragment() {

    lateinit var mContext: Activity
    lateinit var progressDialog: MyProgreeDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = activity!!
        progressDialog = MyProgreeDialog(mContext)

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(setContentView(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setCreatedContentView(view, savedInstanceState)

    }

    abstract fun setContentView(): Int
    abstract fun setCreatedContentView(view: View, savedInstanceState: Bundle?)
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

    fun onComplise() {
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
    fun jumpToFoResult(clazz: Class<*>, resultCode: Int) {
        val intentB = Intent()
        intentB.setClass(mContext, clazz)
        startActivityForResult(intentB, resultCode)
        mContext.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out)
    }

    fun jumpToFoResult(clazz: Class<*>, bundle: Bundle, resultCode: Int) {
        val intentB = Intent()
        intentB.setClass(mContext, clazz)
        intentB.putExtras(bundle)
        startActivityForResult(intentB, resultCode)
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
        startActivityForResult(intentB, resultCode)
        mContext.overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out)
    }

    fun jumpToFoResulBU(clazz: Class<*>, resultCode: Int, title: String) {
        val intentB = Intent()
        intentB.setClass(mContext, clazz)
        intentB.putExtra(BaseActivity.CNT_PARAMETE_TITLE, title)
        startActivityForResult(intentB, resultCode)
        mContext.overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out)
    }

    fun jumpToFoResulBU(clazz: Class<*>, resultCode: Int, key: String, m: ArrayList<RlvSelectVo>, title: String) {
        val intentB = Intent()
        intentB.setClass(mContext, clazz)
        intentB.putExtra(BaseActivity.CNT_PARAMETE_TITLE, title)
        intentB.putExtra(key, m)
        startActivityForResult(intentB, resultCode)
        mContext.overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out)
    }

    fun jumpToFoResultBU(clazz: Class<*>, bundle: Bundle, resultCode: Int) {
        val intentB = Intent()
        intentB.setClass(mContext, clazz)
        intentB.putExtras(bundle)
        startActivityForResult(intentB, resultCode)
        mContext.overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out)
    }

    fun finishBase() {
        mContext.finish()
        mContext.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out)
    }

    fun finishBaseBU() {
        mContext.finish()
        mContext.overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out)
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
    }

    open fun setMangager(rlv: RecyclerView) {
        var layout = GridLayoutManager(mContext, 1)
        layout.orientation = GridLayoutManager.VERTICAL
        rlv.layoutManager = layout
    }

    open fun setMangager(rlv: RecyclerView, id: Int, orientai: Int) {
        var layout = FullyGridLayoutManager(mContext, id)
        layout.orientation = orientai
        rlv.layoutManager = layout
    }

    fun updateUserInfom(userinfom: GetUserInfom) {
        val dbHelp = UserDbHelp.get_Instance(mContext)
        dbHelp!!.updateUserInfom(userinfom.id, userinfom.phone, userinfom.ifRealCertification,
                userinfom.ifDriver, userinfom.ifCar, userinfom.name, userinfom.number, userinfom.portrait,
                userinfom.plateNumber, userinfom.role, userinfom.carPlateColourId,userinfom.ifVert)
    }


    interface ContinueDoEvent {
        fun doEvent()
    }

    fun showGoRenZhen(userinfom: GetUserInfom, doEvent: BaseFragment.ContinueDoEvent) {
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

    fun showGoRenZhen(userinfom: UserDbVo?) {
        if (userinfom == null) return
        if (userinfom.ifReal == "3") {
            showDialogContinue(1, userinfom.phone)
            return
        } else if (userinfom.ifCar == "3") {
            showDialogContinue(3, userinfom.phone)
            return
        } else if (userinfom.ifDriver == "3") {
            showDialogContinue(2, userinfom.phone)
            return
        }
    }

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
     * 设置天监听
     */
    open fun setWeatherOnClick(rlv: RelativeLayout) {
        rlv.setOnClickListener {
            if (GmContentVo.getWeatherVo() == null) {
                showToast("暂无天气信息", activity)
                return@setOnClickListener
            }
            val vo = GmContentVo.getWeatherVo()
            val bunlde = Bundle()
            bunlde.putParcelable(WeatherActivity.TYPE, vo)
            jumpWeatherTo(WeatherActivity::class.java, bunlde, "马道天气")
        }
    }
}