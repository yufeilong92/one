package com.zzzh.akhalteke.bean.vo

import android.app.Activity
import android.graphics.Bitmap
import com.youth.banner.Banner

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.bean.vo
 * @Email : yufeilong92@163.com
 * @Time :2019/6/14 16:55
 * @Purpose :首页
 */
data class MainVo(
    val activity: MutableList<ActivityVo>?=null,//活动
    val banners: MutableList<BannerVo>?=null,//轮播
    val bitmap: BitmapVo,//单片活动
    val buyCar: String="",//购车
    val etc: String="",//etc
    val insurance: String="",//保险
    val oilCard: String="",//油卡
    val violation: String=""//违章
)
data class ActivityVo(
    val image: String,
    val type: String,
    val url: String
)

data class BannerVo(
    val image: String,
    val type: String,
    val url: String
)

data class BitmapVo(
    val image: String,
    val type: String,
    val url: String
)
