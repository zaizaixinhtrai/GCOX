package com.gcox.fansmeet.features.post;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.gcox.fansmeet.AppsterApplication;
import com.gcox.fansmeet.R;
import com.gcox.fansmeet.common.ConstantBundleKey;
import com.gcox.fansmeet.common.Constants;
import com.gcox.fansmeet.core.activity.BaseActivity;
import com.gcox.fansmeet.core.dialog.DialogInfoUtility;
import com.gcox.fansmeet.customview.CustomFontTextView;
import com.gcox.fansmeet.customview.TaggableEditText;
import com.gcox.fansmeet.features.challengeentries.EntriesModel;
import com.gcox.fansmeet.features.postchallenge.PostChallengeActivity;
import com.gcox.fansmeet.features.profile.ItemModelClassNewsFeed;
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.CelebrityModel;
import com.gcox.fansmeet.location.GPSTClass;
import com.gcox.fansmeet.location.GetAddress;
import com.gcox.fansmeet.manager.SocialManager;
import com.gcox.fansmeet.models.PostDataModel;
import com.gcox.fansmeet.util.*;
import com.gcox.fansmeet.webservice.AppsterWebServices;
import com.gcox.fansmeet.webservice.request.LoginFacebookRequestModel;
import com.gcox.fansmeet.webservice.request.PostCreatePostRequestModel;
import com.yalantis.ucrop.UCrop;
import timber.log.Timber;

import java.io.File;

import static com.gcox.fansmeet.util.FileUtility.MEDIA_TYPE_IMAGE_CROPPED;


/**
 * Created by User on 9/21/2015.
 */


public class ActivityPostMedia extends BaseActivity implements PostContract.View, View.OnClickListener,
        SocialManager.SocialSharingListener, SocialManager.SocialLoginListener {

    private final int CROP_PIC = 1;
    private final int UNKNOWN_LOCATION = -1;
    @Bind(R.id.pageTitle)
    TextView pageTitle;
    @Bind(R.id.image_videos)
    ImageView imageVideos;
    @Bind(R.id.post_play_video)
    ImageView postPlayVideo;
    @Bind(R.id.fm_media)
    FrameLayout fmMedia;
    @Bind(R.id.postDescription)
    TaggableEditText edtPostDescription;
    @Bind(R.id.txt_numberText)
    TextView txtNumberText;
    @Bind(R.id.checkInText)
    CustomFontTextView txtCheckIn;
    @Bind(R.id.cancel_text_iv)
    ImageView cancelTextIv;
//
//    @Bind(R.id.ll_share_facebook)
//    LinearLayout llShareFacebook;
//    @Bind(R.id.ll_share_twister)
//    LinearLayout llShareTwitter;
//    @Bind(R.id.ll_share_instagram)
//    LinearLayout llShareInstagram;
//    @Bind(R.id.ll_share_whatsApp)
//    LinearLayout llShareWhatsApp;
//    @Bind(R.id.ll_share_email)
//    LinearLayout llShareEmail;
//    @Bind(R.id.ll_share_others)
//    LinearLayout llShareOthers;
//    @Bind(R.id.v_share_facebook_divider)
//    View vShareFacebookDivider;
//    @Bind(R.id.v_share_twitter_divider)
//    View vShareTwitterDivider;
//    @Bind(R.id.v_share_instagram_divider)
//    View vShareInstagramDivider;
//    @Bind(R.id.v_share_whatsapp_divider)
//    View vShareWhatsappDivider;
    @Bind(R.id.btn_post)
    Button btnPost;
    @Bind(R.id.ll_root)
    LinearLayout llRoot;

    public static final String CHALLENGE_ID = "challenge_id";
    public static final String TYPE_ID = "type_id";
    public static final String TYPE_EDIT = "type_edit";

    private String address = "";
    private String pathVideoCamera = "";
    private Bitmap imageThumbnailVideo;

    private String postDescription = "";
    private Uri uriImageSend;
    private Bitmap bitmapSend;

    private int mediaType = Constants.POST_TYPE_IMAGE;
    private boolean isEditMode = false;
    private Uri uriData;
    private BundleMedia bundleMedia;
    private String pathMedia;
    boolean isCompletedSharing;
    String descriptionBefore = "";

    PostPresenter mPresenter;
    DialogInfoUtility utility;
    private boolean isSubmissionChallenge = false;
    private CelebrityModel editCelebrityModel;
    private EntriesModel editEntriesModel;
    private int positionInList;

    public static Intent createIntent(Context context, int challengeId, int typePost) {
        Intent intent = new Intent(context, ActivityPostMedia.class);
        intent.putExtra(CHALLENGE_ID, challengeId);
        intent.putExtra(TYPE_ID, typePost);
        return intent;
    }

    //==== lifecycle
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_video);
        ButterKnife.bind(this);
        intiView();
        setLimitTextCount();
        initBundle();
        mPresenter = new PostPresenter(AppsterWebServices.get(), "Bearer " + AppsterApplication.mAppPreferences.getUserToken());
        mPresenter.attachView(this);
        createData();
        setOnclickListener();
        disableClickSharing();
        utility = new DialogInfoUtility();
    }

    private void initBundle() {
        bundleMedia = (BundleMedia) getBaseBundle(ConstantBundleKey.BUNDLE_MEDIA_KEY);
        if (bundleMedia != null) {
            mediaType = bundleMedia.getType();
            isSubmissionChallenge = bundleMedia.isSubmissionChallenge;
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            editCelebrityModel = bundle.getParcelable(PostChallengeActivity.CHALLENGE_MODEL);
            if (editCelebrityModel != null) {
                isEditMode = true;
                edtPostDescription.setText(StringUtil.decodeString(editCelebrityModel.getDescription()));
                positionInList = bundle.getInt(PostChallengeActivity.POSITION);
                mediaType = editCelebrityModel.getMediaType();
                if (editCelebrityModel.getPostType() != null && editCelebrityModel.getPostType() == Constants.CHALLENGE_SUBMISSION)
                    isSubmissionChallenge = true;
                switch (mediaType) {
                    case Constants.POST_TYPE_QUOTES:
                        fmMedia.setVisibility(View.GONE);
                        break;
                    case Constants.POST_TYPE_CHALLENGE_SELFIE:
                    case Constants.POST_TYPE_IMAGE:
                        ImageLoaderUtil.displayUserImage(getApplicationContext(), editCelebrityModel.getImage(), imageVideos);
                        break;
                    case Constants.POST_TYPE_VIDEO:
                        postPlayVideo.setVisibility(View.VISIBLE);
                        ImageLoaderUtil.displayUserImage(getApplicationContext(), editCelebrityModel.getImage(), imageVideos);
                        break;
                }
            }

            editEntriesModel = bundle.getParcelable(ConstantBundleKey.BUNDLE_ENTRIES_MODEL);
            if (editEntriesModel != null) {
                positionInList = bundle.getInt(PostChallengeActivity.POSITION);
                isEditMode = true;
                edtPostDescription.setText(StringUtil.decodeString(editEntriesModel.getCaption()));
                mediaType = editEntriesModel.getMediaType();
                isSubmissionChallenge = true;
                switch (mediaType) {
                    case Constants.POST_TYPE_QUOTES:
                        fmMedia.setVisibility(View.GONE);
                        break;
                    case Constants.POST_TYPE_CHALLENGE_SELFIE:
                    case Constants.POST_TYPE_IMAGE:
                        ImageLoaderUtil.displayUserImage(getApplicationContext(), editEntriesModel.getImage(), imageVideos);
                        break;
                    case Constants.POST_TYPE_VIDEO:
                        postPlayVideo.setVisibility(View.VISIBLE);
                        ImageLoaderUtil.displayUserImage(getApplicationContext(), editEntriesModel.getImage(), imageVideos);
                        break;
                }
            }

            String captionText;
            if (edtPostDescription.getText() != null) {
                captionText = edtPostDescription.getText().toString();
                edtPostDescription.setSelection(captionText.length());
            }

            setPageTitle(mediaType);
        }
    }

    private void setPageTitle(int type) {
        switch (type) {
            case Constants.POST_TYPE_QUOTES:
                pageTitle.setText(R.string.post_quotes);
                break;
            case Constants.POST_TYPE_CHALLENGE_SELFIE:
            case Constants.POST_TYPE_IMAGE:
                pageTitle.setText(R.string.post_photo);
                break;
            case Constants.POST_TYPE_VIDEO:
                pageTitle.setText(R.string.post_video);
                break;
        }
    }

    private void createData() {
        switch (mediaType) {
            case Constants.POST_TYPE_QUOTES:
                fmMedia.setVisibility(View.GONE);
                break;

            case Constants.POST_TYPE_CHALLENGE_SELFIE:
            case Constants.POST_TYPE_IMAGE:
                if (bundleMedia != null) onImageChanged(bundleMedia);
                postPlayVideo.setVisibility(View.GONE);
                break;

            case Constants.POST_TYPE_VIDEO:
                if (bundleMedia != null) {
                    uriData = Uri.parse(bundleMedia.getUriPath());
                    pathMedia = getRealPathFromURI(uriData, this);
                    pathVideoCamera = pathMedia;
                    imageThumbnailVideo = VideoUtil.createVideoThumbnail(pathMedia);
                    if (imageThumbnailVideo != null) {
                        imageVideos.setImageBitmap(Bitmap.createScaledBitmap(imageThumbnailVideo,
                                Constants.BITMAP_THUMBNAIL_WIDTH, Constants.BITMAP_THUMBNAIL_HEIGHT, false));
                    }
                    postPlayVideo.setVisibility(View.VISIBLE);
                }
                break;
        }

        setPageTitle(mediaType);

    }

    @Override
    protected void onResume() {
        super.onResume();
        handlePostButton();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMemory();
        destroyDialog();
        mPresenter.detachView();
    }


    //=== end mvp callback

    private void intiView() {
        llRoot.setOnClickListener(this);
        cancelTextIv.setOnClickListener(v -> {
            cancelTextIv.setVisibility(View.INVISIBLE);
            address = "";
            txtCheckIn.setText(address);
            txtCheckIn.setCustomFont(this, getString(R.string.font_opensansregular));
            UiUtils.hideSoftKeyboard(ActivityPostMedia.this);
        });

        txtCheckIn.setOnClickListener(v -> {
            if (txtCheckIn.getLinksClickable()) {
                onClickCheckLocation(v);
                UiUtils.hideSoftKeyboard(ActivityPostMedia.this);
            }
        });

        imageVideos.setOnClickListener(view -> {

            if (mediaType == Constants.POST_TYPE_VIDEO) {
                showVideosPopUp();
            } else if (mediaType == Constants.POST_TYPE_IMAGE || mediaType == Constants.POST_TYPE_CHALLENGE_SELFIE) {
                showPicPopUp();
            }
        });
    }

    private void setLimitTextCount() {
        edtPostDescription.setAnchorView((View) edtPostDescription.getParent().getParent());
        edtPostDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String tex = StringUtil.encodeString(s.toString());
                if (tex.length() > Constants.LIMIT_TEXT_LENG_POST) {
                    String des = StringUtil.decodeString(descriptionBefore);
                    edtPostDescription.setText(des);
                    int selection = TextUtils.isEmpty(des) ? 0 : des.length() - 1;
                    edtPostDescription.setSelection(selection);
                } else {
                    descriptionBefore = tex;
                }
                int newLength = descriptionBefore.length();
                onDescriptionLengthChanged(newLength);
                onCounterChanged(newLength);
                handlePostButton();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setOnclickListener() {
        btnPost.setOnClickListener(this);
        fmMedia.setOnClickListener(this);
    }

    private void onDescriptionLengthChanged(int length) {
//        if (length > 0) {
//            edtPostDescription.setCustomFont(getBaseContext(), getString(R.string.font_opensansregular));
//        } else {
//            edtPostDescription.setCustomFont(getBaseContext(), getString(R.string.font_opensansemibold));
//        }
    }

    private void onCounterChanged(int length) {
        String counter = String.valueOf(length + "/" + Constants.LIMIT_TEXT_LENG_POST);
        txtNumberText.setText(counter);
    }

    void handlePostButton() {
        boolean shouldEnable = false;

        postDescription = edtPostDescription.getText().toString().trim();
//        if (mediaType == Constants.POST_TYPE_QUOTES && !TextUtils.isEmpty(edtPostDescription.getText())) {
//            shouldEnable = true;
//        } else if (mediaType == Constants.POST_TYPE_IMAGE && (bitmapSend != null || isEditMode)) {
//            shouldEnable = true;
//        } else if (mediaType == Constants.POST_TYPE_VIDEO && ((!TextUtils.isEmpty(pathVideoCamera) && imageThumbnailVideo != null) || isEditMode)) {
//            shouldEnable = true;
//        }

//        if (shouldEnable) {
//            btnPost.setEnabled(true);
//        } else {
//            btnPost.setEnabled(false);
//        }

        if (StringUtil.isNullOrEmptyString(postDescription) && mediaType == Constants.POST_TYPE_QUOTES) {
            btnPost.setTextColor(ContextCompat.getColor(this,R.color.white));
        } else {
            btnPost.setTextColor(ContextCompat.getColor(this,R.color.color_2587E7));
        }
    }

    private void onImageChanged(BundleMedia bundleMedia) {
        uriImageSend = Uri.parse(bundleMedia.getUriPath());
        bitmapSend = Utils.getBitmapFromURi(ActivityPostMedia.this, uriImageSend);
        if (bitmapSend != null) {
            imageVideos.setImageBitmap(Bitmap.createScaledBitmap(bitmapSend,
                    Constants.BITMAP_THUMBNAIL_WIDTH, Constants.BITMAP_THUMBNAIL_HEIGHT, false));
        }
    }

    private String removeBlank(String message) {

        message = message.replaceAll("(\\s)+", "$1");

        return message;
    }

    private double mCurrentLat = UNKNOWN_LOCATION;
    private double mCurrentLon = UNKNOWN_LOCATION;

    public void onClickCheckLocation(View view) {

        compositeDisposable.add(mRxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE)
                .subscribe(granted -> {
                    if (granted) {
                        // All requested permissions are granted
                    } else {
                        // At least one permission is denied
                    }
                }));

        compositeDisposable.add(mRxPermissions.request(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(granted -> {
                    if (granted) {
                        // Get Location
                        GPSTClass gpstClass = GPSTClass.getInstance();
                        gpstClass.getLocation(this);
                        // check if GPS enabled
                        if (!gpstClass.canGetLocation()) {
                            // can't get location
                            // GPS or Network is not enabled
                            // Ask user to enable GPS/network in settings
                            showSettingsAlert();
                            return;
                        }

                        mCurrentLat = gpstClass.getLatitude();
                        mCurrentLon = gpstClass.getLongitude();

                        address = GetAddress.getAddress(ActivityPostMedia.this, mCurrentLat, mCurrentLon);

                        if (StringUtil.isNullOrEmptyString(address)) {
                            return;
                        }

                        cancelTextIv.setVisibility(View.VISIBLE);
                        txtCheckIn.setText(address);
                        txtCheckIn.setCustomFont(this, getString(R.string.font_opensansregular));
                    }
                }));
    }

    /**
     * Function to show settings alert dialog On pressing Settings button will
     * lauch Settings Options
     */
    public void showSettingsAlert() {

        DialogbeLiveConfirmation.Builder builder = new DialogbeLiveConfirmation.Builder();
        builder.title("GPS not enabled")
                .message("Do you want to go to settings menu?")
                .confirmText(getString(R.string.setting_slider))
                .singleAction(false)
                .onConfirmClicked(() -> {
                    Intent intent1 = new Intent(
                            Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent1);
                })
                .build().show(this);
    }

    public void post() {

        if (!CheckNetwork.isNetworkAvailable(ActivityPostMedia.this)) {
            utility.showMessage(
                    getString(R.string.app_name),
                    getResources().getString(
                            R.string.no_internet_connection),
                    ActivityPostMedia.this);
            return;
        }

        postDescription = edtPostDescription.getText().toString().trim();
        if (mediaType == Constants.POST_TYPE_QUOTES) {
            if (StringUtil.isNullOrEmptyString(postDescription)) {
                utility.showMessage(getString(R.string.app_name),
                        getString(R.string.please_enter_description),
                        ActivityPostMedia.this);

                return;
            }
        } else if (mediaType == Constants.POST_TYPE_IMAGE || mediaType == Constants.POST_TYPE_CHALLENGE_SELFIE) {
            if (bitmapSend == null && !isEditMode) {
                return;
            }
        } else if (mediaType == Constants.POST_TYPE_VIDEO) {
            if ((StringUtil.isNullOrEmptyString(pathVideoCamera) || imageThumbnailVideo == null) && !isEditMode) {
                return;
            }
        }

        postDescription = removeBlank(postDescription);
        postDescription = StringUtil.encodeString(postDescription);
//        btnPost.setClickable(false);

        PostCreatePostRequestModel request;

        File fileImage = null;

        int challengeId = 0;
        if (isEditMode) {
            if (editCelebrityModel != null) {
                challengeId = editCelebrityModel.getId();
            } else if (editEntriesModel != null) {
                challengeId = editEntriesModel.getId();
            }
        } else if (bundleMedia != null) {
            challengeId = bundleMedia.challengeId;
        }

        if (mediaType == Constants.POST_TYPE_IMAGE || mediaType == Constants.POST_TYPE_CHALLENGE_SELFIE) {
            if (bitmapSend != null) fileImage = Utils.getFileFromBitMap(this, bitmapSend);
            request = new PostCreatePostRequestModel(address,
                    mCurrentLat, mCurrentLon, postDescription, mediaType, fileImage, isSubmissionChallenge, edtPostDescription.getStringTaggedUsersIdAndClear());
        } else if (mediaType == Constants.POST_TYPE_VIDEO) {
            File fileVideo = new File(pathVideoCamera);
            if (imageThumbnailVideo != null) fileImage = Utils.getFileFromBitMap(this, imageThumbnailVideo);
            request = new PostCreatePostRequestModel(address,
                    mCurrentLat, mCurrentLon, postDescription, mediaType, fileImage, fileVideo, isSubmissionChallenge, edtPostDescription.getStringTaggedUsersIdAndClear());
        } else if (mediaType == Constants.POST_TYPE_QUOTES) {
            request = new PostCreatePostRequestModel(address,
                    mCurrentLat, mCurrentLon, postDescription, mediaType, isSubmissionChallenge, edtPostDescription.getStringTaggedUsersIdAndClear());
        } else {
            request = new PostCreatePostRequestModel(address,
                    mCurrentLat, mCurrentLon, postDescription, 1, true, edtPostDescription.getStringTaggedUsersIdAndClear());
        }

        mPresenter.post(isEditMode, isSubmissionChallenge, challengeId, request.build());
    }

    private void onPostError(String message, int code) {
        if (DialogManager.isShowing()) {
            DialogManager.getInstance().dismisDialog();
        }
        handleError(message, code);
    }

    public void onBackClick(View view) {
        confirmBackPressed();
    }

    @Override
    public void onBackPressed() {

        confirmBackPressed();

    }

    private void confirmBackPressed() {

        boolean needShowConfirm = false;

        if (bundleMedia != null) {

            if (bundleMedia.isPost()) {

                if (imageThumbnailVideo != null || bitmapSend != null) {
                    needShowConfirm = true;
                }
            }
        } else {

            if (mediaType == Constants.POST_TYPE_QUOTES) {

                String description = edtPostDescription.getText().toString().trim();

                if (!StringUtil.isNullOrEmptyString(description)) {
                    needShowConfirm = true;
                }

            }
        }

        if (!needShowConfirm) {
            exitPost();
            return;
        }

        DialogbeLiveConfirmation.Builder builder = new DialogbeLiveConfirmation.Builder();
        DialogbeLiveConfirmation confirmation = new DialogbeLiveConfirmation(builder);
        builder.title(getString(R.string.app_name))
                .message(getString(R.string.post_discard_current_post))
                .confirmText(getString(R.string.post_btn_Discard))
                .singleAction(false)
                .onConfirmClicked(this::exitPost)
                .build().show(this);

    }

    private void exitPost() {

        if (imageThumbnailVideo != null && !imageThumbnailVideo.isRecycled()) {
            imageThumbnailVideo.recycle();
            imageThumbnailVideo = null;
        }

        if (bitmapSend != null && !bitmapSend.isRecycled()) {
            bitmapSend.recycle();
            bitmapSend = null;
        }

        Utils.hideSoftKeyboard(this);
        setResult(RESULT_CANCELED);

        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {

            if (requestCode == Constants.REQUEST_CODE_SHARE_FEED) {
                checkShareFeed();
            }

            return;
        }

        Uri imageCroppedURI;

        switch (requestCode) {

            case Constants.REQUEST_PIC_FROM_CROP:

                final Uri resultUri = UCrop.getOutput(data);
                if (resultUri != null) {
                    if (bundleMedia == null) bundleMedia = new BundleMedia();
                    bundleMedia.setUriPath(resultUri.toString());
                    onImageChanged(bundleMedia);
                } else {
                    Toast.makeText(this, R.string.toast_cannot_retrieve_cropped_image, Toast.LENGTH_SHORT).show();
                }
                break;
            case ConstantBundleKey.SHARE_INSTAGRAM_REQUEST:
                setResult(RESULT_OK);
                finish();
                break;

            case Constants.REQUEST_PIC_FROM_LIBRARY:
                if (data != null) {
                    uriImageSend = data.getData();
                    if (uriImageSend != null) {
                        if (editCelebrityModel != null && editCelebrityModel.getMediaType() == Constants.POST_TYPE_CHALLENGE_SELFIE) {
                            openImageEditorScreen(this, uriImageSend, editCelebrityModel.getSelfieImage());
                        } else if (editEntriesModel != null && editEntriesModel.getMediaType() == Constants.POST_TYPE_CHALLENGE_SELFIE) {
                            openImageEditorScreen(this, uriImageSend, editEntriesModel.getSelfieImage());
                        } else {
                            imageCroppedURI = getOutputMediaFileUri(MEDIA_TYPE_IMAGE_CROPPED);
                            performCrop(uriImageSend, imageCroppedURI);
                        }
                    }
                }

                break;

            case Constants.REQUEST_PIC_FROM_CAMERA:
                if (data != null) {
                    uriImageSend = data.getData();
                    if (uriImageSend != null) {
                        if (editCelebrityModel != null && editCelebrityModel.getMediaType() == Constants.POST_TYPE_CHALLENGE_SELFIE) {
                            openImageEditorScreen(this, uriImageSend, editCelebrityModel.getSelfieImage());
                        } else if (editEntriesModel != null && editEntriesModel.getMediaType() == Constants.POST_TYPE_CHALLENGE_SELFIE) {
                            openImageEditorScreen(this, uriImageSend, editEntriesModel.getSelfieImage());
                        } else {
                            if (bundleMedia == null) bundleMedia = new BundleMedia();
                            bundleMedia.setUriPath(uriImageSend.toString());
                            onImageChanged(bundleMedia);
                        }
                    }
                }

                break;

            case Constants.REQUEST_PHOTO_EDITOR_ACTIVITY:
                if (data != null) {
                    uriImageSend = data.getData();
                    if (bundleMedia == null) bundleMedia = new BundleMedia();
                    bundleMedia.setUriPath(uriImageSend.toString());
                    onImageChanged(bundleMedia);
                }
                break;

            case Constants.INSTAGRAM_SHARE_RETURN:

                break;

            case Constants.REQUEST_CODE_SHARE_FEED:
                checkShareFeed();

                break;

            case Constants.PICK_VIDEO_FROM_LIBRARY_REQUEST:
                if (data != null) {
                    fileUri = data.getData();
                    loadVideoAfterPickFromGallery(fileUri);
                }
                break;

            case Constants.CAMERA_VIDEO_REQUEST:
                if (data != null) {
                    fileUri = data.getData();
                    Timber.e("mRecordUrl " + fileUri);
                    pathMedia = getRealPathFromURI(fileUri, this);
                    pathVideoCamera = pathMedia;
                    imageThumbnailVideo = VideoUtil.createVideoThumbnail(pathMedia);
                    if (imageThumbnailVideo != null) {
                        imageVideos.setImageBitmap(Bitmap.createScaledBitmap(imageThumbnailVideo,
                                Constants.BITMAP_THUMBNAIL_WIDTH, Constants.BITMAP_THUMBNAIL_HEIGHT, false));
                    }
                }
                break;

            case Constants.VIDEO_TRIMMED_REQUEST:
                String urlVideo = data.getStringExtra(Constants.VIDEO_PATH);
                Uri videoUri = Uri.fromFile(new File(urlVideo));

                pathMedia = getRealPathFromURI(videoUri, this);
                pathVideoCamera = pathMedia;
                imageThumbnailVideo = VideoUtil.createVideoThumbnail(pathMedia);
                if (imageThumbnailVideo != null) {
                    imageVideos.setImageBitmap(Bitmap.createScaledBitmap(imageThumbnailVideo,
                            Constants.BITMAP_THUMBNAIL_WIDTH, Constants.BITMAP_THUMBNAIL_HEIGHT, false));
                }

                break;

        }
    }


    void checkShareFeed() {

    }

    @Override
    public void onClick(View view) {
        if (preventMultiClicks()) {
            return;
        }
        switch (view.getId()) {

            case R.id.btn_post:
                post();
                break;

            case R.id.ll_root:
                UiUtils.hideSoftKeyboard(this);
                break;

            case R.id.fm_media:
//                showVideosPopUp();
                break;
        }


    }

    @Override
    public void onStartSharing(SocialManager.TypeShare typeShare, Context context) {
        showDialog(context, getString(R.string.post_sharing));

        SocialManager.getInstance().isComepleteSharing = false;
    }

    @Override
    public void onErrorSharing(SocialManager.TypeShare typeShare, Context context, String message) {
        dismissDialog();
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();


        checkShareFeed();

    }

    @Override
    public void onCompleteSharing(SocialManager.TypeShare typeShare, Context context, String message) {
        dismissDialog();
        SocialManager.getInstance().isComepleteSharing = true;
        isCompletedSharing = true;
        SocialManager.getInstance().setBitmapSend(null);
        SocialManager.getInstance().socialSharingListener = null;

        Toast.makeText(getApplicationContext(), getString(R.string.post_share_facebook_success), Toast.LENGTH_SHORT).show();

        checkShareFeed();

    }

    void releaseMemory() {
        if (imageThumbnailVideo != null && !imageThumbnailVideo.isRecycled()) {
            imageThumbnailVideo.recycle();
            imageThumbnailVideo = null;
        }

        if (bitmapSend != null && !bitmapSend.isRecycled()) {
            bitmapSend.recycle();
            bitmapSend = null;
        }
        SocialManager.getInstance().context = null;
    }

    @Override
    public void onNotLoginForSharing() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack();
    }

    @Override
    public void onStartingAuthentication() {

    }

    @Override
    public void onLoginFail(String message) {

    }

    @Override
    public void onAuthentSuccess() {
        AppsterApplication.mAppPreferences.setLoginFacebook(true);
//        typeShare = -1;

    }


    @Override
    public void onCompleteLogin() {
    }


    @Override
    public void loginWithFacebookInfo(LoginFacebookRequestModel requestLogin) {

    }

    void disableClickSharing() {
//        if (bundleMedia == null && mediaType == Constants.POST_TYPE_QUOTES) {
//            llShareInstagram.setEnabled(false);
//
//        } else if (bundleMedia != null && bundleMedia.getType() == Constants.POST_TYPE_QUOTES) {
//            llShareInstagram.setEnabled(false);
//        }
    }

    void showBitmapEditSharing() {

        if (bundleMedia.getUriPath().contains("http")) {

            ImageLoaderUtil.displayMediaImage(this, bundleMedia.getUriPath(), imageVideos);
        }
    }

    @Override
    public void onPostSuccessfully(PostDataModel model) {
        if (isEditMode) {
            Intent intent = getIntent();
            if (editCelebrityModel != null && intent != null) {
                if (isSubmissionChallenge) {
                    editCelebrityModel.setDescription(model.caption);
                } else {
                    editCelebrityModel.setDescription(model.getDescription());
                }
                if ((editCelebrityModel.getMediaType() == Constants.POST_TYPE_IMAGE ||
                        editCelebrityModel.getMediaType() == Constants.POST_TYPE_CHALLENGE_SELFIE) && bitmapSend != null) {
                    editCelebrityModel.setImage(model.getMediaImageThumbnail());
                }

                if (editCelebrityModel.getMediaType() == Constants.POST_TYPE_VIDEO && imageThumbnailVideo != null) {
                    editCelebrityModel.setImage(model.getMediaImageThumbnail());
                    editCelebrityModel.setVideo(model.getMediaVideo());
                }

                intent.putExtra(PostChallengeActivity.CHALLENGE_MODEL, editCelebrityModel);
                intent.putExtra(PostChallengeActivity.POSITION, positionInList);
                setResult(Activity.RESULT_OK, intent);
            } else if (editEntriesModel != null && intent != null) {
                editEntriesModel.setCaption(model.caption);
                if ((editEntriesModel.getMediaType() == Constants.POST_TYPE_IMAGE ||
                        editEntriesModel.getMediaType() == Constants.POST_TYPE_CHALLENGE_SELFIE) && bitmapSend != null) {
                    editEntriesModel.setImage(model.getMediaImageThumbnail());
                }

                if (editEntriesModel.getMediaType() == Constants.POST_TYPE_VIDEO && imageThumbnailVideo != null) {
                    editEntriesModel.setImage(model.getMediaImageThumbnail());
                    editEntriesModel.setVideo(model.getMediaVideo());
                }

                intent.putExtra(ConstantBundleKey.BUNDLE_ENTRIES_MODEL, editEntriesModel);
                intent.putExtra(PostChallengeActivity.POSITION, positionInList);
                setResult(Activity.RESULT_OK, intent);
            }
        } else {
            setResult(RESULT_OK);
        }
        releaseMemory();
        finish();
    }

    @Override
    public void onPostFailed() {

    }

    @Override
    public Context getViewContext() {
        return null;
    }

    @Override
    public void loadError(String errorMessage, int code) {
        handleError(errorMessage, code);
        dismissDialog();
    }

    @Override
    public void showProgress() {
        showDialog(this, getString(R.string.connecting_msg));
    }

    @Override
    public void hideProgress() {
        dismissDialog();
    }
}
