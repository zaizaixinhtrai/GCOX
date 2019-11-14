package com.gcox.fansmeet.webservice.response;

import com.gcox.fansmeet.webservice.CodeDetail;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by User on 3/2/2016.
 */
public class BaseResponse<T> {

    @SerializedName("Code")
    private int mCode;
    @SerializedName("Message")
    private String mMessage;
    @SerializedName("CodeDetails")
    private List<CodeDetail> mCodeDetails;
    @SerializedName("Data")
    private T mData;

    public T getData() {
        return mData;
    }

    public void setData(T data) {
        mData = data;
    }

    public List<CodeDetail> getCodeDetails() {
        return mCodeDetails;
    }

    public void setCodeDetails(List<CodeDetail> codeDetails) {
        mCodeDetails = codeDetails;
    }

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

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
