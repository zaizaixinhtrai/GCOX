package com.gcox.fansmeet.webservice.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ThanhBan on 10/3/2016.
 */

public class VersionResponseModel {

    @SerializedName("IsForceUpdate")
    @Expose
    private boolean mIsForceUpdate;
    @SerializedName("ForceUpdateMessage")
    @Expose
    private String mMessage;
    @SerializedName("AndroidVersion")
    public String androidVersion;
    @SerializedName("GemsForReferrer")
    @Expose
    public int gemsForReferrer;

    public boolean getForceUpdate() {
        return mIsForceUpdate;
    }

    public String getMessage() {
        return mMessage;
    }

}
