package com.zzzh.akhalteke.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.RadioGroup
import android.widget.Toast
import com.iflytek.cloud.*
import com.zzzh.akhalteke.ui.home.EmptyActivity

/**
 * @Author : YFL  is Creating a porject in akhalteke-Android-driver
 * @Package com.zzzh.akhalteke.utils
 * @Email : yufeilong92@163.com
 * @Time :2019/6/27 11:11
 * @Purpose :讯飞语音
 */
class AudioUtils(var context: Context) {
    companion object {
        //被companion object包裹的语句都是private的
        private var singletonInstance: AudioUtils? = null

        @Synchronized
        fun getInstance(context: Context): AudioUtils? {
            if (singletonInstance == null) {
                singletonInstance = AudioUtils(context)
            }
            return singletonInstance
        }
    }

    // 语音合成对象
    private var mTts: SpeechSynthesizer? = null

    // 默认发音人
    private var voicer = "xiaoyan"

    // 缓冲进度
    private var mPercentForBuffering = 0
    // 播放进度
    private var mPercentForPlaying = 0
    // 引擎类型
    private var mEngineType = SpeechConstant.TYPE_CLOUD

    private var mToast: Toast? = null
    fun initVideo() {
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(context, mTtsInitListener)
        if (mTts == null) {
            return
        }
        setParam ()

    }

    /**
     * 初始化监听。
     */
    private val mTtsInitListener = InitListener { code ->
        Log.d("==", "InitListener init() code = $code")
        if (code != ErrorCode.SUCCESS) {
            showTip("初始化失败,错误码：$code,请点击网址https://www.xfyun.cn/document/error-code查询解决方案")
        } else {
//             初始化成功，之后可以调用startSpeaking方法
//             注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
//             正确的做法是将onCreate中的startSpeaking调用移至这里
        }
    }

    fun startVideo(texts: String) {
        // 移动数据分析，收集开始合成事件
        /*FlowerCollector.onEvent(TtsDemo.this, "tts_play");*/
        // 设置参数
        val code = mTts!!.startSpeaking(texts, mTtsListener)
        //			/**
        //			 * 只保存音频不进行播放接口,调用此接口请注释startSpeaking接口
        //			 * text:要合成的文本，uri:需要保存的音频全路径，listener:回调接口
        //			*/
        /*String path = Environment.getExternalStorageDirectory()+"/tts.pcm";
			int code = mTts.synthesizeToUri(text, path, mTtsListener);*/

        if (code != ErrorCode.SUCCESS) {
//            showTip("语音合成失败,错误码: $code,请点击网址https://www.xfyun.cn/document/error-code查询解决方案")
        }
    }

    /**
     * 参数设置
     * @return
     */
    private fun setParam() {
        // 清空参数
        mTts!!.setParameter(SpeechConstant.PARAMS, null)
        // 根据合成引擎设置相应参数
        if (mEngineType == SpeechConstant.TYPE_CLOUD) {
            mTts!!.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD)
            //支持实时音频返回，仅在synthesizeToUri条件下支持
            //mTts.setParameter(SpeechConstant.TTS_DATA_NOTIFY, "1");
            // 设置在线合成发音人
            mTts!!.setParameter(SpeechConstant.VOICE_NAME, voicer)
            //设置合成语速
            mTts!!.setParameter(SpeechConstant.SPEED, "50")
            //设置合成音调
            mTts!!.setParameter(SpeechConstant.PITCH, "50")
            //设置合成音量
            mTts!!.setParameter(SpeechConstant.VOLUME, "50")
        } else {
            mTts!!.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL)
            mTts!!.setParameter(SpeechConstant.VOICE_NAME, "")

        }
        //设置播放器音频流类型
        mTts!!.setParameter(SpeechConstant.STREAM_TYPE, "3")
        // 设置播放合成音频打断音乐播放，默认为true
        mTts!!.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true")

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        mTts!!.setParameter(SpeechConstant.AUDIO_FORMAT, "pcm")
        mTts!!.setParameter(
            SpeechConstant.TTS_AUDIO_PATH,
            Environment.getExternalStorageDirectory().toString() + "/msc/tts.pcm"
        )
    }

    /**
     * 合成回调监听。
     */
    private val mTtsListener = object : SynthesizerListener {

        override fun onSpeakBegin() {
            showTip("开始播放")
        }

        override fun onSpeakPaused() {
            showTip("暂停播放")
        }

        override fun onSpeakResumed() {
            showTip("继续播放")
        }

        override fun onBufferProgress(
            percent: Int, beginPos: Int, endPos: Int,
            info: String
        ) {
            // 合成进度
            mPercentForBuffering = percent
        }

        override fun onSpeakProgress(percent: Int, beginPos: Int, endPos: Int) {
            // 播放进度
            mPercentForPlaying = percent
        }

        override fun onCompleted(error: SpeechError?) {
            if (error == null) {
                showTip("播放完成")
                if (mTts != null) {
                    mTts!!.stopSpeaking()
                    // 退出时释放连接
                    mTts!!.destroy()
                }
            } else if (error != null) {

                showTip(error.getPlainDescription(true))
            }
        }

        override fun onEvent(eventType: Int, arg1: Int, arg2: Int, obj: Bundle) {
        }
    }

    private fun showTip(str: String) {
        mToast!!.setText(str)
        mToast!!.show()
    }

}