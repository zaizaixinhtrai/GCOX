package com.gcox.fansmeet.webservice.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 9/29/2015.
 */

public class EditProfileResponseModel {
    @SerializedName("UserInfo") @Expose
    private SettingResponse mUserInfo;
    @SerializedName("IsNewEmail") @Expose
    private boolean mIsNewEmail;
    @SerializedName("IsSendVerificationEmail") @Expose
    private boolean mIsSendVerificationEmail;

    public boolean isNewEmail() {
        return mIsNewEmail;
    }

    public void setNewEmail(boolean newEmail) {
        mIsNewEmail = newEmail;
    }

    public boolean isSendVerificationEmail() {
        return mIsSendVerificationEmail;
    }

    public void setSendVerificationEmail(boolean sendVerificationEmail) {
        mIsSendVerificationEmail = sendVerificationEmail;
    }

    public SettingResponse getUserInfo() {
        return mUserInfo;
    }

    public void setUserInfo(SettingResponse userInfo) {
        mUserInfo = userInfo;
    }

}
