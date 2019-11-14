package com.gcox.fansmeet.webservice;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Services created with this instance that use Observable will execute on the 'io' scheduler
 * and notify their observer on the 'main thread' scheduler.
 * Created by thanhbc on 8/5/17.
 */

public class RxJavaObserveOnMainThread {
    public static final class ObserveOnMainCallAdapterFactory extends CallAdapter.Factory {
        final Scheduler scheduler;

        public ObserveOnMainCallAdapterFactory(Scheduler scheduler) {
            this.scheduler = scheduler;
        }

        @Override
        public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
            if (getRawType(returnType) != Observable.class) {
                return null; // Ignore non-Observable types.
            }

            // Look up the next call adapter which would otherwise be used if this one was not present.
            //noinspection unchecked returnType checked above to be Observable.
            final CallAdapter<Object, Observable<?>> delegate =
                    (CallAdapter<Object, Observable<?>>) retrofit.nextCallAdapter(this, returnType,
                            annotations);

            return new CallAdapter<Object, Object>() {
                @Override public Object adapt(Call<Object> call) {
                    // Delegate to get the normal Observable...
                    Observable<?> o = delegate.adapt(call);
                    // ...and change it to send notifications to the observer on the specified scheduler.
                    return o.observeOn(scheduler).unsubscribeOn(Schedulers.io());
                }

                @Override public Type responseType() {
                    return delegate.responseType();
                }
            };
        }
    }
}
