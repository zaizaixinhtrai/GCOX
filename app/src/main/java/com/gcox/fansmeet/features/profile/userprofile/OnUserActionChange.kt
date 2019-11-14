package com.gcox.fansmeet.features.profile.userprofile

interface OnUserActionChange {
    fun onFollowChange(isFollow: Boolean, userFollowerCount: Long?)
    fun onBlockUserChange(isBlock: Boolean)
}
