package com.gcox.fansmeet.webservice.request;

import com.gcox.fansmeet.features.login.BaseRegisterRequestModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.File;

/**
 * Created by User on 2/25/2016.
 */
public class RegisterWithFacebookRequestModel extends BaseRegisterRequestModel {
    @SerializedName("FbId") @Expose
    private String mFbId;

    public RegisterWithFacebookRequestModel(String username,
                                            String displayname,
                                            String fb_id,
                                            int device_type,
                                            String device_udid,
                                            String device_token,
                                            double latitude,
                                            double longitude,
                                            String address,
                                            String email,
                                            File profile,
                                            String ref_id,
                                            String Gender, String deviceName, String OSVersion, String version) {
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

        this.mFbId = fb_id;

        handleAddPartData();
    }

    public String getFbId() {
        return mFbId;
    }

    public void setFbId(String fbId) {
        mFbId = fbId;
    }

    @Override
    protected void handleAddPartData() {
        super.handleAddPartData();
        addPartNotEmptyString("FbId", getFbId());
    }
}
