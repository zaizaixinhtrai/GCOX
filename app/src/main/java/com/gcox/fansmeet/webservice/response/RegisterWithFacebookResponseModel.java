package com.gcox.fansmeet.webservice.response;

import com.gcox.fansmeet.models.UserModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 2/25/2016.
 */
public class RegisterWithFacebookResponseModel {

    @SerializedName("IsXmppCreated") @Expose
    private boolean mIsXmppCreated;
    @SerializedName("UserInfo") @Expose
    private UserModel mUserInfo;
    @SerializedName("Expires") @Expose
    private String mExpires;
    @SerializedName("RefreshToken") @Expose
    private String mRefreshToken;
    @SerializedName("AccessToken") @Expose
    private String mAccessToken;
    @SerializedName("RegisterType") @Expose
    private int mRegisterType;

    public boolean isXmppCreated() {
        return mIsXmppCreated;
    }

    public void setIsXmppCreated(boolean isXmppCreated) {
        this.mIsXmppCreated = isXmppCreated;
    }


    public UserModel getUserInfo() {
        return mUserInfo;
    }

    public void setUserInfo(UserModel userInfo) {
        mUserInfo = userInfo;
    }

    public String getExpires() {
        return mExpires;
    }

    public void setExpires(String expires) {
        this.mExpires = expires;
    }

    public String getRefresh_token() {
        return mRefreshToken;
    }

    public void setRefresh_token(String refresh_token) {
        this.mRefreshToken = refresh_token;
    }

    public String getAccess_token() {
        return mAccessToken;
    }

    public void setAccess_token(String access_token) {
        this.mAccessToken = access_token;
    }

    public int getRegisterType() {
        return mRegisterType;
    }
}
