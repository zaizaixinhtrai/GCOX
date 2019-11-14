package com.gcox.fansmeet.data.entity.mapper

import com.gcox.fansmeet.data.entity.LoyaltyEntity
import com.gcox.fansmeet.data.entity.StarEntity
import com.gcox.fansmeet.data.entity.StarsItemEntity
import com.gcox.fansmeet.features.loyalty.LoyaltyHistoriesResponse
import com.gcox.fansmeet.features.loyalty.LoyaltyModel
import com.gcox.fansmeet.features.loyalty.LoyaltyResponse
import com.gcox.fansmeet.features.stars.HistoriesResponse
import com.gcox.fansmeet.features.stars.StarResponse
import com.gcox.fansmeet.features.stars.StarsModel

class LoyaltyMapper {
    fun transform(entity: LoyaltyEntity?): LoyaltyResponse? {
        return entity?.let {
            LoyaltyResponse(
                balance = it.balance,
                historiesEntity = LoyaltyHistoriesResponse(
                    it.historiesEntity?.nextId,
                    it.historiesEntity?.isEnd,
                    it.historiesEntity?.result?.map {
                        LoyaltyModel(
                            displayName = it.displayName,
                            userType = it.userType,
                            loyalty = it.loyalty,
                            timer = it.timer
                        )
                    }
                )
            )
        }
    }

}

