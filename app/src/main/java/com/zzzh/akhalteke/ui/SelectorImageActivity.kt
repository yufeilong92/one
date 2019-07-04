package com.zzzh.akhalteke.ui


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.lipo.utils.UriToFile
import com.zzzh.akhalteke.retrofit.RetrofitFactory
import com.zzzh.akhalteke.retrofit.gsonFactory.BaseEntity
import com.zzzh.akhalteke.utils.PermissionUtils
import com.zzzh.akhalteke.utils.TakePhotoUtils

import com.yanzhenjie.permission.Permission
import com.zzzh.akhalteke.utils.ToolUtils
import com.zzzh.akhalteke.utils.Utils
import com.zzzh.akhalteke.view.dialog.SelectImageDialog
import io.reactivex.android.schedulers.AndroidSchedulers

import io.reactivex.schedulers.Schedulers
import io.reactivex.Flowable
import io.reactivex.Observable
import me.nereo.multi_image_selector.MultiImageSelector
import me.nereo.multi_image_selector.MultiImageSelectorActivity
import okhttp3.MediaType
import java.io.File
import okhttp3.MultipartBody
import okhttp3.RequestBody
import top.zibin.luban.Luban
import java.io.IOException

open class SelectorImageActivity : BaseActivity() {
    override fun setContentView(savedInstanceState: Bundle?) {
    }


    private val REQUEST_IMAGE_BACK = 1002//从相册选择
    private val PHOTO_PIC_CODE = 1001// 拍照

    val imagePaths: ArrayList<String> = arrayListOf()
    lateinit var selectImageDialog: SelectImageDialog
    private var temp = 1

    var mSelectType: Int = -1;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        selectImageDialog = object : SelectImageDialog(mContext) {
            override fun onTakePrice() {
                PermissionUtils.showPermission(
                        this@SelectorImageActivity, "上传图片需要照相和读写权限，是否同意", arrayOf(
                        Permission.CAMERA,
                        Permission.WRITE_EXTERNAL_STORAGE,
                        Permission.READ_EXTERNAL_STORAGE
                )
                ) {
                    toPicture()
                }
            }

            override fun onFromPhoto() {
                PermissionUtils.showPermission(
                        this@SelectorImageActivity, "上传图片需要照相和读写权限，是否同意", arrayOf(
                        Permission.CAMERA,
                        Permission.WRITE_EXTERNAL_STORAGE,
                        Permission.READ_EXTERNAL_STORAGE
                )
                ) {
                    toSelectPhoto()
                }
            }
        }

    }

    fun toSetTemp(t: Int) {
        temp = t
    }

    fun toShowDialog() {
        ToolUtils.showDialog(selectImageDialog)
    }

    fun toShowDialog(id: Int) {
        mSelectType = id;
        ToolUtils.showDialog(selectImageDialog)
    }

    /**
     * @param isUpdataRcipit  是否是长传回单
     */
    var isUpdataRecipt = false

    fun toShowDialog(id: Int, isUpdataRcipit: Boolean) {
        mSelectType = id;
        this.isUpdataRecipt = isUpdataRcipit
        ToolUtils.showDialog(selectImageDialog)
    }

    fun toSelectPhoto() {
        if (temp == 1) {
            MultiImageSelector.create().showCamera(false).single().start(this, REQUEST_IMAGE_BACK)
        } else {
            MultiImageSelector.create().showCamera(false).count(temp).multi().origin(imagePaths)
                    .start(this, REQUEST_IMAGE_BACK)
        }
    }

    private var takePhoneUri: Uri? = null
    fun toPicture() {
        try {
            takePhoneUri = TakePhotoUtils.takePhoto(mContext, PHOTO_PIC_CODE)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun lubanPicture() {
        showProgress()
        if (RetrofitFactory.judgmentNetWork(mContext)) {
            Flowable.just(imagePaths)
                    .observeOn(Schedulers.io())
                    .map { list ->
                        Luban.with(this@SelectorImageActivity)
                                .ignoreBy(200)
                                .setTargetDir(ToolUtils.pathCache(mContext).toString())
                                .load(list).get()
                    }
                    .observeOn(AndroidSchedulers.mainThread())
                    .toObservable()
                    .flatMap { files ->
                        return@flatMap uploadImage(files[0])
                    }.subscribe(this::onBaseNext, this::onError, this::onComplise)
        }
    }

    fun uploadImage(file: File): Observable<BaseEntity<String>> {
        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)//表单类型
        val imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        builder.addFormDataPart("file", file.name, imageBody)//imgfile 后台接收图片流的参数名
        val parts = builder.build().parts()
        return RetrofitFactory.createMainRetrofit().uploadImage(parts)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    open fun onBaseNext(base: BaseEntity<String>) {

    }

    open fun onBaseNext(base: String, id: Int, img: String) {
        mSelectType = -1;
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PHOTO_PIC_CODE && resultCode == Activity.RESULT_OK) {
            if (takePhoneUri != null) {
                imagePaths.clear()
                val filePath = UriToFile.getFilePathFromURI(mContext, takePhoneUri!!)
                imagePaths.add(filePath)
//                lubanPicture()
//                onBaseNext(filePath, mSelectType)
                picture(filePath, mSelectType)
            }
        }

        if (data == null) {
            return
        }
        if (requestCode === REQUEST_IMAGE_BACK) {
            // 获取返回的图片列表(存放的是图片路径)
            imagePaths.clear()
            val paths: MutableList<String> = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT)
            imagePaths.addAll(paths)
//            onBaseNext(paths[0], mSelectType)
            picture(paths[0], mSelectType)
//            lubanPicture()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ToolUtils.dismissDialog(selectImageDialog)
    }

    // 压缩图片
    fun picture(path: String, type: Int) {
//        showProgress()
        Flowable.just(path)
                .observeOn(Schedulers.io())
                .map { list ->
                    Luban.with(this@SelectorImageActivity)
                            .ignoreBy(10)
                            .setTargetDir(ToolUtils.pathCache(mContext).toString())
                            .load(list).get()
                }
                .observeOn(AndroidSchedulers.mainThread())
                .toObservable()
                .subscribe({
                    //                dismissProgress()
                    onFileData(it, type)
                }, { dismissProgress() }, { dismissProgress() })
    }

    open fun onFileData(imageList: MutableList<File>, type: Int) {
        val absolutePath = imageList.get(0).absolutePath
        if (isUpdataRecipt) {
            val string = Utils.file2Base64(absolutePath)
            onBaseNext(string, type, absolutePath)
            return
        }
//        val string = Utils.getBase64String(absolutePath)
//        val string = Utils.file2Base64(absolutePath)
        onBaseNext(absolutePath, type, absolutePath)
    }
}