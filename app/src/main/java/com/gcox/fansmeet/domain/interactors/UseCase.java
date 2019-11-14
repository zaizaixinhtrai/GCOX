package com.gcox.fansmeet.domain.interactors;

import io.reactivex.Observable;
import io.reactivex.Scheduler;

/**
 * Created by thanhbc on 6/18/17.
 */

public abstract class UseCase<T, Params> {

    private final Scheduler mUIThread;
    private final Scheduler mExecutorThread;

    public UseCase(Scheduler uiThread, Scheduler executorThread) {
        this.mUIThread = uiThread;
        this.mExecutorThread = executorThread;
    }

    public abstract Observable<T> buildObservable(Params params);

    public Observable<T> execute(Params params) {
        return buildObservable(params).subscribeOn(mExecutorThread).observeOn(mUIThread);
    }

}
