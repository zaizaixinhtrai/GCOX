package com.gcox.fansmeet.core.mvvm

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.gcox.fansmeet.common.ErrorCode
import com.gcox.fansmeet.exception.GcoxException
import io.reactivex.disposables.Disposable
import timber.log.Timber

/**
 *  Created by DatTN on 11/13/2018<br>
 *      A base view model which handles related view model tasks.
 */
abstract class BaseAndroidViewModel(context: Application) : AndroidViewModel(context) {

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
            is GcoxException -> errorLiveData.value = e
            else -> errorLiveData.value = GcoxException(
                e.message
                    ?: "Unknown exception",
                ErrorCode.UNKNOWN
            )
        }
    }
}