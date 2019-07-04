package com.zzzh.akhalteke.retrofit

import android.content.Context
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.lipo.utils.NetWork
import com.lipo.views.ToastView
import com.zzzh.akhalteke.bean.DataMessageVo
import com.zzzh.akhalteke.retrofit.gsonFactory.GsonDConverterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by apple on 2018/7/8.
 */
object RetrofitFactory {

//    val BASE_URL: String = "http://zzzh.natapp1.cc/"
    val BASE_URL: String =DataMessageVo.HTTP_HEAR

    private val TIMEOUT: Long =60
    private var mainRetrofit: MainRetrofit? = null
    private var mineRetrofit: MineRetrofit? = null

    private val httpClient: OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(InterceptorUtil.LogInterceptor())
            .addInterceptor(InterceptorUtil.HeaderInterceptor())
            .build()

    private fun buildGson(): Gson {
        return GsonBuilder().serializeNulls()
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                .create()
    }

    private fun createRetrofit(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonDConverterFactory(buildGson()))
//                .addConverterFactory(FastJsonConverterFactory())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    fun createMainRetrofit(): MainRetrofit {
        if (mainRetrofit == null) {
            mainRetrofit = createRetrofit().create(MainRetrofit::class.java)
        }
        return mainRetrofit!!
    }

    fun createMineRetrofit(): MineRetrofit {
        if (mineRetrofit == null) {
            mineRetrofit = createRetrofit().create(MineRetrofit::class.java)
        }
        return mineRetrofit!!
    }

    /**
     * 判断当前网络是否可用
     */
    fun judgmentNetWork(context: Context): Boolean {
        if (NetWork.isNetworkConnected(context)) {
            return true
        }
        ToastView.setToasd(context, "当前网络不可用")
        return false
    }


}