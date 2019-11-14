package com.gcox.fansmeet.customview.autolinktextview;

import android.graphics.Color;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.view.View;
import com.gcox.fansmeet.core.activity.BaseActivity;
import com.gcox.fansmeet.util.CustomTabUtils;
import com.gcox.fansmeet.util.NavigationUtil;
import timber.log.Timber;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.gcox.fansmeet.customview.autolinktextview.AutoLinkMode.*;


/**
 * Created by linh on 22/06/2017.
 */

public class AutoLinkUtil {
    private static final int DEFAULT_COLOR = Color.RED;
    public static int mentionModeColor = Color.parseColor("#98d7de");
    private static int hashtagModeColor = DEFAULT_COLOR;
    private static int urlModeColor = Color.parseColor("#0000FF");
    private static int phoneModeColor = DEFAULT_COLOR;
    private static int emailModeColor = DEFAULT_COLOR;
    private static int customModeColor = DEFAULT_COLOR;
    private static int defaultSelectedColor = Color.LTGRAY;

    private static final int MIN_PHONE_NUMBER_LENGTH = 8;

    public static List<AutoLinkItem> matchedRanges(String text, @AutoLinkMode String[] autoLinkModes) {
        return matchedRanges(text, autoLinkModes, "");
    }

    public static List<AutoLinkItem> matchedRanges(String text, @AutoLinkMode String[] autoLinkModes, String customRegex) {
        List<AutoLinkItem> autoLinkItems = new LinkedList<>();

        if (autoLinkModes == null) {
            throw new NullPointerException("Please add at least one mode");
        }

        for (String anAutoLinkMode : autoLinkModes) {
            String regex = Utils.getRegexByAutoLinkMode(anAutoLinkMode, customRegex);
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(text);

            if (anAutoLinkMode.equalsIgnoreCase(MODE_PHONE)) {
                while (matcher.find()) {
                    if (matcher.group().length() > MIN_PHONE_NUMBER_LENGTH)
                        autoLinkItems.add(new AutoLinkItem(
                                matcher.start(),
                                matcher.end(),
                                matcher.group(),
                                anAutoLinkMode));
                }
            } else {
                while (matcher.find()) {
                    autoLinkItems.add(new AutoLinkItem(
                            matcher.start(),
                            matcher.end(),
                            matcher.group(),
                            anAutoLinkMode));
                }
            }
        }

        return autoLinkItems;
    }

    public static int getColorByMode(@AutoLinkMode String autoLinkMode) {
        switch (autoLinkMode) {
            case MODE_HASHTAG:
                return hashtagModeColor;
            case MODE_MENTION:
                return mentionModeColor;
            case MODE_URL:
                return urlModeColor;
            case MODE_PHONE:
                return phoneModeColor;
            case MODE_EMAIL:
                return emailModeColor;
            case MODE_CUSTOM:
                return customModeColor;
            default:
                return DEFAULT_COLOR;
        }
    }

    public static void addAutoLinkMode(AutoLinkTextView... views){
        for (AutoLinkTextView view : views) {
            view.addAutoLinkMode(MODE_URL, MODE_MENTION);
        }
    }

    public static AutoLinkOnClickListener newListener(BaseActivity activity){
        return (autoLinkMode, matchedText) -> {
                Timber.e("%s - %s", autoLinkMode, matchedText);
                switch (autoLinkMode){
                    case MODE_MENTION:
                        NavigationUtil.gotoProfileScreen(activity, AutoLinkItem.getMatchedTextExcludeWildCard(matchedText));
                        break;

                    case MODE_URL:
                        CustomTabUtils.openChromeTab(activity, matchedText);
                        break;
                }
            };
    }

    public static SpannableString formatAutoLink(SpannableString spannableString, List<AutoLinkItem> autoLinkItems,
                                                 boolean isUnderLineEnabled, AutoLinkOnClickListener autoLinkOnClickListener){
        for (final AutoLinkItem autoLinkItem : autoLinkItems) {
            formatAutoLink(spannableString, autoLinkItem, isUnderLineEnabled, autoLinkOnClickListener);
        }
        return spannableString;
    }

    public static SpannableString formatAutoLink(SpannableString spannableString, AutoLinkItem autoLinkItem,
                                                 boolean isUnderLineEnabled, AutoLinkOnClickListener autoLinkOnClickListener){
        int currentColor = AutoLinkUtil.getColorByMode(autoLinkItem.getAutoLinkMode());
        spannableString.setSpan(
                getTouchableSpan(currentColor, isUnderLineEnabled, autoLinkItem, autoLinkOnClickListener),
                autoLinkItem.getStartPoint(),
                autoLinkItem.getEndPoint(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public static Editable formatAutoLink(Editable editable, AutoLinkItem autoLinkItem,
                                                 boolean isUnderLineEnabled, AutoLinkOnClickListener autoLinkOnClickListener){
        int currentColor = AutoLinkUtil.getColorByMode(autoLinkItem.getAutoLinkMode());
        editable.setSpan(
                getTouchableSpan(currentColor, isUnderLineEnabled, autoLinkItem, autoLinkOnClickListener),
                autoLinkItem.getStartPoint(),
                autoLinkItem.getEndPoint(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return editable;
    }

    public static ClickableSpan getTouchableSpan(int color, boolean isUnderLineEnabled, AutoLinkItem autoLinkItem ,AutoLinkOnClickListener autoLinkOnClickListener){
        return getTouchableSpan(color, isUnderLineEnabled, autoLinkItem.getAutoLinkMode(), autoLinkItem.getMatchedText(), autoLinkOnClickListener);
    }

    public static ClickableSpan getTouchableSpan(int color, boolean isUnderLineEnabled, String autoLinkMode, String autoLinkMatchedText,AutoLinkOnClickListener autoLinkOnClickListener){
        return  new TouchableSpan(color, defaultSelectedColor, isUnderLineEnabled) {
            @Override
            public void onClick(View widget) {
                if (autoLinkOnClickListener != null)
                    autoLinkOnClickListener.onAutoLinkTextClick(autoLinkMode, autoLinkMatchedText);
            }
        };
    }
}
