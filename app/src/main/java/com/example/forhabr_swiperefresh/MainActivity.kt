package com.example.forhabr_swiperefresh

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.forhabr_swiperefresh.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(
            this, R.layout.activity_main
        )
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.loadUrl("https://www.journaldev.com")
    }
}
