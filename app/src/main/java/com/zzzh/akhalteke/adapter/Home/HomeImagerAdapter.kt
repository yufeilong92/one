package com.zzzh.akhalteke.adapter.Home

import android.content.Context
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.utils.GlideUtil

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.adapter
 * @Email : yufeilong92@163.com
 * @Time :2019/5/8 10:25
 * @Purpose :首页——图片适配
 */
class HomeImagerAdapter(var context: Context, var infoList: MutableList<String>) :
        BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_home_imager, infoList) {

    override fun convert(helper: BaseViewHolder?, item: String?) {
        val imageView = helper!!.getView<ImageView>(R.id.ic_item_home_content)
        GlideUtil.LoadImagerWithOutHttp(mContext, imageView, item!!)
    }


}