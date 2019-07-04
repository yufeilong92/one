package com.zzzh.akhalteke.retrofit

import com.zzzh.akhalteke.bean.vo.MainVo
import com.zzzh.akhalteke.retrofit.gsonFactory.BaseEntity
import com.zzzh.akhalteke.utils.Constant
import io.reactivex.Observable
import retrofit2.http.*

interface MineRetrofit {

    @FormUrlEncoded
    @POST("appUser/n/sms/validate")//验证短信验证码
    fun validate(@Field("mobile") mobile: String,
                 @Field("smsCode") smsCode: String
    ): Observable<BaseEntity<String>>

    /**
     * 公告
     */
    @GET("appUser/n/userNotice/getNewOne")//取最近一条
    fun noticeNewOne(@Query("userToken") userToken: String = Constant.token
    ): Observable<BaseEntity<String>>

}