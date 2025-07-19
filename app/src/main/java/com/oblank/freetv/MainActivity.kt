package com.oblank.freetv

import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.FragmentActivity

class MainActivity : FragmentActivity() {

    private lateinit var webView: WebView
    private val scrollStep = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.webview)
        
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.allowFileAccess = true
        webView.settings.allowContentAccess = true
        webView.settings.useWideViewPort = true
        webView.settings.loadWithOverviewMode = true
        
        webView.webViewClient = WebViewClient()
        
        webView.loadUrl("https://www.55d4s8c8o6.shop/")
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_DPAD_UP -> {
                webView.scrollBy(0, -scrollStep)
                return true
            }
            KeyEvent.KEYCODE_DPAD_DOWN -> {
                webView.scrollBy(0, scrollStep)
                return true
            }
            KeyEvent.KEYCODE_DPAD_LEFT -> {
                webView.scrollBy(-scrollStep, 0)
                return true
            }
            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                webView.scrollBy(scrollStep, 0)
                return true
            }
            KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER -> {
                simulateClick()
                return true
            }
            KeyEvent.KEYCODE_BACK -> {
                if (webView.canGoBack()) {
                    webView.goBack()
                    return true
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun simulateClick() {
        val x = webView.width / 2f
        val y = webView.height / 2f
        val downTime = System.currentTimeMillis()
        val eventTime = System.currentTimeMillis()
        
        val downEvent = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, x, y, 0)
        val upEvent = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, x, y, 0)
        
        webView.dispatchTouchEvent(downEvent)
        webView.dispatchTouchEvent(upEvent)
        
        downEvent.recycle()
        upEvent.recycle()
    }
}