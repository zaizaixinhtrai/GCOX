package com.gcox.fansmeet.util;

import java.util.TimeZone;

public class TimeUtility {
    public static final TimeZone utcTZ = TimeZone.getTimeZone("UTC");

    public static long toLocalTime(long time, TimeZone to) {
        return convertTime(time, utcTZ, to);
    }

    public static long toUTC(long time, TimeZone from) {
        return convertTime(time, from, utcTZ);
    }

    public static long convertTime(long time, TimeZone from, TimeZone to) {
        return time + getTimeZoneOffset(time, from, to);
    }

    private static long getTimeZoneOffset(long time, TimeZone from, TimeZone to) {
        int fromOffset = from.getOffset(time);
        int toOffset = to.getOffset(time);
        int diff = 0;

        if (fromOffset >= 0){
            if (toOffset > 0){
                toOffset = -1*toOffset;
            } else {
                toOffset = Math.abs(toOffset);
            }
            diff = (fromOffset+toOffset)*-1;
        } else {
            if (toOffset <= 0){
                toOffset = -1*Math.abs(toOffset);
            }
            diff = (Math.abs(fromOffset)+toOffset);
        }
        return diff;
    }
}
