package com.gcox.fansmeet.features.notification

import com.gcox.fansmeet.features.mvpbase.BaseContract

interface NotifyContract {

    interface View : BaseContract.View {

        fun setDataForListView(data: List<NotificationItemModel>, isEnded: Boolean, nextIndex: Int)
        fun onHandleUiAfterApiReturn()
    }

    interface UserActions : BaseContract.Presenter<NotifyContract.View> {
        fun getNotificationList(type: Int, nextId: Int, isShowDialog: Boolean)
    }
}