package com.gcox.fansmeet.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.*;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.widget.TextView;
import com.gcox.fansmeet.util.view.HashTag;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by User on 9/16/2015.
 */
public class StringUtil {
    private static final int SECOND = 1000;
    private static final int MINUTE = 60 * SECOND;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;
    public static final Pattern VALID_ENGLISH_NAME_PATTERN_REGEX = Pattern.compile("[a-zA-Z_0-9]+$");
    private static final Pattern MATCHED_12_UPPERCASE_IN_STRING = Pattern.compile("^(?=(?:[^A-Z]*[A-Z]){12})[a-zA-Z0-9!@#$%^&*()\\-+{}|\\\\;'<>?=_`~\\[\\]:\",./ ]+$");
    private static final Pattern MATCHED_8_CHINESE_IN_STRING = Pattern.compile("^(\\P{sc=Han}*\\p{sc=Han}){8}.*$", Pattern.DOTALL);
    public static final int TEXT_MAX_LENGTH = 12;
    public static final int TEXT_CHINESE_MAX_LENGTH = 8;

    public static boolean isEnglishWord(String string) {
        return VALID_ENGLISH_NAME_PATTERN_REGEX.matcher(string).find();
    }

    public static ArrayList<int[]> getSpans(String body, char prefix) {
        ArrayList<int[]> spans = new ArrayList<>();

        Pattern pattern = Pattern.compile(prefix + "\\w+");
        Matcher matcher = pattern.matcher(body);
        // Check all occurrences
        while (matcher.find()) {
            int[] currentSpan = new int[2];
            currentSpan[0] = matcher.start();
            currentSpan[1] = matcher.end();
            spans.add(currentSpan);
        }

        return spans;
    }

    public static void setSpanComment(SpannableString commentsContent, ArrayList<int[]> hashtagSpans, Context mContext) {
        for (int i = 0; i < hashtagSpans.size(); i++) {
            int[] span = hashtagSpans.get(i);
            int hashTagStart = span[0];
            int hashTagEnd = span[1];

            commentsContent.setSpan(new HashTag(mContext),
                    hashTagStart,
                    hashTagEnd, 0);

        }


    }

    public static String handleThousandNumber(long count) {

        return Utils.formatThousand(count);
    }

    public static boolean isNullOrEmptyString(String string) {
        return string == null || string.isEmpty();

    }

    public static String encodeString(String str) {
        StringBuilder b = new StringBuilder();

        for (char c : str.toCharArray()) {
            if (c >= 128)
                b.append("\\u").append(String.format("%04X", (int) c).toLowerCase());
            else
                b.append(c);
        }

        return b.toString();
    }

    public static String decodeString(String escapedTmp) {

        if (escapedTmp == null)
            return "";

        String escaped = escapedTmp;
        if (!escaped.contains("\\u"))
            return escaped;


        try {
            String processed = "";
            int position = escaped.indexOf("\\u");
            while (position != -1) {
                if (position != 0)
                    processed += escaped.substring(0, position);
                String originalEmoji = escaped.substring(position, position + 6);
                String token = escaped.substring(position + 2, position + 6);
                escaped = escaped.substring(position + 6);
                try {
                    processed += (char) Integer.parseInt(token, 16);

                } catch (NumberFormatException e) {
                    processed += originalEmoji;
//     position += 12;
                }
                position = escaped.indexOf("\\u");

            }
            processed += escaped;

            return processed;
        } catch (Exception e) {
            e.printStackTrace();
            return escapedTmp;
        }
    }

    public static SpannableString formatImage(Context context, String text, Bitmap icon) {
        if (TextUtils.isEmpty(text)) {
            return new SpannableString("");
        }
        ImageSpan imgSpan;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            imgSpan = new CenteredImageSpan(context, icon, ImageSpan.ALIGN_BOTTOM);
        } else {
            imgSpan = new ImageSpan(context, icon, ImageSpan.ALIGN_BASELINE);
        }

        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(imgSpan, 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public static String subStringWithPresetMaxLength(String original) {
        if (isNullOrEmptyString(original)) {
            return "";
        }
        int length = original.length();
        if (length > 12) {
            if (MATCHED_8_CHINESE_IN_STRING.matcher(original).find()) {
                return original.substring(0, TEXT_CHINESE_MAX_LENGTH) + "...";
            } else if (MATCHED_12_UPPERCASE_IN_STRING.matcher(original).find()) {
                return original.substring(0, TEXT_MAX_LENGTH) + "...";
            }
        }
        return original;
    }

    public static String extractUserNameFromEmail(String email) {
        if (TextUtils.isEmpty(email)) return "";
        try {
            return email.substring(0, email.indexOf('@'));
        } catch (StringIndexOutOfBoundsException e) {
            return "";
        }
    }

    public static String convertTimeStampToStringTime(long ms, char divider) {
        StringBuilder text = new StringBuilder("");
        if (ms > DAY) {
            text.append(ms / DAY).append(divider);
            ms %= DAY;
        }
        if (ms > HOUR) {
            int iHour = (int) (ms / HOUR);
            String sHour = (iHour >= 10) ? String.valueOf(iHour) : "0" + iHour;
            text.append(sHour).append(divider);
            ms %= HOUR;
        } else {
            text.append("00").append(divider);
        }

        if (ms > MINUTE) {
            int iMinute = (int) (ms / MINUTE);
            String sMinute = (iMinute >= 10) ? String.valueOf(iMinute) : "0" + iMinute;
            text.append(sMinute).append(divider);
            ms %= MINUTE;
        } else {
            text.append("00").append(divider);
        }

        if (ms > SECOND) {
            int iSecond = (int) (ms / SECOND);
            String sSecond = (iSecond >= 10) ? String.valueOf(iSecond) : "0" + iSecond;
            text.append(sSecond);
        } else {
            text.append("00");
        }

        return text.toString();
    }

    public static String convertTimeStampToStringTime(int s, char divider) {
        long ms = (long) s * SECOND;
        return convertTimeStampToStringTime(ms, divider);
    }

    public static Spanned fromHtml(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY);
        } else {
            //noinspection deprecation
            return Html.fromHtml(text);
        }
    }

    public static String replaceCurrencyString(String textString) {
        if (isNullOrEmptyString(textString)) return textString;
        if (textString.contains(",")) return textString.replace(",", ".");
        return textString;
    }
}
