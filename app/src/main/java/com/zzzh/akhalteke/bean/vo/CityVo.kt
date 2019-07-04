package com.zzzh.akhalteke.bean.vo

/**
 * @Author : YFL  is Creating a porject in Lenovo
 * @Email : yufeilong92@163.com
 * @Time :2019/3/13 9:54
 * @Purpose :地址
 */

 data class CityVo(
    val `data`: MutableList<Province>?=null
)

data class Province(
    var area_name: String?="",
    var citys: MutableList<City>?=null,
    var id: String?=""
)

data class City(
        var area_name: String?="",
        var id: String?="",
        var zones: MutableList<City>?=null
)