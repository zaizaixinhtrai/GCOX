package com.gcox.fansmeet.webservice.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by User on 6/14/2016.
 */
public class BaseDataPagingResponseModel<T> {

    @SerializedName("NextId") @Expose
    private int mNextId;
    @SerializedName("IsEnd") @Expose
    private boolean mIsEnd;
    @SerializedName("TotalRecords") @Expose
    private int mTotalRecords;
    @SerializedName("Result") @Expose
    private List<T> mResult;

    public List<T> getResult() {
        return mResult;
    }

    public void setResult(List<T> result) {
        mResult = result;
    }

    public int getNextId() {
        return mNextId;
    }

    public void setNextId(int nextId) {
        mNextId = nextId;
    }

    public boolean isEnd() {
        return mIsEnd;
    }

    public void setEnd(boolean end) {
        mIsEnd = end;
    }

    public int getTotalRecords() {
        return mTotalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        mTotalRecords = totalRecords;
    }
}
