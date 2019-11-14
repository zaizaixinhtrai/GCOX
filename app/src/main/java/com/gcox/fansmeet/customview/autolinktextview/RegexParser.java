package com.gcox.fansmeet.customview.autolinktextview;

import android.util.Patterns;

/**
 * Created by thanhbc on 6/19/17.
 */

public class RegexParser {
    static final String PHONE_PATTERN = Patterns.PHONE.pattern();
    static final String EMAIL_PATTERN = Patterns.EMAIL_ADDRESS.pattern();
    static final String HASHTAG_PATTERN = "(?:^|\\s|$)#[\\p{L}0-9_]+";
    static final String MENTION_PATTERN = "(?:^|\\s|$|[.])@[\\p{L}0-9_]+";
    static final String URL_PATTERN = Patterns.WEB_URL.pattern();

//            "(^|[\\s.:;?\\-\\]<\\(])" +
//            "((https?://|www\\.|pic\\.)[-\\w;/?:@&=+$\\|\\_.!~*\\|'()\\[\\]%#,☺]+[\\w/#](\\(\\))?)" +
//            "(?=$|[\\s',\\|\\(\\).:;?\\-\\[\\]>\\)])";
}
