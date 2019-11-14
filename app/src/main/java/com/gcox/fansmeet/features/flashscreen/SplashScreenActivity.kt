package com.gcox.fansmeet.features.flashscreen

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.view.View
import com.gcox.fansmeet.AppsterApplication
import com.gcox.fansmeet.BuildConfig
import com.gcox.fansmeet.R
import com.gcox.fansmeet.common.ConstantBundleKey
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.core.activity.BaseActivity
import com.gcox.fansmeet.features.login.LoginActivity
import com.gcox.fansmeet.features.login.LoginViewModel
import com.gcox.fansmeet.features.main.MainActivity
import com.gcox.fansmeet.features.register.RegisterActivity
import com.gcox.fansmeet.manager.ShowErrorManager
import com.gcox.fansmeet.util.AppsterUtility
import com.gcox.fansmeet.util.CheckNetwork
import com.gcox.fansmeet.util.DeviceInfo
import com.gcox.fansmeet.webservice.AppsterWebServices
import com.gcox.fansmeet.webservice.request.*
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

import java.util.concurrent.TimeUnit


class SplashScreenActivity : BaseActivity() {

    private val loginViewModel: SplashScreenViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Possible work around for market launches. See http://code.google.com/p/android/issues/detail?id=2373
        // for more details. Essentially, the market launches the main activity on top of other activities.
        // we never want this to happen. Instead, we check if we are the root and if not, we finish.
        if (!isTaskRoot) {
            val intent = getIntent()
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN == intent.action) {
                Timber.e("Activity is not the root.  Finishing Activity instead of launching.")
                finish()
                return
            }
        }
        setContentView(R.layout.activity_flash_screen)
        observeData()
        checkVersion()
//        if (AppsterApplication.mAppPreferences.isUserLogin) {
//            reLoginApp()
//        } else {
//            navigateLoginActivity()
//        }

    }

    private fun reLoginApp() {
        val userModel = AppsterApplication.mAppPreferences.userModel
        showDialog(this, getString(R.string.connecting_msg))
        if (userModel.isLoginWithFacebook()) {
            val requestLogin = LoginFacebookRequestModel()
            requestLogin.fbId = userModel.fbId
            requestLogin.device_udid = DeviceInfo.getDeviceDetail(this)
            requestLogin.display_name = userModel.displayName
            requestLogin.email = userModel.email
            requestLogin.latitude = userModel.latitude
            requestLogin.longitude = userModel.longitude
            requestLogin.userName = userModel.userName
            loginViewModel.loginWithFacebook(requestLogin)
        } else if (userModel.isLoginWithGmail()) {
            val requestLogin = GoogleLoginRequestModel()
            requestLogin.googleId = userModel.googleId
            requestLogin.device_udid = DeviceInfo.getDeviceDetail(this)
            requestLogin.display_name = userModel.displayName
            requestLogin.email = userModel.email
            requestLogin.latitude = userModel.latitude
            requestLogin.longitude = userModel.longitude
            requestLogin.userName = userModel.userName
            loginViewModel.loginWithGoogle(requestLogin)
        } else if (userModel.isLoginWithInstagram()) {
            val requestLogin = InstagramLoginRequestModel()
            requestLogin.instagramId = userModel.instagramId
            requestLogin.device_udid = DeviceInfo.getDeviceDetail(this)
            requestLogin.display_name = userModel.displayName
            requestLogin.email = userModel.email
            requestLogin.latitude = userModel.latitude
            requestLogin.longitude = userModel.longitude
            requestLogin.userName = userModel.userName
            loginViewModel.loginWithInstagram(requestLogin)
        }

    }

    private fun observeData() {
        loginViewModel.getError().observe(this, Observer {
            Timber.e(it?.message)
            if (it?.code != null) handleError(it.message, it.code)
            dismissDialog()
        })

        loginViewModel.getLoginData.observe(this, Observer {
            dismissDialog()
            navigateMainActivity()
        })
    }

    private fun checkFirstRunForShowAnimation() {

        //        ImageView ivSplashImage = (ImageView) findViewById(R.id.ivSplashImage);
        //        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //        params.setMargins(0, 0, 0, 0);
        //        ivSplashImage.setLayoutParams(params);
        //        ivSplashImage.setImageDrawable(null);
        //        ivSplashImage.setBackground(ContextCompat.getDrawable(this, R.drawable.topbar_belive_logo));
    }


    override fun onResume() {
        super.onResume()
        Timber.e("onResume!!")
    }

    @SuppressLint("CheckResult")
    private fun navigateMainActivity() {
        compositeDisposable.add(Completable.timer(
            TIME_SHOW_ANIMATION_START_APP.toLong(),
            TimeUnit.SECONDS,
            AndroidSchedulers.mainThread()
        )
            .subscribe { this.navigateApplication() })
    }

    private fun checkVersion() {
        if (!CheckNetwork.isNetworkAvailable(applicationContext)) {
            utility.showMessage(getString(R.string.app_name), getString(R.string.no_internet_connection), this)
            return
        }
        val version = BuildConfig.VERSION_NAME
        showDialog(this, getString(R.string.connecting_msg))
        if (!isDestroyed && !isFinishing) {
            compositeDisposable.add(
                AppsterWebServices.get().checkVersion(Constants.ANDROID_DEVICE_TYPE, version)
                    .subscribe({ versionDataResponse ->
                        dismissDialog()
                        if (versionDataResponse.data != null) {
                            if (versionDataResponse.data.forceUpdate) {
                                showForceUpdateDialog(versionDataResponse.data.message)
                            } else {
                                if (AppsterApplication.mAppPreferences.isUserLogin) {
                                    reLoginApp()
                                } else {
                                    navigateLoginActivity()
                                }
                            }
                        }
                        if (versionDataResponse.code != Constants.RESPONSE_FROM_WEB_SERVICE_OK) {
                            handleError(versionDataResponse.message, versionDataResponse.code)
                        }
                    }, { error ->
                        dismissDialog()
                        handleError(error.message, Constants.RETROFIT_ERROR)
                    })
            )
        }

    }

    private fun showForceUpdateDialog(message: String) {
        val clickHandle = View.OnClickListener { v ->
            AppsterUtility.goToPlayStore(this@SplashScreenActivity, REQUEST_GOOGLE_STORE)
        }
        utility.showForceUpdateMessage(getString(R.string.app_name), message, this, clickHandle)
        compositeDisposable.add(Observable.just(1).delay(2900, TimeUnit.MILLISECONDS).subscribe({ integer ->
            AppsterUtility.goToPlayStore(this@SplashScreenActivity, REQUEST_GOOGLE_STORE)
        }, { error -> handleError(error.message, ShowErrorManager.un_know_error) }))
    }

    private fun navigateApplication() {

        val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
        val options = ActivityOptionsCompat.makeCustomAnimation(
            this@SplashScreenActivity,
            R.anim.push_in_to_right, R.anim.push_in_to_left
        )
        this@SplashScreenActivity.startActivity(intent, options.toBundle())
        finish()
    }

    private fun navigateLoginActivity() {
        compositeDisposable.add(Completable.timer(
            TIME_SHOW_ANIMATION_START_APP.toLong(),
            TimeUnit.SECONDS,
            AndroidSchedulers.mainThread()
        ).subscribe {
            val intent = Intent(this, LoginActivity::class.java)
            val options = ActivityOptionsCompat.makeCustomAnimation(
                this@SplashScreenActivity,
                R.anim.push_in_to_right, R.anim.push_in_to_left
            )
            this@SplashScreenActivity.startActivity(intent, options.toBundle())
            finish()
        })
    }

    public override fun onDestroy() {
        super.onDestroy()
        loginViewModel.disposableComposite()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_GOOGLE_STORE) {
            checkVersion()
        }
    }

    companion object {
        private const val TIME_SHOW_ANIMATION_START_APP = 3
        private const val REQUEST_GOOGLE_STORE = 999
    }

}