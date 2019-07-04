package com.zzzh.akhalteke.bean.vo
/**
 * @Author : YFL  is Creating a porject in com.zzzh.akhalteke.bean.vo
 * @Package com.zzzh.akhalteke.bean.vo
 * @Email : yufeilong92@163.com
 * @Time :2019/4/10 17:43
 * @Purpose : 身份证正面认证结果
 */
data class IDCardFResultsVo(
        val issue: String?="",//签发机关
        val effectiveTime: String?="",//	有效时间
        val savePath: String?=""
)

