package com.gcox.fansmeet.webservice.request;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 12/15/2015.
 */
public class VerifyIAPRequestModel {
    @SerializedName("DeviceType") @Expose
    private int mDeviceType;
    @SerializedName("StoreId") @Expose
    private String mStoreId;
    @SerializedName("ReceiptData") @Expose
    private String mReceiptData;
    @SerializedName("Payload") @Expose
    private String mPayload;
    @SerializedName("Signature") @Expose
    private String mSignature;

    public String orderId;

    public int getDevice_type() {
        return mDeviceType;
    }

    public void setDevice_type(int device_type) {
        this.mDeviceType = device_type;
    }

    public String getStore_id() {
        return mStoreId;
    }

    public void setStore_id(String store_id) {
        this.mStoreId = store_id;
    }

    public String getReceipt_data() {
        return mReceiptData;
    }

    public void setReceipt_data(String receipt_data) {
        this.mReceiptData = receipt_data;
    }

    public String getPayload() {
        return mPayload;
    }

    public void setPayload(String payload) {
        this.mPayload = payload;
    }

    public String getSignature() {
        return mSignature;
    }

    public void setSignature(String signature) {
        this.mSignature = signature;
    }


    @Override
    public String toString() {
        return  new Gson().toJson(this);
    }
}
