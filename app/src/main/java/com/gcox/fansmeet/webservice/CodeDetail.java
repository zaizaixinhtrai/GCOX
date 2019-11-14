package com.gcox.fansmeet.webservice;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 6/23/2016.
 */
public class CodeDetail {
    @SerializedName("Code") @Expose
    private int mCode;
    @SerializedName("Message") @Expose
    private String mMessage;

    public int getCode() {
        return mCode;
    }

    public void setCode(int code) {
        mCode = code;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }
}
