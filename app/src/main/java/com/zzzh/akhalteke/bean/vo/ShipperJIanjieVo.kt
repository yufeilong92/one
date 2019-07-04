package com.zzzh.akhalteke.bean.vo

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.bean.vo
 * @Email : yufeilong92@163.com
 * @Time :2019/3/18 18:02
 * @Purpose :货主简介
 */
data class ShipperJIanjieVo(
    val goodsCount: String?="",//	发布货源数量
    val ifCompanyCertification: String?="",//公司是否认证
    val ifRealCertification: String?="",//是否已经实名
    val name: String?="",//货主名字
    val orderCount: String?="",//订单完成数量
    val goodsList: MutableList<DayInfomVo>?=null,//最近两条的货源列表
    val phone: String?="",//货主联系方式
    val portrait: String?="",//货主头像
    val registerTime: String?="",//注册时间
    val corporateName: String?="",//公司名称
    val areaAddress: String?=""//公司地址
)

data class PageListVo(
    val list: MutableList<DayInfomVo>?=null,
    val pageInfo: PageInfo?=null
)

data class DayInfomVo(
    val carLength: String?="",//车长
    val carType: String?="",//车型
    val goodsId: String?="",//货源表ID
    val loadAreaName: String?="",//装货地地区
    val loadTime: String?="",//装货时间
    val unloadAreaName: String?="",//卸货地地区
    val weightVolume: String?=""//重量和体积
)

