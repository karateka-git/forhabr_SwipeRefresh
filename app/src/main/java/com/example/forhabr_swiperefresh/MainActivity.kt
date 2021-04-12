package com.example.forhabr_swiperefresh

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.CookieManager
import android.webkit.WebStorage
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.forhabr_swiperefresh.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), WebViewListener {
    private lateinit var binding: ActivityMainBinding
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_main
        )
        setSupportActionBar(binding.toolbar)
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.setSupportZoom(true)
        binding.webView.settings.builtInZoomControls = true
        binding.webView.settings.displayZoomControls = false
        binding.webView.webViewClient = MyWebViewClient()
        binding.swipeContainer.setOnRefreshListener {
            binding.webView.reload()
            binding.swipeContainer.isRefreshing = false
        }
        binding.webView.setWebPageListener(this)
        binding.webView.loadUrl("https://secure07c.chase.com/web/auth/dashboard#/dashboard/overviewAccounts/overview/index")
    }

    override fun refreshSwipeEnable() {
        Log.d("VLADISLAV", "swipeContainer - ${!binding.swipeContainer.isRefreshing}")
        if (!binding.swipeContainer.isRefreshing) {
            binding.webView.let {
                Log.d("VLADISLAV", "PARAMETERS canScrollVertically - ${it.canScrollVertically(-1)} overScroll - ${it.overScroll}")
                Log.d("VLADISLAV", "BEFORE isNestedScrollingEnabled - ${it.isNestedScrollingEnabled} swipeContainer - ${binding.swipeContainer.isEnabled}")
                it.isNestedScrollingEnabled =
                    it.canScrollVertically(-1) ||
                    !it.overScroll ||
                    it.pointerCount > 1 ||
                    binding.appBarLayout.bottom != binding.appBarLayout.height
                binding.swipeContainer.isEnabled =
                    !it.canScrollVertically(-1) &&
                    it.overScroll &&
                    it.pointerCount == 1 &&
                    binding.appBarLayout.bottom == binding.appBarLayout.height
                Log.d("VLADISLAV", "AFTER isNestedScrollingEnabled - ${it.isNestedScrollingEnabled} swipeContainer - ${binding.swipeContainer.isEnabled}")
            }
        }
    }
}
