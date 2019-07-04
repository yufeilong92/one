package com.zzzh.akhalteke.utils

import android.content.Context
import android.os.Build
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient

class WebHelper(var mContext: Context) {

    fun initWeb(webView: WebView) {
        val webSettings = webView.settings.apply {
            javaScriptEnabled = true // 支持js
            useWideViewPort = false // 将图片调整到适合webview的大小
            loadWithOverviewMode = true // 缩放至屏幕的大小
            loadsImagesAutomatically = true //  支持自动加载图片
            builtInZoomControls = true  // 设置支持缩放
            setSupportZoom(true) // 支持缩放

            domStorageEnabled = true
            allowContentAccess = true
            setAppCacheEnabled(true)
            databaseEnabled = true
            cacheMode = WebSettings.LOAD_NO_CACHE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
        }
        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        webView.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        webSettings.setAppCachePath(mContext.cacheDir.absolutePath)

        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view!!.loadUrl(url)
                return true
            }
        }
    }

    fun initWebSimple(webView: WebView){
        val webSettings = webView.settings.apply {
            javaScriptEnabled = true // 支持js
            useWideViewPort = false // 将图片调整到适合webview的大小
            loadWithOverviewMode = true // 缩放至屏幕的大小
            loadsImagesAutomatically = true //  支持自动加载图片
            builtInZoomControls = true  // 设置支持缩放
            setSupportZoom(true) // 支持缩放
        }
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view!!.loadUrl(url)
                return true
            }
        }
    }

    fun toWebChromeClient(webView: WebView){
        webView.webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView?, title: String?) {
                // 设置title
                super.onReceivedTitle(view, title)
            }

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                // 加载进度
                super.onProgressChanged(view, newProgress)
                if(newProgress == 100){

                }
            }
        }
    }

}