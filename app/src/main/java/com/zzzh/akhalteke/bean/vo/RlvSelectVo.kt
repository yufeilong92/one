package com.zzzh.akhalteke.bean.vo

import java.io.Serializable

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.bean
 * @Email : yufeilong92@163.com
 * @Time :2019/3/13 14:36
 * @Purpose :用户选中结果
 */
class RlvSelectVo  :Serializable{
    var select: Boolean = false
    var id: String? = ""
    var name: String? = ""
    //筛选类型
    var type: String? = ""

}