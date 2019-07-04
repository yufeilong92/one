package com.zzzh.akhalteke

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import com.yanzhenjie.permission.Permission
import com.zzzh.akhalteke.adapter.ViewPagerAdaper
import com.zzzh.akhalteke.dbHelp.UserDbHelp
import com.zzzh.akhalteke.fragment.guider.GuiderFragment
import com.zzzh.akhalteke.ui.BaseActivity
import com.zzzh.akhalteke.ui.login.LoginActivity
import com.zzzh.akhalteke.utils.AppManager
import com.zzzh.akhalteke.utils.PermissionUtils
import com.zzzh.akhalteke.utils.PreferencesUtils
import com.zzzh.akhalteke.utils.Utils
import com.zzzh.akhalteke.view.customview.PageIndicator
import kotlinx.android.synthetic.main.activity_guide.*

/**
 * @Author : YFL  is Creating a porject in com.zzzh.akhalteke
 * @Package com.zzzh.akhalteke
 * @Email : yufeilong92@163.com
 * @Time :2019/3/28 15:34
 * @Purpose :引导页
 */
class GuideActivity : BaseActivity() {
    override fun setContentView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_guide)
        initView()
    }

    fun initView() {
        val app = PreferencesUtils().getFirstStartApp()
        if (app){
            jumpTo(LoginActivity::class.java)
            val appManager: AppManager = AppManager.appManager
            appManager.finishActivity(GuideActivity::class.java)
            return
        }
        val length: Int = 2
        val list = mutableListOf<Fragment>()
        val title = mutableListOf<String>()
        for (id in 0 until length) {
            list.add(GuiderFragment.newInstance(id.toString(), ""))
        }
        val mAdapter = ViewPagerAdaper(supportFragmentManager, list, title)
        vp_loop_advertisement.adapter = mAdapter
        vp_loop_advertisement.addOnPageChangeListener(PageIndicator(mContext, dot_horizontal, 2))
    }
}
