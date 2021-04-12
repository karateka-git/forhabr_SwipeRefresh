package com.example.forhabr_swiperefresh

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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
        binding.webView.loadUrl("https://www.google.com/search?newwindow=1&sxsrf=ALeKk02Xt8LeDNhoH8es8g-jq_Rd0Y6MtA%3A1614334825312&ei=acs4YNKnEqKHwPAP7I2LiAo&q=disney&oq=disney&gs_lcp=Cgdnd3Mtd2l6EAxQAFgAYKwXaABwAngAgAG9AYgBvQGSAQMwLjGYAQCqAQdnd3Mtd2l6wAEB&sclient=gws-wiz&ved=0ahUKEwiSgL7uqYfvAhWiAxAIHezGAqEQ4dUDCA0")
        binding.swipeContainer.setOnRefreshListener {
            binding.webView.reload()
            binding.swipeContainer.isRefreshing = false
        }
        binding.webView.setOnClickListener { binding.webView.requestFocus() }
        binding.webView.setWebPageListener(this)
    }

    override fun refreshSwipeEnable() {
        Log.d("VLADISLAV", "swipeContainer - ${!binding.swipeContainer.isRefreshing}")
        if (!binding.swipeContainer.isRefreshing) {
            binding.webView.let {
                Log.d("VLADISLAV", "PARAMETERS canScrollVertically - ${it.canScrollVertically(-1)} overScroll - ${it.overScroll}")
                Log.d("VLADISLAV", "BEFORE isNestedScrollingEnabled - ${it.isNestedScrollingEnabled} swipeContainer - ${binding.swipeContainer.isEnabled}")
                it.isNestedScrollingEnabled =
                    it.canScrollVertically(-1) ||
                    !it.overScroll
                binding.swipeContainer.isEnabled =
                    !it.canScrollVertically(-1) &&
                    it.overScroll
                Log.d("VLADISLAV", "AFTER isNestedScrollingEnabled - ${it.isNestedScrollingEnabled} swipeContainer - ${binding.swipeContainer.isEnabled}")
            }
        }
    }
}
