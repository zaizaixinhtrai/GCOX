package com.gcox.fansmeet.features.home;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@IntDef({HomeChildType.CELEBRITIES, HomeChildType.MERCHANTS, HomeChildType.EVENTS,HomeChildType.INFLUENCERS})
@Retention(RetentionPolicy.SOURCE)
public @interface HomeChildType {
    int CELEBRITIES = 1;
    int MERCHANTS = 3;
    int EVENTS = 4;
    int INFLUENCERS = 2;
}

