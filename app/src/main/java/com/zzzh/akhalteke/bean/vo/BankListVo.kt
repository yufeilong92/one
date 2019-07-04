package com.zzzh.akhalteke.bean.vo

import java.io.Serializable

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.bean.vo
 * @Email : yufeilong92@163.com
 * @Time :2019/3/21 18:53
 * @Purpose :银行卡vo
 */
data class BankListVo(
    val bank: String?="",
    val cardNumber: String?="",
    val id: String?=""
):Serializable