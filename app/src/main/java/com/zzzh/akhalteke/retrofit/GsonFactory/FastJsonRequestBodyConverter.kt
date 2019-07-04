package com.zzzh.akhalteke.retrofit.gsonFactory

import okhttp3.RequestBody
import retrofit2.Converter
import com.alibaba.fastjson.JSON
import okhttp3.MediaType


class FastJsonRequestBodyConverter<T>: Converter<T, RequestBody>{
    private val MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8")

    override fun convert(value: T): RequestBody {
        return RequestBody.create(MEDIA_TYPE, JSON.toJSONBytes(value))
    }
}