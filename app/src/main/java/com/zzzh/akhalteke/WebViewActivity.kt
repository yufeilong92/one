package com.zzzh.akhalteke

import android.os.Bundle
import com.zzzh.akhalteke.bean.DataMessageVo
import com.zzzh.akhalteke.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_web_view.*
import android.graphics.Bitmap
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.KeyEvent
import android.webkit.*
import android.annotation.SuppressLint
import android.webkit.WebResourceRequest


/**
 * @Author : YFL  is Creating a porject in com.zzzh.akhalteke.ui.me
 * @Package com.zzzh.akhalteke.ui.me
 * @Email : yufeilong92@163.com
 * @Time :2019/3/30 15:40
 * @Purpose :web加载
 */
class WebViewActivity : BaseActivity() {
    companion object {
        val URL = "param_webview"
        val TYPE = "type"
        val NEW_TYPE = "NEWTYPE"
        val NO_HEAR_HTTP_TYPE = "NOHEARHTTPTYPE"
    }

    private var mUrl: String? = ""
    private var mType: String? = ""
    override fun setContentView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_web_view)
        if (intent != null) {
            mUrl = intent.getStringExtra(URL)
            mType = intent.getStringExtra(TYPE)
        }
        initWebView()
    }

    fun initWebView() {
        //解决webview缓存问题
        if (mType == NEW_TYPE) {
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
                        view!!.loadUrl(url)
                        return true
                    } else {
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
                if (!this@WebViewActivity.isFinishing)
                    showProgress()
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (!this@WebViewActivity.isFinishing)
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
