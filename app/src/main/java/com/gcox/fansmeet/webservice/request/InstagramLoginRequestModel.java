package com.gcox.fansmeet.webservice.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by linh on 13/12/2016.
 */

public class InstagramLoginRequestModel extends BaseLoginRequestModel {
    @SerializedName("InstagramId")
    private String mInstagramId;

    public String getInstagramId() {
        return mInstagramId;
    }

    public void setInstagramId(String instagramId) {
        mInstagramId = instagramId;
    }
}
