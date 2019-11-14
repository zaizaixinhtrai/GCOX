package com.gcox.fansmeet.data.repository.datasource.cloud

import com.gcox.fansmeet.data.entity.BoxesTypeEntity
import com.gcox.fansmeet.data.entity.PrizeEntity
import com.gcox.fansmeet.data.repository.datasource.RewardDataSource
import com.gcox.fansmeet.features.rewards.models.CelebrityBoxesModel
import com.gcox.fansmeet.features.rewards.models.UsePointsResponsModel
import com.gcox.fansmeet.util.AppsterUtility
import com.gcox.fansmeet.webservice.AppsterWebserviceAPI
import com.gcox.fansmeet.webservice.response.*
import io.reactivex.Observable

/**
 * Created by ngoc on 5/18/18.
 */
class CloudRewardDataSource
constructor(private val service: AppsterWebserviceAPI) : RewardDataSource {

    override fun getPackages(ownerId: Int): Observable<BaseResponse<CelebrityBoxesModel>> {
        return service.getBoxes(AppsterUtility.getAuth(),ownerId)
    }

    override fun checkUnredeemed(): Observable<BaseResponse<List<UsePointsResponsModel>>> {
        return service.checkUnredeemed(AppsterUtility.getAuth())
    }

    override fun getBoxesList(ownerId: Int): Observable<BaseResponse<List<BoxesTypeEntity>>> {
        return service.getBoxesList(AppsterUtility.getAuth(), ownerId)
    }

    override fun getPrizeList(
        type: Int,
        nextId: Int,
        limit: Int
    ): Observable<BaseResponse<BaseDataPagingResponseModel<PrizeEntity>>> {
        return service.getPrizeList(AppsterUtility.getAuth(),type, nextId, limit)
    }

    override fun getRewardList(nextId: Int, limit: Int): Observable<BaseResponse<BoxListResponse>> {
        return service.getRewardList(AppsterUtility.getAuth(), nextId, limit)
    }

}