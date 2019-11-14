package com.gcox.fansmeet.data.repository.datasource

import com.gcox.fansmeet.data.entity.FollowUserEntity
import com.gcox.fansmeet.data.entity.SendGiftEntity
import com.gcox.fansmeet.models.FollowUserModel
import com.gcox.fansmeet.webservice.response.BaseResponse
import io.reactivex.Observable

/**
 * Created by ngoc on 5/18/18.
 */
interface UserActionDataSource {

    fun sendStart(request: Int, start: Int): Observable<BaseResponse<SendGiftEntity>>

    fun followUser(followUserId: Int): Observable<BaseResponse<FollowUserEntity>>

    fun unFollowUser(followUserId: Int): Observable<BaseResponse<FollowUserEntity>>

    fun likePost(postId: Int, type: Int): Observable<BaseResponse<Int>>

    fun unlike(postId: Int, type: Int): Observable<BaseResponse<Int>>

    fun blockUser(userId: Int): Observable<BaseResponse<Boolean>>

    fun reportPost(postId: Int, reason: String, type: Int): Observable<BaseResponse<Boolean>>

    fun reportEntries(postId: Int, reason: String, type: Int): Observable<BaseResponse<Boolean>>
}