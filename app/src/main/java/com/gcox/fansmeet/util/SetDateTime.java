package com.gcox.fansmeet.util;

import android.content.Context;
import com.gcox.fansmeet.R;
import timber.log.Timber;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by User on 8/31/2015.
 */
public class SetDateTime {

    public static final int SECOND_MILLIS = 1000;
    public static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    public static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    public static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    private static final String PocketTimeFormatter = "d'th' MMM yyyy, h.mma";
    private static final int DAY_OF_YEAR = 365;
    private static final int DAY_OF_MONTH = 30;
    private static final int DAY_OF_WEEK = 7;
    private static final String GMT_TIME_FORMAT_LIVE_STREAM = "dd/MM/yyyy'T'HH:mm:ss.SSS'Z'";


    public static String convertTimeStamp(String time_stamp, Context context) {

        time_stamp = time_stamp.trim();

        long dv = 0;
        Date df;
        String time1 = "";
        String date_current = "";
        Date date = new Date();
        long now = date.getTime();
        long time;
        long diff;
        String str = "";

        try {
            dv = Long.valueOf(time_stamp) * 1000;// its
        } catch (Exception e) {
            e.printStackTrace();

            return "";
        }
        df = new Date(dv);
//        time1 = new SimpleDateFormat("hh:mma")
        time1 = android.text.format.DateFormat.getTimeFormat(context)
                .format(df);
        date_current = new SimpleDateFormat(context.getString(R.string.formatter_dmy)).format(df);

        time = df.getTime();

        diff = now - time;

        if (diff < 24 * HOUR_MILLIS) {

            str = time1;

        } else if (diff < 48 * HOUR_MILLIS) {
            str = context.getString(R.string.time_yesterday);
        } else {
            str = date_current;
        }

        return str;
    }


    public static String partTimeNews(String time, Context context) {

        if (StringUtil.isNullOrEmptyString(time)) {
            return "";
        }

        String formatDate = time;
        DateFormat dateFormatter = android.text.format.DateFormat.getTimeFormat(context);
        dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date startDateTime = null;
        try {
            startDateTime = dateFormatter.parse(time);
            dateFormatter.setTimeZone(TimeZone.getDefault());
            formatDate = dateFormatter.format(startDateTime);

        } catch (Exception e) {

        }
        Date date = convertStringToDate(formatDate, GMT_TIME_FORMAT_LIVE_STREAM);
        String timeString = time;

        if (date != null)
            timeString = partDateTimeToPassStringAgo(date, context);
        return timeString;
    }


    public static String partTimeForFeedItem(String time, Context context) {
        if (StringUtil.isNullOrEmptyString(time)) {
            return "";
        }
        String formatDate = time;
        SimpleDateFormat dateFormatter = new SimpleDateFormat(GMT_TIME_FORMAT_LIVE_STREAM, Locale.getDefault());
        dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date startDateTime = null;
        try {
            startDateTime = dateFormatter.parse(time);
            dateFormatter.setTimeZone(TimeZone.getDefault());
            formatDate = dateFormatter.format(startDateTime);

        } catch (Exception e) {
            Timber.e(e.getMessage());

        }
        Date date = convertStringToDate(formatDate, GMT_TIME_FORMAT_LIVE_STREAM);
        String timeString = time;

        if (date != null)
            timeString = partDateTimeToPassStringAgo(date, context);
        return timeString;
    }

    public static String partTimeForEditChallenge(String time, Context context) {
        if (StringUtil.isNullOrEmptyString(time)) {
            return "";
        }
        String formatDate = time;
        SimpleDateFormat dateFormatter = new SimpleDateFormat(GMT_TIME_FORMAT_LIVE_STREAM, Locale.getDefault());
        dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date startDateTime = null;
        try {
            startDateTime = dateFormatter.parse(time);
            dateFormatter.setTimeZone(TimeZone.getDefault());
            formatDate = dateFormatter.format(startDateTime);

        } catch (Exception e) {
            Timber.e(e.getMessage());
        }
        Date date = convertStringToDate(formatDate, GMT_TIME_FORMAT_LIVE_STREAM);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy, HH:mm", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }

    public static String parseTimeForTransactionItem(String time) {
        if (StringUtil.isNullOrEmptyString(time)) {
            return "";
        }
//        2017-10-19T03:48:43
        String formatDate = time;
        SimpleDateFormat dateFormatter = new SimpleDateFormat(GMT_TIME_FORMAT_LIVE_STREAM, Locale.getDefault());
        dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date startDateTime = null;
        try {
            startDateTime = dateFormatter.parse(time);
            dateFormatter.setTimeZone(TimeZone.getDefault());
            formatDate = dateFormatter.format(startDateTime);
            Timber.e(formatDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Date date = convertStringToDate(formatDate, GMT_TIME_FORMAT_LIVE_STREAM);

        return new SimpleDateFormat("d MMM yyyy, HH:mm", Locale.getDefault()).format(date);
    }


    public static String getDOB(String time, Context context) {

        if (StringUtil.isNullOrEmptyString(time)) {
            return "";
        }
        DateFormat dateFormatter = new SimpleDateFormat(context.getString(R.string.formatter_dob), Locale.getDefault());
        Date startDateTime;
        try {
            startDateTime = dateFormatter.parse(time);
        } catch (Exception e) {
            return time;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(startDateTime);

        return Utils.checkDigit(cal.get(Calendar.DATE)) + "/" + Utils.checkDigit(cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
    }

    public static String timeChallengeDuration(String startAtTime, String endedAtTime, Context context) {

        if (StringUtil.isNullOrEmptyString(startAtTime) || StringUtil.isNullOrEmptyString(endedAtTime)) return "";
        String datetime;
        Date createdDate = partDateTimeChallengeDuration(startAtTime);
        Date endedAtDate = partDateTimeChallengeDuration(endedAtTime);

        if (endedAtDate.getYear() > createdDate.getYear()) {
            datetime = convertDateToString(createdDate, context.getString(R.string.formatter_wall_feed)) + " - "
                    + convertDateToString(endedAtDate, context.getString(R.string.formatter_wall_feed));
        } else {
            if (compareDateChallenge(createdDate, endedAtDate)) {
                datetime = convertDateToString(createdDate, "dd MMM") + " - "
                        + convertDateToString(endedAtDate, "dd MMM yyyy");
            } else {
                datetime = convertDateToString(createdDate, "dd MMM") + " - "
                        + convertDateToString(endedAtDate, "dd MMM yyyy");
            }
        }

        return datetime;
    }

    private static boolean compareDateChallenge(Date createdDate, Date endedAtDate) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(createdDate);

        int createdDay = cal.get(Calendar.DAY_OF_MONTH);
        int createdMonth = cal.get(Calendar.MONTH);
        int createdYear = cal.get(Calendar.YEAR);

        cal.setTime(endedAtDate);

        int endedDay = cal.get(Calendar.DAY_OF_MONTH);
        int endedMonth = cal.get(Calendar.MONTH);
        int endedYear = cal.get(Calendar.YEAR);


        return (createdDay == endedDay) && (createdMonth == endedMonth) && (createdYear == endedYear);
    }

    public static Date partDateTimeChallengeDuration(String time) {

        String formatDate = time;
        SimpleDateFormat dateFormatter = new SimpleDateFormat(GMT_TIME_FORMAT_LIVE_STREAM, Locale.getDefault());
        dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date startDateTime = null;
        try {
            startDateTime = dateFormatter.parse(time);
            dateFormatter.setTimeZone(TimeZone.getDefault());
            formatDate = dateFormatter.format(startDateTime);

        } catch (Exception e) {
            Timber.e(e.getMessage());

        }
        return convertStringToDate(formatDate, GMT_TIME_FORMAT_LIVE_STREAM);

//        String timeString = time;

//        if (date != null) {
//            Date currentDate = new Date();
//
//            if (currentDate.getYear() > date.getYear()) {
//                timeString = convertDateToString(date, context.getString(R.string.formatter_wall_feed));
//            } else {
//                timeString = android.text.format.DateFormat.format("dd", date) + " " + android.text.format.DateFormat.format("MMM", date);
//            }
//        }
//
//        return timeString;

    }

    public static String partDateTimeToPassStringAgoWallFeed(Date date, Context context) {
        String datetime;
        Date currentDate = new Date();
        long currentDateTime = currentDate.getTime();
        long passDatetime = date.getTime();
        long countDate = currentDateTime - passDatetime;

        if (countDate < MINUTE_MILLIS) {
            datetime = context.getString(R.string.time_just_now);
        } else if (countDate < 2 * MINUTE_MILLIS) {
            datetime = context.getString(R.string.time_a_minute_ago);
        } else if (countDate < 50 * MINUTE_MILLIS) {
            datetime = context.getString(R.string.posted) + " " + countDate / MINUTE_MILLIS + " " + context.getString(R.string.time_minutes_ago);
        } else if (countDate < 120 * MINUTE_MILLIS) {
            datetime = context.getString(R.string.time_an_hour_ago);
        } else if (countDate < 24 * HOUR_MILLIS) {
            datetime = context.getString(R.string.posted) + " " + countDate / HOUR_MILLIS + " " + context.getString(R.string.time_hours_ago);
        } else {
            if (currentDate.getYear() > date.getYear()) {
                datetime = convertDateToString(date, context.getString(R.string.formatter_wall_feed));
            } else {
                datetime = context.getString(R.string.posted) + " " + new SimpleDateFormat(android.text.format.DateFormat.getBestDateTimePattern(Locale.getDefault(), context.getString(R.string.formatter_wall_feed_date)), Locale.getDefault()).format(date);
            }
        }

        return datetime;
    }

    public static String partDateTimeToPassStringAgo(Date date, Context context) {
        String datetime = "";
        Date currentDate = new Date();
        long currentDateTime = currentDate.getTime();
        long passDatetime = date.getTime();
        long countDate = currentDateTime - passDatetime;

        if (countDate < MINUTE_MILLIS) {
            datetime = context.getString(R.string.time_just_now);
        } else if (countDate < 2 * MINUTE_MILLIS) {
            datetime = context.getString(R.string.time_a_minute_ago);
        } else if (countDate < 50 * MINUTE_MILLIS) {
            datetime = countDate / MINUTE_MILLIS + " " + context.getString(R.string.time_minutes_ago);
        } else if (countDate < 120 * MINUTE_MILLIS) {
            datetime = context.getString(R.string.time_an_hour_ago);
        } else if (countDate < 24 * HOUR_MILLIS) {
            datetime = countDate / HOUR_MILLIS + " " + context.getString(R.string.time_hours_ago);
        } else if (countDate < 48 * HOUR_MILLIS) {
            datetime = context.getString(R.string.time_yesterday);
        } else {

            long naturalPartsYear = countDate / DAY_MILLIS / DAY_OF_YEAR;
            long naturalPartsMonth = countDate / DAY_MILLIS / DAY_OF_MONTH;
            long naturalWeeksMonth = countDate / DAY_MILLIS / DAY_OF_WEEK;

            if (naturalPartsYear > 0) {

                if (naturalPartsYear == 1) {

                    datetime = context.getString(R.string.time_an_year_ago);
                } else {

                    datetime = naturalPartsYear + " " + context.getString(R.string.time_year_ago);
                }

            } else if (naturalPartsMonth > 0) {

                if (naturalPartsMonth == 1) {
                    datetime = context.getString(R.string.time_an_month_ago);
                } else {
                    datetime = +naturalPartsMonth + " " + context.getString(R.string.time_month_ago);
                }

            } else if (naturalWeeksMonth > 0) {

                if (naturalWeeksMonth == 1) {
                    datetime = context.getString(R.string.time_a_week_ago);
                } else {
                    datetime = naturalWeeksMonth + " " + context.getString(R.string.time_weeks_ago);
                }

            } else {

                datetime = countDate / DAY_MILLIS + " " + context.getString(R.string.time_days_ago);
            }
        }

        return datetime;
    }

    public static String partTimeNotification(String time, Context context) {

        if (StringUtil.isNullOrEmptyString(time)) {
            return "";
        }

        String formatDate = time;
        SimpleDateFormat dateFormatter = new SimpleDateFormat(GMT_TIME_FORMAT_LIVE_STREAM);
        dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date startDateTime = null;
        try {
            startDateTime = dateFormatter.parse(time);
            dateFormatter.setTimeZone(TimeZone.getDefault());
            formatDate = dateFormatter.format(startDateTime);

        } catch (Exception e) {
            Timber.e(e);
        }
        Date date = convertStringToDate(formatDate, GMT_TIME_FORMAT_LIVE_STREAM);
        String timeString = time;

        if (date != null)
            timeString = partDateTimeToPassStringAgoForNOtification(date, context);
        return timeString;
    }

    public static String partDateTimeToPassStringAgoForNOtification(Date date, Context context) {
        String datetime = "";
        Date currentDate = new Date();
        long currentDateTime = currentDate.getTime();
        long passDatetime = date.getTime();
        long countDate = currentDateTime - passDatetime;

        if (countDate < MINUTE_MILLIS) {
            datetime = context.getString(R.string.time_just_now);
        } else if (countDate < 2 * MINUTE_MILLIS) {
            datetime = context.getString(R.string.time_a_minute_ago);
        } else if (countDate < 50 * MINUTE_MILLIS) {
            datetime = "" + countDate / MINUTE_MILLIS + " " + context.getString(R.string.time_minutes_ago);
        } else if (countDate < 120 * MINUTE_MILLIS) {
            datetime = context.getString(R.string.time_an_hour_ago);
        } else if (countDate < 24 * HOUR_MILLIS) {
            datetime = "" + countDate / HOUR_MILLIS + " " + context.getString(R.string.time_hours_ago);
        } else if (countDate < 48 * HOUR_MILLIS) {
            datetime = context.getString(R.string.time_day_ago);
        } else {

            long naturalPartsYear = countDate / DAY_MILLIS / DAY_OF_YEAR;
            long naturalPartsMonth = countDate / DAY_MILLIS / DAY_OF_MONTH;
            long naturalWeeksMonth = countDate / DAY_MILLIS / DAY_OF_WEEK;

            if (naturalPartsYear > 0) {

                if (naturalPartsYear == 1) {

                    datetime = context.getString(R.string.time_an_year_ago);
                } else {

                    datetime = "" + naturalPartsYear + " " + context.getString(R.string.time_year_ago);
                }

            } else if (naturalPartsMonth > 0) {

                if (naturalPartsMonth == 1) {
                    datetime = context.getString(R.string.time_an_month_ago);
                } else {
                    datetime = "" + naturalPartsMonth + " " + context.getString(R.string.time_month_ago);
                }

            } else if (naturalWeeksMonth > 0) {

                if (naturalWeeksMonth == 1) {
                    datetime = context.getString(R.string.time_a_week_ago);
                } else {
                    datetime = "" + naturalWeeksMonth + " " + context.getString(R.string.time_weeks_ago);
                }

            } else {

                datetime = "" + countDate / DAY_MILLIS + " " + context.getString(R.string.time_days_ago);
            }
        }

        return datetime;
    }

    public static Date convertStringToDate(String strDate, String formatDate) {
        SimpleDateFormat formatter = new SimpleDateFormat(formatDate);
        Date date = null;
        try {
            date = formatter.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    public static String convertDateToString(Date date, DateFormat format) {
        return format.format(date);
    }

    public static String convertDateToString(Date date, String formatDate) {
        SimpleDateFormat formatter = new SimpleDateFormat(formatDate, Locale.US);
        return formatter.format(date);
    }

    public static String convertTimePocket(String datecreated) {

        long dv;
        try {
            dv = Long.valueOf(datecreated) * 1000;
        } catch (Exception e) {
            return "";
        }

        Date df = new Date(dv);

        Format formatter = new SimpleDateFormat(PocketTimeFormatter);

        return formatter.format(df);
    }

    public static boolean compareDate(String start, String end) {

        if (start.equals(end)) {
            return true;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd/MM/yyyy");
        Date convertedDate1 = new Date();
        Date convertedDate2 = new Date();
        try {
            convertedDate1 = dateFormat.parse(start);
            convertedDate2 = dateFormat.parse(end);
            return convertedDate2.after(convertedDate1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }


    public static String convertTimeStampNotify(String time) {

        if (time == null || time.equals("")) {
            return "";
        }

        String date = "";

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String currentDateandTime = sdf.format(new Date());

        long dv = 0;
        try {
            dv = Long.valueOf(time) * 1000;// its need to be in milisecond
        } catch (Exception e) {
            return "";
        }
        Date df = new Date(dv);
        date = AppsterUtility.convertDateTimeString(df, "dd/MM/yyyy");

        if (date != null && !date.equals(currentDateandTime)) {
            return date;
        } else {

            return new SimpleDateFormat("hh:mma").format(df);
        }
    }

    public static String convertSecondsToTime(long seconds) {

        int hours = (int) seconds / 3600;
        int remainder = (int) seconds - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;
        String format = "%02d"; // two digits

        return String.format(format, hours) + ":" + String.format(format, mins) + ":" + String.format(format, secs);
    }

    public static String getCurrentGMTTime() {
        DateFormat df = new SimpleDateFormat(GMT_TIME_FORMAT_LIVE_STREAM);
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        return df.format(new Date());
    }
}
