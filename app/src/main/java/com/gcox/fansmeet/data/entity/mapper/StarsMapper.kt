package com.gcox.fansmeet.data.entity.mapper

import com.gcox.fansmeet.data.entity.StarEntity
import com.gcox.fansmeet.features.stars.HistoriesResponse
import com.gcox.fansmeet.features.stars.StarResponse
import com.gcox.fansmeet.features.stars.StarsModel

class StarsMapper {
    fun transform(entity: StarEntity?): StarResponse? {
        return entity?.let {
            StarResponse(
                balance = it.balance,
                historiesResponse = HistoriesResponse(
                    it.historiesEntity?.nextId,
                    it.historiesEntity?.isEnd,
                    it.historiesEntity?.result?.map {
                        StarsModel(
                            title = it.title,
                            entryId = it.entryId,
                            star = it.star,
                            updated = it.updated
                        )
                    }

                )
            )
        }
    }

}

