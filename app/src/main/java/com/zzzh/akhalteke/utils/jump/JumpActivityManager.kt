package com.zzzh.akhalteke.utils.jump

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.zzzh.akhalteke.MainActivity
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.ui.BaseActivity
import com.zzzh.akhalteke.ui.login.CodeActivity
import com.zzzh.akhalteke.ui.login.LoginActivity

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.utils
 * @Email : yufeilong92@163.com
 * @Time :2019/6/14 10:37
 * @Purpose :跳转管理
 */
class JumpActivityManager(var activity: Activity) {


    companion object {
        //被companion object包裹的语句都是private的
        private var singletonInstance: JumpActivityManager? = null

        @Synchronized
        fun getInstance(m: Activity): JumpActivityManager? {
            if (singletonInstance == null) {
                singletonInstance =
                    JumpActivityManager(m)
            }
            return singletonInstance
        }
    }


    fun jumpTo(clazz: Class<*>) {
        val intentB = Intent()
        intentB.setClass(activity, clazz)
        activity.startActivity(intentB)
        activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out)
    }

    fun jumpTo(clazz: Class<*>, title: String) {
        val intentB = Intent()
        intentB.setClass(activity, clazz)
        intentB.putExtra(BaseActivity.CNT_PARAMETE_TITLE, title)
        activity.startActivity(intentB)
        activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out)
    }

    fun jumpTo(clazz: Class<*>, bundle: Bundle) {
        val intentB = Intent()
        intentB.setClass(activity, clazz)
        intentB.putExtras(bundle)
        activity.startActivity(intentB)
        activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out)
    }

    fun jumpTo(clazz: Class<*>, bundle: Bundle, title: String) {
        val intentB = Intent()
        intentB.setClass(activity, clazz)
        intentB.putExtras(bundle)
        intentB.putExtra(BaseActivity.CNT_PARAMETE_TITLE, title)
        activity.startActivity(intentB)
        activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out)
    }

    fun jumpWeatherTo(clazz: Class<*>, bundle: Bundle, title: String) {
        val intentB = Intent()
        intentB.setClass(activity, clazz)
        intentB.putExtras(bundle)
        intentB.putExtra(BaseActivity.CNT_PARAMETE_TITLE, title)
        activity.startActivity(intentB)
        activity.overridePendingTransition(R.anim.weather_up, R.anim.weather_down)
    }

    fun jumpToWeather(clazz: Class<*>, bundle: Bundle, title: String) {
        val intentB = Intent()
        intentB.setClass(activity, clazz)
        intentB.putExtras(bundle)
        intentB.putExtra(BaseActivity.CNT_PARAMETE_TITLE, title)
        activity.startActivity(intentB)
        activity.overridePendingTransition(R.anim.weather_up, R.anim.weather_down)
    }

    fun jumpToFoResult(clazz: Class<*>, resultCode: Int) {
        val intentB = Intent()
        intentB.setClass(activity, clazz)
        activity.startActivityForResult(intentB, resultCode)
        activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out)
    }

    fun jumpToFoResult(clazz: Class<*>, bundle: Bundle, resultCode: Int) {
        val intentB = Intent()
        intentB.setClass(activity, clazz)
        intentB.putExtras(bundle)
        activity.startActivityForResult(intentB, resultCode)
        activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out)
    }

    fun jumpToBU(clazz: Class<*>) {
        val intentB = Intent()
        intentB.setClass(activity, clazz)
        activity.startActivity(intentB)
        activity.overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out)
    }

    fun jumpToBU(clazz: Class<*>, bundle: Bundle) {
        val intentB = Intent()
        intentB.setClass(activity, clazz)
        intentB.putExtras(bundle)
        activity.startActivity(intentB)
        activity.overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out)
    }

    fun jumpToFoResulBU(clazz: Class<*>, resultCode: Int) {
        val intentB = Intent()
        intentB.setClass(activity, clazz)
        activity.startActivityForResult(intentB, resultCode)
        activity.overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out)
    }

    fun jumpToFoResultBU(clazz: Class<*>, bundle: Bundle, resultCode: Int) {
        val intentB = Intent()
        intentB.setClass(activity, clazz)
        intentB.putExtras(bundle)
        activity.startActivityForResult(intentB, resultCode)
        activity.overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out)
    }

    fun jumpToFoResulBU(clazz: Class<*>, bundle: Bundle, title: String, resultCode: Int) {
        val intentB = Intent()
        intentB.setClass(activity, clazz)
        intentB.putExtras(bundle)
        intentB.putExtra(BaseActivity.CNT_PARAMETE_TITLE, title)
        activity.startActivityForResult(intentB, resultCode)
        activity.overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out)
    }

    /**
     * 跳转主界面
     */
    fun startMainActivity() {
        jumpTo(MainActivity::class.java)
    }

    /**
     * 跳转验证码界面
     */
    fun startCodeActivity(phone:String) {
        val bundle = Bundle();
        bundle.putString(CodeActivity.PHONEKEY, phone)
        jumpTo(CodeActivity::class.java, bundle)
    }

    /**
     * 跳转登录界面
     */
    fun startLoginActivity(){
        jumpTo(LoginActivity::class.java)
    }



}