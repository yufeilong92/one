package com.zzzh.akhalteke.bean.vo

import com.zzzh.akhalteke.utils.StringUtil

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.bean.vo
 * @Email : yufeilong92@163.com
 * @Time :2019/3/22 15:03
 * @Purpose :我的运单标题vo
 */
object MyOrderTitleVo {
    private var yunShunNumber: String= "0"
    private var completaNumber: String = "0"
    private var cancleNumber: String = "0"
    fun setYunNumber(number: String) {
        if (StringUtil.isEmpty(number) )return
        this.yunShunNumber = number
    }

    fun getYunNumber() : String? {
       return this.yunShunNumber
    }

    fun setCompletaNumber(number: String) {
        if (StringUtil.isEmpty(number) )return
        this.completaNumber = number
    }

    fun getCompletaNumber(): String? {
       return this.completaNumber
    }

    fun setCancleNumber(number: String) {
        if (StringUtil.isEmpty(number) )return
        this.cancleNumber = number
    }

    fun getCancleNumber() : String? {
      return  this.cancleNumber
    }

}