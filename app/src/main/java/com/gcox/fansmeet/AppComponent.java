package com.gcox.fansmeet;

import android.app.Application;
import android.content.Context;
import com.gcox.fansmeet.data.di.ApiServiceModule;
import com.gcox.fansmeet.data.di.SchedulerModule;
import com.gcox.fansmeet.webservice.AppsterWebserviceAPI;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import io.reactivex.Scheduler;

import javax.inject.Named;
import javax.inject.Singleton;

import static com.gcox.fansmeet.data.di.ApiServiceModule.APP_AUTHEN;
import static com.gcox.fansmeet.data.di.ApiServiceModule.BASE_URL;
import static com.gcox.fansmeet.data.di.SchedulerModule.IO;
import static com.gcox.fansmeet.data.di.SchedulerModule.UI;

/**
 * Created by thanhbc on 3/17/18.
 */

@Singleton
@Component(modules = {AppModule.class, ApiServiceModule.class, SchedulerModule.class,
        AndroidInjectionModule.class,ActivityBuilder.class})
public interface AppComponent {

    @Component.Builder
    interface Builder {
        AppComponent build();

        @BindsInstance
        Builder application(Application application);

        @BindsInstance
        Builder serviceUrl(@Named(BASE_URL) String url);

//        @BindsInstance
//        Builder appAuthen(@Named(APP_AUTHEN) String authen);
    }

    void inject(AppsterApplication application);

    Context context();


    @Named(UI)
    Scheduler uiThread();

    @Named(IO)
    Scheduler executorThread();

    AppsterWebserviceAPI service();

    @Named(APP_AUTHEN)
    String authen();

//    TransactionRepositoryComponent plus(TransactionRepositoryModule transactionRepositoryModule);
}
