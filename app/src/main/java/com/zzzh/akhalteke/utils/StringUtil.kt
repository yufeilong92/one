package com.zzzh.akhalteke.utils

/**
 * @Author : YFL  is Creating a porject in Lenovo
 * @Email : yufeilong92@163.com
 * @Time :2019/3/6 17:37
 * @Purpose :
 */

object StringUtil {


    fun  isEmpty(str: String?): Boolean {
        if (str == null || str.equals("")) {
            return true
        }
        return false
    }
}