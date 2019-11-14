package com.gcox.fansmeet.customview.hashtag;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import com.gcox.fansmeet.R;
import com.gcox.fansmeet.customview.CustomTypefaceSpan;
import com.gcox.fansmeet.util.CenteredImageSpan;
import com.gcox.fansmeet.util.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by linh on 16/05/2017.
 */

public class SpannableUtil {

    @Nullable
    public static CharSequence replaceGemIcon(Context context, String source) {
        return replaceGemIcon(context, source, 0);
    }

    @Nullable
    public static CharSequence replaceSquareGemIcon(Context context, String source) {
        return replaceWithSquareGemIcon(context, source, 0);
    }

    @Nullable
    public static CharSequence replaceGemIcon(Context context, String source, @ColorRes int boldColor) {
        SpannableStringBuilder spannableString = new SpannableStringBuilder(source);
        Pattern pattern = Pattern.compile(":gem:");
        Matcher matcher = pattern.matcher(source);

        Bitmap gem = null;
        int sizeH = Utils.dpToPx(13f);//(int) (-tvInviteFriends.getPaint().ascent());
        int sizeW = Utils.dpToPx(9f);
        while (matcher.find()) {
            if (gem == null) {
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.refill_gem_icon);
                gem = Bitmap.createScaledBitmap(bitmap, sizeW, sizeH, true);
                bitmap.recycle();
            }
            ImageSpan span = new CenteredImageSpan(context, gem, ImageSpan.ALIGN_BASELINE);
            spannableString.setSpan(span, matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        //remove text holder
        return makeBoldText(context, spannableString.subSequence(0, spannableString.length()), boldColor);
    }
    @Nullable
    public static CharSequence replaceWithSquareGemIcon(Context context, String source, @ColorRes int boldColor) {
        SpannableStringBuilder spannableString = new SpannableStringBuilder(source);
        Pattern pattern = Pattern.compile(":gem:");
        Matcher matcher = pattern.matcher(source);

        Bitmap gem = null;
        int sizeH = Utils.dpToPx(9f);//(int) (-tvInviteFriends.getPaint().ascent());
        int sizeW = Utils.dpToPx(9f);
        while (matcher.find()) {
            if (gem == null) {
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.refill_gem_icon);
                gem = Bitmap.createScaledBitmap(bitmap, sizeW, sizeH, true);
                bitmap.recycle();
            }
            ImageSpan span = new CenteredImageSpan(context, gem, ImageSpan.ALIGN_BASELINE);
            spannableString.setSpan(span, matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        //remove text holder
        return makeBoldText(context, spannableString.subSequence(0, spannableString.length()), boldColor);
    }

    /**
     * make the text which between text holder ** ** bold.
     */
    private static CharSequence makeBoldText(Context context, CharSequence source, @ColorRes int color) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(source);
//        Pattern boldPattern = Pattern.compile("(?<=\\*\\*)(.*)(?=\\*\\*)");
        Pattern boldPattern = Pattern.compile("(\\*\\*)(.+?)(\\*\\*)");
        Matcher boldMatcher = boldPattern.matcher(source);
        int boldColor = (color == 0) ? R.color.color_9b9b9b : color;

        int indexDecreaseHop = 0;
        while (boldMatcher.find()) {//Get your friends inboard BeLive and BOTH of you will receive :gem: **100**
            int start = boldMatcher.start();
            int end = boldMatcher.end();
            String text = boldMatcher.group();
            String boldText = text.substring(2, text.length() - 2);
            spannableStringBuilder.replace(start - indexDecreaseHop, end - indexDecreaseHop, boldText);
            spannableStringBuilder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, boldColor)), start - indexDecreaseHop, end - indexDecreaseHop - 4, 0);
            spannableStringBuilder.setSpan(new CustomTypefaceSpan("", Typeface.createFromAsset(context.getAssets(), "fonts/opensanssemibold.ttf")), start - indexDecreaseHop, end - indexDecreaseHop - 4, 0);
            indexDecreaseHop += 4;
        }
        return spannableStringBuilder.subSequence(0, spannableStringBuilder.length());
    }

    public static CharSequence makeHighLightQuery(Context context, String source, String query) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(source);
        Pattern boldPattern = Pattern.compile(query, Pattern.CASE_INSENSITIVE);
        Matcher boldMatcher = boldPattern.matcher(source);
        while (boldMatcher.find()) {
            int start = boldMatcher.start();
            int end = boldMatcher.end();
            spannableStringBuilder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.color_query_highlight)), start, end, 0);
        }
        return spannableStringBuilder.subSequence(0, spannableStringBuilder.length());
    }
}
