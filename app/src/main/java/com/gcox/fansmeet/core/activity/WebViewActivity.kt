package com.gcox.fansmeet.core.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import com.gcox.fansmeet.R
import com.gcox.fansmeet.common.ConstantBundleKey
import kotlinx.android.synthetic.main.activity_view_web.*

/**
 * Created by User on 10/13/2015.
 */
class WebViewActivity : AppCompatActivity() {

    private var url: String? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_web)
        intent?.extras?.let {
            url = it.getString(ConstantBundleKey.BUNDLE_URL_FOR_WEBVIEW)
            web_container.webChromeClient = WebChromeClient()
            web_container.webViewClient = object : WebViewClient() {
            }
            web_container.settings.javaScriptEnabled = true
            web_container.loadUrl(url)
        }
    }

    companion object {
        @JvmStatic
        fun createIntent(context: Context, webUrl: String): Intent {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra(ConstantBundleKey.BUNDLE_URL_FOR_WEBVIEW, webUrl)
            return intent
        }
    }
}
