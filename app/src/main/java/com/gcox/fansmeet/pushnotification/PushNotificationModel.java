package com.gcox.fansmeet.pushnotification;

import android.os.Parcel;
import android.os.Parcelable;

public class PushNotificationModel implements Parcelable {

    private String mTitle;
    private String message;
    private int thanks_id;
    private int notification_type;
    private int pushNotificationId;
    private int PostId;
    private String ProfilePic;
    private String mPushImageUrl;
    private String Gender;
    private int UserId;
    private int RoleId;
    private String DisplayName;
    private int time_stamp;
    private boolean isRead;

    private int profile_id;
    private String slug;
    private String playUrl;
    private String userName;

    private String ActionUserId;
    private String ActionUsername;
    private String ActionUserDisplayName;
    private String ActionUserProfilePic;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getUsername() {
        return userName;
    }

    public void setUsername(String username) {
        this.userName = username;
    }

    public String getActionUserId() {
        return ActionUserId;
    }

    public void setActionUserId(String actionUserId) {
        ActionUserId = actionUserId;
    }

    public String getActionUsername() {
        return ActionUsername;
    }

    public void setActionUsername(String actionUsername) {
        ActionUsername = actionUsername;
    }

    public String getActionUserDisplayName() {
        return ActionUserDisplayName;
    }

    public void setActionUserDisplayName(String actionUserDisplayName) {
        ActionUserDisplayName = actionUserDisplayName;
    }

    public String getActionUserProfilePic() {
        return ActionUserProfilePic;
    }

    public void setActionUserProfilePic(String actionUserProfilePic) {
        ActionUserProfilePic = actionUserProfilePic;
    }

    public int getProfile_id() {
        return profile_id;
    }

    public void setProfile_id(int profile_id) {
        this.profile_id = profile_id;
    }


    public void setMessage(String message) {
        this.message = message;
    }

    public void setThanks_id(int thanks_id) {
        this.thanks_id = thanks_id;
    }

    public void setNotification_type(int notification_type) {
        this.notification_type = notification_type;
    }

    public void setPushNotificationId(int pushNotificationId) {
        this.pushNotificationId = pushNotificationId;
    }

    public void setPostId(int PostId) {
        this.PostId = PostId;
    }

    public void setProfilePic(String ProfilePic) {
        this.ProfilePic = ProfilePic;
    }

    public void setGender(String Gender) {
        this.Gender = Gender;
    }

    public void setUserId(int UserId) {
        this.UserId = UserId;
    }

    public void setRoleId(int RoleId) {
        this.RoleId = RoleId;
    }

    public void setDisplayName(String DisplayName) {
        this.DisplayName = DisplayName;
    }

    public void setTime_stamp(int time_stamp) {
        this.time_stamp = time_stamp;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public String getMessage() {
        return message;
    }

    public int getThanks_id() {
        return thanks_id;
    }

    public int getNotification_type() {
        return notification_type;
    }

    public int getPushNotificationId() {
        return pushNotificationId;
    }

    public int getPostId() {
        return PostId;
    }

    public String getProfilePic() {
        return ProfilePic;
    }

    public String getGender() {
        return Gender;
    }

    public int getUserId() {
        return UserId;
    }

    public int getRoleId() {
        return RoleId;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public int getTime_stamp() {
        return time_stamp;
    }

    public boolean getIsRead() {
        return isRead;
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

    public PushNotificationModel() {
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
        dest.writeInt(this.notification_type);
        dest.writeInt(this.pushNotificationId);
        dest.writeInt(this.PostId);
        dest.writeString(this.ProfilePic);
        dest.writeString(this.Gender);
        dest.writeInt(this.UserId);
        dest.writeInt(this.RoleId);
        dest.writeString(this.DisplayName);
        dest.writeInt(this.time_stamp);
        dest.writeByte(this.isRead ? (byte) 1 : (byte) 0);
        dest.writeInt(this.profile_id);
        dest.writeString(this.slug);
        dest.writeString(this.playUrl);
        dest.writeString(this.userName);
        dest.writeString(this.ActionUserId);
        dest.writeString(this.ActionUsername);
        dest.writeString(this.ActionUserDisplayName);
        dest.writeString(this.ActionUserProfilePic);
        dest.writeString(this.mPushImageUrl);
    }

    protected PushNotificationModel(Parcel in) {
        this.mTitle = in.readString();
        this.message = in.readString();
        this.thanks_id = in.readInt();
        this.notification_type = in.readInt();
        this.pushNotificationId = in.readInt();
        this.PostId = in.readInt();
        this.ProfilePic = in.readString();
        this.Gender = in.readString();
        this.UserId = in.readInt();
        this.RoleId = in.readInt();
        this.DisplayName = in.readString();
        this.time_stamp = in.readInt();
        this.isRead = in.readByte() != 0;
        this.profile_id = in.readInt();
        this.slug = in.readString();
        this.playUrl = in.readString();
        this.userName = in.readString();
        this.ActionUserId = in.readString();
        this.ActionUsername = in.readString();
        this.ActionUserDisplayName = in.readString();
        this.ActionUserProfilePic = in.readString();
        this.mPushImageUrl = in.readString();
    }

    public static final Creator<PushNotificationModel> CREATOR = new Creator<PushNotificationModel>() {
        @Override
        public PushNotificationModel createFromParcel(Parcel source) {
            return new PushNotificationModel(source);
        }

        @Override
        public PushNotificationModel[] newArray(int size) {
            return new PushNotificationModel[size];
        }
    };
}
