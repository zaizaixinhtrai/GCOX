package com.gcox.fansmeet.features.profile.userprofile;

/**
 * Created by User on 9/22/2015.
 */
public interface OnUserProfileChangeView {
    void onChangeToListView();
    void onChangeToGridView();
    void onChangeToGift();
    void onViewMoreClick();
    void onFollowClicked(boolean isFollow);
    void onTopFansImageClicked(int userId,String userName);
    void onFollowingClicked();
    void onFollowerClicked();
}