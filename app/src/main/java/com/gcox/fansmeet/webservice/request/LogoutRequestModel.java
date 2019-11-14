package com.gcox.fansmeet.webservice.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 10/13/2015.
 */
public class LogoutRequestModel {
    @SerializedName("DeviceToken") @Expose
    private String mDeviceToken;

    public String getDevice_token() {
        return mDeviceToken;
    }

    public void setDevice_token(String device_token) {
        this.mDeviceToken = device_token;
    }
}
