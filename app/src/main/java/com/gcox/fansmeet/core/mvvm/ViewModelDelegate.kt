package com.gcox.fansmeet.core.mvvm

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class ViewModelDelegate {

    private var mCompositeDisposable: CompositeDisposable? = null

    fun addDisposable(disposable: Disposable) {
        if (mCompositeDisposable == null || mCompositeDisposable!!.isDisposed) {
            mCompositeDisposable = CompositeDisposable()
        }
        mCompositeDisposable!!.add(disposable)
    }

    fun clear() {
        mCompositeDisposable?.dispose()
    }
}