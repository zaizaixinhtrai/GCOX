package com.gcox.fansmeet.features.editprofile

import android.arch.lifecycle.Observer
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.appster.extensions.loadImg
import com.gcox.fansmeet.AppsterApplication
import com.gcox.fansmeet.AppsterApplication.Companion.mAppPreferences
import com.gcox.fansmeet.R
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.core.activity.BaseToolBarActivity
import com.gcox.fansmeet.core.adapter.DisplayableItem
import com.gcox.fansmeet.features.loyalty.viewholders.LoyaltyViewHolder
import com.gcox.fansmeet.util.EmailUtil
import com.gcox.fansmeet.util.FileUtility
import com.gcox.fansmeet.util.StringUtil
import com.gcox.fansmeet.util.Utils
import com.gcox.fansmeet.webservice.request.EditProfileRequestModel
import com.gcox.fansmeet.webservice.response.SettingResponse
import com.jakewharton.rxbinding2.widget.RxTextView
import com.yalantis.ucrop.UCrop
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.android.viewmodel.ext.android.viewModel
import kotlinx.android.synthetic.main.activity_edit_profile.*
import timber.log.Timber
import java.util.concurrent.TimeUnit

class EditProfileActivity : BaseToolBarActivity(), LoyaltyViewHolder.OnClickListener {

    private val editProfileViewModel: EditProfileViewModel by viewModel()
    private var bitmapSend: Bitmap? = null
    private var isEmailCorrect = true
    private var displayNameBefore = ""
    private var bioBefore: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        useAppToolbarBackButton()
        eventClickBack.setOnClickListener { onBackPressed() }
        setToolbarColor(ContextCompat.getColor(this, R.color.color_00203B))
        observeData()
    }

    override fun getLayoutContentId(): Int {
        return R.layout.activity_edit_profile
    }

    override fun init() {
        handleTurnoffMenuSliding()
        setTopBarTile(getString(R.string.edit_profile))
        setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        visibleRightButton()
        rightButton.setTextColor(Color.parseColor("#278BED"))
        rightButton.setBackgroundColor(Color.TRANSPARENT)
        rightButton.text = getString(R.string.save)
        fmUserImage.setOnClickListener { showPicPopUp() }
        rightButton.setOnClickListener { updateProfile() }
        setEdtUserIdWatcher()
        ivUserImage.loadImg(AppsterApplication.mAppPreferences.userModel.userImage)
        edtDisplayName.setText(StringUtil.decodeString(AppsterApplication.mAppPreferences.userModel.displayName!!))
        edtEmail.setText(AppsterApplication.mAppPreferences.userModel.email!!)
        edtBio.setText(StringUtil.decodeString(AppsterApplication.mAppPreferences.userModel?.about))
        if (AppsterApplication.mAppPreferences.userModel.email.isNullOrEmpty()) isEmailCorrect = false
        displayNameBefore = AppsterApplication.mAppPreferences.userModel.displayName!!
        bioBefore = AppsterApplication.mAppPreferences.userModel?.about
        tvUserName.text = AppsterApplication.mAppPreferences.userModel?.userName
    }

    private fun setEdtUserIdWatcher() {
        compositeDisposable.add(
            RxTextView.textChangeEvents(edtEmail)
                .observeOn(Schedulers.computation())
                .debounce(500, TimeUnit.MILLISECONDS) // default Scheduler is Computation
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    enableEmailInvalid(it.text().toString().trim())
                }, { error -> Timber.e(error) })
        )

        compositeDisposable.add(
            RxTextView.textChangeEvents(edtDisplayName)
                .observeOn(Schedulers.computation())
                .debounce(500, TimeUnit.MILLISECONDS) // default Scheduler is Computation
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    onDisplayNameCounterChanged(it.text().toString().trim())
                }, { error -> Timber.e(error) })
        )

        compositeDisposable.add(
            RxTextView.textChangeEvents(edtBio)
                .observeOn(Schedulers.computation())
                .debounce(500, TimeUnit.MILLISECONDS) // default Scheduler is Computation
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    onBioCounterChanged(it.text().toString().trim())
                }, { error -> Timber.e(error) })
        )
    }

    private fun onDisplayNameCounterChanged(textInput: String) {
        val tex = StringUtil.encodeString(textInput)
        if (tex.length > Constants.LIMIT_DISPLAY_NAME) {
            val des = StringUtil.decodeString(displayNameBefore)
            edtDisplayName.setText(des)
            val selection = if (TextUtils.isEmpty(des)) 0 else des.length - 1
            edtDisplayName.setSelection(selection)
        } else {
            displayNameBefore = tex
        }
        val newLength = displayNameBefore.length
        val counter = newLength.toString() + "/" + Constants.LIMIT_DISPLAY_NAME
        tvLimitDisplayName.text = counter
    }

    private fun onBioCounterChanged(textInput: String) {
        val tex = StringUtil.encodeString(textInput)
        if (tex.length > Constants.LIMIT_BIO) {
            val des = StringUtil.decodeString(bioBefore)
            edtBio.setText(des)
            val selection = if (TextUtils.isEmpty(des)) 0 else des.length - 1
            edtBio.setSelection(selection)
        } else {
            bioBefore = tex
        }
        val newLength = bioBefore?.length
        val counter = newLength.toString() + "/" + Constants.LIMIT_BIO
        tvLimitBio.text = counter
    }

    private fun enableEmailInvalid(email: String) {

        if (TextUtils.isEmpty(email)) {
            tvInvalidEmail.visibility = View.INVISIBLE
            isEmailCorrect = true
            edtEmail.replaceCustomDrawableEnd(0)
        } else {
            if (EmailUtil.isEmail(email)) {
                if (AppsterApplication.mAppPreferences.userModel.email != null &&
                    email.contentEquals(AppsterApplication.mAppPreferences.userModel.email!!)
                ) {
                    isEmailCorrect = true
                    tvInvalidEmail.visibility = View.INVISIBLE
                    edtEmail.replaceCustomDrawableEnd(0)
                } else {
                    editProfileViewModel.checkEmailAvailable(email)
                }
            } else {
                isEmailCorrect = false
                tvInvalidEmail.visibility = View.VISIBLE
                edtEmail.replaceCustomDrawableEnd(R.drawable.ic_id_name_invalid)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (bitmapSend != null && !bitmapSend!!.isRecycled) bitmapSend!!.recycle()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == BaseToolBarActivity.RESULT_OK) {
            when (requestCode) {
                Constants.REQUEST_PIC_FROM_LIBRARY -> {
                    val imageCroppedURI: Uri
                    try {
                        imageCroppedURI = getOutputMediaFileUri(FileUtility.MEDIA_TYPE_IMAGE_CROPPED)
                    } catch (e: NullPointerException) {
                        Timber.d(e)
                        return
                    }

                    if (data?.data != null) {
                        fileUri = data.data
                        performCrop(fileUri, imageCroppedURI)
                    }
                }

                Constants.REQUEST_PIC_FROM_CROP -> {
                    if (data != null) {
                        val resultUri = UCrop.getOutput(data)
                        if (resultUri != null) {
                            fileUri = resultUri
                            onImageChanged(fileUri)
                        } else {
                            Toast.makeText(this, R.string.toast_cannot_retrieve_cropped_image, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }

                Constants.REQUEST_PIC_FROM_CAMERA -> {
                    if (data?.data != null) {
                        fileUri = data.data
                        onImageChanged(fileUri)
                    }
                }
            }
        }
    }

    private fun onImageChanged(fileUri: Uri) {
        bitmapSend = Utils.getBitmapFromURi(this, fileUri)
        if (bitmapSend != null) {
            ivUserImage.setImageBitmap(
                Bitmap.createScaledBitmap(
                    bitmapSend,
                    Constants.BITMAP_THUMBNAIL_WIDTH, Constants.BITMAP_THUMBNAIL_HEIGHT, false
                )
            )
        }
    }

    private fun observeData() {
        editProfileViewModel.getError().observe(this, Observer {
            dismissDialog()
        })

        editProfileViewModel.response.observe(this, Observer {
            dismissDialog()
            if (it != null)
                saveData(it)
            finish()
        })

        editProfileViewModel.checkEmail.observe(this, Observer {
            if (it != null) {
                if (it) {
                    isEmailCorrect = false
                    tvInvalidEmail.visibility = View.VISIBLE
                    edtEmail.replaceCustomDrawableEnd(R.drawable.ic_id_name_invalid)
                } else {
                    isEmailCorrect = true
                    tvInvalidEmail.visibility = View.INVISIBLE
                    edtEmail.replaceCustomDrawableEnd(0)
                }
            }
        })
    }

    private fun saveData(userInfo: SettingResponse) {
        val userInfoModel = AppsterApplication.mAppPreferences.userModel
        userInfoModel.displayName = userInfo.displayName
        userInfoModel.email = edtEmail.text.toString().trim()
        userInfoModel.userImage = userInfo.userImage
        userInfoModel.about = userInfo.about
        AppsterApplication.mAppPreferences.saveUserInforModel(userInfoModel)

        Toast.makeText(this, getString(R.string.saved), Toast.LENGTH_LONG).show()
    }

    private fun updateProfile() {
        val displayName = edtDisplayName.text.toString().trim()
        if (displayName.isEmpty()) {
            Toast.makeText(applicationContext, getString(R.string.display_name_required), Toast.LENGTH_SHORT).show()
            return
        }

        if (!isEmailCorrect) {
            Toast.makeText(applicationContext, getString(R.string.input_email_error), Toast.LENGTH_SHORT).show()
            return
        }

        var bio = edtBio.text.toString().trim()
        while (bio.contains("\n" + "\n")) {
            bio = bio.replace("\n" + "\n", "\n")
        }
        val request = EditProfileRequestModel(
            AppsterApplication.mAppPreferences.userModel.gender,
            StringUtil.encodeString(displayName),
            AppsterApplication.mAppPreferences.userModel.doB,
            edtEmail.text.toString().trim(),
            AppsterApplication.mAppPreferences.userModel.nationality,
            "",
            "",
            Utils.getFileFromBitMap(this, bitmapSend),
            "",
            StringUtil.encodeString(bio),
            AppsterApplication.mAppPreferences.devicesUDID
        )
        showDialog(this, getString(R.string.connecting_msg))
        editProfileViewModel.updateProfile(request.build())
    }
}