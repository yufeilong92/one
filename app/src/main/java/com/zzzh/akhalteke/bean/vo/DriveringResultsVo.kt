package com.zzzh.akhalteke.bean.vo
/**
 * @Author : YFL  is Creating a porject in com.zzzh.akhalteke.bean.vo
 * @Package com.zzzh.akhalteke.bean.vo
 * @Email : yufeilong92@163.com
 * @Time :2019/4/10 17:43
 * @Purpose : 驾驶员信息认证结果
 */
data class DriveringResultsVo(
        val owner: String?="",//姓名
        val plateNumber: String?="",//编号
        val imagePath: String?=""
)

