package com.gcox.fansmeet.pushnotification;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 10/23/2015.
 */
public class NotificationPushModel implements Parcelable {
    @SerializedName("title") @Expose
    private String mTitle;
    private String message;
    private int thanks_id;
    @SerializedName("type") @Expose
    private int type;

    private int postId;
    private String userImage;
    private String Gender;
    private int userId;
    private int RoleId;

    // follow iOS to use 'displayName'
    // don't use 'userDisplayName' for new push
    @SerializedName("userDisplayName") @Expose
    private String userDisplayName;
    @SerializedName("displayName") @Expose
    private String displayName;

    private int time_stamp;
    private boolean isRead;
    private String userName;
    private String slug;
    @SerializedName("pushImageUrl") @Expose
    private String mPushImageUrl;
    private String playUrl;
    private boolean isMaintenance;
    @SerializedName("postType") @Expose
    public int postType;

    public boolean isMaintenance() {
        return isMaintenance;
    }

    public void setMaintenance(boolean maintenance) {
        isMaintenance = maintenance;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getThanks_id() {
        return thanks_id;
    }

    public void setThanks_id(int thanks_id) {
        this.thanks_id = thanks_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRoleId() {
        return RoleId;
    }

    public void setRoleId(int roleId) {
        RoleId = roleId;
    }

    public String getUserDisplayName() {
        if (displayName != null) return displayName;
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(int time_stamp) {
        this.time_stamp = time_stamp;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public String getPushImageUrl() {
        return mPushImageUrl;
    }

    public void setPushImageUrl(String pushImageUrl) {
        mPushImageUrl = pushImageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mTitle);
        dest.writeString(this.message);
        dest.writeInt(this.thanks_id);
        dest.writeInt(this.postId);
        dest.writeString(this.userImage);
        dest.writeString(this.Gender);
        dest.writeInt(this.userId);
        dest.writeInt(this.RoleId);
        dest.writeString(this.userDisplayName);
        dest.writeInt(this.time_stamp);
        dest.writeByte(this.isRead ? (byte) 1 : (byte) 0);
        dest.writeString(this.userName);
        dest.writeString(this.slug);
        dest.writeString(this.playUrl);
        dest.writeInt(this.postType);
        dest.writeInt(this.type);
    }

    protected NotificationPushModel(Parcel in) {
        this.mTitle = in.readString();
        this.message = in.readString();
        this.thanks_id = in.readInt();
        this.postId = in.readInt();
        this.userImage = in.readString();
        this.Gender = in.readString();
        this.userId = in.readInt();
        this.RoleId = in.readInt();
        this.userDisplayName = in.readString();
        this.time_stamp = in.readInt();
        this.isRead = in.readByte() != 0;
        this.userName = in.readString();
        this.slug = in.readString();
        this.playUrl = in.readString();
        this.postType = in.readInt();
        this.type = in.readInt();
    }

    public static final Creator<NotificationPushModel> CREATOR = new Creator<NotificationPushModel>() {
        @Override
        public NotificationPushModel createFromParcel(Parcel source) {
            return new NotificationPushModel(source);
        }

        @Override
        public NotificationPushModel[] newArray(int size) {
            return new NotificationPushModel[size];
        }
    };
}
