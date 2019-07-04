package com.zzzh.akhalteke.bean.vo

 class GetUserInfom(
    val id: String?="",//车主表主键
    val ifCar: String?="",//是否车辆认证
    val ifDriver: String?="",//是否驾驶员认证
    val ifRealCertification: String?="",//是否实名认证
    val name: String?="",//姓名
    val number: String?="",//身份证
    val phone: String?="",//姓名
    val plateNumber: String?="",//车牌号
    val portrait: String?="",//头像
    val ifVert: String?="",//809
    val role: String?="",
    val token: String?="",
    val carPlateColourId: String?=""//车牌颜色，蓝色-1，黄色-2，绿色-3，黄绿色-4
)

