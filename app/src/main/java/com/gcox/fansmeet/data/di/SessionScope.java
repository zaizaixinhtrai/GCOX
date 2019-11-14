package com.gcox.fansmeet.data.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by thanhbc on 3/19/18.
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface SessionScope {
}
