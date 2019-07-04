package com.zzzh.akhalteke.bean.vo

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.bean
 * @Email : yufeilong92@163.com
 * @Time :2019/3/15 15:39
 * @Purpose :获取货源列表vo
 */
data class FindGoodVo(
    val list: MutableList<GoodItem>?=null,
    val pageInfo: PageInfo?=null
)
data class GoodItem(
    val carLength: String?="",//车长
    val carType: String?="",//车型
    val comments: String?="",//备注
    val createdTime: String?="",
    val goodsId: String?="",//货源ID
    val goodsName: String?="",//货物名称
    val goodsType: String?="",//用车类型，整车-1、零担-2
    val loadAreaCode: String?="",//装货地地区编码
    val loadAddress:String?="",//装货地址详情
    val unloadAddress:String?="",//卸货地址详情
    val loadType: String?="",//装卸方式一装一卸-1、一装两卸-2、一装多卸-3、两装一卸-4、两装两卸-5、多装多卸-6
    val payType: String?="",//支付方式
    val shipperId: String?="",//货主id
    val shipperName: String?="",//货主姓名
    val shipperPhone: String?="",//货主电话
    val shipperPortrait: String?="",//	货主头像
    val unloadAreaCode: String?="",//卸货地地区编码
    val weightVolume: String?="",//货源重量和体积
    val ifRealCertification: String?="",//货源重量和体积
    val loadTime: String?=""//装货时间（123今天明天后天，使用逗号相连）

)

data class PageInfo(
    val first: Boolean, //是否是第一页
    val last: Boolean, //是否是最后一夜
    val page: Int, //当前页数
    val size: Int,//每页请求数
    val total: Int, //总个数
    val totalPage: Int//总页数
)