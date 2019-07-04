package com.zzzh.akhalteke.retrofit

import com.zzzh.akhalteke.bean.vo.*
import com.zzzh.akhalteke.retrofit.gsonFactory.BaseEntity
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.*

/**
 * Created by apple on 2018/7/15.
 */
interface MainRetrofit {

    @Multipart
    @POST("appUser/n/attach/uploadFile")//上传
    fun uploadImage(
            @Part partList: MutableList<MultipartBody.Part>
    ): Observable<BaseEntity<String>>

    @FormUrlEncoded
    @POST("login/driver/getCode")//用户登录或注册输入手机号后获取验证码
    fun getCode(
            @Field("phone") phone: String
    ): Observable<BaseEntity<GetGmVo>>

    @FormUrlEncoded
    @POST("login/driver/checkCode")//用户登录或注册输入手机号后获取验证码
    fun getCheckCode(
            @Field("phone") phone: String,
            @Field("code") code: String,
            @Field("phoneType") phoneType: String,
            @Field("brand") brand: String,
            @Field("model") model: String
    ): Observable<BaseEntity<GetUserInfom>>

    //
//    @FormUrlEncoded
//    @POST("auth/driver/realName")//实名认证
//    fun submitUserInfom(
//        @Field("id") id: String,
//        @Field("iDnumber") iDnumber: String,
//        @Field("iDAddress") iDAddress: String,
//        @Field("iDAddressReverse") iDAddressReverse: String,
//        @Field("portrait") portrait: String,
//        @Field("name") name: String
//    ): Observable<BaseEntity<RealNameVo>>
    @Multipart
    @POST("auth/driver/realName")//实名认证
    fun submitUserInfom(
            @Part partList: MutableList<MultipartBody.Part>
    ): Observable<BaseEntity<RealNameVo>>

//    @FormUrlEncoded
//    @POST("auth/driver")//驾驶员信息认证
//    fun submitDriverInfom(
//            @Field("id") id: String,
//            @Field("driversLicense") driversLicense: String,
//            @Field("role") role: String,
//            @Field("qualificationLicense") qualificationLicense: String
//    ): Observable<BaseEntity<String>>

    @Multipart
    @POST("auth/driver")//驾驶员信息认证
    fun submitDriverInfom(
            @Part partList: MutableList<MultipartBody.Part>
    ): Observable<BaseEntity<String>>

//    @FormUrlEncoded
//    @POST("auth/car")//车辆信息认证
//    fun submitAuthCarInfom(
//            @Field("id") id: String,
//            @Field("drivingLicense") driversLicense: String,
//            @Field("serviceLicense") serviceLicense: String,
//            @Field("plateNumber") plateNumber: String,
//            @Field("carPlateColour") carPlateColour: String,
//            @Field("carType") carType: String,
//            @Field("carLength") carLength: String,
//            @Field("load") load: String
//    ): Observable<BaseEntity<CarVo>>
    @Multipart
    @POST("auth/car")//车辆信息认证
    fun submitAuthCarInfom(
            @Part partList: MutableList<MultipartBody.Part>
    ): Observable<BaseEntity<CarVo>>

    @FormUrlEncoded
    @POST("goods/driver/getList")//获取货源列表（发货中）
    fun getGoodsInfomList(
            @Field("id") id: String,
            @Field("loadAreaCode") loadAreaCode: String,
            @Field("unloadAreaCode") unloadAreaCode: String,
            @Field("sortType") sortType: String,
            @Field("screen") screen: String,
            @Field("type") type: String,
            @Field("loadTime") loadTime: String,
            @Field("carLength") carLength: String,
            @Field("carType") carType: String
    ): Observable<BaseEntity<GetUserInfom>>


    @FormUrlEncoded
    @POST("goods/driver/get")//查看货源详情（发货中）
    fun getGoodsInfom(
            @Field("id") id: String,
            @Field("goodsId") goodsId: String
    ): Observable<BaseEntity<InfomVo>>

    @FormUrlEncoded
    @POST("driver/carLength/list")//获取车长列表
    fun getCarLength(
            @Field("id") id: String
    ): Observable<BaseEntity<MutableList<CarLengthVo>>>


    @FormUrlEncoded
    @POST("driver/carType/list")//获取车长列表
    fun getCarType(
            @Field("id") id: String
    ): Observable<BaseEntity<MutableList<CarTypeVo>>>

    @FormUrlEncoded
    @POST("test")//测试加密
    fun Test(
            @Field("id") id: String,
            @Field("iname") name: String,
            @Field("iage") age: String
    ): Observable<BaseEntity<GmVO>>

    @FormUrlEncoded
    @POST("goods/driver/getList")//获取货源列表（发货中）
    fun requestDriverList(
            @Field("id") id: String,
            @Field("loadAreaCode") loadAreaCode: String,
            @Field("unloadAreaCode") unloadAreaCode: String,
            @Field("sortType") sortType: String,
//        @Field("screen") screen: String,
            @Field("type") type: String,
            @Field("loadTime") loadTime: String,
            @Field("carLength") carLength: String,
            @Field("carType") carType: String,
            @Field("page") page: String,
            @Field("size") size: String
    ): Observable<BaseEntity<FindGoodVo>>

    @FormUrlEncoded
    @POST("driver/getShipperById")//查看货主信息简介
    fun requestShipperById(
            @Field("userId") carOwnerId: String,
            @Field("shipperId") shipperId: String
    ): Observable<BaseEntity<ShipperJIanjieVo>>

    @FormUrlEncoded
    @POST("driver/getShipperGoods")//查看货主货源列表
    fun requestShipperGoods(
            @Field("userId") carOwnerId: String,
            @Field("shipperId") shipperId: String,
            @Field("page") page: String,
            @Field("size") size: String
    ): Observable<BaseEntity<DayGoodListVo>>

    @FormUrlEncoded
    @POST("driver/order/list")//司机端获取订单列表
    fun requestOrderList(
            @Field("id") id: String,
            @Field("status") status: String,//订单状态1-运输中，2-已完成，3-已取消，为空默认未1
            @Field("page") page: String,
            @Field("size") size: String
    ): Observable<BaseEntity<OrderInfomVo>>


    @FormUrlEncoded
    @POST("driver/order/info")//查看司机端订单详情
    fun requestOrderInfom(
            @Field("userId") userId: String,
            @Field("orderId") orderId: String//	订单id
    ): Observable<BaseEntity<OrderAgreementVo>>

    @FormUrlEncoded
    @POST("driver/agreement/info")//司机端查看协议详情
    fun requestAgreementInfom(
            @Field("userId") userId: String,
            @Field("orderId") orderId: String//	订单id
    ): Observable<BaseEntity<LookAgreementVo>>

    @FormUrlEncoded
    @POST("driver/agreement/confirm")//司机确认协议
    fun submitAgreementConfirm(
            @Field("carOwnerId") carOwnerId: String,//	车主id
            @Field("orderId") orderId: String,//	订单id
            @Field("agree") agree: String//	是否同意
    ): Observable<BaseEntity<GmVO>>

    @FormUrlEncoded
    @POST("driver/bankCard/list")//银行卡列表
    fun requesBankCardList(
            @Field("userId") userId: String//	车主id
    ): Observable<BaseEntity<MutableList<BankListVo>>>

    @FormUrlEncoded
    @POST("driver/checkCardType")//检查银行卡是否正确，是否为借记卡
    fun requesCheckCardType(
            @Field("userId") userId: String,//	车主id
            @Field("cardNumber") cardNumber: String//	银行卡号
    ): Observable<BaseEntity<String>>


    @FormUrlEncoded
    @POST("driver/bankCard/delete")//根据银行卡id删除
    fun submitBankCardDelete(
            @Field("userId") userId: String,//	车主id
            @Field("cardId") cardId: String//		银行卡主键
    ): Observable<BaseEntity<GmVO>>

    @FormUrlEncoded
    @POST("driver/bankCard/add")//根据银行卡id删除
    fun submitBankCardAdd(
            @Field("userId") userId: String,//	车主id
            @Field("name") name: String,//		持卡人姓名
            @Field("idCardNumber") idCardNumber: String,//省份证号
            @Field("cardNumber") cardNumber: String,//银行卡号
            @Field("phone") phone: String,//		银行预留手机号
            @Field("bank") bank: String//		银行卡校验时返回的银行编码
    ): Observable<BaseEntity<GmVO>>

    @FormUrlEncoded
    @POST("driver/feedback")//意见反馈
    fun submitFeedBack(
            @Field("userId") userId: String,//	车主id
            @Field("content") content: String//		内容
    ): Observable<BaseEntity<String>>

    @FormUrlEncoded
    @POST("driver/center")//个人中心
    fun reqeustDriverUser(
            @Field("userId") userId: String//	车主id
    ): Observable<BaseEntity<String>>

    @FormUrlEncoded
    @POST("driver/order/receipt/upload")//上传回单
    fun submitUploadRecepit(
            @Field("userId") userId: String,//	车主id
            @Field("orderId") orderId: String,//	订单id
            @Field("receiptOne") receiptOne: String,//	回单
            @Field("receiptTwo") receiptTwo: String,//	回单
            @Field("receiptThree") receiptThree: String//	回单
    ): Observable<BaseEntity<String>>


    @FormUrlEncoded
    @POST("driver/order/receipt")//查看回单
    fun requestOrderRecepit(
            @Field("userId") userId: String,//	车主id
            @Field("orderId") orderId: String//	订单id
    ): Observable<BaseEntity<String>>

    //钱包首页
    @FormUrlEncoded
    @POST("driver/center/account")
    fun requestCenterAccount(
            @Field("userId") userId: String,//	车主id
            @Field("page") page: String,//	页数，0为第一页
            @Field("size") size: String//	页数，0为第一页
    ): Observable<BaseEntity<MyMoneyTiXianVo>>

    //获取公司账户
    @FormUrlEncoded
    @POST("driver/getCompanyAccount")
    fun requestCompanyAccount(
            @Field("userId") userId: String//	车主id
    ): Observable<BaseEntity<String>>

    //获取账户余额
    @FormUrlEncoded
    @POST("driver/account/balance")
    fun requestAccountBalance(
            @Field("userId") userId: String//	车主id
    ): Observable<BaseEntity<String>>

    //提现
    @FormUrlEncoded
    @POST("driver/account/cash")//提现
    fun submitAccountCash(
            @Field("userId") userId: String,//	车主id
            @Field("sum") sum: String,//	金额
            @Field("bankId") bankId: String//银行卡id
    ): Observable<BaseEntity<String>>

    //提现记录
    @FormUrlEncoded
    @POST("driver/account/cashList")//提现
    fun requesAccountCashList(
            @Field("userId") userId: String,//	车主id
            @Field("page") page: String,//		页数，0为第一页
            @Field("size") size: String//每页大小
    ): Observable<BaseEntity<TiXianListVo>>


    //检查司机信息
    @FormUrlEncoded
    @POST("driver/info/check")//
    fun requestInfomCheck(
            @Field("userId") userId: String//	车主id
    ): Observable<BaseEntity<GetUserInfom>>


    //上传轨迹点
    @FormUrlEncoded
    @POST("driver/point/add")//轨迹
    fun submitPointAdd(
            @Field("userId") userId: String,//	车主id
            @Field("latitude") latitude: String,//	车主id
            @Field("longitude") longitude: String,//	车主id
            @Field("loc_time") loc_time: String,//	车主id
            @Field("coord_type_input") coord_type_input: String//	车主id
    ): Observable<BaseEntity<String>>

    @Multipart
    @POST("auth/driver/idCard/face")//上传识别身份证正面
    fun submitIdCardZ(
            @Part partList: MutableList<MultipartBody.Part>
    ): Observable<BaseEntity<IDCardZResultsVo>>

    @Multipart
    @POST("auth/driver/idCard/back")//上传识别身份证反面
    fun submitIdCardF(
            @Part partList: MutableList<MultipartBody.Part>
    ): Observable<BaseEntity<IDCardFResultsVo>>

    @Multipart
    @POST("auth/driver/driversLicense")//识别驾驶证
    fun submitDriversLicense(
            @Part partList: MutableList<MultipartBody.Part>
    ): Observable<BaseEntity<DriverResultsVo>>
    @Multipart
    @POST("auth/driver/drivingLicense")//识别行驶证
    fun submitDriveringLicense(
            @Part partList: MutableList<MultipartBody.Part>
    ): Observable<BaseEntity<DriveringResultsVo>>

    @Multipart
    @POST("driver/order/receipt/upload")//上传回单
    fun submitUploadRecepitOne(
            @Part partList: MutableList<MultipartBody.Part>
    ): Observable<BaseEntity<DriveringResultsVo>>
    @Multipart
    @POST("driver/portrait/update")//修改头像
    fun submitUploadHear(
            @Part partList: MutableList<MultipartBody.Part>
    ): Observable<BaseEntity<String>>

    //根据经纬度查询天气
    @FormUrlEncoded
    @POST("driver/weather")
    fun requestWeather(
            @Field("userId") userId: String,//	车主id
            @Field("formType") formType: String,//	坐标类型
            @Field("lat") lat: String,//	纬度
            @Field("lng") lng: String,//	经度
            @Field("need3HourForcast") need3HourForcast: String,//	是否需要当天每3或6小时一次的天气预报列表
            @Field("needAlarm") needAlarm: String,//	是否需要天气预警
            @Field("needHourData") needHourData: String,//	是否需要每小时数据的累积数组
            @Field("needIndex") needIndex: String,//	是否需要返回指数数据
            @Field("needMoreDay") needMoreDay: String//	是否需要返回7天数据中的后4天
    ): Observable<BaseEntity<String>>


    @FormUrlEncoded
    @POST("driver/index")
    fun requestMainContent(
        @Field("userId") userId: String//	车主id
    ): Observable<BaseEntity<MainVo>>
}
