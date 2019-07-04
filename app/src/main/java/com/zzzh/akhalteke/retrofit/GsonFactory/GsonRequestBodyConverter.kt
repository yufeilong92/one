package com.zzzh.akhalteke.retrofit.gsonFactory

import com.google.gson.Gson
import retrofit2.Converter
import com.google.gson.TypeAdapter
import com.zzzh.akhalteke.utils.ToolUtils
import okhttp3.MediaType
import java.nio.charset.Charset
import okhttp3.RequestBody
import java.io.OutputStreamWriter
import okio.Buffer


class GsonRequestBodyConverter<T>(val gson: Gson, val adapter: TypeAdapter<T>): Converter<T, RequestBody> {

    private val MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8")
    private val UTF_8 = Charset.forName("UTF-8")


    override fun convert(value: T): RequestBody {
        val buffer = Buffer()
        val writer = OutputStreamWriter(buffer.outputStream(), UTF_8)
        val jsonWriter = gson.newJsonWriter(writer)
        adapter.write(jsonWriter, value)
        jsonWriter.close()
        return RequestBody.create(MEDIA_TYPE, buffer.readByteString())
    }
}