package com.zzzh.akhalteke.ui.weather

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.zzzh.akhalteke.R
import com.zzzh.akhalteke.WebViewActivity
import com.zzzh.akhalteke.bean.DataMessageVo
import com.zzzh.akhalteke.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_web_view.*

/**
 * @Author : YFL  is Creating a porject in com.zzzh.akhalteke.ui.weather
 * @Package com.zzzh.akhalteke.ui.weather
 * @Email : yufeilong92@163.com
 * @Time :2019/5/6 17:01
 * @Purpose :天气webview
 */
class WeatherWebView2Activity : BaseActivity() {
    companion object {
        val URL = "param_webview"
        val TYPE = "type"
        val NEW_TYPE = "NEWTYPE"
        val NO_HEAR_HTTP_TYPE = "NOHEARHTTPTYPE"
    }

    private var mUrl: String? = ""
    private var mType: String? = ""
    override fun setContentView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_weather_web_view2)
        if (intent != null) {
            mUrl = intent.getStringExtra(WebViewActivity.URL)
            mType = intent.getStringExtra(WebViewActivity.TYPE)
        }
        initWebView()
    }

    fun initWebView() {
        //解决webview缓存问题
        if (mType == WebViewActivity.NEW_TYPE) {
            web_view_gm.loadUrl(mUrl)
        } else {
            web_view_gm.loadUrl(DataMessageVo.HTTP_HEAR + mUrl)
        }
        setWebVIewSetting(web_view_gm)
        web_view_gm.webChromeClient = object : WebChromeClient() {};
        web_view_gm.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                if (url == null) return false
                try {
                    if (url.startsWith("http:") || url.startsWith("https:")) {
                        if (!url.contains("https://apip.weatherdt.com/h5.html?id=HAXF8dYQvm")) {
                            return true
                        }
                        view!!.loadUrl(url)
                        return true
                    } else {
                        if (!url.contains("https://apip.weatherdt.com/h5.html?id=HAXF8dYQvm")) {
                            return true
                        }
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        mContext.startActivity(intent)
                        return true
                    }
                } catch (e: Exception) { //防止crash (如果手机上没有安装处理某个scheme开头的url的APP, 会导致crash)
                    return false
                }
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                showProgress()
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                dismissProgress()
            }

        }

    }

    /**
     * 设置点击返回按钮，跳转到上一个html页面，而不是退出当前activity
     *
     * @param keyCode
     * @param event
     * @return
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (event.keyCode == KeyEvent.KEYCODE_BACK) {
            if (keyCode == KeyEvent.KEYCODE_BACK && web_view_gm.canGoBack()) {
                web_view_gm.goBack()// 返回前一个页面
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

}
