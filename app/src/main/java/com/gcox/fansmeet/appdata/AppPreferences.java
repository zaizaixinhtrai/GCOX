package com.gcox.fansmeet.appdata;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.gcox.fansmeet.models.UserModel;
import com.gcox.fansmeet.webservice.request.VerifyIAPRequestModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import timber.log.Timber;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppPreferences {
    private SharedPreferences pref;
    private final String APP_PREF_USER_PASSWORD_KEY = "user_password";
    private final String APP_PREF_USER_ID_KEY = "user_id";
    private final String APP_PREF_APP_LANGUAGE_KEY = "language";
    private final String APP_PREF_HAS_SHOW_BOARDING = "has_show_broarding";
    private final String APP_PREF_LANGUAGE_TYPE = "language_type";
    private final String APP_PREF_DISPLAY_NAME_KEY = "display_name";

    private final String APP_PREF_USER_KEY = "user_model";
    private final String APP_PREF_USER_TOKEN = "user_token";
    private final String APP_PREF_DEVICE_ID = "Device_ID";
    private final String APP_PREF_DEVICE_UDID = "Device_UDID";
    private final String APP_FACEBOOK_LOGIN = "login";

    private final String NUMBER_NOTIFICATION = "NUMBER_NOTIFICATION";
    private final String NUMBER_MESSAGE_UNREAD = "NUMBER_MESSAGE_UNREAD";
    private final String LIVE_STREAMS_API_TOKEN = "api_token";
    private final String FACEBOOK_DISPLAY_NAME = "FACEBOOK_DISPLAY_NAME";
    private final String APP_PREF_GOTO_SETTING_LOCATION = "APP_PREF_GOTO_SETTING_LOCATION";

    private final String APP_PREF_MUTE_UN_MUTE = "APP_PREF_MUTE_UNMUTE";
    private final String APP_PREF_FLAG_CHANGE_LANGUAGE = "FLAG_CHANGE_LANGUAGE";
    private final String APP_CURRENT_ACTIVE_MENU = "APP_CURRENT_ACTIVE_MENU";

    private final String APP_LIST_ID_USER_SEND_NEW_MESSAGES = "APP_LIST_ID_USER_SEND_NEW_MESSAGES";
    private final String APP_PREF_STREAM_NOTIFY = "stream_notify";
    private final String APP_PREF_CURRENT_PAGER_ON_HOME = "current_pager_on_home";
    private final String APP_PREF_SCREEN_WIDTH = "SCREEN_WIDTH";
    private final String USER_LOGIN_TYPE = "LOGIN_TYPE";
    private final String USER_FLAG_NEWLY_USER = "USER_FLAG_NEWLY_USER";
    private final String USER_COUNTRY_CODE = "USER_COUNTRY_CODE";
    private final String APP_VIDEO_FRAME_RATE = "APP_VIDEO_FRAME_RATE";
    private final String APP_IMAGE_CACHED = "APP_IMAGE_CACHED";
    private final static String REFERRAL_ID = "REFERRAL_ID";
    private final String APP_CURRENT_VIEW_STREAM = "APP_CURRENT_VIEW_STREAM";
    final String PREF_VERSION_CODE_KEY = "version_code";
    private final String APP_MAINTENANCE_KEY = "maintenance_key";
    private final String REMOTE_APP_CONFIGURATION = "REMOTE_APP_CONFIGURATION";

    private final String PLAYTOKEN_USERNAME = "PLAYTOKEN_USERNAME";
    private final String APP_NEED_REFRESH_GRID_AND_LIST_ON_ME = "need_refresh_grid_and_list_on_me";
    private final String APP_PREF_NEW_POST = "new_post_from_following";
    private final String APP_PREF_NOT_NEED_REFRESH_HOME = "not_need_refresh_home";
    private final String APP_PREF_SHARE_STREAM_MODEL = "share_stream_model";
    private final String APP_PREF_IAP_MODEL = "in_app_purcher_model";
    private final String APP_PREF_ORDER_LIST = "order_id_list";
    private final String APP_PREF_PRIZE_BOX_FIRST_TYPE = "box_first_type";

    Gson mGson;

    private static AppPreferences instance;
    private static UserModel mCacheUser;

    public AppPreferences(Context context) {
        pref = context
                .getSharedPreferences("MyPrefLogin", Context.MODE_PRIVATE);
        mGson = new Gson();
    }

    public static AppPreferences getInstance(Context mContext) {
        if (instance == null) {
            instance = new AppPreferences(mContext);
        }
        return instance;
    }

    public SharedPreferences getSharedPreferences() {
        return pref;
    }

    public UserModel getUserModel() {
        if (mCacheUser == null) {
            mCacheUser = getJson(APP_PREF_USER_KEY, UserModel.class);
        }
        return mCacheUser;

    }

    public int getFirstTypeOfBox() {
        return pref.getInt(APP_PREF_PRIZE_BOX_FIRST_TYPE, 1);
    }

    public void setFirstTypeOfBox(int type) {
        Editor editor = pref.edit();
        editor.putInt(APP_PREF_PRIZE_BOX_FIRST_TYPE, type);
        editor.apply();
    }

    public boolean getIsNewPostFromFollowingUsers() {
        return pref.getBoolean(APP_PREF_NEW_POST, false);
    }

    public void setIsIsNewPostFromFollowingUsers(boolean isNeed) {
        Editor editor = pref.edit();
        editor.putBoolean(APP_PREF_NEW_POST, isNeed);
        editor.apply();
    }

    public boolean getIsRefreshGridAndList() {
        return pref.getBoolean(APP_NEED_REFRESH_GRID_AND_LIST_ON_ME, false);
    }

    public void setIsRefreshGridAndList(boolean isNeed) {
        Editor editor = pref.edit();
        editor.putBoolean(APP_NEED_REFRESH_GRID_AND_LIST_ON_ME, isNeed);
        editor.apply();
    }

    public void saveCurrentVersionCode(int currentVersionCode) {
        Editor editor = pref.edit();
        editor.putInt(PREF_VERSION_CODE_KEY, currentVersionCode);
        editor.apply();
    }

    public int getCurrentVersionCode() {
        return pref.getInt(PREF_VERSION_CODE_KEY, -1);
    }

    public ArrayList<String> getListUserNameSendNewMessages() {

        Gson gson = new Gson();
        String json = pref.getString(APP_LIST_ID_USER_SEND_NEW_MESSAGES, null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();

        return gson.fromJson(json, type);
    }

    public void saveListUserNameSendNewMessage(List<String> arrayUserId) {
        Gson gson = new Gson();
        String json = gson.toJson(arrayUserId);
        Editor editor = pref.edit();
        editor.putString(APP_LIST_ID_USER_SEND_NEW_MESSAGES, json);
        editor.apply();
    }


    public void deleteNewMessageList() {
        saveListUserNameSendNewMessage(Collections.emptyList());
    }

    public void setLoginFacebook(boolean isLogin) {
        Editor editor = pref.edit();
        editor.putBoolean(APP_FACEBOOK_LOGIN, isLogin);
        editor.commit();

    }

    public boolean isLoginFacebook() {
        return pref.getBoolean(APP_FACEBOOK_LOGIN, false);
    }

    public String getDisplayName() {
        return pref.getString(APP_PREF_DISPLAY_NAME_KEY, "");
    }

    public int getLanguageType() {
        return pref.getInt(APP_PREF_LANGUAGE_TYPE, 0);
    }

    public void setLanguageType(int language_type) {
        Editor editor = pref.edit();
        editor.putInt(APP_PREF_LANGUAGE_TYPE, language_type);
        editor.apply();
    }

    public boolean getHasShowBoarding() {
        return pref.getBoolean(APP_PREF_HAS_SHOW_BOARDING, false);
    }

    public void setHasShowBoarding(boolean hasShow) {
        Editor editor = pref.edit();
        editor.putBoolean(APP_PREF_HAS_SHOW_BOARDING, hasShow);
        editor.apply();
    }

    public String getAppLanguage() {
        return pref.getString(APP_PREF_APP_LANGUAGE_KEY, "en");
    }

    public void setAppLanguage(String language) {
        Editor editor = pref.edit();
        editor.putString(APP_PREF_APP_LANGUAGE_KEY, language);
        editor.apply();
    }


    public int getUserLoginType() {
        return pref.getInt(USER_LOGIN_TYPE, -1);
    }

    public void setUserLoginType(int type) {
        Editor editor = pref.edit();
        editor.putInt(USER_LOGIN_TYPE, type);
        editor.apply();
    }

    public String getUserCountryCode() {
        return pref.getString(USER_COUNTRY_CODE, "");
    }

    public void setUserCountryCode(String countryCode) {
        Editor editor = pref.edit();
        editor.putString(USER_COUNTRY_CODE, countryCode);
        editor.commit();
    }

    public String getUsePassword() {
        return pref.getString(APP_PREF_USER_PASSWORD_KEY, "");
    }

    public void setUsePassword(String userName) {
        Editor editor = pref.edit();
        editor.putString(APP_PREF_USER_PASSWORD_KEY, userName);
        editor.apply();
    }


    public boolean isGoToSettingLocation() {
        return pref.getBoolean(APP_PREF_GOTO_SETTING_LOCATION, true);
    }

    public void setGoToSettingLocation(boolean isSetting) {
        Editor editor = pref.edit();
        editor.putBoolean(APP_PREF_GOTO_SETTING_LOCATION, isSetting);
        editor.apply();
    }

    public void clearAllParamLogin() {
        int typeLanguage = getLanguageType();
        String language = getAppLanguage();
        pref.edit().clear().apply();

        // reset data
        setLanguageType(typeLanguage);
        setAppLanguage(language);
        setHasShowBoarding(true);
        // clear data
        mCacheUser = null;
    }

    private <T> T getJson(String key, Class<T> cls) {
        String json = pref.getString(key, null);
        if (json == null) {
            return null;
        }
        return mGson.fromJson(json, cls);
    }

    private void saveModelToJson(String key, Object o) {
        if (o != null) {
            Editor editor = pref.edit();
            editor.remove(key);
            editor.putString(key, mGson.toJson(o));
            editor.apply();
        }
    }

    public String getUserToken() {
        return pref.getString(APP_PREF_USER_TOKEN, "");
    }

    public String getUserTokenRequest() {
        return "Bearer " + pref.getString(APP_PREF_USER_TOKEN, "");
    }

    public void saveUserToken(String token) {
        Editor editor = pref.edit();
        editor.putString(APP_PREF_USER_TOKEN, token);
        editor.apply();
    }

    public String getDevicesToken() {
        return pref.getString(APP_PREF_DEVICE_ID, "");
    }

    public void saveDevicesToken(String deviceID) {
        Editor editor = pref.edit();
        editor.putString(APP_PREF_DEVICE_ID, deviceID);
        editor.apply();
    }

    public String getDevicesUDID() {
        return pref.getString(APP_PREF_DEVICE_UDID, "");
    }

    public void setDevicesUDID(String deviceID) {
        Editor editor = pref.edit();
        editor.putString(APP_PREF_DEVICE_UDID, deviceID);
        editor.apply();
    }

    public void setStringPreferenceData(String key, String value) {
        Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getStringPreference(String key, String defaultValue) {
        return pref.getString(key, defaultValue);

    }

    public void setIntPreferenceData(String key, int value) {
        Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.apply();

    }

    public void setBooleanPreferenceData(String key, boolean value) {
        Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBooleanPreference(String key, boolean defaultValue) {
        return pref.getBoolean(key, defaultValue);
    }

    public int getIntPreference(String key, int defaultValue) {
        return pref.getInt(key, defaultValue);
    }

    public void setLongPreferenceData(String key, long value) {
        Editor editor = pref.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public long getLongPreference(String key, long defaultValue) {
        return pref.getLong(key, defaultValue);
    }

    public String getLiveStreamAPIToken() {
        return pref.getString(LIVE_STREAMS_API_TOKEN, "");
    }

    public void setLiveStreamAPIToken(String token) {
        Editor editor = pref.edit();
        editor.putString(LIVE_STREAMS_API_TOKEN, token);
        editor.apply();
    }

    public int getNumberUnreadMessage() {
        return pref.getInt(NUMBER_MESSAGE_UNREAD, 0);
    }

    public void setNumberUnreadMessage(int numberUnread) {
        Editor editor = pref.edit();
        editor.putInt(NUMBER_MESSAGE_UNREAD, numberUnread);
        editor.apply();
    }

    public int getNumberUnreadNotification() {
        return pref.getInt(NUMBER_NOTIFICATION, 0);
    }

    public void setNumberUnreadNotification(int numberUnread) {
        Editor editor = pref.edit();
        editor.putInt(NUMBER_NOTIFICATION, numberUnread);
        editor.apply();
    }

    public String getFacebookDisplayName() {
        return pref.getString(FACEBOOK_DISPLAY_NAME, "");
    }

    public void setFacebookDisplayName(String fbDisplayName) {
        Editor editor = pref.edit();
        editor.putString(FACEBOOK_DISPLAY_NAME, fbDisplayName);
        editor.apply();
    }

    public boolean getIsMuteVideos() {
        return pref.getBoolean(APP_PREF_MUTE_UN_MUTE, true);
    }

    public void setIsMuteVideos(boolean isFirst) {
        Editor editor = pref.edit();
        editor.putBoolean(APP_PREF_MUTE_UN_MUTE, isFirst);
        editor.apply();
    }

    public boolean getIsChangeLanguage() {
        return pref.getBoolean(APP_PREF_FLAG_CHANGE_LANGUAGE, false);
    }

    public void setChangeLanguage(boolean isFirst) {
        Editor editor = pref.edit();
        editor.putBoolean(APP_PREF_FLAG_CHANGE_LANGUAGE, isFirst);
    }

    public int getCurrentStateMenuItem() {
        return pref.getInt(APP_CURRENT_ACTIVE_MENU, 0);
    }

    public void setCurrentStateMenuItem(int idMenuItem) {
        Editor editor = pref.edit();
        editor.putInt(APP_CURRENT_ACTIVE_MENU, idMenuItem);
        editor.apply();
    }

    public int getCurrentTagOnHome() {
        return pref.getInt(APP_PREF_CURRENT_PAGER_ON_HOME, 0);
    }

    public void setCurrentTagOnHome(int tag) {
        Editor editor = pref.edit();
        editor.putInt(APP_PREF_CURRENT_PAGER_ON_HOME, tag);
        editor.apply();
    }

    public void setScreenWidth(int screenWidth) {
        Editor editor = pref.edit();
        editor.putInt(APP_PREF_SCREEN_WIDTH, screenWidth);
        editor.apply();
    }

    public int getScreenWidth() {
        return pref.getInt(APP_PREF_SCREEN_WIDTH, 0);
    }

    public boolean getFlagNewlyUser() {
        return pref.getBoolean(USER_FLAG_NEWLY_USER, false);
    }

    public void setFlagNewlyUser(boolean isNewly) {
        Editor editor = pref.edit();
        editor.putBoolean(USER_FLAG_NEWLY_USER, isNewly);
        editor.apply();
    }

    public void setCachedTime(long time) {
        Editor editor = pref.edit();
        editor.putLong(APP_IMAGE_CACHED, time);
        editor.apply();
    }

    public long getCachedTime() {
        return pref.getLong(APP_IMAGE_CACHED, 1);
    }

    public void setReferralId(String referralId) {
        pref.edit().putString(REFERRAL_ID, referralId).apply();
    }

    public String getReferralId() {
        return pref.getString(REFERRAL_ID, "");
    }

    public void setPlayTokenUserName(String username) {
        if (username != null) {
            Editor editor = pref.edit();
            editor.putString(PLAYTOKEN_USERNAME, username);
            editor.apply();
        } else {
            Editor editor = pref.edit();
            editor.remove(PLAYTOKEN_USERNAME);
            editor.apply();
        }
    }

    public String getPlayTokenUserName() {
        return pref.getString(PLAYTOKEN_USERNAME, null);
    }

    public boolean isUserLogin() {
        return getUserModel() != null;
    }

    public void saveUserInforModel(UserModel userInforModel) {
        mCacheUser = userInforModel;
        saveModelToJson(APP_PREF_USER_KEY, userInforModel);
    }

    public void saveOrderObjectToList(VerifyIAPRequestModel orderId) {

        ArrayList<VerifyIAPRequestModel> orderIds = loadOrderObjectToList();
        if (orderIds != null) {
            if (!orderIds.contains(orderId)) orderIds.add(orderId);
        }

        Editor editor = pref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(orderIds);
        editor.putString(APP_PREF_ORDER_LIST, json);
        editor.apply();
    }

    public void removeOrderObjectToList(VerifyIAPRequestModel orderId) {

        Timber.e("removeOrderIdToList");

        ArrayList<VerifyIAPRequestModel> orderIds = loadOrderObjectToList();
        if (orderIds != null) {
            orderIds.remove(orderId);
        }

        Editor editor = pref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(orderIds);
        editor.putString(APP_PREF_ORDER_LIST, json);
        editor.apply();
    }

    public ArrayList<VerifyIAPRequestModel> loadOrderObjectToList() {
        ArrayList<VerifyIAPRequestModel> callLog;
        Gson gson = new Gson();
        String json = pref.getString(APP_PREF_ORDER_LIST, "");
        if (json == null || json.isEmpty()) {
            callLog = new ArrayList<>();
        } else {
            Type type = new TypeToken<List<VerifyIAPRequestModel>>() {
            }.getType();
            callLog = gson.fromJson(json, type);
        }
        return callLog;
    }
}
