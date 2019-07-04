package com.zzzh.akhalteke.ui.video

import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.yanzhenjie.permission.Permission
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.ui.BaseActivity
import com.zzzh.akhalteke.utils.PermissionUtils
import kotlinx.android.synthetic.main.activity_open_video.*

class OpenVideoActivity : BaseActivity() {
    override fun setContentView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_open_video)
        initView()
    }

    fun initView() {
        btn_open_video.setOnClickListener {
            PermissionUtils.showPermission(mContext, "",
                    arrayOf(Permission.READ_EXTERNAL_STORAGE,
                            Permission.WRITE_EXTERNAL_STORAGE,
                            Permission.RECORD_AUDIO,
                            Permission.CAMERA)) {
                startActivityForResult(Intent(this@OpenVideoActivity,
                        VideoActivity::class.java), 100)

            }

        }
    }

    private fun initVideo(mVideoPath: String) {
        videoView_main.setVideoPath(mVideoPath)
        videoView_main.setOnPreparedListener(MediaPlayer.OnPreparedListener { mp ->
            val lp = videoView_main.layoutParams
            val videoWidth = mp.videoWidth
            val videoHeight = mp.videoHeight
            val videoProportion = videoWidth.toFloat() / videoHeight.toFloat()
            val screenWidth = fl.width
            val screenHeight = fl.height
            val screenProportion = screenWidth.toFloat() / screenHeight.toFloat()
            if (videoProportion > screenProportion) {
                lp.width = screenWidth
                lp.height = (screenWidth.toFloat() / videoProportion).toInt()
            } else {
                lp.width = (videoProportion * screenHeight.toFloat()).toInt()
                lp.height = screenHeight
            }
            videoView_main.layoutParams = lp

            Log.e("videoView",
                    "videoWidth:$videoWidth, videoHeight:$videoHeight")
        })
        videoView_main.setOnCompletionListener(MediaPlayer.OnCompletionListener {
            //                mIvPlayMain.setVisibility(View.VISIBLE);
            iv_thumb_main.visibility = View.VISIBLE
        })
        videoStart()
    }

    fun videoStart() {
        videoView_main.start()
    }

    override fun onPause() {
        super.onPause()
        videoPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        videoDestroy()
    }

    fun videoPause() {
        if (videoView_main != null && videoView_main.isPlaying()) {
            videoView_main.pause()
        }
    }

    fun videoDestroy() {
        if (videoView_main != null) {
            videoView_main.stopPlayback()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 101) {
            Log.i("CJT", "picture")
            setShow(false)
            val path = data!!.getStringExtra("path")
            image_photo_main.setImageBitmap(BitmapFactory.decodeFile(path))
        }
        if (resultCode == 102) {

            setShow(true)
            val path = data!!.getStringExtra("path")
            Log.i("CJT", "video=$path")
            initVideo(path)
        }
        if (resultCode == 103) {
            Toast.makeText(this, "请检查相机权限~", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setShow(show: Boolean) {
        fl.visibility = if (show) View.VISIBLE else View.GONE
        image_photo_main.visibility = if (show) View.GONE else View.VISIBLE

    }
}
