package com.gcox.fansmeet.webservice.request;

import com.gcox.fansmeet.features.login.BaseRegisterRequestModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.File;

/**
 * Created by linh on 01/11/2016.
 */

public class RegisterWithGoogleRequestModel extends BaseRegisterRequestModel {
    @SerializedName("GoogleId") @Expose
    private String mGoogleId;

    public RegisterWithGoogleRequestModel(String username,
                                          String displayname,
                                          String GoogleId,
                                          int device_type,
                                          String device_udid,
                                          String device_token,
                                          double latitude,
                                          double longitude,
                                          String address,
                                          String email,
                                          File profile,
                                          String ref_id,
                                          String Gender,
                                          String deviceName,
                                          String OSVersion,
                                          String version) {

        super(username,
                displayname,
                device_type,
                device_udid,
                device_token,
                latitude,
                longitude,
                address,
                email,
                profile,
                ref_id,
                Gender,deviceName,OSVersion,version);

        this.mGoogleId = GoogleId;

        handleAddPartData();
    }


    public String getGoogleId() {
        return mGoogleId;
    }

    public void setGoogleId(String googleId) {
        mGoogleId = googleId;
    }

    @Override
    protected void handleAddPartData() {
        super.handleAddPartData();
        addPartNotEmptyString("GoogleId", getGoogleId());
    }
}
