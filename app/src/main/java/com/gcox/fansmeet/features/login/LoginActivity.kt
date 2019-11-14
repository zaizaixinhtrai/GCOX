package com.gcox.fansmeet.features.login

import android.arch.lifecycle.Observer
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import com.gcox.fansmeet.AppsterApplication
import com.gcox.fansmeet.R
import com.gcox.fansmeet.common.ConstantBundleKey
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.core.activity.BaseActivity
import com.gcox.fansmeet.core.activity.WebViewActivity
import com.gcox.fansmeet.customview.ScalableVideo.ScalableType
import com.gcox.fansmeet.features.main.MainActivity
import com.gcox.fansmeet.features.register.RegisterActivity
import com.gcox.fansmeet.location.GPSTClass
import com.gcox.fansmeet.manager.ShowErrorManager
import com.gcox.fansmeet.manager.SocialManager
import com.gcox.fansmeet.models.TwitterInformationModel
import com.gcox.fansmeet.util.CheckNetwork
import com.gcox.fansmeet.util.DeviceInfo
import com.gcox.fansmeet.util.StringUtil
import com.gcox.fansmeet.util.instagram.InstagramHelper
import com.gcox.fansmeet.util.instagram.InstagramModel
import com.gcox.fansmeet.webservice.request.*
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.identity.TwitterAuthClient
import com.twitter.sdk.android.core.models.User
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class LoginActivity : BaseActivity(), GoogleApiClient.OnConnectionFailedListener, SocialManager.SocialLoginListener,
    InstagramHelper.OAuthAuthenticationListener {


    private val loginViewModel: LoginViewModel by viewModel()
    private val mTwitterAuthClient: TwitterAuthClient by lazy { TwitterAuthClient() }
    private var instagramHelper: InstagramHelper? = null
    private var saveGoogleLoginModel: GoogleLoginRequestModel? = null
    private var saveFacebookLoginModel: LoginFacebookRequestModel? = null
    private var saveInstagramLoginModel: InstagramLoginRequestModel? = null
    private var isPause: Boolean = false


    companion object {
        private const val PLAY_SERVICES_RESOLUTION_REQUEST = 9000
        private const val MESSAGE_GET_START_APP = 999999
        const val GG_REQUEST_SIGN_IN = 9009
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        calcKeyboardHeight()
        updateTermTextView()
        observeData()
        setupEvents()
        initInstagramHelper()

    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
            if (requestCode == GG_REQUEST_SIGN_IN) {
                val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
                onGoogleLoginResponse(result)
            }

            SocialManager.getInstance().onActivityResult(requestCode, resultCode, data)
            mTwitterAuthClient.onActivityResult(requestCode, resultCode, data)
        } catch (e: Exception) {
            Timber.e(e)
        }

    }

    override fun onResume() {
        super.onResume()

        if (isPause) {
            startMedia()
        } else {
            playVideo()
        }
        isPause = false
    }

    override fun onPause() {
        super.onPause()
        pauseMedia()
        isPause = true
    }

    override fun onDestroy() {
        super.onDestroy()
        loginViewModel.disposableComposite()
        releaseMedia()
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    override fun onStartingAuthentication() {
    }

    override fun onLoginFail(message: String?) {
    }

    override fun onAuthentSuccess() {
    }

    override fun loginWithFacebookInfo(requestLogin: LoginFacebookRequestModel?) {
        showDialog(this, getString(R.string.connecting_msg))
        saveFacebookLoginModel = requestLogin
        loginViewModel.loginWithFacebook(requestLogin)
    }

    override fun onCompleteLogin() {
    }

    override fun onInstagramLoginSuccessfully(instagramSession: InstagramModel?) {
        // userInfoHashmap = instagramHelper.
        val requestLogin = InstagramLoginRequestModel()
        requestLogin.device_udid = DeviceInfo.getDeviceDetail(this)

        instagramSession?.let {
            requestLogin.instagramId = instagramSession.id
            requestLogin.userName = instagramSession.username
            requestLogin.display_name = instagramSession.fullName
            AppsterApplication.mAppPreferences.facebookDisplayName = instagramSession.fullName
            if (instagramSession?.profilePicture != null)
                requestLogin.profile_Pic = instagramSession.profilePicture
        }

        setBaseLoginRequest(requestLogin)
        showDialog(this, getString(R.string.connecting_msg))
        saveInstagramLoginModel = requestLogin
        loginViewModel.loginWithInstagram(requestLogin)
    }

    override fun onInstagramLoginFail(error: String?) {
    }

    fun initInstagramHelper() {
        instagramHelper = InstagramHelper(
            this, Constants.INSTAGRAM_CLIENT_ID,
            Constants.INSTAGRAM_CLIENT_SECRET, Constants.INSTAGRAM_CALLBACK_URL
        )
        instagramHelper?.setListener(this)
    }

    private fun updateTermTextView() {
        val bySignUp = getString(R.string.login_by_signing_up) + " "
        val terms = getString(R.string.login_terms)
        val and = " " + getString(R.string.login_and) + " "
        val privacy = getString(R.string.login_privacy_policy)
        val dots = getString(R.string.login_dot)

        val text = bySignUp + terms + and + privacy + dots

        val styledString = SpannableString(text)
        // underline text
        styledString.setSpan(
            UnderlineSpan(), bySignUp.length,
            bySignUp.length + terms.length, 0
        )
        styledString.setSpan(
            UnderlineSpan(),
            bySignUp.length + terms.length + and.length,
            bySignUp.length + terms.length + and.length + privacy.length, 0
        )

        val clickableTerms = object : ClickableSpan() {

            override fun onClick(widget: View) {
                val options = ActivityOptionsCompat.makeCustomAnimation(
                    this@LoginActivity,
                    R.anim.push_in_to_right,
                    R.anim.push_in_to_left
                )
                val intentTerms = WebViewActivity.createIntent(this@LoginActivity, Constants.URL_TERMS_CONDITION)
                ActivityCompat.startActivity(this@LoginActivity, intentTerms, options.toBundle())
            }
        }

        styledString.setSpan(
            clickableTerms,
            bySignUp.length,
            bySignUp.length + terms.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        val clickablePrivacy = object : ClickableSpan() {

            override fun onClick(widget: View) {
                val options = ActivityOptionsCompat.makeCustomAnimation(
                    this@LoginActivity,
                    R.anim.push_in_to_right,
                    R.anim.push_in_to_left
                )
                val intentPrivacy = WebViewActivity.createIntent(this@LoginActivity, Constants.URL_PRIVACY_POLICY)
                ActivityCompat.startActivity(this@LoginActivity, intentPrivacy, options.toBundle())
            }
        }
        styledString.setSpan(
            clickablePrivacy, bySignUp.length + terms.length + and.length,
            bySignUp.length + terms.length + and.length + privacy.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        styledString.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this@LoginActivity, R.color.white)),
            0, text.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        tv_term.text = styledString
        tv_term.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun observeData() {
        loginViewModel.getError().observe(this, Observer {
            Timber.e(it?.message)
            if (it!!.code == ShowErrorManager.user_not_found) {
                when (loginViewModel.loginType) {
                    LoginViewModel.LoginType.GOOGLE -> {
                        goToCreateProfile(saveGoogleLoginModel!!)
                    }

                    LoginViewModel.LoginType.FACEBOOK -> {
                        goToCreateProfile(saveFacebookLoginModel!!)
                    }

                    LoginViewModel.LoginType.INSTAGRAM -> {
                        goToCreateProfile(saveInstagramLoginModel!!)
                    }
                }
            } else {
                handleError(it.message, it.code)
            }
            dismissDialog()
        })

        loginViewModel.getLoginData.observe(this, Observer {
            dismissDialog()
            navigateToActivity(MainActivity::class.java)
        })
    }

    private fun setupEvents() {
        btn_facebook_login.setOnClickListener { onFacebookButtonClicked() }
        btn_google_login.setOnClickListener { onGoogleButtonClicked() }
        btn_instagram_login.setOnClickListener { instagramHelper?.authorize() }
    }

    private fun onTwitterClicked() {

        if (preventMultiClicks()) {
            return
        }

        if (!CheckNetwork.isNetworkAvailable(this@LoginActivity)) {
            utility?.showMessage(
                getString(R.string.app_name),
                getString(R.string.no_internet_connection),
                this@LoginActivity
            )
            return
        }
        getTwitterInformation(mTwitterAuthClient)
    }

    private fun getTwitterInformation(twitterAuthClient: TwitterAuthClient) {
        twitterAuthClient.authorize(this, object : com.twitter.sdk.android.core.Callback<TwitterSession>() {

            override fun success(result: Result<TwitterSession>) {
                fetchTwitterEmail(result.data)
            }

            override fun failure(exception: TwitterException) {
                Toast.makeText(applicationContext, exception.message, Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    private fun fetchTwitterEmail(twitterSession: TwitterSession) {

        val informationModel = TwitterInformationModel()
        //pass includeEmail : true if you want to fetch Email as well
        if (getTwitterSession() != null) {
            mTwitterAuthClient.requestEmail(twitterSession, object : Callback<String>() {
                override fun success(result: Result<String>) {
                    informationModel.twitterEmail = result.data

                    val twitterApiClient = TwitterCore.getInstance().apiClient
                    val call = twitterApiClient.accountService.verifyCredentials(true, false, true)
                    call.enqueue(object : Callback<User>() {
                        override fun success(result: Result<User>) {
                            val user = result.data
                            val imageProfileUrl = user.profileImageUrl.replace("_normal", "")

                            informationModel.twitterImage = imageProfileUrl
                            informationModel.twitterId = user.id.toString() + ""
                            informationModel.twitterUsername = user.screenName
                            informationModel.twitterDisplayName = user.name
                            informationModel.twitterEmail = user.email
                            getTwitterInformationSuccess(informationModel)
                        }

                        override fun failure(exception: TwitterException) {
                            Toast.makeText(applicationContext, exception.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    })

                }

                override fun failure(exception: TwitterException) {
                    Toast.makeText(applicationContext, exception.message, Toast.LENGTH_SHORT)
                        .show()
                }
            })

        }
    }

    private fun getTwitterInformationSuccess(informationModel: TwitterInformationModel) {
        val loginRequestModel = TwitterLoginRequestModel()
        loginRequestModel.twitterId = informationModel.twitterId
        loginRequestModel.email = informationModel.twitterEmail
        loginRequestModel.display_name = informationModel.twitterDisplayName
        loginRequestModel.userName = informationModel.twitterUsername
        loginRequestModel.profile_Pic = informationModel.twitterImage

        setBaseLoginRequest(loginRequestModel)
        showDialog(this, getString(R.string.connecting_msg))
//        loginViewModel.loginWithTwitter(loginRequestModel)
    }

    private fun getTwitterSession(): TwitterSession {
        return TwitterCore.getInstance().sessionManager.activeSession

        //NOTE : if you want to get token and secret too use uncomment the below code
        /*TwitterAuthToken authToken = session.getAuthToken();
        String token = authToken.token;
        String secret = authToken.secret;*/
    }

    private fun onGoogleLoginResponse(result: GoogleSignInResult) {
        if (result.isSuccess) {
            // Signed in successfolly, show authenticated UI.
            val acct = result.signInAccount
            val requestLogin = GoogleLoginRequestModel()
            requestLogin.device_udid = DeviceInfo.getDeviceDetail(this)
            val email = acct!!.email
            requestLogin.userName = StringUtil.extractUserNameFromEmail(email)
            requestLogin.email = email
            requestLogin.googleId = acct.id
            requestLogin.display_name = acct.displayName
            AppsterApplication.mAppPreferences.facebookDisplayName = acct.displayName
            if (acct.photoUrl != null)
                requestLogin.profile_Pic = acct.photoUrl!!.toString()

            setBaseLoginRequest(requestLogin)
            showDialog(this, getString(R.string.connecting_msg))
            loginViewModel.loginWithGoogle(requestLogin)
            saveGoogleLoginModel = requestLogin
            SocialManager.logoutGoogle(this, null)
        }
    }

    private fun setBaseLoginRequest(loginRequest: BaseLoginRequestModel): BaseLoginRequestModel {
        AppsterApplication.mAppPreferences.devicesUDID = DeviceInfo.getDeviceDetail(this)//android_id
        loginRequest.device_token = AppsterApplication.mAppPreferences.devicesToken//GCM token
        loginRequest.device_udid = AppsterApplication.mAppPreferences.devicesUDID

        //        if (mRxPermissions.isGranted(android.Manifest.permission.ACCESS_COARSE_LOCATION) && mRxPermissions.isGranted(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
        val gpstClass = GPSTClass.getInstance()
        gpstClass.getLocation(this)

        var latitude = 0.0
        var longitude = 0.0

        // check if GPS enabled
        if (gpstClass.canGetLocation()) {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            latitude = gpstClass.latitude
            longitude = gpstClass.longitude

        }
        //        }

        loginRequest.longitude = longitude
        loginRequest.latitude = latitude
        return loginRequest
    }

    private fun onGoogleButtonClicked() {
        // Preventing multiple clicks, using threshold of 1 second
        if (preventMultiClicks()) {
            return
        }

        if (!CheckNetwork.isNetworkAvailable(this@LoginActivity)) {
            utility?.showMessage(
                getString(R.string.app_name),
                getString(R.string.no_internet_connection),
                this@LoginActivity
            )
            return
        }

        SocialManager.loginWithGoogle(this, LoginActivity.GG_REQUEST_SIGN_IN, this)
    }

    private fun loginWithFacebook() {
        val request = LoginFacebookRequestModel()
        request.device_udid = DeviceInfo.getDeviceDetail(this)
        SocialManager.getInstance().login(this, this, request)
    }

    private fun onFacebookButtonClicked() {
        // Preventing multiple clicks, using threshold of 1 second
        if (preventMultiClicks()) {
            return
        }

        if (!CheckNetwork.isNetworkAvailable(this@LoginActivity)) {
            utility?.showMessage(
                getString(R.string.app_name),
                getString(R.string.no_internet_connection),
                this@LoginActivity
            )
            return
        }

        loginWithFacebook()
    }

    private fun goToCreateProfile(requestModel: BaseLoginRequestModel) {
        val options = ActivityOptionsCompat.makeCustomAnimation(
            this@LoginActivity,
            R.anim.push_in_to_right,
            R.anim.push_in_to_left
        )
        val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
        intent.putExtra(ConstantBundleKey.BUNDLE_TYPE_KEY, Constants.LoginType.SOCIAL_TYPE)
        intent.putExtra(ConstantBundleKey.BUNDLE_LOGIN_DISPLAY_NAME, requestModel.display_name)
        var id = ""
        var loginFrom = ""
        when (requestModel) {
            is LoginFacebookRequestModel -> {
                id = requestModel.fbId!!
                loginFrom = ConstantBundleKey.LOGIN_FROM.ARG_LOGIN_FACEBOOK
            }
            is TwitterLoginRequestModel -> {

                id = requestModel.twitterId!!
                loginFrom = ConstantBundleKey.LOGIN_FROM.ARG_LOGIN_TWITTER
            }
            is GoogleLoginRequestModel -> {
                id = requestModel.googleId!!
                loginFrom = ConstantBundleKey.LOGIN_FROM.ARG_LOGIN_GOOGLE

            }
            is InstagramLoginRequestModel -> {
                id = requestModel.instagramId
                loginFrom = ConstantBundleKey.LOGIN_FROM.ARG_LOGIN_INSTAGRAM
            }
        }
        var expectedUserId =
            if (requestModel.display_name != null && TextUtils.isDigitsOnly(requestModel.display_name)) requestModel.email else requestModel.display_name
        if (TextUtils.isEmpty(expectedUserId)) expectedUserId = requestModel.userName
        intent.putExtra(ConstantBundleKey.BUNDLE_LOGIN_ID, id)
        intent.putExtra(ConstantBundleKey.BUNDLE_LOGIN_FROM, loginFrom)
        intent.putExtra(ConstantBundleKey.BUNDLE_LOGIN_PROFILE_PIC, requestModel.profile_Pic)
        intent.putExtra(ConstantBundleKey.BUNDLE_LOGIN_EMAIL, requestModel.email)
        intent.putExtra(ConstantBundleKey.BUNDLE_LOGIN_GENDER, requestModel.gender)
        intent.putExtra(ConstantBundleKey.BUNDLE_LOGIN_DISPLAY_NAME, requestModel.display_name)
        intent.putExtra(RegisterActivity.EXPECTED_USER_ID, expectedUserId)

        ActivityCompat.startActivity(this@LoginActivity, intent, options.toBundle())
    }

    private fun releaseMedia() {
        try {
            videoView.stop()
            videoView.release()
            //                video_view = null;
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    private fun pauseMedia() {
        videoView.pause()
    }

    private fun startMedia() {
        videoView.start()
    }

    private fun playVideo() {

        //        releaseMedia();
        //        if (animation == null) {
        //            animation = new TranslateAnimation(0.0f, 200.0f,
        //                    0.0f, 0.0f);          //  new TranslateAnimation(xFrom,xTo, yFrom,yTo)
        //            animation.setDuration(6000);  // animation duration
        //            animation.setRepeatCount(Animation.INFINITE);  // animation repeat count
        //            animation.setRepeatMode(2);   // repeat animation (left to right, right to left )
        //            //animation.setFillAfter(true);
        //        }
        //
        //        video_view.startAnimation(animation);  // start animation

        try {

            videoView.setRawData(R.raw.gcox_vertical)
            videoView.setVolume(0f, 0f)
            videoView.isLooping = true
            videoView.prepare { it.start() }
        } catch (ioe: Exception) {
            Timber.e(ioe)
        }

    }

    private fun calcKeyboardHeight() {
        val root = findViewById<View>(R.id.root_view)
        root.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val r = Rect()
                root.getWindowVisibleDisplayFrame(r)
                val screenHeight = root.rootView.height
                val keyboardHeight = screenHeight - r.bottom

                // IF height diff is more then 150, consider keyboard as visible.
                if (keyboardHeight > 150) {
                    AppsterApplication.mAppPreferences.setIntPreferenceData(Constants.KEYBOARD_HEIGHT, keyboardHeight)
                    root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
                videoView.setScalableType(ScalableType.CENTER_CROP)
                videoView.invalidate()
            }
        })
    }
}