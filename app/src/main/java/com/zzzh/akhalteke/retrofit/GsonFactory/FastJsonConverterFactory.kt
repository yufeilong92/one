package com.zzzh.akhalteke.retrofit.gsonFactory

import retrofit2.Converter
import retrofit2.Retrofit
import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.lang.reflect.Type


class FastJsonConverterFactory : Converter.Factory() {



    fun create(): FastJsonConverterFactory {
        return FastJsonConverterFactory()
    }

    /**
     * 需要重写父类中responseBodyConverter，该方法用来转换服务器返回数据
     */
    override fun responseBodyConverter(type: Type, annotations: Array<Annotation>, retrofit: Retrofit): Converter<ResponseBody, *> {
        return FastJsonResponseBodyConverter<Any>(type)
    }

    /**
     * 需要重写父类中responseBodyConverter，该方法用来转换发送给服务器的数据
     */
    override fun requestBodyConverter(type: Type, parameterAnnotations: Array<Annotation>, methodAnnotations: Array<Annotation>, retrofit: Retrofit): Converter<*, RequestBody> {
        return FastJsonRequestBodyConverter<BaseEntity<*>>()
    }

}