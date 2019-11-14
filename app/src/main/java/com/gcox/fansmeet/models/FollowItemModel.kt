package com.gcox.fansmeet.models

/**
 * Created by User on 9/28/2015.
 */
 class FollowItemModel (
     val userId: Int? = 0,
     val displayName: String? = null,
     val userImage: String? = null,
     val gender: String? = null,
     var isFollow: Boolean? ,
     val userName: String? = null,
     val about:String?,
     val description:String?,
     val bannerImage:String?,
     val type:Int?

)
