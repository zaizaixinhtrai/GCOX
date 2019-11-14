package com.gcox.fansmeet.data.repository.datasource

import com.gcox.fansmeet.data.entity.BoxesTypeEntity
import com.gcox.fansmeet.data.entity.PrizeEntity
import com.gcox.fansmeet.features.rewards.models.CelebrityBoxesModel
import com.gcox.fansmeet.features.rewards.models.UsePointsResponsModel
import com.gcox.fansmeet.webservice.response.BaseDataPagingResponseModel
import com.gcox.fansmeet.webservice.response.BaseResponse
import com.gcox.fansmeet.webservice.response.BoxListResponse
import io.reactivex.Observable

/**
 * Created by ngoc on 5/18/18.
 */
interface RewardDataSource {
    fun getRewardList(nextId: Int, limit: Int): Observable<BaseResponse<BoxListResponse>>
    fun getPrizeList(
        type: Int,
        nextId: Int,
        limit: Int
    ): Observable<BaseResponse<BaseDataPagingResponseModel<PrizeEntity>>>

    fun getBoxesList(ownerId: Int): Observable<BaseResponse<List<BoxesTypeEntity>>>

    fun checkUnredeemed(): Observable<BaseResponse<List<UsePointsResponsModel>>>

    fun getPackages(ownerId: Int): Observable<BaseResponse<CelebrityBoxesModel>>
}