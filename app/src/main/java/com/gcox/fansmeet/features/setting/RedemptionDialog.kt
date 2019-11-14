package com.gcox.fansmeet.features.setting

import android.app.Dialog
import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import com.gcox.fansmeet.AppsterApplication
import com.gcox.fansmeet.R
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.exception.GcoxException
import com.gcox.fansmeet.util.DialogManager
import com.gcox.fansmeet.util.Utils
import com.gcox.fansmeet.webservice.AppsterWebServices
import com.gcox.fansmeet.webservice.request.VerifyIAPRequestModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.dialog_redemption.*
import timber.log.Timber
import android.support.v4.app.FragmentActivity
import android.view.inputmethod.InputMethodManager
import com.gcox.fansmeet.core.dialog.DialogbeLiveConfirmation



/**
 * Created by Ngoc on 9/7/2017.
 */

class RedemptionDialog : DialogFragment() {

    private var compositeDisposable: CompositeDisposable? = null

    override fun onStart() {
        super.onStart()
        if (dialog != null && dialog.window != null) {
            dialog.window!!.setBackgroundDrawableResource(R.color.transparent)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        if (dialog.window != null) {
            dialog.window!!.attributes.windowAnimations = R.style.DialogZoomAnimation
        }

        return dialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return inflater.inflate(R.layout.dialog_redemption, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = true
        cancel.setOnClickListener {
            dismissAllowingStateLoss()
            Utils.hideSoftKeyboard(activity)
        }
        ok.setOnClickListener {
            val redemptionString = edtRedemptionCode.text.toString().trim()
            if (redemptionString.isNullOrEmpty()) {
                Toast.makeText(
                    context?.applicationContext,
                    context?.getString(R.string.please_input_redemption_code),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                addRedemptionCode(redemptionString)
            }
        }
    }

    override fun dismiss() {
        super.dismiss()
        if (compositeDisposable != null && compositeDisposable!!.isDisposed) compositeDisposable!!.dispose()
    }

    private fun addRedemptionCode(requestModel: String) {
        if (!DialogManager.isShowing()) {
            DialogManager.getInstance().showDialog(context, this.resources.getString(R.string.connecting_msg))
        }

        if (compositeDisposable == null) compositeDisposable = CompositeDisposable()
        compositeDisposable!!.add(
            AppsterWebServices.get().addRedemptionCode(
                AppsterApplication.mAppPreferences.userTokenRequest,
                requestModel
            )
                .subscribe({ responeModel ->
                    DialogManager.getInstance().dismisDialog()
                    if (responeModel.code == Constants.RESPONSE_FROM_WEB_SERVICE_OK && responeModel.data.gems != null) {
                        Timber.e("Bean =" + responeModel.data.gems)
                        AppsterApplication.mAppPreferences.userModel.gems = responeModel.data.gems!!
                        edtRedemptionCode.setText("")
                        Toast.makeText(
                            context?.applicationContext,
                            context?.getString(R.string.submit_redemption_code_success),
                            Toast.LENGTH_LONG
                        ).show()
                        dismissAllowingStateLoss()
                        Utils.hideSoftKeyboard(activity)
                        val builder = DialogbeLiveConfirmation.Builder()
                        builder.title(getString(R.string.app_name))
                            .message(String.format(
                                getString(R.string.redemption_receipt_gem),
                                responeModel.data.addedGems
                            ))
                            .confirmText(getString(R.string.btn_text_ok))
                            .singleAction(true)
                            .build().show(activity)
                    } else if (responeModel.code == 1502) {
                        Toast.makeText(
                            context?.applicationContext,
                            context?.getString(R.string.redemption_code_not_found),
                            Toast.LENGTH_LONG
                        ).show()
                    } else if (responeModel.code == 1503) {
                        Toast.makeText(
                            context?.applicationContext,
                            context?.getString(R.string.redemption_code_already_applied),
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }, { error ->
                    DialogManager.getInstance().dismisDialog()
                    Timber.e(error.message)
                })
        )

    }

    companion object {

        private const val ENTRY_ID = "entry_it"

        @JvmStatic
        fun newInstance(): RedemptionDialog {
//            val dialog = RedemptionDialog()
//            val bundle = Bundle()
//            bundle.putInt(ENTRY_ID, entryId)
//            dialog.arguments = bundle
            return RedemptionDialog()
        }
    }
}
