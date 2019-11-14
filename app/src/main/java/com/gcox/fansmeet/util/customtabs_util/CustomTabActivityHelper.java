package com.gcox.fansmeet.util.customtabs_util;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.v4.content.ContextCompat;
import com.gcox.fansmeet.R;

import java.util.List;

public class CustomTabActivityHelper {
    private CustomTabsSession mCustomTabsSession;
    private CustomTabsClient mClient;
    private CustomTabsServiceConnection mConnection;
    private ConnectionCallback mConnectionCallback;

    public static CustomTabsIntent.Builder getBuilderIntent(Context context, String url) {
        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();
        int color = ContextCompat.getColor(context, R.color.background_tabtrip);
        intentBuilder.setToolbarColor(color);
        intentBuilder.setShowTitle(true);
        String menuItemTitle = context.getString(R.string.custom_tab_menu_title_share);
        PendingIntent menuItemPendingIntent = createPendingShareIntent(context, url);
        intentBuilder.addMenuItem(menuItemTitle, menuItemPendingIntent);

        intentBuilder.setStartAnimations(context.getApplicationContext(),
                R.anim.custom_tab_slide_up, R.anim.custom_tab_donothing);
        intentBuilder.setExitAnimations(context.getApplicationContext(),
                0, R.anim.custom_tab_slide_down);
        intentBuilder.setActionButton(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_share),
                context.getString(R.string.custom_tab_menu_title_share), createPendingShareIntent(context, url), true);

        return intentBuilder;
    }

    private static PendingIntent createPendingShareIntent(Context context, String url) {
        Intent actionIntent = new Intent(Intent.ACTION_SEND);
        actionIntent.setType("text/plain");
        actionIntent.putExtra(Intent.EXTRA_TEXT, url.toLowerCase());
        return PendingIntent.getActivity(context.getApplicationContext(), 100, actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * Opens the URL on a Custom Tab if possible. Otherwise fallsback to opening it on a WebView
     *
     * @param activity          The host context
     * @param customTabsIntent a CustomTabsIntent to be used if Custom Tabs is available
     * @param uri              the Uri to be opened
     * @param fallback         a CustomTabFallback to be used if Custom Tabs is not available
     */
    public static void openCustomTab(Activity activity,
                                     CustomTabsIntent customTabsIntent,
                                     Uri uri,
                                     CustomTabFallback fallback) {
        String packageName = CustomTabsHelper.getPackageNameToUse(activity.getApplicationContext());

        //If we cant find a package name, it means there's no browser that supports
        //Chrome Custom Tabs installed. So, we fallback to the webview
        if (packageName == null) {
            if (fallback != null) {
                fallback.openUri(activity.getApplicationContext(), uri);
            }
        } else {
            customTabsIntent.intent.setPackage(packageName);
//            customTabsIntent.intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            customTabsIntent.launchUrl(activity, uri);
        }
    }

    /**
     * Unbinds the Activity from the Custom Tabs Service
     *
     * @param activity the activity that is connected to the service
     */
    public void unbindCustomTabsService(Activity activity) {
        if (mConnection == null) return;
        activity.unbindService(mConnection);
        mClient = null;
        mCustomTabsSession = null;
    }

    /**
     * Creates or retrieves an exiting CustomTabsSession
     *
     * @return a CustomTabsSession
     */
    public CustomTabsSession getSession() {
        if (mClient == null) {
            mCustomTabsSession = null;
        } else if (mCustomTabsSession == null) {
            mCustomTabsSession = mClient.newSession(null);
        }
        return mCustomTabsSession;
    }

    /**
     * Register a Callback to be called when connected or disconnected from the Custom Tabs Service
     *
     * @param connectionCallback
     */
    public void setConnectionCallback(ConnectionCallback connectionCallback) {
        this.mConnectionCallback = connectionCallback;
    }

    /**
     * Binds the Activity to the Custom Tabs Service
     *
     * @param activity the activity to be binded to the service
     */
    public void bindCustomTabsService(Activity activity) {
        if (mClient != null) return;

        String packageName = CustomTabsHelper.getPackageNameToUse(activity);
        if (packageName == null) return;
        mConnection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client) {
                mClient = client;
                mClient.warmup(0L);
                if (mConnectionCallback != null) mConnectionCallback.onCustomTabsConnected();
                //Initialize a session as soon as possible.
                getSession();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mClient = null;
                if (mConnectionCallback != null) mConnectionCallback.onCustomTabsDisconnected();
            }
        };
        CustomTabsClient.bindCustomTabsService(activity, packageName, mConnection);
    }

    public boolean mayLaunchUrl(Uri uri, Bundle extras, List<Bundle> otherLikelyBundles) {
        if (mClient == null) return false;

        CustomTabsSession session = getSession();
        if (session == null) return false;

        return session.mayLaunchUrl(uri, extras, otherLikelyBundles);
    }

    /**
     * A Callback for when the service is connected or disconnected. Use those callbacks to
     * handle UI changes when the service is connected or disconnected
     */
    public interface ConnectionCallback {
        /**
         * Called when the service is connected
         */
        void onCustomTabsConnected();

        /**
         * Called when the service is disconnected
         */
        void onCustomTabsDisconnected();
    }

    /**
     * To be used as a fallback to open the Uri when Custom Tabs is not available
     */
    public interface CustomTabFallback {
        /**
         * @param context The Context that wants to open the Uri
         * @param uri     The uri to be opened by the fallback
         */
        void openUri(Context context, Uri uri);
    }

}