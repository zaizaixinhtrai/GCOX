package com.gcox.fansmeet.features.notification

import com.appster.features.mvpbase.BasePresenter
import com.gcox.fansmeet.AppsterApplication
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.exception.GcoxException
import com.gcox.fansmeet.webservice.AppsterWebServices
import io.reactivex.disposables.CompositeDisposable

class NotifyPresenter(
    notifyView: NotifyContract.View
) : BasePresenter<NotifyContract.View>(), NotifyContract.UserActions {

    var compositeDisposable: CompositeDisposable


    init {
        attachView(notifyView)
        compositeDisposable = CompositeDisposable()
    }

    override fun getNotificationList(type: Int, nextId: Int, isShowDialog: Boolean) {
        if (isShowDialog) view?.showProgress()

        compositeDisposable.add(
            AppsterWebServices.get().getNotificationList(
                "Bearer " + AppsterApplication.mAppPreferences.userToken,
                type,
                nextId,
                Constants.PAGE_LIMITED
            ).subscribe({ response ->
                when (response.code) {
                    Constants.RESPONSE_FROM_WEB_SERVICE_OK -> {
                        val data = response.data
                        view?.setDataForListView(data.result, data.isEnd, data.nextId)
                    }
                    else -> view?.loadError(response.message, response.code)
                }
                if (isShowDialog) view?.hideProgress()
            }, { error ->
                if (isShowDialog) view?.hideProgress()
                view?.apply {
                    onHandleUiAfterApiReturn()
                    if (error is GcoxException) {
                        loadError(error.message, error.code)
                    } else {
                        handleRetrofitError(error)
                    }
                }
            })
        )

    }


}