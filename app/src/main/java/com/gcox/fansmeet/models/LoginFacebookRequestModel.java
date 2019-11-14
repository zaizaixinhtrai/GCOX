package com.gcox.fansmeet.models;


import com.gcox.fansmeet.webservice.request.BaseLoginRequestModel;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 9/24/2015.
 */
public class LoginFacebookRequestModel extends BaseLoginRequestModel {
    @SerializedName("FbId")
    private String mFbId;

    public String getFb_id() {
        return mFbId;
    }

    public void setFb_id(String fb_id) {
        this.mFbId = fb_id;
    }
}
