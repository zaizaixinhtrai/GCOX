package com.gcox.fansmeet.data.entity

import com.google.gson.annotations.SerializedName

class DeleteCommentEntity (@field:SerializedName("CommentCount")
                           val commentCount: Int? = 0)