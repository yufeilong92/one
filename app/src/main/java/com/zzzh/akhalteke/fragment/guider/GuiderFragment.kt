package com.zzzh.akhalteke.fragment.guider

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zzzh.akhalteke.GuideActivity
import com.zzzh.akhalteke.dbHelp.UserDbHelp
import com.zzzh.akhalteke.ui.BaseFragment
import com.zzzh.akhalteke.ui.login.LoginActivity
import com.zzzh.akhalteke.utils.AppManager
import com.zzzh.akhalteke.utils.PreferencesUtils
import kotlinx.android.synthetic.main.fragment_guider.*
import org.greenrobot.eventbus.HandlerPoster
import android.text.method.Touch.onTouchEvent
import android.view.MotionEvent
import android.R
import android.widget.Toast
import java.util.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * @Author : YFL  is Creating a porject in com.zzzh.akhalteke.fragment.guider
 * @Package com.zzzh.akhalteke.fragment.guider
 * @Email : yufeilong92@163.com
 * @Time :2019/3/28 15:33
 * @Purpose :引导页
 */
class GuiderFragment : BaseFragment() {

    private var param1: String? = null
    private var param2: String? = null

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GuiderFragment().apply {
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

    override fun setContentView(): Int {
        return com.zzzh.akhalteke.R.layout.fragment_guider
    }

    override fun setCreatedContentView(view: View, savedInstanceState: Bundle?) {
        when (param1) {
            "0" -> {
                setImge(com.zzzh.akhalteke.R.mipmap.ic_guilde_one)
            }
            "1" -> {
//                tv_guilder_centent.visibility = View.GONE
                setImge(com.zzzh.akhalteke.R.mipmap.ic_guilde_two)
                tv_guilder_centent.setOnClickListener {

                }
            }
//            "2" -> {
//                setImge(R.mipmap.guide_three)
//                iv_fragment_guider.setOnClickListener {
//                    PreferencesUtils().saveFirstStartApp()
//                    val appManager: AppManager = AppManager.appManager
//                    appManager.finishActivity(GuideActivity::class.java)
//                    jumpTo(LoginActivity::class.java)
//                }
//            }
        }

    }

    fun setImge(id: Int) {
        val bitmap = BitmapFactory.decodeResource(resources, id)
        iv_fragment_guider.setImageBitmap(bitmap)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (userVisibleHint) {//界面可见
            if (param1 != null || param1 == "1") {
                Toast.makeText(mContext,"3秒后跳转首页",Toast.LENGTH_SHORT).show()
                start()
            }
        } else {//界面不可见

        }
    }

    var timer: Timer? = null
    private fun start() {
        val task = object : TimerTask() {
            override fun run() {
                PreferencesUtils().saveFirstStartApp()
                val appManager: AppManager = AppManager.appManager
                appManager.finishActivity(GuideActivity::class.java)
                jumpTo(LoginActivity::class.java)
            }
        }
        if (timer == null) {
            timer = Timer()
        }
        timer!!.schedule(task, 3000)
    }

    private fun stop() {
        if (timer != null)
            timer!!.cancel()
        timer = null
    }

    override fun onDestroy() {
        super.onDestroy()
        if (param1 != null || param1 == "1")
            stop()
    }
}
