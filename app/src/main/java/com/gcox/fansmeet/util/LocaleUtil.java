package com.gcox.fansmeet.util;

import com.gcox.fansmeet.common.Constants;

import java.util.Locale;

/**
 * Created by Jensen on 17/5/17.
 */

public class LocaleUtil {

    public static int getLocaleNumber() {
        String languageCode = Locale.getDefault().getLanguage();
        String countryCode = Locale.getDefault().getCountry();
        if ("zh".equalsIgnoreCase(languageCode)) {
            switch (countryCode) {
                case "SG":
                    return 1;
                case "CN":
                    return 2;
                case "TW":
                    return 3;
                case "HK":
                    return 4;
                case "MO":
                    return 5;
                default:
                    return 1;
            }
        } else {
            if (Constants.VIETNAMESE_LANGUAGE_PHONE.equals(languageCode)) return 6;
            return 0;
        }
    }

    public static String getLocaleString() {
        String languageCode = Locale.getDefault().getLanguage();
        String countryCode = Locale.getDefault().getCountry();
        return languageCode + "-" + countryCode;
    }

    public static boolean isChineseLanguage() {
        String language = Locale.getDefault().getLanguage();
        return language.equals("zh");
    }
}
