package com.gcox.fansmeet.webview

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import com.gcox.fansmeet.R
import com.gcox.fansmeet.common.ConstantBundleKey
import com.gcox.fansmeet.core.activity.BaseToolBarActivity
import com.gcox.fansmeet.util.CheckNetwork
import com.gcox.fansmeet.util.DialogManager
import kotlinx.android.synthetic.main.activity_webview_with_toolbar.*

class WebViewWithToolBarActivity : BaseToolBarActivity() {

    private var mWebView: WebView? = null
    private val mHandler = Handler()

    //region-------activity life cycle-------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prizeUrlInfo = intent?.getStringExtra(ConstantBundleKey.BUNDLE_URL_FOR_WEBVIEW)
        if (TextUtils.isEmpty(prizeUrlInfo)) {
            // This should never happen
            finish()
            return
        }
        mHandler.postDelayed({
            setupWebViewAndShowInfo(prizeUrlInfo!!)
        }, 500)
//        setupWebViewAndShowInfo(prizeUrlInfo)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        mWebView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mWebView?.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
        mWebView?.destroy()
    }
    //endregion -------activity life cycle-------

    //region -------inheritance methods-------

    //endregion -------inheritance methods-------

    //region -------implement methods-------
    override fun getLayoutContentId(): Int {
        return R.layout.activity_webview_with_toolbar
    }

    override fun init() {
        val title = intent?.getStringExtra(ConstantBundleKey.BUNDLE_TITLE_FOR_WEBVIEW)
        if (TextUtils.isEmpty(title)) {
            removeToolbarTitle()
        } else {
            setTopBarTile(title)
        }
//        goneNotify(true)
        useAppToolbarBackButton()
        eventClickBack.setOnClickListener {
            finish()
        }
    }
    //endregion -------implement methods-------

    //region -------inner methods-------
    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebViewAndShowInfo(prizeUrlInfo: String) {
        if (!CheckNetwork.isNetworkAvailable(this)) {
            utility.showMessage("", getString(R.string.network_error), this)
            return
        }
        showProgress()
        val wv = WebView(this)
        wv.layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        wv.settings?.apply {
            javaScriptEnabled = true
        }
        wv.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                hideProgress()
            }
        }
        wv.loadUrl(prizeUrlInfo)
        mWebView = wv
        lo_prize_info.addView(wv)
    }

    fun showProgress() {
        DialogManager.getInstance().showDialog(this, resources.getString(R.string.connecting_msg), false)
    }

    fun hideProgress() {
        DialogManager.getInstance().dismisDialog()
    }
    //endregion -------inner methods-------


    //region -------inner class-------
    companion object {
        @JvmStatic
        fun createIntent(activity: Activity, title: String?, url: String): Intent {
            val intent = Intent(activity, WebViewWithToolBarActivity::class.java)
            intent.putExtra(ConstantBundleKey.BUNDLE_TITLE_FOR_WEBVIEW, title)
            intent.putExtra(ConstantBundleKey.BUNDLE_URL_FOR_WEBVIEW, url)
            return intent
        }
    }
    //endregion -------inner class-------
}
