package com.zzzh.akhalteke.bean.vo

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.bean.vo
 * @Email : yufeilong92@163.com
 * @Time :2019/3/20 15:34
 * @Purpose :司机确认协议
 */
data class LookAgreementVo(
    val appointment: String?="",//其他约定
    val carTypeLength: String?="",//车长车型
    val companyAmount: String?="",//通过平台支付运费
    val confirmTime: String?="",//确认时间
    val goodsName: String?="",//货物名称
    val id: String?="",//协议id
    val launchTime: String?="",//发起时间
    val loadAddress: String?="",//装货地址
    val loadTime: String?="",//装货时间
    val orderId: String?="",//	订单id
    val ownerCarNumber: String?="",//承运司机车牌号
    val ownerName: String?="",//承运司机名字
    val ownerPhone: String?="",//承运司机手机
    val payTime: String?="",//付款时间
    val shipperAddress: String?="",//发货人地址
    val shipperName: String?="",//发货人姓名
    val shipperPhone: String?="",//发货人手机
    val status: String?="",//状态
    val sumAmount: String?="",//总运费
    val unloadAddress: String?="",//卸货地址
    val unloadTime: String?="",//卸货时间
    val weightVolume: String?="",//重量体积
    val driverPortrait: String?="",//承运司机头像
    val shipperPortrait: String?=""//货主头像
)
