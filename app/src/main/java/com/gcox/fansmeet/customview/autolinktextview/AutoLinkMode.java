package com.gcox.fansmeet.customview.autolinktextview;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.gcox.fansmeet.customview.autolinktextview.AutoLinkMode.MODE_HASHTAG;
import static com.gcox.fansmeet.customview.autolinktextview.AutoLinkMode.MODE_MENTION;
import static com.gcox.fansmeet.customview.autolinktextview.AutoLinkMode.MODE_URL;

import static com.gcox.fansmeet.customview.autolinktextview.AutoLinkMode.MODE_CUSTOM;
import static com.gcox.fansmeet.customview.autolinktextview.AutoLinkMode.MODE_EMAIL;
import static com.gcox.fansmeet.customview.autolinktextview.AutoLinkMode.MODE_PHONE;

/**
 * Created by thanhbc on 6/19/17.
 */

@StringDef({MODE_HASHTAG,MODE_MENTION,MODE_URL,MODE_PHONE,MODE_EMAIL,MODE_CUSTOM})
@Retention(RetentionPolicy.SOURCE)
public @interface AutoLinkMode {
    String MODE_HASHTAG= "Hashtag";
    String MODE_MENTION= "Mention";
    String MODE_URL= "Url";
    String MODE_PHONE= "Phone";
    String MODE_EMAIL= "Email";
    String MODE_CUSTOM= "Custom";
}
