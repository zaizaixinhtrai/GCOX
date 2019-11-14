package com.gcox.fansmeet.util.customtabs_util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.gcox.fansmeet.common.ConstantBundleKey;
import com.gcox.fansmeet.webview.ActivityViewWeb;

/**
 * A Fallback that opens a Webview when Custom Tabs is not available
 */
public class WebviewFallback implements CustomTabActivityHelper.CustomTabFallback {
    @Override
    public void openUri(Context context, Uri uri) {
        Intent intent = new Intent(context, ActivityViewWeb.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(ConstantBundleKey.BUNDLE_URL_FOR_WEBVIEW, uri.toString());
        context.startActivity(intent);
    }
}