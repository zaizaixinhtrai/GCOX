package com.gcox.fansmeet.core.activity

import android.support.v7.app.AppCompatActivity
import android.view.View
import com.gcox.fansmeet.AppsterApplication
import com.gcox.fansmeet.R
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.core.dialog.DialogInfoUtility
import com.gcox.fansmeet.exception.GcoxException
import com.gcox.fansmeet.manager.ShowErrorManager

abstract class HandleErrorActivity : AppCompatActivity() {

    fun handleError(errorMessage: String?, errorCode: Int) {
        if (this.isFinishing) {
            return
        }
        val utility = DialogInfoUtility()
        if (errorCode == Constants.RETROFIT_ERROR) {

            if (errorMessage != null && !errorMessage.isEmpty()) {
                utility.showMessage(
                    getString(R.string.app_name),
                    getString(R.string.check_your_connection),
                    this@HandleErrorActivity
                )
            } else {
                utility.showMessage(
                    getString(R.string.app_name),
                    getString(R.string.activity_sign_unknown_error),
                    this@HandleErrorActivity
                )
            }
        } else if (errorCode == Constants.RESPONSE_DUPLICATE_LOGIN) {
            AppsterApplication.application.multipleLiveData.postValue(
                GcoxException(
                    errorMessage!!,
                    errorCode
                )
            )
        } else if (errorCode == ShowErrorManager.account_deactivate_or_invalid_email ||
            errorCode == ShowErrorManager.account_deactivated_or_suspended ||
            errorCode == ShowErrorManager.authentication_error
        ) {
            val mclick = View.OnClickListener { v -> AppsterApplication.logout(v.context) }

            utility.showMessage(
                getString(R.string.app_name),
                errorMessage,
                this@HandleErrorActivity,
                mclick
            )

        } else {
            utility.showMessage(getString(R.string.app_name), errorMessage, this@HandleErrorActivity)
        }
    }
}