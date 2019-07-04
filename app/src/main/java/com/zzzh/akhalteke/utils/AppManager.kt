package com.zzzh.akhalteke.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.Context

import java.util.Stack

class AppManager {

    /**
     * 添加Activity到堆栈
     */
    fun addActivity(activity: Activity) {
        if (activityStack == null) {
            activityStack = Stack()
        }
        activityStack!!.add(activity)

    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    fun currentActivity(): Activity {
        return activityStack!!.lastElement()
    }

    /**
     * 获取当前activity的个数
     * @return
     */
    fun activityCount(): Int {
        return if (activityStack == null) {
            0
        } else activityStack!!.size
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    fun finishActivity() {
        var activity: Activity? = activityStack!!.lastElement()
        if (activity != null) {
            activity.finish()
            activity = null
        }
    }

    /**
     * 结束指定的Activity
     */
    fun finishActivity(activity: Activity?) {
        var activity = activity
        if (activity != null) {
            activityStack!!.remove(activity)
            activity.finish()
            activity = null
        }
    }

    /**
     * 结束指定类名的Activity
     */
    fun finishActivity(cls: Class<*>) {
        for (activity in activityStack!!) {
            if (activity.javaClass == cls) {
                finishActivity(activity)
                return
            }
        }
    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        var i = 0
        val size = activityStack!!.size
        while (i < size) {
            if (null != activityStack!![i]) {
                activityStack!![i].finish()
            }
            i++
        }
        activityStack!!.clear()
    }

    /**
     * 退出应用程序
     */
    fun AppExit(context: Context) {
        try {
            finishAllActivity()
            val activityMgr = context
                    .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            activityMgr.restartPackage(context.packageName)
            System.exit(0)
        } catch (e: Exception) {
        }

    }

    companion object {

        private var activityStack: Stack<Activity>? = null
        private var mInstance: AppManager? = null

        /**
         * 单例模式实例
         */
        val appManager: AppManager
            get() {
                if (mInstance == null) {
                    mInstance = AppManager()
                }
                return mInstance!!
            }
    }

}
