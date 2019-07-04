package com.zzzh.akhalteke.bean.vo

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.bean.vo
 * @Email : yufeilong92@163.com
 * @Time :2019/3/19 18:40
 * @Purpose :我的运输vo
 */
data class OrderInfomVo(
    val list: MutableList<OrderInfom>?=null,
    val pageInfo: PageInfo?=null
)
data class OrderInfom(
    val carLength: String?="",//车长
    val carType: String?="",//车型
    val corporateName: String?="",//货主公司名称
    val cost: String?="",//运费
    val createdTime: String?="",//创建时间
    val goodsId: String?="",//货源id
    val ifAgreement: String?="",//是否签订协议
    val ifPay: String?="",//是否支付
    val loadAreaName: String?="",//装货地区名称
    val loadAddress: String?="",//装货详情
    val unloadAddress: String?="",//卸货卸货详情
    val loadTime: String?="",//装货时间
    val name: String?="",//货物名称
    val orderId: String?="",//订单表主键
    val shipperId: String?="",//货主ID
    val shipperName: String?="",//货主名称
    val shipperPhone: String?="",//货主公司名称
    val shipperPortrait: String?="",//货主头像
    val unloadAreaName: String?="",//卸货地区名称
    val weightVolume: String?=""//重量体积
)

