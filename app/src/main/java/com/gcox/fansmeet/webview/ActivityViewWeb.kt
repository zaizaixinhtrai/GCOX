package com.gcox.fansmeet.webview

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.text.TextUtils
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.share.Sharer
import com.gcox.fansmeet.R
import com.gcox.fansmeet.appdata.AppPreferences
import com.gcox.fansmeet.common.ConstantBundleKey
import com.gcox.fansmeet.core.activity.BaseToolBarActivity
import com.gcox.fansmeet.manager.SocialManager
import com.gcox.fansmeet.models.UserModel
import com.gcox.fansmeet.util.BranchIoUtil
import com.gcox.fansmeet.util.CheckNetwork
import kotlinx.android.synthetic.main.activity_view_web.*
import timber.log.Timber

/**
 * Created by User on 10/13/2015.
 */
class ActivityViewWeb : BaseToolBarActivity() {


    private var url: String? = null
    private val TWEET_SHARE_REQUEST_CODE = 509
    private val FACEBOOK_SHARE_REQUEST_CODE = 609
    val fbCallbackManager: CallbackManager by lazy { CallbackManager.Factory.create() }
    internal var mSharableBranchIoUrl: String? = null
    val mUserModel: UserModel by lazy { AppPreferences.getInstance(this).userModel }
    private var isShowTabBar = false
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_view_web)
        intent?.extras?.let {
            url = it.getString(ConstantBundleKey.BUNDLE_URL_FOR_WEBVIEW)
            isShowTabBar = it.getBoolean(IS_SHOW_TAB_BAR)
            if (isShowTabBar) setTopBarTile("")

            if (isShowTabBar) {
                setToolbarColor("#00203B")
            } else {
                handleToolbar(false)
                updateTopPadding(0)
            }

            web_container.webChromeClient = WebChromeClient()
//            web_container.isHorizontalScrollBarEnabled = false
            web_container.webViewClient = object : WebViewClient() {
                @RequiresApi(Build.VERSION_CODES.N)
                override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                    when {
                        request.url.toString().startsWith("belivesgapp://") -> handleActions(
                            request.url.toString().substring(
                                "belivesgapp://".length
                            )
                        )
                        else -> view.loadUrl(request.url.toString())
                    }
                    return true
                }

                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    when {
                        url.startsWith("belivesgapp://") -> handleActions(url.substring("belivesgapp://".length))
                        else -> view.loadUrl(url)
                    }
                    return true
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    if (isShowTabBar) this@ActivityViewWeb.setTopBarTile(view?.title)
                }
            }

            handleTurnoffMenuSliding()
            web_container.settings.javaScriptEnabled = true
            web_container.loadUrl(url)
        }
    }

    private fun updateTopPadding(topPadding: Int) {
        if (contentLayout != null && contentLayout.paddingTop != topPadding) {
            contentLayout.setPadding(0, topPadding, 0, 0)
        }
    }

    override fun getLayoutContentId(): Int {
        return R.layout.activity_view_web
    }

    override fun init() {
        useAppToolbarBackButton()
        eventClickBack.setOnClickListener {
            finish()
        }
    }

    private fun handleActions(uri: String?) {
        Timber.e("handleActions ${uri.toString()}")
        val actions = uri?.split("/".toRegex())
        when (actions?.size) {
            1 -> handlePrimaryAction(actions[0])
            3 -> handleMultipleActions(actions[0], actions[1], actions[2])
            else -> Unit
        }

    }

    private fun handleMultipleActions(primary: String, secondary: String, content: String) {
        when (primary) {
            "share" -> when (secondary) {
                "facebook" -> sharefacebook(content)
                "twitter" -> shareTwitter(content)
                "whatsapp" -> shareWhatsApp(content)
                "email" -> shareEmail(content)
                else -> shareOther(content)
            }
            else -> Unit
        }
    }

    private fun handlePrimaryAction(it: String) {
        if (it.equals("close", true)) {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    companion object {
        @JvmStatic
        val IS_SHOW_TAB_BAR = "is_show_tab_bar"

        fun createIntent(context: Context, webUrl: String, isShowTabBar: Boolean): Intent {
            val intent = Intent(context, ActivityViewWeb::class.java)
            intent.putExtra(ConstantBundleKey.BUNDLE_URL_FOR_WEBVIEW, webUrl)
            intent.putExtra(IS_SHOW_TAB_BAR, isShowTabBar)
            return intent
        }
    }

    fun sharefacebook(msgContent: String?) {
        getBranchIoUrl(BranchIoUtil.OnBranchIoCallback { url ->
            if (!isDestroyed) {
                mSharableBranchIoUrl = url
                SocialManager.getInstance().shareURLToFacebook(this, url,
                    FACEBOOK_SHARE_REQUEST_CODE, fbCallbackManager, object : FacebookCallback<Sharer.Result> {
                        override fun onSuccess(result: Sharer.Result) {
                            Timber.e("onSuccess")
                        }

                        override fun onCancel() {
                            Timber.e("onCancel")
                        }

                        override fun onError(error: FacebookException) {
                            Timber.e("onError")
                        }
                    })
            }
        })

    }

    fun shareTwitter(msgContent: String?) {
        getBranchIoUrl(BranchIoUtil.OnBranchIoCallback { url ->

            if (!isDestroyed) {
                val content = String.format(getString(R.string.invite_sns_trivia_message), mUserModel.referralId)
                SocialManager.getInstance().ShareFeedQuotesToTwitter(this, content, url)

            }
        })
    }

    fun shareEmail(msgContent: String?) {
        getBranchIoUrl(BranchIoUtil.OnBranchIoCallback { url ->
            if (!isDestroyed) {
                val content = String.format(getString(R.string.invite_sns_trivia_message), mUserModel.referralId)
                val subject = getString(R.string.invite_mail_subject)
                SocialManager.getInstance().shareURLToEmail(this, content, subject, url)

            }
        })
    }

    fun shareWhatsApp(msgContent: String?) {
        getBranchIoUrl(BranchIoUtil.OnBranchIoCallback { url ->
            if (!isDestroyed) {
                val content = String.format(getString(R.string.invite_sns_trivia_message), mUserModel.referralId)
                SocialManager.getInstance().shareVideoToWhatsapp(this, content, url)

            }
        })
    }

    fun shareOther(msgContent: String?) {
        getBranchIoUrl(BranchIoUtil.OnBranchIoCallback { url ->
            if (!isDestroyed) {
                val content =
                    String.format(getString(R.string.invite_sns_trivia_message), mUserModel.referralId) + "\n" + url
                val sharingIntent = Intent(Intent.ACTION_SEND)
                sharingIntent.type = "text/plain"
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "")
                sharingIntent.putExtra(Intent.EXTRA_TEXT, content)
                startActivity(Intent.createChooser(sharingIntent, "Share via"))

            }
        })
    }


    fun getBranchIoUrl(callback: BranchIoUtil.OnBranchIoCallback) {
        if (!CheckNetwork.isNetworkAvailable(this)) {
            return
        }

        if (!TextUtils.isEmpty(mSharableBranchIoUrl)) {
            callback.onComplete(mSharableBranchIoUrl)
            return
        }

        BranchIoUtil.generateBranchIoUrl(this, mUserModel.userImage, mUserModel.referralId, callback)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        fbCallbackManager.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            TWEET_SHARE_REQUEST_CODE -> if (resultCode == RESULT_OK) {

            }
            else -> {
            }
        }
        Timber.e("Request code %d - resultCode %s", requestCode, resultCode)
    }
}
