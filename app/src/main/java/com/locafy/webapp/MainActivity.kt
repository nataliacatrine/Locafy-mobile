package com.locafy.webapp

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.browser.customtabs.CustomTabsIntent
import com.locafy.webapp.databinding.ActivityMainBinding

class MainActivity : ComponentActivity() {

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val webView = binding.webView

        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.setSupportMultipleWindows(true)
        webView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

        // Define o comportamento ao carregar URLs
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                url?.let {
                    if (it.contains("accounts.google.com") || it.contains("oauth") || !it.contains("locafyproject.netlify.app")) {
                        // Se for link de autenticação ou link externo, abre com Custom Tabs
                        openCustomTab(it)
                        return true
                    } else {
                        // Se for link interno, mantém dentro do WebView
                        view?.loadUrl(it)
                        return true
                    }
                }
                return false
            }
        }
        webView.loadUrl("https://locafyproject.netlify.app/")
    }

    // Função para abrir link externo usando Chrome Custom Tabs
    private fun openCustomTab(url: String) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(url))
    }
}
