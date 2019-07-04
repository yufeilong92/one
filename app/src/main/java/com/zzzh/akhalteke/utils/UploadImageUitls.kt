package com.zzzh.akhalteke_shipper.utils

import android.content.Context
import com.lipo.utils.MyMD5
import com.zzzh.akhalteke.bean.vo.GmContentVo
import com.zzzh.akhalteke.utils.MD5
import com.zzzh.akhalteke.utils.StringUtil
import com.zzzh.akhalteke.utils.ToolUtils
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import top.zibin.luban.Luban
import java.io.File

object UploadImageUitls {

     fun lubanPicture(
        mContext: Context,
        imagePaths: MutableList<String>,
        params: MutableMap<String, String>
    ): Observable<MutableList<MultipartBody.Part>> {
        return Flowable.just(imagePaths)
            .observeOn(Schedulers.io())
            .map { list ->
                Luban.with(mContext)
                    .ignoreBy(100)
                    .setTargetDir(ToolUtils.pathCache(mContext).toString())
                    .load(list).get()
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .toObservable()
            .flatMap {
                return@flatMap uploadImage(it[0].absoluteFile, params)
            }
    }

    private fun uploadImage(file: File, params: MutableMap<String, String>): Observable<MutableList<MultipartBody.Part>> {
        return Single.fromCallable {
            val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)//表单类型
//            params.put("userId", GmContentVo.getUserInfom()!!.userId?: "")
            val sortString = StringBuffer()
            for (str in params.toSortedMap().keys) {
                var valueName = params[str]
                sortString.append("&$str=$valueName")
                builder.addFormDataPart(str, valueName)
            }
            if (!StringUtil.isEmpty(GmContentVo.getUserInfom()!!.token)) {
                sortString.append("&token=${GmContentVo.getUserInfom()!!.token}")
            }
            builder.addFormDataPart("sign", MD5.getMD5String(sortString.substring(1).toString()).toUpperCase())
            val imageBody = RequestBody.create(MediaType.parse("image/png"), file)
            builder.addFormDataPart("file", file.name, imageBody)//imgfile 后台接收图片流的参数名
            return@fromCallable builder.build().parts()
        }.toObservable()
    }

}