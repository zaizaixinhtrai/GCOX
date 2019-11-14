package com.gcox.fansmeet.data.entity.mapper

import com.gcox.fansmeet.data.entity.TopFansEntity
import com.gcox.fansmeet.domain.models.TopFansResponseModel
import com.gcox.fansmeet.features.topfans.TopFanModel
import com.gcox.fansmeet.webservice.response.BaseDataPagingResponseModel

class TopFansMapper {


    fun transform(entity: TopFansEntity?): TopFansResponseModel? {
        return entity?.let {

            val response = BaseDataPagingResponseModel<TopFanModel>()
            response.isEnd = it.topFansResponse?.isEnd!!
            response.nextId = it.topFansResponse.nextId!!
            response.result = it.topFansResponse.result?.map {
                TopFanModel(
                    it.userId!!,
                    it.userName!!,
                    it.displayName!!,
                    it.userImage!!,
                    it.userBannerImage!!,
                    it.giftCount!!
                )
            }

            return TopFansResponseModel(entity.totalGiftCount,response)
        }
    }

}
