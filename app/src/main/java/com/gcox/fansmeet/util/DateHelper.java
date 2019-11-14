package com.gcox.fansmeet.util;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import timber.log.Timber;

public class DateHelper {

    private DateHelper() {
    }

    public static String getCurrentDateTimeNotification(Context context) {
        return android.text.format.DateFormat.getTimeFormat(context).format(new Date(System.currentTimeMillis()));
    }

    public static String parse(Date date, String format) {
        SimpleDateFormat dateParser = new SimpleDateFormat(format);
        dateParser.setTimeZone(TimeZone.getDefault());

        return dateParser.format(date);
    }

    public static Date parse(String s, String format) {
        SimpleDateFormat dateParser = new SimpleDateFormat(format);
        dateParser.setTimeZone(TimeZone.getDefault());
        Date date = new Date();
        try {
            date = dateParser.parse(s);
        } catch (Exception e) {
            Timber.e(e);
        }
        return date;
    }


    public static String convertMilisecondsToHoursFormat(long seconds) {
        long hours = seconds / 3600;
        if (hours == 0) {
            return String.format("%02d:%02d", (seconds % 3600) / 60, (seconds % 60));
        } else {
            return String.format("%02d:%02d:%02d", seconds / 3600, (seconds % 3600) / 60, (seconds % 60));
        }

    }



}
