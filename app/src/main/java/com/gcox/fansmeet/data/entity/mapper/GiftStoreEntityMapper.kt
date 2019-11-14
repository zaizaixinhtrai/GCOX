package com.gcox.fansmeet.data.entity.mapper

import com.gcox.fansmeet.data.entity.GiftStoreEntity
import com.gcox.fansmeet.data.entity.SendGiftEntity
import com.gcox.fansmeet.domain.models.GiftStoreModel
import com.gcox.fansmeet.features.profile.userprofile.gift.GiftItemModel
import com.gcox.fansmeet.features.profile.userprofile.gift.SendGiftModel
import com.gcox.fansmeet.webservice.response.BaseResponse

/**
 * Created by ngoc on 3/27/18.
 */
class GiftStoreEntityMapper {
    fun transform(entity: BaseResponse<GiftStoreEntity>?): GiftStoreModel? {
        return entity?.data?.let {
            GiftStoreModel(it.gems, it.giftItems.map {
                GiftItemModel().apply {
                    id = it.id
                    gem = it.gem
                    receivedStar = it.receivedStar
                    image = it.image
                    created = it.created
                    updated = it.updated
                }
            })
        }
    }

    fun transform(entity: BaseResponse<SendGiftEntity>?): SendGiftModel? {
        return entity?.data?.let {
            SendGiftModel(it.senderGems, it.receiverStars )
        }
    }

}