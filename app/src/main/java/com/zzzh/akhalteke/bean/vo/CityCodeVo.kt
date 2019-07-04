package com.zzzh.akhalteke.bean.vo

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.bean.vo
 * @Email : yufeilong92@163.com
 * @Time :2019/5/8 18:04
 * @Purpose :城市码
 */
data class CityCodeVo(
    val `data`: List<Data>
)

data class Data(
    val code: String,
    val name: String
)