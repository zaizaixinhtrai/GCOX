package com.gcox.fansmeet.webservice.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 12/16/2015.
 */

public class VerifyIAPResponeModel {
    @SerializedName("Gems")
    @Expose
    private long mTotalBean;

    @SerializedName("TransactionId")
    @Expose
    private String mTransactionId;

    @SerializedName("PurchaseTime")
    @Expose
    private long mPurchaseTime;

    public long getTotalBeanIncrease() {
        return mTotalBean;
    }

    public String getTransactionId() {
        return mTransactionId;
    }

    public long getPurchaseTime() {
        return mPurchaseTime;
    }
}
