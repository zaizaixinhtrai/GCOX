package com.gcox.fansmeet.webservice.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 10/20/2015.
 */
public class SendGiftRequestModel {
    @SerializedName("GiftId")
    @Expose
    private int giftId;
    @SerializedName("ReceiverUserId")
    @Expose
    private int receiverUserId;
    @SerializedName("StreamId")
    @Expose
    private int streamId;
    @SerializedName("UsingGiftInInventory")
    @Expose
    private boolean usingGiftInInventory;
    public void setGift_id(int gift_id) {
        this.giftId = gift_id;
    }


    public void setReceiver_user_id(int receiver_user_id) {
        this.receiverUserId = receiver_user_id;
    }

    public void setStream_id(int stream_id) {
        this.streamId = stream_id;
    }

    public void setUsingGiftInInventory(boolean usingGiftInInventory) {
        this.usingGiftInInventory = usingGiftInInventory;
    }
}
