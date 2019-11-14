package com.gcox.fansmeet.webservice.request;

import com.gcox.fansmeet.common.Constants;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 6/14/2016.
 */
public class BasePagingRequestModel {
    @SerializedName("NextId")
    @Expose
    private int mNextId;
    @SerializedName("Limit")
    @Expose
    private int mLimit = Constants.PAGE_LIMITED;

    public BasePagingRequestModel() {
    }

    public BasePagingRequestModel(int nextId, int limit) {
        mNextId = nextId;
        mLimit = limit;
    }

    public int getNextId() {
        return mNextId;
    }

    public void setNextId(int NextId) {
        this.mNextId = NextId;
    }

    public int getLimit() {
        return mLimit;
    }

    public void setLimit(int Limit) {
        this.mLimit = Limit;
    }
}
