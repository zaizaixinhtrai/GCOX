package com.gcox.fansmeet.domain.repository

import com.gcox.fansmeet.data.entity.SendGiftEntity
import com.gcox.fansmeet.models.FollowUserModel
import io.reactivex.Observable

/**
 * Created by ngoc on 5/18/18.
 */
interface UserActionRepository{
    fun sendStart(request: Int,start:Int) : Observable<SendGiftEntity>

    fun followUser(followUserId: Int):Observable<FollowUserModel>

    fun unFollowUser(followUserId: Int):Observable<FollowUserModel>

    fun likePost(postId: Int, type: Int):Observable<Int>

    fun unlike(postId: Int, type: Int):Observable<Int>

    fun blockUser(userId: Int):Observable<Boolean>

    fun reportPost(postId: Int,reason:String,type: Int):Observable<Boolean>

    fun reportEntries(postId: Int,reason:String,type: Int):Observable<Boolean>
}