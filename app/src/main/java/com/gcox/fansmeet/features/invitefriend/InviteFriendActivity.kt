package com.gcox.fansmeet.features.invitefriend

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import com.gcox.fansmeet.AppsterApplication
import com.gcox.fansmeet.BuildConfig
import com.gcox.fansmeet.R
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.core.activity.BaseToolBarActivity
import com.gcox.fansmeet.util.CheckNetwork
import com.gcox.fansmeet.util.CopyTextUtils
import com.gcox.fansmeet.webservice.AppsterWebServices
import kotlinx.android.synthetic.main.activity_invite_friend.*

class InviteFriendActivity : BaseToolBarActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutContentId(): Int = R.layout.activity_invite_friend

    override fun init() {
        useAppToolbarBackButton()
        eventClickBack.setOnClickListener { onBackPressed() }
        setToolbarColor(ContextCompat.getColor(this, R.color.color_00203B))
        handleTurnoffMenuSliding()
        setTopBarTile(getString(R.string.invite_a_friend_slider))
        setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        btShare.setOnClickListener { shareCode() }
        tvRefId.text = AppsterApplication.mAppPreferences.userModel.referralId.toString()
        tvRefId.setOnLongClickListener {
            CopyTextUtils.CopyClipboard(
                applicationContext,
                AppsterApplication.mAppPreferences.userModel.referralId.toString(),
                getString(R.string.share_refid)
            )
            false
        }

        checkGemsForReferrer()
    }

    private fun shareCode() {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                getString(
                    R.string.invite_sns_message,
                    getString(R.string.app_name),
                    AppsterApplication.mAppPreferences.userModel.referralId.toString()
                )
            )
            type = "text/plain"
        }
        startActivity(Intent.createChooser(sendIntent, resources.getText(R.string.share_via)))
    }

    private fun checkGemsForReferrer() {
        if (!CheckNetwork.isNetworkAvailable(applicationContext)) {
            utility.showMessage(getString(R.string.app_name), getString(R.string.no_internet_connection), this)
            return
        }
        val version = BuildConfig.VERSION_CODE.toString()
        showDialog(this, getString(R.string.connecting_msg))
        if (!isDestroyed && !isFinishing) {
            compositeDisposable.add(
                AppsterWebServices.get().checkVersion(Constants.ANDROID_DEVICE_TYPE, version)
                    .subscribe({ versionDataResponse ->
                        dismissDialog()
                        if (versionDataResponse.data != null) {
                            tvInviteFriendTitle.text = String.format(
                                getString(R.string.invite_friend_title),
                                versionDataResponse.data.gemsForReferrer
                            )
                        }
                    }, { error ->
                        dismissDialog()
                        handleError(error.message, Constants.RETROFIT_ERROR)
                    })
            )
        }

    }
}