package com.gcox.fansmeet.domain.repository

import com.gcox.fansmeet.features.prizelist.models.BoxesModel
import com.gcox.fansmeet.features.prizelist.models.Prize
import com.gcox.fansmeet.features.prizelist.models.PrizeResponse
import com.gcox.fansmeet.features.rewards.models.CelebrityBoxesModel
import com.gcox.fansmeet.features.rewards.models.PrizeCollectModel
import com.gcox.fansmeet.features.rewards.models.RewardListResponse
import io.reactivex.Observable

/**
 * Created by ngoc on 5/18/18.
 */
interface RewardRepository {
    fun getRewardList(nextId: Int, limit: Int): Observable<RewardListResponse>

    fun getPrizeList(type: Int, nextId: Int, limit: Int): Observable<PrizeResponse>

    fun getBoxesList(ownerId: Int): Observable<List<BoxesModel>>

    fun checkUnredeemed(): Observable<List<PrizeCollectModel>>

    fun getPackages(ownerId: Int): Observable<CelebrityBoxesModel>

}