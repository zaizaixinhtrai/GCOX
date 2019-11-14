package com.gcox.fansmeet.data.di;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by thanhbc on 3/17/18.
 */

@Module
public class SchedulerModule {

    public static final String IO = "executor_thread";
    public static final String UI = "ui_thread";
    @Provides
    @Singleton
    @Named(IO)
    Scheduler provideExecutorThread() {
        return Schedulers.io();
    }

    @Provides
    @Singleton
    @Named(UI)
    Scheduler provideUiThread() {
        return AndroidSchedulers.mainThread();
    }
}
