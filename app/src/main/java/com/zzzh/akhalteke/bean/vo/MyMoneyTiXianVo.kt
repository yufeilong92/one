package com.zzzh.akhalteke.bean.vo

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.bean.vo
 * @Email : yufeilong92@163.com
 * @Time :2019/3/30 17:45
 * @Purpose :我的钱包提现记录
 */
data class MyMoneyTiXianVo(
        val list: MutableList<MyMoneyTiXianData>? = null,
        val pageInfo: PageInfo? = null,
        val usableAmount: String = ""
)

data class MyMoneyTiXianData(
        var sum: String = "",
        var status: String = "",
        var bank: String = "",
        var bankNumber: String = "",
        var createdTime: String = "",
        var eventType: String = ""
)

