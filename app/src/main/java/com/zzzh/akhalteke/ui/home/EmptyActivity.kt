package com.zzzh.akhalteke.ui.home

import android.os.Bundle
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.bean.DataMessageVo
import com.zzzh.akhalteke.ui.BaseActivity
import com.zzzh.akhalteke.utils.Utils
import kotlinx.android.synthetic.main.activity_empty.*
import kotlinx.android.synthetic.main.gm_rl_title_transparency.*

/**
 * @Author : YFL  is Creating a porject in com.zzzh.akhalteke.ui.home
 * @Package com.zzzh.akhalteke.ui.home
 * @Email : yufeilong92@163.com
 * @Time :2019/6/12 18:19
 * @Purpose :空白界面
 */
class EmptyActivity : BaseActivity() {
    override fun setContentView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_empty)
        tv_activity_title.text = "联系方式"
        btn_empty_phone.setOnClickListener {
            Utils.playPhone(mContext, DataMessageVo.KEFU_PHONE)
        }
    }


}
