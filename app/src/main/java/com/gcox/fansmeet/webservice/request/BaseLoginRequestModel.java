package com.gcox.fansmeet.webservice.request;

import com.gcox.fansmeet.common.Constants;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by linh on 01/11/2016.
 */

public class BaseLoginRequestModel {
    @SerializedName("UserName") @Expose
    private String mUserName;
    @SerializedName("Email") @Expose
    private String mEmail;
    @SerializedName("DisplayName") @Expose
    private String mDisplayName;
    @SerializedName("Latitude") @Expose
    private double mLatitude;
    @SerializedName("Longitude") @Expose
    private double mLongitude;
    @SerializedName("DeviceUdid") @Expose
    private String mDeviceUdid;
    @SerializedName("DeviceType") @Expose
    private int mDeviceType = Constants.ANDROID_DEVICE_TYPE;
    @SerializedName("Profile_Pic") @Expose
    private String mProfile_Pic;
    @SerializedName("Gender") @Expose
    private String mGender;
    @SerializedName("DeviceToken") @Expose
    private String mDeviceToken;

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public int getDevice_type() {
        return mDeviceType;
    }

    public void setDeviceType(int deviceType) {
        mDeviceType = deviceType;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        this.mEmail = email;
    }

    public String getDisplay_name() {
        return mDisplayName;
    }

    public void setDisplay_name(String display_name) {
        this.mDisplayName = display_name;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        this.mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        this.mLongitude = longitude;
    }


    public String getDevice_udid() {
        return mDeviceUdid;
    }

    public void setDevice_udid(String device_udid) {
        this.mDeviceUdid = device_udid;
    }

    public String getProfile_Pic() {
        return mProfile_Pic;
    }

    public void setProfile_Pic(String profile_Pic) {
        this.mProfile_Pic = profile_Pic;
    }

    public String getGender() {
        return mGender;
    }

    public void setGender(String gender) {
        this.mGender = gender;
    }

    public String getDevice_token() {
        return mDeviceToken;
    }

    public void setDevice_token(String device_token) {
        this.mDeviceToken = device_token;
    }
}
