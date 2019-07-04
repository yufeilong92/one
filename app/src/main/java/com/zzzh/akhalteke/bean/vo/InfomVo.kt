package com.zzzh.akhalteke.bean.vo

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.bean.vo
 * @Email : yufeilong92@163.com
 * @Time :2019/3/16 15:50
 * @Purpose :货源详情
 */
data class InfomVo(
        val carLength: String? = "",//车长（车辆信息）
        val carType: String? = "",//车型（车辆信息）
        val comments: String? = "",//备注（其他信息）
        val corporateName: String? = "",//货主组织名称（货主信息）
        val cost: String? = "",//运费（其他信息）
        val createdTime: String? = "",
        val goodsId: String? = "",
        val goodsName: String? = "",//货物名称（货物信息）
        val goodsType: String? = "",//用车类型，整车-1、零担-2（货物信息）
        val loadAddress: String? = "",//装货地详细地址
        val loadAreaCode: String? = "",//装货地地区编码
        val loadTime: String? = "",//装货时间（装卸信息）
        val loadType: String? = "",//装卸方式一装一卸-1、一装两卸-2、一装多卸-3、两装一卸-4、两装两卸-5、多装多卸-6（装卸信息）
        val payType: String? = "",//支付方式（其他信息）
        val shipperId: String? = "",//所发货源的货主ID（货主信息）
        val shipperName: String? = "",//货主姓名（货主信息）
        val shipperPhone: String? = "",//货主电话（货主信息）
        val shipperPortrait: String? = "",//货主头像（货主信息）
        val unloadAddress: String? = "",//详细地址（装卸信息）
        val unloadAreaCode: String? = "",//卸货地地区编码（装卸信息）
        val weightVolume: String? = "",//货源重量和体积（货物信息，使用
        val registerTime: String? = "",//货源重量和体积（货物信息，使用
        val ifRealCertification: String = "" //是否实名认证

)