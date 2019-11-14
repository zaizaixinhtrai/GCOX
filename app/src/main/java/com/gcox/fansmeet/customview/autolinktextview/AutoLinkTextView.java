package com.gcox.fansmeet.customview.autolinktextview;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.view.View;
import com.gcox.fansmeet.customview.CustomFontTextView;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.gcox.fansmeet.customview.autolinktextview.AutoLinkMode.MODE_PHONE;
import static com.gcox.fansmeet.customview.autolinktextview.AutoLinkMode.MODE_CUSTOM;
import static com.gcox.fansmeet.customview.autolinktextview.AutoLinkMode.MODE_EMAIL;
import static com.gcox.fansmeet.customview.autolinktextview.AutoLinkMode.MODE_HASHTAG;
import static com.gcox.fansmeet.customview.autolinktextview.AutoLinkMode.MODE_MENTION;
import static com.gcox.fansmeet.customview.autolinktextview.AutoLinkMode.MODE_URL;


/**
 * Created by thanhbc on 6/19/17.
 */

public class AutoLinkTextView extends CustomFontTextView {

    static final String TAG = AutoLinkTextView.class.getSimpleName();

    private static final int MIN_PHONE_NUMBER_LENGTH = 8;

    private static final int DEFAULT_COLOR = Color.RED;

    private AutoLinkOnClickListener autoLinkOnClickListener;

    @AutoLinkMode
    private String[] autoLinkModes;

    private String customRegex;

    private boolean isUnderLineEnabled = false;

    private int mentionModeColor = Color.parseColor("#98d7de");
    private int hashtagModeColor = DEFAULT_COLOR;
    private int urlModeColor = Color.parseColor("#0000FF");
    private int phoneModeColor = DEFAULT_COLOR;
    private int emailModeColor = DEFAULT_COLOR;
    private int customModeColor = DEFAULT_COLOR;
    private int defaultSelectedColor = Color.LTGRAY;

    public AutoLinkTextView(Context context) {
        super(context);
    }

    public AutoLinkTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setHighlightColor(int color) {
        super.setHighlightColor(Color.TRANSPARENT);
    }

    public void setAutoLinkText(String text) {
        SpannableString spannableString = makeSpannableString(text);
        setText(spannableString);
        setMovementMethod(new LinkTouchMovementMethod());
    }

    public void setAutoLinkText(SpannableString spannableString) {
        List<AutoLinkItem> autoLinkItems = matchedRanges(spannableString.toString());
        setAutoLinkSpan(spannableString, autoLinkItems);
        setText(spannableString);
        setMovementMethod(new LinkTouchMovementMethod());
    }

    public void appendAutoLinkText(String text) {
        SpannableString spannableString = makeSpannableString(text);
        append(spannableString);
        setMovementMethod(new LinkTouchMovementMethod());
    }

    private SpannableString makeSpannableString(String text) {
        final SpannableString spannableString = new SpannableString(text);
        List<AutoLinkItem> autoLinkItems = matchedRanges(text);
        setAutoLinkSpan(spannableString, autoLinkItems);

        return spannableString;
    }

    private void setAutoLinkSpan(SpannableString spannableString, List<AutoLinkItem> autoLinkItems){
        for (final AutoLinkItem autoLinkItem : autoLinkItems) {
            int currentColor = getColorByMode(autoLinkItem.getAutoLinkMode());

            TouchableSpan clickableSpan = new TouchableSpan(currentColor, defaultSelectedColor, isUnderLineEnabled) {
                @Override
                public void onClick(View widget) {
                    if (autoLinkOnClickListener != null)
                        autoLinkOnClickListener.onAutoLinkTextClick(
                                autoLinkItem.getAutoLinkMode(),
                                autoLinkItem.getMatchedText());
                }
            };

            spannableString.setSpan(
                    clickableSpan,
                    autoLinkItem.getStartPoint(),
                    autoLinkItem.getEndPoint(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }


    private List<AutoLinkItem> matchedRanges(String text) {

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

    private int getColorByMode(@AutoLinkMode String autoLinkMode) {
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

    public void setMentionModeColor(@ColorInt int mentionModeColor) {
        this.mentionModeColor = mentionModeColor;
    }

    public void setHashtagModeColor(@ColorInt int hashtagModeColor) {
        this.hashtagModeColor = hashtagModeColor;
    }

    public void setUrlModeColor(@ColorInt int urlModeColor) {
        this.urlModeColor = urlModeColor;
    }

    public void setPhoneModeColor(@ColorInt int phoneModeColor) {
        this.phoneModeColor = phoneModeColor;
    }

    public void setEmailModeColor(@ColorInt int emailModeColor) {
        this.emailModeColor = emailModeColor;
    }

    public void setCustomModeColor(@ColorInt int customModeColor) {
        this.customModeColor = customModeColor;
    }

    public void setSelectedStateColor(@ColorInt int defaultSelectedColor) {
        this.defaultSelectedColor = defaultSelectedColor;
    }

    public void addAutoLinkMode(@AutoLinkMode String... autoLinkModes) {
        this.autoLinkModes = autoLinkModes;
    }

    public void setCustomRegex(String regex) {
        this.customRegex = regex;
    }

    public void setAutoLinkOnClickListener(AutoLinkOnClickListener autoLinkOnClickListener) {
        this.autoLinkOnClickListener = autoLinkOnClickListener;
    }

    public void enableUnderLine() {
        isUnderLineEnabled = true;
    }

}
