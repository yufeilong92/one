package com.zzzh.akhalteke.ui.video

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.cjt2325.cameralibrary.JCameraView
import com.cjt2325.cameralibrary.listener.ClickListener
import com.cjt2325.cameralibrary.listener.ErrorListener
import com.cjt2325.cameralibrary.listener.JCameraListener
import com.cjt2325.cameralibrary.util.FileUtil
import com.iceteck.silicompressorr.SiliCompressor
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.ui.BaseActivity
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_video.*
import java.io.File

class VideoActivity : BaseActivity() {
    private val mDisposables = CompositeDisposable()
    override fun setContentView(savedInstanceState: Bundle?) {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_video)

        initCameraView()
    }

    fun initCameraView() {
        mJCameraView.setSaveVideoPath(Environment.getExternalStorageDirectory().path + File.separator + "JCamera")
        //设置只能录像或只能拍照或两种都可以（默认两种都可以）
        mJCameraView.setFeatures(JCameraView.BUTTON_STATE_BOTH)

//设置视频质量
        mJCameraView.setMediaQuality(JCameraView.MEDIA_QUALITY_MIDDLE)
        mJCameraView.setTip("点击拍照 长按录像")
        mJCameraView.setMediaQuality(JCameraView.MEDIA_QUALITY_MIDDLE)

        mJCameraView.setErrorLisenter(object : ErrorListener {
            override fun onError() {
                //打开Camera失败回调
                val intent = Intent()
                setResult(103, intent)
                finish()
            }

            override fun AudioPermissionError() {
                //没有录取权限回调
                Toast.makeText(this@VideoActivity, "给点录音权限可以?", Toast.LENGTH_SHORT).show()
            }
        })
        mJCameraView.setJCameraLisenter(object : JCameraListener {
            override fun captureSuccess(bitmap: Bitmap) {
                //获取图片bitmap
                //获取图片bitmap
                //                Log.i("JCameraView", "bitmap = " + bitmap.getWidth());
                val path = FileUtil.saveBitmap("JCamera", bitmap)
                val intent = Intent()
                intent.putExtra("path", path)
                setResult(101, intent)
                finish()
            }

            override fun recordSuccess(url: String, firstFrame: Bitmap) {
                //获取视频路径
                val path = FileUtil.saveBitmap("JCamera", firstFrame)
                Log.i("CJT", "url = $url, Bitmap = $path")
                //                Intent intent = new Intent();
                //                intent.putExtra("path", path);
                //                setResult(101, intent);
                compressVideo(url)
            }

        })


        mJCameraView.setLeftClickListener(ClickListener { this@VideoActivity.finish() })
        mJCameraView.setRightClickListener(ClickListener { Toast.makeText(this@VideoActivity, "Right", Toast.LENGTH_SHORT).show() })
    }

    /**
     * 视频压缩
     */
    private fun compressVideo(srcPath: String) {
        NormalProgressDialog
                .showLoading(this, "视频处理中", false)
        val destDirPath = VideoUtil.getTrimmedVideoDir(this, "small_video")
        Observable.create(ObservableOnSubscribe<String> { emitter ->
            try {
                var outWidth = 0
                var outHeight = 0
                //竖屏
                outWidth = 480
                outHeight = 720
                val compressedFilePath = SiliCompressor.with(this@VideoActivity)
                        .compressVideo(srcPath, destDirPath, outWidth, outHeight, 900000)
                emitter.onNext(compressedFilePath)
            } catch (e: Exception) {
                emitter.onError(e)
            }

            emitter.onComplete()
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<String> {
                    override fun onSubscribe(d: Disposable) {
                        mDisposables.add(d)
                    }

                    override fun onNext(outputPath: String) {
                        //源路径: /storage/emulated/0/Android/data/com.kangoo.diaoyur/cache/small_video/trimmedVideo_20180514_163858.mp4
                        //压缩路径: /storage/emulated/0/Android/data/com.kangoo.diaoyur/cache/small_video/VIDEO_20180514_163859.mp4
                        Log.e("==", "compressVideo---onSuccess")
                        //获取视频第一帧图片
                        NormalProgressDialog.stopLoading()
                        val intent = Intent()
                        intent.putExtra("path", outputPath)
                        setResult(102, intent)
                        finish()
                        //                        ExtractVideoInfoUtil mExtractVideoInfoUtil = new ExtractVideoInfoUtil(outputPath);
                        //                        Bitmap bitmap = mExtractVideoInfoUtil.extractFrame();
                        //                        String firstFrame = FileUtil.saveBitmap("small_video", bitmap);
                        //                        if (bitmap != null && !bitmap.isRecycled()) {
                        //                            bitmap.recycle();
                        //                            bitmap = null;
                        //                        }

                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        Log.e("===", "compressVideo---onError:$e")
                        Toast.makeText(this@VideoActivity, "视频压缩失败", Toast.LENGTH_SHORT).show()
                    }

                    override fun onComplete() {}
                })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mDisposables != null && !mDisposables.isDisposed) {
            mDisposables.dispose()
            mDisposables.clear()
        }
    }
}
