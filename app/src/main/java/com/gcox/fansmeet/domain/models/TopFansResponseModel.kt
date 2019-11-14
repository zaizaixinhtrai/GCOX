package com.gcox.fansmeet.domain.models

import com.gcox.fansmeet.features.topfans.TopFanModel
import com.gcox.fansmeet.webservice.response.BaseDataPagingResponseModel

class TopFansResponseModel (
    var totalGiftCount: Long? = 0,
    var responseModel: BaseDataPagingResponseModel<TopFanModel>?
)