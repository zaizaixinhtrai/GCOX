package com.gcox.fansmeet.customview.autolinktextview;

import timber.log.Timber;

/**
 * Created by thanhbc on 6/19/17.
 */

public class AutoLinkItem {
    @AutoLinkMode
    private String autoLinkMode;

    private String matchedText;

    private int startPoint,endPoint;

    AutoLinkItem(int startPoint, int endPoint, String matchedText, @AutoLinkMode String autoLinkMode) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.matchedText = matchedText;
        this.autoLinkMode = autoLinkMode;
    }

    @AutoLinkMode
    public String getAutoLinkMode() {
        return autoLinkMode;
    }

    public String getMatchedText() {
        return matchedText;
    }

    public String getMatchedTextExcludeWildCard(){
        return getMatchedTextExcludeWildCard(matchedText);
    }

    public static String getMatchedTextExcludeWildCard(String matchedText){
        int index = matchedText.indexOf("@");
        try {
            return matchedText.substring(++index);
        }catch (IndexOutOfBoundsException e){
            Timber.e(e);
            return "";
        }
    }

    public int getStartPoint() {
        return startPoint;
    }

    public int getEndPoint() {
        return endPoint;
    }
}
