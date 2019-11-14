package com.gcox.fansmeet.features.topfans;

/**
 * Created by User on 8/30/2016.
 */
public class TopFanModel {

    private int userId;
    private String userName = "";
    private String displayName = "";
    private String userImage = "";
    private String gender;
    private String userBannerImage;
    private int giftCount = 100;

    public TopFanModel(int userId, String userName, String displayName, String userImage, String userBannerImage, int giftCount) {
        this.userId = userId;
        this.userName = userName;
        this.displayName = displayName;
        this.userImage = userImage;
        this.userBannerImage = userBannerImage;
        this.giftCount = giftCount;
    }

    public String getUserBannerImage() {
        return userBannerImage;
    }

    public void setUserBannerImage(String userBannerImage) {
        this.userBannerImage = userBannerImage;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int UserId) {
        this.userId = UserId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String UserName) {
        this.userName = UserName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String DisplayName) {
        this.displayName = DisplayName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String UserImage) {
        this.userImage = UserImage;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String Gender) {
        this.gender = Gender;
    }

    public int getGiftCount() {
        return giftCount;
    }

    public void setGiftCount(int GiftReceivedCount) {
        this.giftCount = GiftReceivedCount;
    }


}
