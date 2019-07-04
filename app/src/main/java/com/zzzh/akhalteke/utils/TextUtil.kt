package com.zzzh.akhalteke.utils

import android.view.View
import android.widget.TextView

/**
 * @Author : YFL  is Creating a porject in com.zzzh.akhalteke.utils
 * @Package com.zzzh.akhalteke.utils
 * @Email : yufeilong92@163.com
 * @Time :2019/6/4 11:48
 * @Purpose :
 */
object TextUtil {
    //设置字体显示
    fun setViewShow(tv: TextView, string: String?) {
        if (StringUtil.isEmpty(string)) {
            tv.visibility = View.GONE
            return
        }
        tv.visibility = View.VISIBLE
        tv.text = string
    }
}