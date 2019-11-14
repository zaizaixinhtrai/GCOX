package com.gcox.fansmeet.features.post;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@IntDef({EdittingType.SUBMISSION_EDIT, EdittingType.POST_EDIT})
@Retention(RetentionPolicy.SOURCE)
public @interface EdittingType {
    int SUBMISSION_EDIT = 1;
    int POST_EDIT = 2;
}

