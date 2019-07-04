package com.zzzh.akhalteke.utils

import android.content.Context
import android.net.Uri
import android.support.v4.content.res.ResourcesCompat
import com.facebook.drawee.drawable.ProgressBarDrawable
import com.facebook.drawee.drawable.ScalingUtils
import com.facebook.drawee.generic.GenericDraweeHierarchy
import com.facebook.drawee.generic.RoundingParams
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.request.ImageRequest
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.request.ImageRequestBuilder
import android.os.Build
import android.support.annotation.DrawableRes
import com.facebook.drawee.interfaces.DraweeController
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.common.memory.PooledByteBuffer
import com.facebook.common.references.CloseableReference
import com.facebook.common.memory.PooledByteBufferInputStream
import com.facebook.datasource.BaseDataSubscriber
import com.facebook.datasource.DataSource
import com.zzzh.akhalteke.BaseApplication
import com.zzzh.akhalteke.R
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.Executors


/**
 * 图片加载控件
 *
 * 参考网址：https://blog.csdn.net/ljy_programmer/article/details/78273267
 *
 */
object ImageLoadingUtils {

    private var res = BaseApplication.toInstance().resources
    private var retryImage = ResourcesCompat.getDrawable(res, R.color.image_gray, null)
    private var failureImage = ResourcesCompat.getDrawable(res, R.color.image_gray, null)
    private var placeholderImage = ResourcesCompat.getDrawable(res, R.color.image_gray, null)

    private fun setHierarchay(hierarchy: GenericDraweeHierarchy, isProgress: Boolean, isRound: Boolean, radius: Float) {
        //重新加载显示的图片
        hierarchy.setRetryImage(retryImage)
        //加载失败显示的图片
//        hierarchy.setFailureImage(failureImage, ScalingUtils.ScaleType.CENTER_CROP)
//        //加载完成前显示的占位图
//        hierarchy.setPlaceholderImage(placeholderImage, ScalingUtils.ScaleType.CENTER_CROP)
        //设置加载成功后图片的缩放模式
        hierarchy.actualImageScaleType = ScalingUtils.ScaleType.CENTER_CROP

        if (isProgress) {
            //显示加载进度条，使用自带的new ProgressBarDrawable()
            //默认会显示在图片的底部，可以设置进度条的颜色。
            hierarchy.setProgressBarImage(ProgressBarDrawable())
        }
        if (isRound) {
            //设置图片加载为圆形
            hierarchy.roundingParams = RoundingParams.asCircle()
        }
        if (radius > 0) {
            //设置图片加载为圆角，并可设置圆角大小
            hierarchy.roundingParams = RoundingParams.fromCornersRadius(radius)
        }
    }

    private fun getImageRequest(uri: Uri, simpleDraweeView: SimpleDraweeView): ImageRequest {
        val width: Int
        val height: Int
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            width = simpleDraweeView.width
            height = simpleDraweeView.height
        } else {
            width = simpleDraweeView.maxWidth
            height = simpleDraweeView.maxHeight
        }

        //根据请求路径生成ImageRequest的构造者
        val builder = ImageRequestBuilder.newBuilderWithSource(uri)
        //调整解码图片的大小
        if (width > 0 && height > 0) {
            builder.resizeOptions = ResizeOptions(width, height)
        }
        //设置是否开启渐进式加载，仅支持JPEG图片
        builder.isProgressiveRenderingEnabled = true

//        //图片变换处理
//        val processorBuilder = CombinePostProcessors.Builder()
//        //加入模糊变换
//        processorBuilder.add(BlurPostprocessor(context, radius))
//        //加入灰白变换
//        processorBuilder.add(GrayscalePostprocessor())
//        //应用加入的变换
//        builder.setPostprocessor(processorBuilder.build())
//        //更多图片变换请查看https://github.com/wasabeef/fresco-processors

        return builder.build()
    }

    private fun getController(request: ImageRequest, oldController: DraweeController?): DraweeController {
        val builder = Fresco.newDraweeControllerBuilder()
        builder.imageRequest = request//设置图片请求
        builder.tapToRetryEnabled = false//设置是否允许加载失败时点击再次加载
        builder.autoPlayAnimations = true//设置是否允许动画图自动播放
        builder.oldController = oldController
        return builder.build()
    }

    fun loadImage(simpleDraweeView: SimpleDraweeView, uri: Uri) {
        //设置Hierarchy
        setHierarchay(simpleDraweeView.hierarchy, false, false, -1f)
        //构建并获取ImageRequest
        val imageRequest = getImageRequest(uri, simpleDraweeView)
        //构建并获取Controller
        val draweeController = getController(imageRequest, simpleDraweeView.controller)
        //开始加载
        simpleDraweeView.controller = draweeController
    }

    /**
     * 加载带圆角的图片
     */
    fun loadImageRadius(simpleDraweeView: SimpleDraweeView, url: String, radius: Float) {
        var uri: Uri = Uri.parse(url)
        //设置Hierarchy
        setHierarchay(simpleDraweeView.hierarchy, false, false, radius)
        //构建并获取ImageRequest
        val imageRequest = getImageRequest(uri, simpleDraweeView)
        //构建并获取Controller
        val draweeController = getController(imageRequest, simpleDraweeView.controller!!)
        //开始加载
        simpleDraweeView.controller = draweeController
    }

    /**
     * 加载圆形图片
     */
    fun loadImageRound(simpleDraweeView: SimpleDraweeView, url: String) {
        var uri: Uri = Uri.parse(url)
        //设置Hierarchy
        setHierarchay(simpleDraweeView.hierarchy, false, true, -1f)
        //构建并获取ImageRequest
        val imageRequest = getImageRequest(uri, simpleDraweeView)
        //构建并获取Controller
        val draweeController = getController(imageRequest, simpleDraweeView.controller!!)
        //开始加载
        simpleDraweeView.controller = draweeController
    }

    /**
     * 加载网络图片，包括gif/webp动图
     */
    fun loadNetImage(simpleDraweeView: SimpleDraweeView, url: String) {
        val uri = Uri.parse(url?:"")
        loadImage(simpleDraweeView, uri)
    }

    /**
     * 加载本地文件图片
     */
    fun loadLocalImage(simpleDraweeView: SimpleDraweeView, fileName: String) {
        val uri = Uri.parse("file://$fileName")
        loadImage(simpleDraweeView, uri)
    }

    /**
     * 加载res下资源图片
     */
    fun loadResourceImage(simpleDraweeView: SimpleDraweeView, @DrawableRes resId: Int) {
        val uri = Uri.parse("res:///$resId")
        loadImage(simpleDraweeView, uri)
    }

    /**
     * 加载ContentProvider下的图片
     */
    fun loadContentProviderImage(simpleDraweeView: SimpleDraweeView, resId: Int) {
        val uri = Uri.parse("content:///$resId")
        loadImage(simpleDraweeView, uri)
    }

    /**
     * 加载asset图片
     */
    fun loadAssetImage(simpleDraweeView: SimpleDraweeView, resId: Int) {
        val uri = Uri.parse("asset:///$resId")
        loadImage(simpleDraweeView, uri)
    }

    fun downLoadImage(context: Context, url: String, saveFile: File, onSuccess: (saveFile: File) -> Unit, onFail: (e: Throwable) -> Unit) {
        //参考自https://github.com/hpdx/fresco-helper/blob/master/fresco-helper/src/main/java/com/facebook/fresco/helper/ImageLoader.java
        val uri = Uri.parse(url)
        val imagePipeline = Fresco.getImagePipeline()
        val builder = ImageRequestBuilder.newBuilderWithSource(uri)
        val imageRequest = builder.build()

        // 获取未解码的图片数据
        val dataSource = imagePipeline.fetchEncodedImage(imageRequest, context)
        dataSource.subscribe(object : BaseDataSubscriber<CloseableReference<PooledByteBuffer>>() {


            public override fun onNewResultImpl(dataSource: DataSource<CloseableReference<PooledByteBuffer>>) {
                if (!dataSource.isFinished) {
                    return
                }

                val imageReference = dataSource.result
                if (imageReference != null) {

                    val closeableReference = imageReference.clone()
                    try {
                        val pooledByteBuffer = closeableReference.get()
                        val inputStream = PooledByteBufferInputStream(pooledByteBuffer)
                        val outputStream = FileOutputStream(saveFile)

                        var byteread: Int = 0
                        val buffer = ByteArray(1024)

                        inputStream.use { input ->
                            outputStream.use { outputStream ->
                                while (input.read(buffer).also { byteread = it } != -1) {
                                    outputStream.write(buffer, 0, byteread)
                                }
                            }
                        }
                        onSuccess(saveFile)
                    } catch (e: Exception) {
                        onFail(e)
                        e.printStackTrace()
                    } finally {
                        imageReference.close()
                        closeableReference.close()
                    }
                }
            }

            override fun onProgressUpdate(dataSource: DataSource<CloseableReference<PooledByteBuffer>>?) {
                val progress = (dataSource!!.progress * 100).toInt()
//                RingLog.d("fresco下载图片进度：$progress")
            }

            override fun onFailureImpl(dataSource: DataSource<CloseableReference<PooledByteBuffer>>?) {
                val throwable = dataSource!!.failureCause
                onFail(throwable!!)
            }

        }, Executors.newSingleThreadExecutor())
    }

}