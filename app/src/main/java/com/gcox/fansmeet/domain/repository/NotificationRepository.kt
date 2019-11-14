package com.gcox.fansmeet.domain.repository

import com.gcox.fansmeet.features.notification.NotificationItemModel
import com.gcox.fansmeet.webservice.response.BaseDataPagingResponseModel
import io.reactivex.Observable

interface NotificationRepository {
    fun getNotificationList(notificationStatus: Int, nextIndex: Int, pageLimit:Int): Observable<BaseDataPagingResponseModel<NotificationItemModel>>
}