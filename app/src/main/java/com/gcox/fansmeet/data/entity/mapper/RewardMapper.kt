package com.gcox.fansmeet.data.entity.mapper

import com.gcox.fansmeet.data.entity.BoxesTypeEntity
import com.gcox.fansmeet.data.entity.PrizeEntity
import com.gcox.fansmeet.features.prizelist.models.*
import com.gcox.fansmeet.features.prizelist.models.BoxesModel
import com.gcox.fansmeet.features.rewards.models.*
import com.gcox.fansmeet.webservice.response.BaseDataPagingResponseModel
import com.gcox.fansmeet.webservice.response.BoxListResponse

class RewardMapper {

    fun transform(entity: BoxListResponse): RewardListResponse? {
        entity.let {
            return RewardListResponse(
                entity.boxes?.map {
                    RewardItem(user = RewardUser(
                        it.userName,
                        it.userImage,
                        it.displayName,
                        it.ownerId
                    ),
                        prizeItemImages = it.prizeItemImages
                    )
                }, nextId = entity.nextId!!,
                isEnded = entity.isEnded!!
            )
        }
    }

    fun transform(entity: BaseDataPagingResponseModel<PrizeEntity>): PrizeResponse? {
        entity.let {
            return PrizeResponse(
                entity.nextId,
                entity.isEnd,
                entity.result?.map {
                    Prize(
                        it.id!!,
                        it.image,
                        it.description,
                        it.title,
                        it.type,
                        it.webUrl
                    )
                }
            )
        }
    }

    fun transform(entity: List<BoxesTypeEntity>): List<BoxesModel>? {
        entity.let {
            return entity.map {
                BoxesModel(it.id!!, it.ownerId!!, it.name!!, it.type!!)
            }
        }
    }


    fun transforms(entity: List<UsePointsResponsModel>): List<PrizeCollectModel>? {
        entity.let {
            return entity.map { PrizeCollectModel(it.id!!, it.itemName!!, it.title, it.description, it.image) }
        }
    }
}

