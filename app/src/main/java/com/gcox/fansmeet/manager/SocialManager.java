package com.gcox.fansmeet.manager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;

import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import android.widget.Toast;
import com.facebook.*;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.Sharer;
import com.facebook.share.model.*;
import com.facebook.share.widget.ShareDialog;
import com.gcox.fansmeet.AppsterApplication;
import com.gcox.fansmeet.common.Constants;
import com.gcox.fansmeet.features.profile.ItemModelClassNewsFeed;
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.CelebrityModel;
import com.gcox.fansmeet.util.*;
import com.gcox.fansmeet.webservice.request.LoginFacebookRequestModel;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;
import io.reactivex.disposables.CompositeDisposable;
import org.json.JSONException;
import org.json.JSONObject;
import org.reactivestreams.Subscription;
import timber.log.Timber;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;

import com.gcox.fansmeet.R;

/**
 * Created by USER on 10/14/2015.
 */
public class SocialManager {


    private static CallbackManager callbackManager;
    private static SocialManager instance = null;
    static GoogleApiClient googleApiClient;
    public Context context;
    public boolean login;
    private boolean createdAccount;
    private Bitmap bitmapSend;
    //    AccessToken accessToken = null;
    SocialLoginListener socialLoginListener;
    public SocialSharingListener socialSharingListener;
    boolean isStartingTask;
    public boolean isComepleteSharing;
    private RxPermissions mRxPermissions;
    CompositeDisposable mSubscriptionExternalPermissions;

    public static String SHARE_TYPE_POST = "post";
    public static String SHARE_TYPE_STREAM = "stream";

    public enum TypeShare {
        SHARE_FACEBOOK,
        SHARE_INSTAGRAM,
        SHARE_TWITTER,
        SHARE_WHATSAPP
    }


    private Bitmap getBitmapSend() {
        return bitmapSend;
    }

    public void setBitmapSend(Bitmap bitmapSend) {
        this.bitmapSend = bitmapSend;
    }

    public boolean isCreatedAccount() {
        return createdAccount;
    }

    public void setCreatedAccount(boolean createdAccount) {
        this.createdAccount = createdAccount;
    }


    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public void cancelStartingTask() {
        isStartingTask = false;
    }


    static synchronized public SocialManager getInstance() {
        if (instance == null) {
            instance = new SocialManager();
        }

        return instance;
    }

    public static void cancelInstance() {
        callbackManager = null;
        instance = null;

    }

    public void login(final Context context, SocialLoginListener callback, final LoginFacebookRequestModel requestLogin) {
        this.context = context;

        socialLoginListener = callback;

//        if (AccessToken.getCurrentAccessToken() == null || AccessToken.getCurrentAccessToken().isExpired()) {
        isStartingTask = true;
        if (socialLoginListener != null) {
            socialLoginListener.onStartingAuthentication();
        }

        // Logout
        LoginManager.getInstance().logOut();
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions((Activity) context,
                Collections.singletonList("public_profile, email,user_friends"));

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(final LoginResult loginResult) {
                        requestDataFacebook(loginResult.getAccessToken(), requestLogin);
                    }

                    @Override
                    public void onCancel() {

                        if (AppsterApplication.mAppPreferences.isLoginFacebook()) {
                            if (socialLoginListener != null) {
                                socialLoginListener.loginWithFacebookInfo(requestLogin);
                            }
                        }
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        System.out.println("onError");

                    }
                });

//        } else {
//            requestDataFacebook(AccessToken.getCurrentAccessToken(), requestLogin);
//        }

    }


    void requestDataFacebook(AccessToken mToken, final LoginFacebookRequestModel requestLogin) {
        Bundle params = new Bundle();
        params.putString("fields", "id,name,email,gender");
        new GraphRequest(mToken, "/me", params, HttpMethod.GET, response -> {
            if (response.getError() != null) {
                // handle error
                System.out.println("ERROR");
                if (socialLoginListener != null) {
                    socialLoginListener.onLoginFail(String.valueOf(response.getError()));
                }
            } else {
                System.out.println("Success");

                try {
                    if (socialLoginListener != null) {
                        socialLoginListener.onAuthentSuccess();
                    }
                    JSONObject json = response.getJSONObject();
                    String str_email;
                    try {
                        str_email = json.getString("email");
                    } catch (JSONException error) {
                        str_email = "no email";
                    }
                    String gender = "";
                    if (json.has("gender")) {
                        gender = json.getString("gender");
                    }
                    String str_id = json.getString("id");
                    String str_name = json.getString("name");
                    requestLogin.setEmail(str_email);
                    requestLogin.setUserName(StringUtil.extractUserNameFromEmail(str_email));
                    requestLogin.setFbId(str_id);
                    requestLogin.setDisplay_name(str_name);
                    AppsterApplication.mAppPreferences.setFacebookDisplayName(str_name);
                    requestLogin.setProfile_Pic(Constants.getFBImageProfile(str_id));
                    requestLogin.setGender(gender);

                    isStartingTask = false;
                    if (socialLoginListener != null) {
                        socialLoginListener.loginWithFacebookInfo(requestLogin);
                    }

                } catch (JSONException e) {
                    if (socialLoginListener != null) {
                        socialLoginListener.onLoginFail(response.getError().toString());
                    }

                }

            }


        }).executeAsync();
    }

    public void loginInSettingScreen(final Context context) {
        this.context = context;

        socialLoginListener = (SocialLoginListener) context;

        if (AccessToken.getCurrentAccessToken() == null || AccessToken.getCurrentAccessToken().isExpired()) {
            isStartingTask = true;
            if (socialLoginListener != null) {
                socialLoginListener.onStartingAuthentication();
            }

            // Logout
            LoginManager.getInstance().logOut();

            callbackManager = CallbackManager.Factory.create();
            LoginManager.getInstance().logInWithReadPermissions((Activity) context,
                    Collections.singletonList("public_profile, email"));

            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(final LoginResult loginResult) {

                            System.out.println("Success");
                            Bundle params = new Bundle();
                            params.putString("fields", "id,name,email");
                            new GraphRequest(loginResult.getAccessToken(), "/me", params, HttpMethod.GET, response -> {
                                if (response.getError() != null) {
                                    // handle error
                                    System.out.println("ERROR");
                                    if (socialLoginListener != null) {
                                        socialLoginListener.onLoginFail(response.getError().toString());
                                    }
                                } else {
                                    System.out.println("Success");

                                    try {
                                        if (socialLoginListener != null) {
                                            socialLoginListener.onAuthentSuccess();
                                        }
                                        JSONObject json = response.getJSONObject();
                                        String str_name = json.getString("name");

                                        AppsterApplication.mAppPreferences.setFacebookDisplayName(str_name);
                                        isStartingTask = false;

                                        if (socialLoginListener != null) {
                                            socialLoginListener.onCompleteLogin();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        if (socialLoginListener != null) {
                                            socialLoginListener.onLoginFail(response.getError().toString());
                                        }
                                    }
                                }
                            }).executeAsync();
                        }


                        @Override
                        public void onCancel() {


                        }

                        @Override
                        public void onError(FacebookException exception) {
                            Timber.d(exception);

                        }
                    });

        }
    }

    public void logOut() {
        LoginManager.getInstance().logOut();
        socialSharingListener = null;
        setBitmapSend(null);
        callbackManager = null;
        instance = null;
        context = null;
        AppsterApplication.mAppPreferences.setLoginFacebook(false);
        AccessToken.setCurrentAccessToken(null);
    }

    public boolean isFacebookLoggedIn() {

        return !(AccessToken.getCurrentAccessToken() == null || AccessToken.getCurrentAccessToken().isExpired());

    }

    public void getFacebookAccessToken(Context context, OnGetFBTokenListener listener) {
        Timber.e("getFacebookAccessToken");
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null && !accessToken.isExpired()) {
            listener.onSuccess(accessToken.getToken());
        } else {
            if (context == null) return;
            // Logout
            LoginManager.getInstance().logOut();
            isStartingTask = true;
            callbackManager = CallbackManager.Factory.create();
            LoginManager.getInstance().logInWithReadPermissions((Activity) context,
                    Collections.singletonList("public_profile, email,user_friends"));

            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {

                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            Timber.e(loginResult.toString());
                            listener.onSuccess(loginResult.getAccessToken().getToken());
                            isStartingTask = false;
                        }

                        @Override
                        public void onCancel() {
                        }

                        @Override
                        public void onError(FacebookException error) {
                            Timber.e(error);
                            listener.onFailed();
                        }
                    });
        }
    }


    public void shareFeedToFacebook(Context context, CelebrityModel item) {
        if (item == null) return;
        if (item.getWebPostUrl() == null) return;
        ShareDialog shareDialog = new ShareDialog((AppCompatActivity) context);
        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(item.getWebPostUrl()))
                .build();

        shareDialog.show(linkContent);
    }

    public void shareURLToFacebook(Context context, String Url) {
        shareURLToFacebook(context, Url, -1, null, null);
    }

    public void shareURLToFacebook(Context context, String Url, int requestCode, CallbackManager callbackManager, FacebookCallback<Sharer.Result> shareCallBack) {
        if (Url == null) return;
        ShareDialog shareDialog = new ShareDialog((AppCompatActivity) context);
        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(Url))
                .build();
        if (requestCode != -1 && shareCallBack != null && callbackManager != null)
            shareDialog.registerCallback(callbackManager, shareCallBack, requestCode);
        shareDialog.show(linkContent);
    }

    public void shareURLToFacebook(Context context, String Url, int requestCode, FacebookCallback<Sharer.Result> shareCallBack) {
        isStartingTask = true;
        if (callbackManager == null) callbackManager = CallbackManager.Factory.create();
        shareURLToFacebook(context, Url, requestCode, callbackManager, shareCallBack);
    }


    private String getFileNameFromURL(String url) {

        if (StringUtil.isNullOrEmptyString(url)) {
            return "";
        }

        return url.substring(url.lastIndexOf("/"));
    }

    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        if (isStartingTask && callbackManager != null) {
            callbackManager.onActivityResult(requestCode, responseCode, data);
        }

    }

    private Uri replaceUriNotAllowed(Context context, Uri uri) {
        if (uri == null)
            return uri;

        String uriString = uri.toString();
        if (StringUtil.isNullOrEmptyString(uriString)) return uri;
        if (uriString.startsWith("file:///")) {
            uriString = uriString.replace("file:///", "");
        }

        Uri newUri = FileProvider.getUriForFile(context, com.gcox.fansmeet.BuildConfig.APPLICATION_ID + ".provider", new File(uriString));
//        String stringUri = newUri.toString();
//        if (stringUri.contains(".png")) {
//            int indexLast = stringUri.lastIndexOf(".png");
//
//            stringUri = stringUri.substring(0, indexLast + 4);
//        }

//        Uri outUri = Uri.parse(stringUri);
        return newUri;

    }

    private String getRealPathFromURI(Context context, Uri contentURI) {
        String result;
        Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { //checking
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    private byte[] readBytes(String dataPath) throws IOException {

        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];

        try (InputStream inputStream = new FileInputStream(dataPath)) {
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
            inputStream.close();
        } catch (Exception e) {
            Timber.e(e);
        }
        return byteBuffer.toByteArray();
    }

    public void shareURLToOthers(Context context, String content, String url) {
        String str = content + "\n" + url;
        shareQuotesToOthers(context, str);

    }

    private void shareQuotesToOthers(Context context, String content) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        intent.putExtra(Intent.EXTRA_TEXT, content + "\n");
        intent.setType("text/plain");
//        context.startActivity(Intent.createChooser(intent, "Share Via"));
        ((Activity) context).startActivityForResult(Intent.createChooser(intent, "Share Via"), Constants.REQUEST_CODE_SHARE_FEED);
    }


    public static void loginWithGoogle(final AppCompatActivity context, int requestCode, GoogleApiClient.OnConnectionFailedListener callback) {
        googleApiClient = getGoogleApiClient(context, getGoogleSignInOptions());

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        context.startActivityForResult(signInIntent, requestCode);
    }

    private static GoogleSignInOptions getGoogleSignInOptions() {
        return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
    }

    private static GoogleApiClient getGoogleApiClient(Context context, GoogleSignInOptions gso) {
        if (googleApiClient == null) {
            return new GoogleApiClient.Builder(context.getApplicationContext())
                    .enableAutoManage((AppCompatActivity) context, null)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }

        return googleApiClient;
    }

    public static void logoutGoogle(final AppCompatActivity activity, GoogleApiClient.ConnectionCallbacks callback) {
        if (googleApiClient == null) {
            googleApiClient = getGoogleApiClient(activity, getGoogleSignInOptions());
        }
        googleApiClient.connect();
        googleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                        status -> {
                            if (googleApiClient != null && status.isSuccess() && googleApiClient.isConnected()) {
                                if (activity != null && !activity.isFinishing()) {
                                    googleApiClient.stopAutoManage(activity);
                                }
                                googleApiClient.disconnect();
                                googleApiClient = null;
                            }
                            Timber.d("logoutGoogle %s", String.valueOf(status.isSuccess()));
                        });
                if (callback != null) {
                    callback.onConnected(bundle);
                }
            }

            @Override
            public void onConnectionSuspended(int i) {
                if (callback != null) {
                    callback.onConnectionSuspended(i);
                }
            }
        });
    }

    public void ShareFeedQuotesToTwitter(Context context, String content, String url) {
        Intent intent = new TweetComposer.Builder(context)
                .text(content + "\n" + url).createIntent();
        ((Activity) context).startActivityForResult(intent, Constants.TWEET_SHARE_REQUEST_CODE);
    }

    public void shareURLToEmail(Context context, String content, String subject, String url) {
        String str = content + "\n" + url;
        shareQuotesToEmail(context, subject, str);

    }

    private void shareQuotesToEmail(Context context, String subject, String content) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, content + "\n");

        try {
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                ((Activity) context).startActivityForResult(intent, Constants.REQUEST_CODE_SHARE_FEED);
            } else {
//                AppsterApplication.mAppPreferences.saveShareStreamModel(null);
            }
        } catch (android.content.ActivityNotFoundException ex) {
            Timber.d(ex);
            Toast.makeText(context.getApplicationContext(), context.getString(R.string.email_have_not_been_installed), Toast.LENGTH_SHORT).show();
//            AppsterApplication.mAppPreferences.saveShareStreamModel(null);
        }
    }

    public void shareVideoToWhatsapp(Context context, String content, String url) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_TEXT, content + "\n" + url);
        sendIntent.setPackage("com.whatsapp");

        try {
            ((Activity) context).startActivityForResult(sendIntent, Constants.REQUEST_CODE_SHARE_FEED);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context.getApplicationContext(), context.getString(R.string.whatsapp_have_not_been_installed), Toast.LENGTH_SHORT).show();
        }
    }

    public void shareFeedToInstagram(final Context context, final CelebrityModel itemFeed) {
        if (itemFeed == null) return;
        mRxPermissions = new RxPermissions((Activity) context);
        if (itemFeed.getMediaType() == Constants.POST_TYPE_IMAGE || itemFeed.getMediaType() == Constants.POST_TYPE_CHALLENGE_SELFIE) {

            String fileName = getFileNameFromURL(itemFeed.getImage().trim());
//            DownloadBitmap.getInstance().deleteAllFile();
            mSubscriptionExternalPermissions = new CompositeDisposable();
            mSubscriptionExternalPermissions.add(mRxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .subscribe(granted -> {
                        if (granted) {
                            DownloadBitmap.getInstance().deleteAllFile();
                            if (DownloadBitmap.getInstance().isNeedDownloadedImage(Constants.APPSTERS_IMAGE_SHARE,
                                    fileName)) {
                                DownloadBitmap.getInstance().downloadBitmap(context, itemFeed.getImage(),
                                        Constants.APPSTERS_IMAGE_SHARE, fileName,
                                        new DownloadBitmap.IDownloadListener() {
                                            @Override
                                            public void successful(String filePath) {
                                                shareFeedToInstagram(context, itemFeed.getMediaType(), Uri.parse("file://" + filePath));
                                                if (mSubscriptionExternalPermissions != null && !mSubscriptionExternalPermissions.isDisposed())
                                                    mSubscriptionExternalPermissions.dispose();
                                            }

                                            @Override
                                            public void fail() {
                                                Timber.e("DownloadBitmap_fail");
                                                if (mSubscriptionExternalPermissions != null && !mSubscriptionExternalPermissions.isDisposed())
                                                    mSubscriptionExternalPermissions.dispose();
                                            }
                                        });
                            } else {
                                shareFeedToInstagram(context, itemFeed.getMediaType(), Uri.parse("file://" + Constants.APPSTERS_IMAGE_SHARE +
                                        fileName));
                                if (mSubscriptionExternalPermissions != null && !mSubscriptionExternalPermissions.isDisposed())
                                    mSubscriptionExternalPermissions.dispose();
                            }
                        } else {
                            if (mSubscriptionExternalPermissions != null && !mSubscriptionExternalPermissions.isDisposed())
                                mSubscriptionExternalPermissions.dispose();
                        }
                    }));

        } else if (itemFeed.getMediaType() == Constants.POST_TYPE_VIDEO) {
            mSubscriptionExternalPermissions = new CompositeDisposable();
            mSubscriptionExternalPermissions.add(mRxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .subscribe(granted -> {
                        if (granted) {
                            FileUtility.deleteVideoCacheFile();
                            DownloadVideos.getInstance().isVideoAlreadyDownloaded(itemFeed.getVideo(),
                                    (isNeedToDownload, videoLocalPath) -> ((Activity) context).runOnUiThread(() -> {
                                        if (!isNeedToDownload) {
                                            shareFeedToInstagram(context, itemFeed.getMediaType(), Uri.parse(videoLocalPath));
                                        } else {
                                            DownloadVideos.getInstance().downloadVideoFile(itemFeed.getVideo(), new DownloadVideos.IDownloadListener() {
                                                @Override
                                                public void successful(final String filePath) {
                                                    ((Activity) context).runOnUiThread(() -> {
                                                        shareFeedToInstagram(context, itemFeed.getMediaType(), Uri.parse(filePath));
                                                        if (mSubscriptionExternalPermissions != null && !mSubscriptionExternalPermissions.isDisposed())
                                                            mSubscriptionExternalPermissions.dispose();
                                                    });
                                                }

                                                @Override
                                                public void fail() {
                                                    if (mSubscriptionExternalPermissions != null && !mSubscriptionExternalPermissions.isDisposed())
                                                        mSubscriptionExternalPermissions.dispose();
                                                }
                                            });
                                        }
                                    }));
                        } else {
                            if (mSubscriptionExternalPermissions != null && !mSubscriptionExternalPermissions.isDisposed())
                                mSubscriptionExternalPermissions.dispose();
                        }
                    }));
        } else {

            Toast.makeText(context.getApplicationContext(), context.getString(R.string.share_instagram_not_share_text), Toast.LENGTH_SHORT).show();
        }
    }

    public void shareFeedToInstagram(Context context, int type, final Uri uri) {

        // Create the new Intent using the 'Send' action.\
        String typeShare = null;
        if (type == Constants.POST_TYPE_IMAGE || type == Constants.POST_TYPE_CHALLENGE_SELFIE) {
            typeShare = "image/*";

        } else if (type == Constants.POST_TYPE_VIDEO) {
            typeShare = "video/*";
        }

        Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.instagram.android");
        if (intent != null) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.setPackage("com.instagram.android");

            shareIntent.putExtra(Intent.EXTRA_STREAM, replaceUriNotAllowed(context, uri));
            shareIntent.setType(typeShare);
            ((Activity) context).startActivityForResult(shareIntent, Constants.REQUEST_CODE_SHARE_FEED);
            //Close current activity
        } else {
            Toast.makeText(context.getApplicationContext(), context.getString(R.string.share_no_instagram_app), Toast.LENGTH_SHORT).show();
        }
    }

    public void shareFeedToTwitter(final Context context, final CelebrityModel item, boolean isStream) {
        if (item == null) return;
        if (item.getMediaType() == Constants.POST_TYPE_IMAGE || item.getMediaType() == Constants.POST_TYPE_CHALLENGE_SELFIE) {

            String fileName = getFileNameFromURL(replaceTimeInUrl(item.getImage()));

            DownloadBitmap.getInstance().deleteAllFile();
            if (DownloadBitmap.getInstance().isNeedDownloadedImage(Constants.APPSTERS_IMAGE_SHARE,
                    fileName)) {

                DownloadBitmap.getInstance().downloadBitmap(context, item.getImage(),
                        Constants.APPSTERS_IMAGE_SHARE, fileName,
                        new DownloadBitmap.IDownloadListener() {
                            @Override
                            public void successful(String filePath) {
                                ShareFeedImageToTwitter(context, Uri.parse(filePath), item.getWebPostUrl(), item.getUserName(), SHARE_TYPE_POST);
                            }

                            @Override
                            public void fail() {

                            }
                        });
            } else {
                ShareFeedImageToTwitter(context,
                        Uri.parse(Constants.APPSTERS_IMAGE_SHARE + File.separator + fileName),
                        item.getWebPostUrl(), item.getUserName(), SHARE_TYPE_POST);
            }

        } else if (item.getMediaType() == Constants.POST_TYPE_VIDEO) {

            FileUtility.deleteVideoCacheFile();
            DownloadVideos.getInstance().isVideoAlreadyDownloaded(item.getVideo(),
                    (isNeedToDownload, videoLocalPath) -> ((Activity) context).runOnUiThread(() -> {
                        if (!isNeedToDownload) {
                            shareVideoToTwitter(context, videoLocalPath, item.getWebPostUrl(), item.getUserName(), SHARE_TYPE_POST);
                        } else {

                            DownloadVideos.getInstance().downloadVideoFile(item.getVideo(), new DownloadVideos.IDownloadListener() {
                                @Override
                                public void successful(final String filePath) {
                                    ((Activity) context).runOnUiThread(() -> shareVideoToTwitter(context, filePath, item.getWebPostUrl(), item.getUserName(), SHARE_TYPE_POST));
                                }

                                @Override
                                public void fail() {
                                }
                            });
                        }
                    }));
        } else {
            ShareFeedQuotesToTwitter(context, item.getWebPostUrl(), item.getUserName(), "", isStream ? SHARE_TYPE_STREAM : SHARE_TYPE_POST, true);
        }
    }

    void ShareFeedImageToTwitter(Context context, final Uri uri, String url, String userName, String type) {

        if (StringUtil.isNullOrEmptyString(url)) return;
        URL urlImage;
        try {
            urlImage = new URL(url);
        } catch (MalformedURLException e) {
            Timber.e(e);
            return;
        }

        TweetComposer.Builder builder = new TweetComposer.Builder(context)
                .text(getShareContent(context, userName, "", type, false))
                .image(uri)
                .url(urlImage);

        builder.show();
    }

    @SuppressLint("StringFormatInvalid")
    public String getShareContent(Context context, String userName, String title, String type, boolean isHost) {
        return String.format(context.getString(R.string.header_title_share_post), context.getString(R.string.app_name), type, userName);
    }

    void shareVideoToTwitter(Context context, String filepath, String url, String userName, String type) {

        try {
            File f = new File(filepath);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setClassName("com.twitter.android", "com.twitter.composer.SelfThreadComposerActivity");
            intent.setType("video/*");
//            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
            intent.putExtra(Intent.EXTRA_TEXT, getShareContent(context, userName, "", type, false) + " " + url);
            intent.putExtra(Intent.EXTRA_SUBJECT, getShareContent(context, userName, "", type, false) + " " + url);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.putExtra(Intent.EXTRA_STREAM, replaceUriNotAllowed(context, Uri.fromFile(f)));
            } else {
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
            }
            // Check if the Twitter app is installed on the phone.
            context.getPackageManager().getPackageInfo("com.twitter.android", 0);
            ((Activity) context).startActivityForResult(intent, Constants.REQUEST_CODE_SHARE_FEED);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context.getApplicationContext(), context.getString(R.string.share_no_twitter_app), Toast.LENGTH_LONG).show();

        }
    }

    public void ShareFeedQuotesToTwitter(Context context, String url, String userName, String title, String type, boolean isHost) {
        ShareFeedQuotesToTwitter(context, getShareContent(context, userName, title, type, isHost), url);
    }

    public void shareFeedToShareAction(final boolean isEmail, final Context context, final CelebrityModel itemFeed, boolean isStream) {
        if (itemFeed == null) return;
        if (itemFeed.getMediaType() == Constants.POST_TYPE_IMAGE || itemFeed.getMediaType() == Constants.POST_TYPE_CHALLENGE_SELFIE) {

            String fileName = getFileNameFromURL(replaceTimeInUrl(itemFeed.getImage()));

            DownloadBitmap.getInstance().deleteAllFile();
            if (DownloadBitmap.getInstance().isNeedDownloadedImage(Constants.APPSTERS_IMAGE_SHARE,
                    fileName)) {

                DownloadBitmap.getInstance().downloadBitmap(context, itemFeed.getImage(),
                        Constants.APPSTERS_IMAGE_SHARE, fileName,
                        new DownloadBitmap.IDownloadListener() {
                            @Override
                            public void successful(String filePath) {
                                if (isEmail) {
                                    shareImageToEmail(context, Uri.parse("file://" + filePath), itemFeed.getWebPostUrl(), itemFeed.getUserName(), SHARE_TYPE_POST);
                                } else {
                                    shareImageToOthers(context, Uri.parse("file://" + filePath), itemFeed.getWebPostUrl(), itemFeed.getUserName(), SHARE_TYPE_POST);

                                }
                            }

                            @Override
                            public void fail() {

                            }
                        });
            } else {
                if (isEmail) {
                    shareImageToEmail(context, Uri.parse("file://" + Constants.APPSTERS_IMAGE_SHARE +
                            fileName), itemFeed.getWebPostUrl(), itemFeed.getUserName(), SHARE_TYPE_POST);
                } else {
                    shareImageToOthers(context, Uri.parse("file://" + Constants.APPSTERS_IMAGE_SHARE +
                            fileName), itemFeed.getWebPostUrl(), itemFeed.getUserName(), SHARE_TYPE_POST);
                }
            }

        } else if (itemFeed.getMediaType() == Constants.POST_TYPE_VIDEO) {

            if (!isEmail) {
                shareVideoToOthers(context, itemFeed.getWebPostUrl(), itemFeed.getUserName(), SHARE_TYPE_POST);
                return;
            }
            FileUtility.deleteVideoCacheFile();
            DownloadVideos.getInstance().isVideoAlreadyDownloaded(itemFeed.getVideo(), (isNeedToDownload, videoLocalPath) -> ((Activity) context).runOnUiThread(() -> {
                if (!isNeedToDownload) {
                    shareVideoToEmail(context, Uri.parse("file://" + videoLocalPath), itemFeed.getWebPostUrl(), itemFeed.getUserName(), SHARE_TYPE_POST);
                } else {

                    DownloadVideos.getInstance().downloadVideoFile(itemFeed.getVideo(), new DownloadVideos.IDownloadListener() {
                        @Override
                        public void successful(final String filePath) {
                            ((Activity) context).runOnUiThread(() -> shareVideoToEmail(context, Uri.parse("file://" + videoLocalPath), itemFeed.getWebPostUrl(), itemFeed.getUserName(), SHARE_TYPE_POST));

                        }

                        @Override
                        public void fail() {
                        }
                    });
                }
            }));

        } else {
            if (isEmail) {
                shareQuotesToEmail(context, itemFeed, isStream);
            } else {
                shareQuotesToOthers(context, itemFeed.getWebPostUrl(), itemFeed.getUserName(), isStream ? SHARE_TYPE_STREAM : SHARE_TYPE_POST);
            }
        }
    }

    void shareImageToEmail(Context context, Uri uriImage, String urlImage, String userName, String type) {

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, getShareContent(context, userName, "", type, false).trim());
        if (!StringUtil.isNullOrEmptyString(urlImage)) intent.putExtra(android.content.Intent.EXTRA_TEXT, urlImage);
        intent.putExtra(Intent.EXTRA_STREAM, replaceUriNotAllowed(context, uriImage));
        try {
            if (intent.resolveActivity(context.getPackageManager()) != null) {
                ((Activity) context).startActivityForResult(intent, Constants.REQUEST_CODE_SHARE_FEED);
            }
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context.getApplicationContext(), context.getString(R.string.email_have_not_been_installed), Toast.LENGTH_SHORT).show();
        }
    }

    void shareImageToOthers(Context context, Uri uriImage, String urlImage, String userName, String type) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        intent.putExtra(Intent.EXTRA_STREAM, replaceUriNotAllowed(context, uriImage));
        intent.putExtra(Intent.EXTRA_TEXT, getShareContent(context, userName, "", type, false) + urlImage);

        context.startActivity(Intent.createChooser(intent, "Share Via"));

    }

    void shareVideoToOthers(Context context, String urlImage, String userName, String type) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, getShareContent(context, userName, "", type, false) + urlImage);

        context.startActivity(Intent.createChooser(intent, "Share Via"));

    }

    void shareVideoToEmail(Context context, Uri uriImage, String urlImage, String userName, String type) {

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, getShareContent(context, userName, "", type, false).trim());
        intent.putExtra(android.content.Intent.EXTRA_TEXT, urlImage);
        intent.putExtra(Intent.EXTRA_STREAM, uriImage);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            ((Activity) context).startActivityForResult(intent, Constants.REQUEST_CODE_SHARE_FEED);
        }
    }

    private void shareQuotesToEmail(Context context, CelebrityModel itemFeed, boolean isStream) {
        String subject = getShareContent(context, itemFeed.getUserName(), "", isStream ? SHARE_TYPE_STREAM : SHARE_TYPE_POST, false).trim();
        String url = itemFeed.getWebPostUrl();
        shareQuotesToEmail(context, subject, url);
    }

    private void shareQuotesToOthers(Context context, String url, String userName, String type) {
        String content;
        content = getShareContent(context, userName, "", type, false) + url;

        shareQuotesToOthers(context, content);
    }

    public void shareFeedToWhatsapp(final Context context, final CelebrityModel itemFeed, boolean isStream) {
        if (itemFeed == null) return;
        mRxPermissions = new RxPermissions((Activity) context);
        if (itemFeed.getMediaType() == Constants.POST_TYPE_IMAGE || itemFeed.getMediaType() == Constants.POST_TYPE_CHALLENGE_SELFIE) {

            String fileName = getFileNameFromURL(replaceTimeInUrl(itemFeed.getImage()));

//            DownloadBitmap.getInstance().deleteAllFile();
            mSubscriptionExternalPermissions = new CompositeDisposable();
            mSubscriptionExternalPermissions.add(mRxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(granted -> {
                        if (granted) {
                            if (DownloadBitmap.getInstance().isNeedDownloadedImage(Constants.APPSTERS_IMAGE_SHARE, fileName)) {
                                DownloadBitmap.getInstance().deleteAllFile();
                                DownloadBitmap.getInstance().downloadBitmap(context, itemFeed.getImage(),
                                        Constants.APPSTERS_IMAGE_SHARE, fileName,
                                        new DownloadBitmap.IDownloadListener() {
                                            @Override
                                            public void successful(String filePath) {
                                                shareImageToWhatsapp(context, Uri.parse(filePath), itemFeed.getWebPostUrl(), itemFeed.getUserName(), SHARE_TYPE_POST);
                                                if (mSubscriptionExternalPermissions != null && !mSubscriptionExternalPermissions.isDisposed())
                                                    mSubscriptionExternalPermissions.dispose();
                                            }

                                            @Override
                                            public void fail() {
                                                if (mSubscriptionExternalPermissions != null && !mSubscriptionExternalPermissions.isDisposed())
                                                    mSubscriptionExternalPermissions.dispose();
                                            }
                                        });
                            } else {
                                shareImageToWhatsapp(context, Uri.parse(Constants.APPSTERS_IMAGE_SHARE +
                                        fileName), itemFeed.getWebPostUrl(), itemFeed.getUserName(), SHARE_TYPE_POST);
                                if (mSubscriptionExternalPermissions != null && !mSubscriptionExternalPermissions.isDisposed())
                                    mSubscriptionExternalPermissions.dispose();
                            }
                        }
                        if (mSubscriptionExternalPermissions != null && !mSubscriptionExternalPermissions.isDisposed())
                            mSubscriptionExternalPermissions.dispose();
                    }));

        } else if (itemFeed.getMediaType() == Constants.POST_TYPE_VIDEO) {
            shareVideoToWhatsapp(context, itemFeed.getWebPostUrl(), itemFeed.getUserName(), "", SHARE_TYPE_POST, true);
        } else {
            shareQuotesToWhatsapp(context, itemFeed.getTitle(), itemFeed.getWebPostUrl(), itemFeed.getUserName(), isStream ? SHARE_TYPE_STREAM : SHARE_TYPE_POST);
        }
    }

    private String replaceTimeInUrl(String url) {
        if (StringUtil.isNullOrEmptyString(url)) return url;
        if (url.contains(".png?t=")) {
            int indexLast = url.lastIndexOf(".png?t=");
            url = url.substring(0, indexLast + 4);
        } else if (url.contains(".jpg?t=")) {
            int indexLast = url.lastIndexOf(".jpg?t=");
            url = url.substring(0, indexLast + 4);
        }

        return url;
    }

    void shareImageToWhatsapp(Context context, Uri uriImage, String urlImage, String userName, String type) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setType("image/*");
        sendIntent.setPackage("com.whatsapp");
        sendIntent.putExtra(Intent.EXTRA_STREAM, replaceUriNotAllowed(context, uriImage));
        sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        sendIntent.putExtra(Intent.EXTRA_TEXT, getShareContent(context, userName, "", type, false) + urlImage);

        try {
            ((Activity) context).startActivityForResult(sendIntent, Constants.REQUEST_CODE_SHARE_FEED);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context.getApplicationContext(), context.getString(R.string.whatsapp_have_not_been_installed), Toast.LENGTH_SHORT).show();
        }
    }

    public void shareVideoToWhatsapp(Context context, String urlvideo, String userName, String title, String type, boolean isHost) {
        shareVideoToWhatsapp(context, urlvideo, getShareContent(context, userName, title, type, isHost));
    }

    private void shareQuotesToWhatsapp(Context context, String text, String url, String userName, String type) {
        String content;
        if (SHARE_TYPE_STREAM.equals(type)) {
            content = getShareContent(context, userName, "", type, false) + "\n" + url;
        } else {
            content = getShareContent(context, userName, "", type, false) + url;
        }
        shareQuotesToWhatsapp(context, text, content);
    }

    public void shareQuotesToWhatsapp(Context context, String title, String content) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.setPackage("com.whatsapp");
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        sendIntent.putExtra(Intent.EXTRA_TEXT, content);

        try {
            ((Activity) context).startActivityForResult(sendIntent, Constants.REQUEST_CODE_SHARE_FEED);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context.getApplicationContext(), context.getString(R.string.whatsapp_have_not_been_installed), Toast.LENGTH_SHORT).show();
        }
    }

    public static void releaseGoogleLogin(AppCompatActivity context) {
        if (googleApiClient != null) {
            googleApiClient.stopAutoManage(context);
            googleApiClient.disconnect();
            googleApiClient = null;
        }
    }

    public interface SocialLoginListener {
        void onStartingAuthentication();


        void onLoginFail(String message);

        void onAuthentSuccess();

        void loginWithFacebookInfo(LoginFacebookRequestModel requestLogin);


        void onCompleteLogin();

    }


    public interface SocialSharingListener {

        void onNotLoginForSharing();

        void onStartSharing(TypeShare typeShare, Context context);

        void onErrorSharing(TypeShare typeShare, Context context, String message);

        void onCompleteSharing(TypeShare typeShare, Context context, String message);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public interface OnGetFBTokenListener {

        void onSuccess(String accessToken);

        void onFailed();
    }
}


