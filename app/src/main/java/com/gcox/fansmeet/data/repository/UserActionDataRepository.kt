package com.gcox.fansmeet.data.repository

import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.data.entity.SendGiftEntity
import com.gcox.fansmeet.data.entity.mapper.FollowUserMapper
import com.gcox.fansmeet.data.entity.mapper.UserActionEntityMapper
import com.gcox.fansmeet.data.repository.datasource.UserActionDataSource
import com.gcox.fansmeet.domain.repository.UserActionRepository
import com.gcox.fansmeet.exception.GcoxException
import com.gcox.fansmeet.models.FollowUserModel
import io.reactivex.Observable

/**
 * Created by ngoc on 5/18/18.
 */
class UserActionDataRepository constructor(@Remote private val userActionDataSource: UserActionDataSource) :
    UserActionRepository {

    private val followUserMapper by lazy { FollowUserMapper() }

    override fun reportEntries(postId: Int, reason: String, type: Int): Observable<Boolean> {
        return userActionDataSource.reportEntries(postId,reason, type).flatMap {
            when (it.code) {
                Constants.RESPONSE_FROM_WEB_SERVICE_OK -> Observable.just(it.data)
                else -> Observable.error(GcoxException(it.message, it.code))
            }
        }
    }

    override fun reportPost(postId: Int,reason:String,type: Int): Observable<Boolean> {
        return userActionDataSource.reportPost(postId,reason, type).flatMap {
            when (it.code) {
                Constants.RESPONSE_FROM_WEB_SERVICE_OK -> Observable.just(it.data)
                else -> Observable.error(GcoxException(it.message, it.code))
            }
        }
    }

    override fun blockUser(userId: Int): Observable<Boolean> {
        return userActionDataSource.blockUser(userId).flatMap {
            when (it.code) {
                Constants.RESPONSE_FROM_WEB_SERVICE_OK -> Observable.just(it.data)
                else -> Observable.error(GcoxException(it.message, it.code))
            }
        }
    }

    override fun unFollowUser(followUserId: Int): Observable<FollowUserModel> {
        return userActionDataSource.unFollowUser(followUserId).flatMap {
            when (it.code) {
                Constants.RESPONSE_FROM_WEB_SERVICE_OK -> Observable.just(followUserMapper.transform(it.data))
                else -> Observable.error(GcoxException(it.message, it.code))
            }
        }
    }

    private val userActionEntityMapper = UserActionEntityMapper()

    override fun unlike(postId: Int, type: Int): Observable<Int> {
        return userActionDataSource.unlike(postId, type).flatMap {
            when (it.code) {
                Constants.RESPONSE_FROM_WEB_SERVICE_OK -> Observable.just(it.data)
                else -> Observable.error(GcoxException(it.message, it.code))
            }
        }
    }

    override fun likePost(postId: Int, type: Int): Observable<Int> {

        return userActionDataSource.likePost(postId, type).flatMap {
            when (it.code) {
                Constants.RESPONSE_FROM_WEB_SERVICE_OK -> Observable.just(it.data)
                else -> Observable.error(GcoxException(it.message, it.code))
            }
        }
    }

    override fun followUser(followUserId: Int): Observable<FollowUserModel> {
        return userActionDataSource.followUser(followUserId).flatMap {
            when (it.code) {
                Constants.RESPONSE_FROM_WEB_SERVICE_OK -> Observable.just(followUserMapper.transform(it.data))
                else -> Observable.error(GcoxException(it.message, it.code))
            }
        }
    }

    override fun sendStart(request: Int, start: Int): Observable<SendGiftEntity> {
        return userActionDataSource.sendStart(request, start).flatMap {
            when (it.code) {
                Constants.RESPONSE_FROM_WEB_SERVICE_OK -> Observable.just(it.data)
                else -> Observable.error(GcoxException(it.message, it.code))
            }
        }
    }

}