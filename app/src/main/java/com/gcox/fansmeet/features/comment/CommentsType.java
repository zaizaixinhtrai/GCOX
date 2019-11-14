package com.gcox.fansmeet.features.comment;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.gcox.fansmeet.features.comment.CommentsType.CHALLENGES;
import static com.gcox.fansmeet.features.comment.CommentsType.ENTRIES;


/**
 * Created by Ngoc on 6/3/17.
 */

@IntDef({CHALLENGES, ENTRIES})
@Retention(RetentionPolicy.SOURCE)
public @interface CommentsType {
    int ENTRIES = 2;
    int CHALLENGES = 1;
}
