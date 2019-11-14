package com.gcox.fansmeet.webservice.request;

import com.gcox.fansmeet.features.login.BaseRegisterRequestModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.File;

/**
 * Created by ngoc on 14/12/2016.
 */

public class RegisterWithInstagramRequestModel extends BaseRegisterRequestModel {
    @SerializedName("InstagramId") @Expose
    private String mInstagramId;

    public RegisterWithInstagramRequestModel(String username,
                                             String displayname,
                                             String instagramId,
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
        super(username, displayname, device_type, device_udid, device_token, latitude, longitude, address, email, profile, ref_id, Gender,deviceName,OSVersion,version);
        this.mInstagramId = instagramId;
        handleAddPartData();
    }


    public String getInstagramId() {
        return mInstagramId;
    }

    public void setInstagramId(String instagramId) {
        mInstagramId = instagramId;
    }

    @Override
    protected void handleAddPartData() {
        super.handleAddPartData();
        addPartNotEmptyString("InstagramId", mInstagramId);
    }


}
