package com.zzzh.akhalteke.retrofit.gsonFactory

import okhttp3.ResponseBody
import retrofit2.Converter
import java.lang.reflect.Type
import com.alibaba.fastjson.JSON
import okio.Okio
import java.io.IOException


class FastJsonResponseBodyConverter<T>(val type:Type) : Converter<ResponseBody, T>{

    @Throws(IOException::class)
    override fun convert(value: ResponseBody): T {
        val bufferedSource = Okio.buffer(value.source())
        val tempStr = bufferedSource.readUtf8()
        bufferedSource.close()
        return JSON.parseObject(tempStr, type)
    }
}