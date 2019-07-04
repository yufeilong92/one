package com.zzzh.akhalteke.dbHelp.Vo

class UserDbVo(
    var id: Int = 0,
    var token: String = "",//用户token
    var userId: String = "",//用户id
    var phone: String = "",//手机号码
    var ifReal: String = "",//是否实名认证
    var ifDriver: String = "",//是否驾驶员认证
    var ifCar: String = "",//是否车辆认证
    var name: String = "",//姓名
    var hear: String = "",//头像
    var plateNumber: String = "",//车牌号
    var idnumber: String = "",//身份证
    var role: String = "",//身份证
    var extend: String = "",//809 1 是 2 为否
    var extend_one: String = "",
    var extend_two: String = "",
    var extend_three: String = "",
    var extend_four: String = "",
    var guider: String = "0",//是否查看过启动界面 0为没有 1查看过
    var carPlateColourId: String = "0"//车牌颜色，蓝色-1，黄色-2，绿色-3，黄绿色-4
)