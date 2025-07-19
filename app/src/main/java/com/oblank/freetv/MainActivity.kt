package com.oblank.freetv

import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.FragmentActivity

class MainActivity : FragmentActivity() {

    private lateinit var webView: WebView
    private val scrollStep = 100
    private var currentFocusIndex = 0
    private var focusableElements = 0

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
        
        webView.addJavascriptInterface(FocusInterface(), "AndroidFocus")
        
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                injectFocusCSS()
                initializeFocus()
            }
        }
        
        webView.loadUrl("https://www.55d4s8c8o6.shop/")
    }

    private fun injectFocusCSS() {
        val css = """
            javascript:(function() {
                var style = document.createElement('style');
                style.innerHTML = `
                    .tv-focused {
                        border: 2px solid red !important;
                        box-sizing: border-box !important;
                    }
                    .module-poster-item, .module-item {
                        transition: border 0.2s ease !important;
                    }
                    .fixedGroup, .small-icon-container {
                        display: none !important;
                    }
                `;
                document.head.appendChild(style);
            })();
        """
        webView.loadUrl(css)
    }

    private fun initializeFocus() {
        val js = """
            javascript:(function() {
                window.focusableElements = document.querySelectorAll('.module-poster-item, .module-item');
                window.currentFocusIndex = 0;
                if (window.focusableElements.length > 0) {
                    window.focusableElements[0].classList.add('tv-focused');
                    window.focusableElements[0].scrollIntoView({behavior: 'smooth', block: 'center'});
                }
                AndroidFocus.updateElementCount(window.focusableElements.length);
            })();
        """
        webView.loadUrl(js)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_DPAD_UP -> {
                navigateFocus(-1)
                return true
            }
            KeyEvent.KEYCODE_DPAD_DOWN -> {
                navigateFocus(1)
                return true
            }
            KeyEvent.KEYCODE_DPAD_LEFT -> {
                navigateFocus(-1)
                return true
            }
            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                navigateFocus(1)
                return true
            }
            KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER -> {
                clickFocusedElement()
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

    private fun navigateFocus(direction: Int) {
        val js = """
            javascript:(function() {
                if (window.focusableElements && window.focusableElements.length > 0) {
                    window.focusableElements[window.currentFocusIndex].classList.remove('tv-focused');
                    
                    window.currentFocusIndex += $direction;
                    if (window.currentFocusIndex < 0) {
                        window.currentFocusIndex = window.focusableElements.length - 1;
                    } else if (window.currentFocusIndex >= window.focusableElements.length) {
                        window.currentFocusIndex = 0;
                    }
                    
                    window.focusableElements[window.currentFocusIndex].classList.add('tv-focused');
                    window.focusableElements[window.currentFocusIndex].scrollIntoView({behavior: 'smooth', block: 'center'});
                }
            })();
        """
        webView.loadUrl(js)
    }

    private fun clickFocusedElement() {
        val js = """
            javascript:(function() {
                if (window.focusableElements && window.currentFocusIndex < window.focusableElements.length) {
                    window.focusableElements[window.currentFocusIndex].click();
                }
            })();
        """
        webView.loadUrl(js)
    }

    inner class FocusInterface {
        @JavascriptInterface
        fun updateElementCount(count: Int) {
            focusableElements = count
        }
    }
}