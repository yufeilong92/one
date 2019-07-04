package com.zzzh.akhalteke.Event

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.Event
 * @Email : yufeilong92@163.com
 * @Time :2019/3/15 10:39
 * @Purpose :刷新运单数据
 */

class RefreshWayBillTitleEvent {
    var type: Int=0;

    constructor(type: Int) {
        this.type = type
    }

}