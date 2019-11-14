package com.gcox.fansmeet.features.rewards.models

class RewardListResponse(
    val listBox: List<RewardItem>?,
    val nextId: Int,
    val isEnded: Boolean
)