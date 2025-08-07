package com.goldmorning.bigscreen02

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import android.webkit.WebChromeClient
import android.widget.Toast
import androidx.fragment.app.FragmentActivity

class MainActivity : FragmentActivity() {

    private lateinit var webView1: WebView
    private lateinit var webView2: WebView
    private var currentWebViewIndex = 1
    private val webViews = mutableListOf<WebView>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView1 = findViewById(R.id.webview1)
        webView2 = findViewById(R.id.webview2)
        
        webViews.add(webView1)
        webViews.add(webView2)
        
        setupWebView(webView1, "file:///android_asset/video_player.html")
        setupWebView(webView2, "https://opcenter.huangjinx.com/#/quote")
        
        // Set initial focus and visibility - show webView2 (index 1) by default
        webViews[currentWebViewIndex].visibility = View.VISIBLE
        webViews[currentWebViewIndex].requestFocusFromTouch()
        webViews[(currentWebViewIndex + 1) % webViews.size].visibility = View.GONE

        // Ensure the activity can receive key events
        window.decorView.requestFocus()
    }

    override fun onResume() {
        super.onResume()
        // Ensure current webview has focus when activity resumes
        webViews[currentWebViewIndex].requestFocusFromTouch()
    }

    private fun setupWebView(webView: WebView, url: String) {
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.allowFileAccess = true
        webView.settings.allowContentAccess = true
        webView.settings.useWideViewPort = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.setSupportZoom(false)
        webView.settings.builtInZoomControls = false
        webView.settings.displayZoomControls = false
        webView.settings.mediaPlaybackRequiresUserGesture = false
        webView.settings.userAgentString = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"

        webView.webChromeClient = WebChromeClient()
        
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                
                // Only inject CSS for webview2 (trading platform)
                if (view == webView2) {
                    injectTradingPlatformCSS(webView2)
                }
            }
        }
        
        webView.isFocusable = false
        webView.isFocusableInTouchMode = false

        webView.loadUrl(url)
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            val keyCode = event.keyCode
            val currentWebView = webViews[currentWebViewIndex]

            Log.d("RemoteControl", "dispatchKeyEvent: $keyCode (${getKeyName(keyCode)})")

            // Block all directional keys from reaching webview elements
            when (keyCode) {
                KeyEvent.KEYCODE_DPAD_LEFT,
                KeyEvent.KEYCODE_DPAD_RIGHT,
                KeyEvent.KEYCODE_DPAD_UP,
                KeyEvent.KEYCODE_DPAD_DOWN,
                KeyEvent.KEYCODE_DPAD_CENTER,
                KeyEvent.KEYCODE_ENTER -> {
                    // Handle webview switching for directional keys
                    if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT ||
                        keyCode == KeyEvent.KEYCODE_DPAD_RIGHT ||
                        keyCode == KeyEvent.KEYCODE_DPAD_UP ||
                        keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                        val newIndex = (currentWebViewIndex + 1) % webViews.size
                        switchWebView(newIndex)
                    }
                    // Consume all directional keys, prevent webview elements from responding
                    return true
                }
                KeyEvent.KEYCODE_BACK -> {
                    if (currentWebView.canGoBack()) {
                        currentWebView.goBack()
                        return true
                    }
                }
            }
        }
        return super.dispatchKeyEvent(event)
    }


    private fun getKeyName(keyCode: Int): String {
        return when (keyCode) {
            KeyEvent.KEYCODE_DPAD_LEFT -> "LEFT"
            KeyEvent.KEYCODE_DPAD_RIGHT -> "RIGHT"
            KeyEvent.KEYCODE_DPAD_UP -> "UP"
            KeyEvent.KEYCODE_DPAD_DOWN -> "DOWN"
            KeyEvent.KEYCODE_DPAD_CENTER -> "CENTER"
            KeyEvent.KEYCODE_ENTER -> "ENTER"
            KeyEvent.KEYCODE_BACK -> "BACK"
            else -> "KEY_$keyCode"
        }
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        Log.d("KeyEvent", "Key up: $keyCode")
        return super.onKeyUp(keyCode, event)
    }

    private fun injectTradingPlatformCSS(webView: WebView) {
        val css = """
            javascript:(function() {
                var style = document.createElement('style');
                style.innerHTML = `
                    .header-logout-btn:focus {
                        background-color: transparent !important;
                        border: 1px solid #466df5 !important;
                        outline: none !important;
                    }
                    .header-logout-btn {
                        transition: background-color 0.2s ease, border 0.2s ease !important;
                    }
                `;
                document.head.appendChild(style);
                console.log('CSS injected for .header-logout-btn focus styling');
            })();
        """
        webView.loadUrl(css)
        Log.d("CSSInjection", "CSS injected for trading platform logout button focus styling")
    }

    private fun switchWebView(newIndex: Int) {
        // Validate index
        if (newIndex < 0 || newIndex >= webViews.size) {
            Log.d("WebViewSwitch", "Invalid index: $newIndex")
            return
        }

        Log.d("WebViewSwitch", "=== SWITCHING WEBVIEW ===")
        Log.d("WebViewSwitch", "From: $currentWebViewIndex, To: $newIndex")

        val webviewNames = arrayOf("自动线演示", "金晨大屏")

        // Log before switching
        Log.d("WebViewSwitch", "Before switch:")
        webViews.forEachIndexed { index, webView ->
            Log.d("WebViewSwitch", "  WebView $index visibility: ${webView.visibility}")
        }

        // Hide current webview and handle video playback
        Log.d("WebViewSwitch", "Hiding webview $currentWebViewIndex")
        if (currentWebViewIndex == 0) { // Video player is webview1 (index 0)
            webViews[currentWebViewIndex].evaluateJavascript("handleVisibilityChange()", null)
        }
        webViews[currentWebViewIndex].visibility = View.GONE
        webViews[currentWebViewIndex].clearFocus()

        // Show new webview and handle video playback
        Log.d("WebViewSwitch", "Showing webview $newIndex")
        currentWebViewIndex = newIndex
        webViews[currentWebViewIndex].visibility = View.VISIBLE
        webViews[currentWebViewIndex].requestFocusFromTouch()
        webViews[currentWebViewIndex].bringToFront()
        
        // Handle video playback for video webview
        if (newIndex == 0) { // Video player is webview1 (index 0)
            webViews[currentWebViewIndex].evaluateJavascript("handleVisibilityChange()", null)
        }
        
        // Log after switching
        Log.d("WebViewSwitch", "After switch:")
        webViews.forEachIndexed { index, webView ->
            Log.d("WebViewSwitch", "  WebView $index visibility: ${webView.visibility}")
        }
        
        Toast.makeText(this, "${webviewNames[newIndex]}", Toast.LENGTH_SHORT).show()
        Log.d("WebViewSwitch", "=== SWITCH COMPLETE ===")
    }
}