package com.zzzh.akhalteke.bean.vo

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.bean.vo
 * @Email : yufeilong92@163.com
 * @Time :2019/3/19 11:13
 * @Purpose :当天货源
 */
data class DayGoodListVo(
    val list: MutableList<DayInfomVo>?=null,
    val pageInfo: PageInfo?=null
)

