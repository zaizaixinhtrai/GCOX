package com.gcox.fansmeet.webservice.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by USER on 10/8/2015.
 */
public class NotificationRequestModel extends BasePagingRequestModel{
    @SerializedName("NotificationStatus") @Expose
    private int mNotificationStatus;

    public void setNotification_status(int notification_status) {
        this.mNotificationStatus = notification_status;
    }

    public int getNotification_status() {
        return mNotificationStatus;
    }

}
