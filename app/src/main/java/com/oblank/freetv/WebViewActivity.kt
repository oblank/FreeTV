package com.oblank.freetv

import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class WebViewActivity : AppCompatActivity() {
    
    private lateinit var webView: WebView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        
        webView = findViewById(R.id.webview)
        setupWebView()
        loadWebsite()
        setupBackPressedCallback()
    }
    
    private fun setupWebView() {
        webView.apply {
            webViewClient = WebViewClient()
            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                loadWithOverviewMode = true
                useWideViewPort = true
                builtInZoomControls = false
                displayZoomControls = false
                setSupportZoom(false)
                allowFileAccess = true
                allowContentAccess = true
                setGeolocationEnabled(false)
            }
            
            requestFocus()
            isFocusable = true
            isFocusableInTouchMode = true
        }
    }
    
    private fun loadWebsite() {
        webView.loadUrl("https://www.55d4s8c8o6.shop/")
    }
    
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_DPAD_LEFT -> {
                if (webView.canGoBack()) {
                    webView.goBack()
                    return true
                }
            }
            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                if (webView.canGoForward()) {
                    webView.goForward()
                    return true
                }
            }
            KeyEvent.KEYCODE_BACK -> {
                // Let OnBackPressedDispatcher handle back navigation
                return false
            }
            KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER -> {
                webView.dispatchKeyEvent(event)
                return true
            }
            KeyEvent.KEYCODE_DPAD_UP -> {
                webView.scrollBy(0, -100)
                return true
            }
            KeyEvent.KEYCODE_DPAD_DOWN -> {
                webView.scrollBy(0, 100)
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }
    
    private fun setupBackPressedCallback() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (webView.canGoBack()) {
                    webView.goBack()
                } else {
                    finish()
                }
            }
        })
    }
}