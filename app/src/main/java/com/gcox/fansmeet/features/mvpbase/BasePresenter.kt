package com.appster.features.mvpbase

import android.content.Context
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.features.mvpbase.BaseContract
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * Created by thanhbc on 4/26/17.
 *
 * Base class that implements the Presenter interface and provides a base implementation for
 * attachView() and detachView(). It also handles keeping a reference to the mView that
 * can be accessed from the children classes by calling getView().
 */
open class BasePresenter<T : BaseContract.View> : BaseContract.Presenter<T> {

    var view: T? = null
        private set

//    private val mCompositeSubscription = CompositeSubscription()
    private var subscribeScheduler: Scheduler? = null

    val isViewAttached: Boolean
        get() = view != null

    private val schedulersTransformer: ObservableTransformer<T, T> = object : ObservableTransformer<T, T> {
        override fun apply(upstream: Observable<T>): ObservableSource<T> {
            return upstream.subscribeOn(defaultSubscribeScheduler())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(defaultSubscribeScheduler())
        }

    }

    override fun attachView(view: T) {
        this.view = view
    }

    override fun detachView() {
        view = null
    }

    fun checkViewAttached() {
        if (!isViewAttached) {
            throw MvpViewNotAttachedException()
        }
    }

    class MvpViewNotAttachedException : RuntimeException("Please call Presenter.attachView(MvpView) before" + " requesting data to the Presenter")

    //Reusing Transformers - Singleton
    fun <T> applySchedulers(): ObservableTransformer<T, T> {
        return schedulersTransformer as ObservableTransformer<T, T>
    }

    fun defaultSubscribeScheduler(): Scheduler {
        if (subscribeScheduler == null) {
            subscribeScheduler = Schedulers.io()
        }
        return subscribeScheduler ?: Schedulers.io()
//        return subscribeScheduler
    }

    fun handleRetrofitError(error: Throwable) {
        Timber.e(error)
        view?.let {
            it.hideProgress()
            it.loadError(error.message, Constants.RETROFIT_ERROR)
        }
    }

    fun checkNotNull(`object`: Any?): Boolean {
        return `object` != null
    }

    fun getContext(): Context? {
        return view?.viewContext
    }
}
