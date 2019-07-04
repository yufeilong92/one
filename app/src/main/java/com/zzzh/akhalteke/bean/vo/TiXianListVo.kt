package com.zzzh.akhalteke.bean.vo

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.bean.vo
 * @Email : yufeilong92@163.com
 * @Time :2019/4/1 11:53
 * @Purpose :提现记录vo
 */
data class TiXianListVo(
        val list: MutableList<TiXianItemVo>?=null,
        val pageInfo: PageInfo?=null
)

data class TiXianItemVo(
    val bank: String?="",//提现银行
    val bankNumber: String?="",//提现卡号
    val createdTime: String?="",//发起时间
    val status: String?="",//状态1-成功，2-审核中，3失败
    val sum: String?=""//	金额
)