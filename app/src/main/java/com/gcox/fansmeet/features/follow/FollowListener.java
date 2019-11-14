package com.gcox.fansmeet.features.follow;

/**
 * Created by User on 11/16/2015.
 */
public interface FollowListener {
    void onFollowClicked(int position, int userId, boolean isFollowing);

    void onUserImageClicked(int userId, String userName);
}
