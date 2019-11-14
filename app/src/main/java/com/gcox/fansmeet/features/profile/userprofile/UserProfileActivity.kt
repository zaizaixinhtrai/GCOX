package com.gcox.fansmeet.features.profile.userprofile

import android.content.Context
import android.content.Intent
import android.content.Intent.getIntent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.gcox.fansmeet.R
import com.gcox.fansmeet.common.ConstantBundleKey
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.core.activity.BaseToolBarActivity
import com.gcox.fansmeet.features.post.ActivityPostMedia
import com.gcox.fansmeet.features.post.BundleMedia
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.CelebrityModel
import com.gcox.fansmeet.util.FileUtility
import com.yalantis.ucrop.UCrop
import timber.log.Timber
import java.io.File

/**
 * Created by ngoc on 10/13/15.
 */
class UserProfileActivity : BaseToolBarActivity() {

    private var userID: Int = 0
    private var mUserName: String? = ""
    private var fragmentMe: FragmentMe? = null
    private var itemJoin: CelebrityModel? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBundle()
        handleTurnoffMenuSliding()
        useAppToolbarBackButton()
        eventClickBack.setOnClickListener { v -> onBackPressed() }
        setToolbarColor(ContextCompat.getColor(this, R.color.color_00203B))
        if (savedInstanceState == null) {
            fragmentMe = FragmentMe.getInstance(userID, mUserName!!, true)
            showFullFragmentScreen(fragmentMe)
        }
    }

    private fun initBundle() {
        val bundle = intent?.extras
        if (bundle != null) {
            userID = bundle.getInt(USER_PROFILE_ID, 0)
            mUserName = bundle.getString(ARG_USER_NAME)
        }
    }

    override fun getLayoutContentId(): Int {
        return 0
    }

    override fun init() {

    }

    override fun onResume() {
        super.onResume()

    }

    override fun onBackPressed() {
        if (fragmentMe != null && fragmentMe!!.isFollowStatusChanced) {
            val intent = Intent()
            intent.putExtra(
                ConstantBundleKey.BUNDLE_DATA_FOLLOW_USER_FROM_PROFILE_ACTIVITY,
                !fragmentMe!!.followStatusBefore!!
            )
            intent.putExtra(
                ConstantBundleKey.BUNDLE_USER_ID,
                fragmentMe!!.userID
            )
            setResult(RESULT_OK, intent)
        }
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
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
                            if (itemJoin != null) {
                                if (itemJoin!!.mediaType == Constants.POST_TYPE_CHALLENGE_SELFIE) {
                                    openImageEditorScreen(this, resultUri, itemJoin!!.selfieImage!!)
                                } else {
                                    startPostWithMedia(resultUri, Constants.POST_TYPE_IMAGE)
                                }
                            }

                        } else {
                            Toast.makeText(this, R.string.toast_cannot_retrieve_cropped_image, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }

                Constants.REQUEST_PIC_FROM_CAMERA -> {
                    if (data?.data != null) {
                        fileUri = data.data
                        if (itemJoin != null) {
                            if (itemJoin!!.mediaType == Constants.POST_TYPE_CHALLENGE_SELFIE) {
                                openImageEditorScreen(this, fileUri, itemJoin!!.selfieImage!!)
                            } else {
                                startPostWithMedia(fileUri, Constants.POST_TYPE_IMAGE)
                            }
                        }
                    }
                }

                Constants.CAMERA_VIDEO_REQUEST -> {
                    if (data?.data != null) {
                        fileUri = data.data
                        Timber.e("mRecordUrl $fileUri")
                        startPostWithMedia(fileUri, Constants.POST_TYPE_VIDEO)
                    }
                }

                Constants.PICK_VIDEO_FROM_LIBRARY_REQUEST -> {
                    if (data?.data != null) {
                        fileUri = data.data
                        loadVideoAfterPickFromGallery(fileUri)
                    }
                }

                Constants.VIDEO_TRIMMED_REQUEST -> {
                    if (data != null) {
                        val urlImage = data.getStringExtra(Constants.VIDEO_PATH)
                        val image = Uri.fromFile(File(urlImage))
                        startPostWithMedia(image, Constants.POST_TYPE_VIDEO)
                    }
                }

                Constants.REQUEST_PHOTO_EDITOR_ACTIVITY -> {
                    if (data?.data != null) {
                        fileUri = data.data
                        startPostWithMedia(fileUri, Constants.POST_TYPE_IMAGE)
                    }
                }
            }
        }
    }

    private fun startPostWithMedia(fileUri: Uri, type: Int) {
        val intent = Intent(this, ActivityPostMedia::class.java)
        val bundleMedia = BundleMedia()
        bundleMedia.setIsPost(true)
        bundleMedia.key = ConstantBundleKey.BUNDLE_MEDIA_KEY
        bundleMedia.type = type
        bundleMedia.uriPath = fileUri.toString()
        bundleMedia.setIntent(intent)
        if (itemJoin != null) {
            bundleMedia.challengeId = itemJoin!!.id!!
            bundleMedia.isSubmissionChallenge = true
        }

        val options =
            ActivityOptionsCompat.makeCustomAnimation(this, R.anim.push_in_to_right, R.anim.push_in_to_left)
        ActivityCompat.startActivityForResult(this, intent, Constants.POST_REQUEST, options.toBundle())
    }

    fun joinChallengeClicked(item: CelebrityModel) {
        itemJoin = item
        when (item.mediaType) {
            Constants.POST_TYPE_IMAGE, Constants.POST_TYPE_CHALLENGE_SELFIE -> showPicPopUp()
            Constants.POST_TYPE_VIDEO -> showVideosPopUp()
            Constants.POST_TYPE_QUOTES -> startPostWithMedia(Uri.parse(""), Constants.POST_TYPE_QUOTES)
        }
    }

    fun showNoUserDataDialog(message:String){
        utility.showMessage(getString(R.string.app_name),
            message,
            this
        ) {
            finish()
        }
    }

    companion object {
        const val ARG_USER_BLOCKED = "ARG_USER_BLOCKED"
        const val USER_PROFILE_ID = "user_id"
        const val ARG_USER_NAME = "user_name"

        fun newIntent(context: Context, userId: Int, userName: String): Intent {
            val intent = Intent(context, UserProfileActivity::class.java)
            intent.putExtra(USER_PROFILE_ID, userId)
            intent.putExtra(ARG_USER_NAME, userName)
            return intent
        }
    }
}
