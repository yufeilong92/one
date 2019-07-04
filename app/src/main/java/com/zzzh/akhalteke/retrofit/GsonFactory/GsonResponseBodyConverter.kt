package com.zzzh.akhalteke.retrofit.gsonFactory

import com.google.gson.Gson
//import com.igexin.sdk.PushManager
import com.zzzh.akhalteke.utils.Constant
import com.zzzh.akhalteke.utils.PreferencesUtils
import com.zzzh.akhalteke.utils.ToolUtils
import okhttp3.ResponseBody
import retrofit2.Converter
import java.lang.reflect.Type
import org.json.JSONObject


class GsonResponseBodyConverter<T>(val gson:Gson, val type:Type): Converter<ResponseBody,T>{

    override fun convert(value: ResponseBody?): T {
        val response = value!!.string()
        value.use { value ->
            // 这里的type实际类型是 LoginUserEntity<User>  User就是user字段的对象。
            if (!response.startsWith("{") || !response.endsWith("}")) {
                throw ResultException("服务器有误，请联系客服", "-10", "")
            }
            val jsonResult: JSONObject = JSONObject(response)
                    ?: throw ResultException("服务器有误，请联系客服", "-10", "")
            val status = jsonResult.optString("code")
            if (ToolUtils.isEmpty(status)) {
                throw ResultException("其他问题", "-1038",response)
            } else {
                val code = jsonResult.optString("code")

                when(code){
                    "0000" -> {
                        return gson.fromJson(response, type)
                    }
                    else -> {
                        throw ResultException(jsonResult.optString("msg"), code, jsonResult.optString("data")
                                ?: "")
                    }
                }
            }
        }
    }
}