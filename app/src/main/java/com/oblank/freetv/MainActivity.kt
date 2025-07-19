package com.oblank.freetv

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.FragmentActivity

/**
 * Loads [MainFragment].
 */
class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Launch WebViewActivity directly
        val intent = Intent(this, WebViewActivity::class.java)
        startActivity(intent)
        finish()
    }
}