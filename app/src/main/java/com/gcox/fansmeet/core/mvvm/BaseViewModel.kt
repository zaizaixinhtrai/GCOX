package com.gcox.fansmeet.core.mvvm

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.gcox.fansmeet.AppsterApplication
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.common.ErrorCode
import com.gcox.fansmeet.domain.interactors.UseCase
import com.gcox.fansmeet.exception.GcoxException
import com.gcox.fansmeet.webservice.response.BaseResponse
import io.reactivex.disposables.Disposable
import timber.log.Timber

/**
 *  Created by DatTN on 11/13/2018<br>
 *      A base view model which handles related view model tasks.
 */
abstract class BaseViewModel : ViewModel() {

    private val delegate = ViewModelDelegate()

    protected val errorLiveData = MutableLiveData<GcoxException>()

    protected fun addDisposable(disposable: Disposable) {
        delegate.addDisposable(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        delegate.clear()
    }

    fun getError(): LiveData<GcoxException> {
        return errorLiveData
    }

    fun handleError(e: Throwable) {
        Timber.e("Baseviewmodel handle error -> " + e.message)
        e.printStackTrace()
        when (e) {
            is GcoxException -> {
                Timber.e("GcoxException %s code %s",e.message,e.code)
                if (e.code == Constants.RESPONSE_DUPLICATE_LOGIN) {
                    AppsterApplication.application.multipleLiveData
                        .postValue(e)
                } else {
                    errorLiveData.value = e
                }
            }
            else -> errorLiveData.value = GcoxException(
                e.message
                    ?: "Unknown exception",
                ErrorCode.UNKNOWN
            )
        }
    }

    /**
     * Run a use case and listen to the callback with the raw data
     *
     * @param useCase the use case to run
     * @param params the params
     * @param success the callback to trigger if the use case is executed (this does not mean the it runs successfully, might be api error for example)
     * @param error the callback to trigger if the request is failed
     */
    fun <T, Params> runRawUseCase(
        useCase: UseCase<T, Params>,
        params: Params?,
        success: ((T) -> Unit)? = null,
        error: ((Throwable) -> Boolean)? = null
    ) {
        addDisposable(useCase.execute(params).subscribe({
            success?.invoke(it)
        }, {
            val consumed = error?.invoke(it)
            if (consumed != true) {
                handleError(it)
            }
        }))
    }

    /**
     * Run a use case and listen to the callback with the data
     *
     * @param useCase the use case to run
     * @param params the params
     * @param success the callback to trigger if the use case is successfully
     */
    fun <T, Params> runUseCaseWithBaseResponse(
        useCase: UseCase<BaseResponse<T>, Params>,
        params: Params?,
        success: ((T) -> Unit)
    ) {
        addDisposable(useCase.execute(params).subscribe({
            if (it.code == Constants.RESPONSE_FROM_WEB_SERVICE_OK) {
                success.invoke(it.data)
            } else {
                handleError(GcoxException(it.message, it.code))
            }
        }, {
            handleError(it)
        }))
    }

    /**
     * Run a use case and listen to the callback with the data
     *
     * @param useCase the use case to run
     * @param params the params
     * @param success the callback to trigger if the use case is successfully
     */
    fun <T, Params> runUseCase(
        useCase: UseCase<T, Params>,
        params: Params?,
        success: ((T) -> Unit)
    ) {
        addDisposable(useCase.execute(params).subscribe({
            success.invoke(it)
        }, {
            handleError(it)
        }))
    }

    /**
     * Run a use case and listen to the callback with the data
     *
     * @param useCase the use case to run
     * @param params the params
     * @param success the callback to trigger if the use case is successfully
     * @param error the callback to trigger if the use case is failed
     */
    fun <T, Params> runUseCaseWithBaseResponse(
        useCase: UseCase<BaseResponse<T>, Params>,
        params: Params?,
        success: ((T) -> Unit),
        error: ((Throwable) -> Unit)
    ) {
        addDisposable(useCase.execute(params).subscribe({
            if (it.code == Constants.RESPONSE_FROM_WEB_SERVICE_OK) {
                success.invoke(it.data)
            } else {
                error.invoke(GcoxException(it.message, it.code))
            }
        }, {
            error.invoke(it)
        }))
    }

}