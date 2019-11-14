package com.gcox.fansmeet.customview.hashtag;

import com.google.gson.annotations.SerializedName;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by User on 9/28/2015.
 */
public class FollowItem extends RealmObject {
    @PrimaryKey
    @SerializedName("UserId")private String UserId;
    @SerializedName("DisplayName")private String DisplayName;
    @SerializedName("UserImage")private String UserImage;
    @SerializedName("UserName")private String UserName;

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }

    public String getProfilePic() {
        return UserImage;
    }

    public void setProfilePic(String profilePic) {
        UserImage = profilePic;
    }

}
