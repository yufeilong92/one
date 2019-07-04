package com.zzzh.akhalteke.bean.vo

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.bean.vo
 * @Email : yufeilong92@163.com
 * @Time :2019/4/3 15:59
 * @Purpose :更新vo
 */
data class UpDataVo(
        var version: String? = "",//用于显示更新的版本号
        var versionCode: Int? = 0,//用与判断是否是最新版本
        var type: Int? = 0,//1 强制，2 建议
        var path: String? = "",//路径
        var updateLog: String? = "",//更新日志
        var appSize: String? = ""//大小
)