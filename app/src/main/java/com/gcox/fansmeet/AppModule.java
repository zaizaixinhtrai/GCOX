package com.gcox.fansmeet;

import android.app.Application;
import android.content.Context;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

/**
 * Created by thanhbc on 3/17/18.
 */

@Module
public class AppModule {
    @Provides
    @Singleton
    public Context provideContext(Application application) {
        return application;
    }
}
