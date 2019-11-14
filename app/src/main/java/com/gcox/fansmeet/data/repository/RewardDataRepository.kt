package com.gcox.fansmeet.data.repository

import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.data.entity.PrizeEntity
import com.gcox.fansmeet.data.entity.mapper.RewardMapper
import com.gcox.fansmeet.data.repository.datasource.RewardDataSource
import com.gcox.fansmeet.domain.repository.RewardRepository
import com.gcox.fansmeet.exception.GcoxException
import com.gcox.fansmeet.features.prizelist.models.BoxesModel
import com.gcox.fansmeet.features.prizelist.models.Prize
import com.gcox.fansmeet.features.prizelist.models.PrizeResponse
import com.gcox.fansmeet.features.rewards.models.CelebrityBoxesModel
import com.gcox.fansmeet.features.rewards.models.PrizeCollectModel
import com.gcox.fansmeet.features.rewards.models.RewardItem
import com.gcox.fansmeet.features.rewards.models.RewardListResponse
import com.gcox.fansmeet.webservice.response.BaseDataPagingResponseModel
import io.reactivex.Observable

/**
 * Created by ngoc on 5/18/18.
 */
class RewardDataRepository constructor(private val rewardDataSource: RewardDataSource) :
    RewardRepository {

    override fun getPackages(ownerId: Int): Observable<CelebrityBoxesModel> {
        return rewardDataSource.getPackages(ownerId).flatMap {
            when (it.code) {
                Constants.RESPONSE_FROM_WEB_SERVICE_OK -> Observable.just(it.data)
                else -> Observable.error(GcoxException(it.message, it.code))
            }
        }
    }

    override fun checkUnredeemed(): Observable<List<PrizeCollectModel>> {
        return rewardDataSource.checkUnredeemed().flatMap {
            when (it.code) {
                Constants.RESPONSE_FROM_WEB_SERVICE_OK -> Observable.just(rewardMapper.transforms(it.data))
                else -> Observable.error(GcoxException(it.message, it.code))
            }
        }
    }

    override fun getBoxesList(ownerId: Int): Observable<List<BoxesModel>> {
        return rewardDataSource.getBoxesList(ownerId).flatMap {
            when (it.code) {
                Constants.RESPONSE_FROM_WEB_SERVICE_OK -> Observable.just(rewardMapper.transform(it.data))
                else -> Observable.error(GcoxException(it.message, it.code))
            }
        }
    }

    override fun getRewardList(nextId: Int, limit: Int): Observable<RewardListResponse> {
        return rewardDataSource.getRewardList(nextId, limit).flatMap {
            when (it.code) {
                Constants.RESPONSE_FROM_WEB_SERVICE_OK -> Observable.just(rewardMapper.transform(it.data))
                else -> Observable.error(GcoxException(it.message, it.code))
            }
        }
    }

    override fun getPrizeList(type: Int, nextId: Int, limit: Int): Observable<PrizeResponse> {
        return rewardDataSource.getPrizeList(type, nextId, limit).flatMap {
            when (it.code) {
                Constants.RESPONSE_FROM_WEB_SERVICE_OK -> Observable.just(rewardMapper.transform(it.data))
                else -> Observable.error(GcoxException(it.message, it.code))
            }
        }
    }

    private val rewardMapper by lazy { RewardMapper() }
}