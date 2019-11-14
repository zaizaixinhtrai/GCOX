package com.gcox.fansmeet

import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.app.NotificationManager
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.multidex.MultiDex
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import com.appster.extensions.distinctUntilChanged
import com.appster.extensions.showCustomToast
import com.crashlytics.android.Crashlytics
import com.facebook.FacebookSdk
import com.facebook.stetho.Stetho

import com.gcox.fansmeet.appdata.AppPreferences
import com.gcox.fansmeet.common.Constants
import com.gcox.fansmeet.common.Constants.RESPONSE_DUPLICATE_LOGIN
import com.gcox.fansmeet.data.di.appModules
import com.gcox.fansmeet.exception.GcoxException
import com.gcox.fansmeet.features.login.LoginActivity
import com.gcox.fansmeet.manager.SocialManager
import com.gcox.fansmeet.pushnotification.OneSignalUtil
import com.gcox.fansmeet.webservice.AppsterWebServices
import com.onesignal.OneSignal
import com.twitter.sdk.android.core.DefaultLogger
import com.twitter.sdk.android.core.Twitter
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.twitter.sdk.android.core.TwitterConfig
import com.uphyca.stetho_realm.RealmInspectorModulesProvider
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import org.koin.android.ext.android.startKoin
import io.reactivex.ObservableTransformer
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmConfiguration
import timber.log.Timber
import io.fabric.sdk.android.Fabric

import javax.inject.Inject


/**
 * Created by sonnguyen on 9/22/15.
 */
class AppsterApplication : android.support.multidex.MultiDexApplication(), HasActivityInjector {
    lateinit var mContext: Context
    private var subscribeScheduler: Scheduler? = null
    @Inject
    internal var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>? = null
    val multipleLiveData = MutableLiveData<GcoxException>()
    private val schedulersTransformer = ObservableTransformer<Any, Any> { t ->
        t.subscribeOn(defaultSubscribeScheduler())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(defaultSubscribeScheduler())
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        mContext = applicationContext
        application = this
        facebookConfig()
        registerActivityLifecycleCallbacks(ActivityLifecycleHandler())
        twitterConfig()
        setupRealm()
        mAppPreferences = AppPreferences.getInstance(this)

        // start Koin
        startKoin()

//        if (BuildConfig.DEBUG) {
        initStetho()
        Timber.plant(object : Timber.DebugTree() {
            override fun createStackElementTag(element: StackTraceElement): String? {
                return super.createStackElementTag(element) + ":" + element.lineNumber
            }
        })
//        }
        multipleLiveData.distinctUntilChanged().observeForever { s ->
            when (s?.code) {
                RESPONSE_DUPLICATE_LOGIN -> {
                    logout(this)
                    showCustomToast(s.message.toString(), Color.WHITE,R.drawable.toast_background,LENGTH_LONG)
                }
                else -> {
                }
            }
        }
        // Fabric
        Fabric.with(this, Crashlytics())

        // OneSignal - notification
        OneSignalUtil.init(this)
        OneSignal.setLocationShared(false)
        OneSignalUtil.setUser(mAppPreferences.userModel)
    }

    fun startKoin() {
        // start Koin
        startKoin(this, appModules)
    }

    private fun facebookConfig() {
        FacebookSdk.sdkInitialize(applicationContext)
    }

    private fun twitterConfig() {
        val authConfig = TwitterAuthConfig(Constants.TWITTER_KEY, Constants.TWITTER_SECRET)
        val config = TwitterConfig.Builder(this)
            .logger(DefaultLogger(Log.DEBUG))//enable logging when app is in debug mode
            .twitterAuthConfig(authConfig)//pass the created app Consumer KEY and Secret also called API Key and Secret
            .debug(true)//enable debug mode
            .build()

        //finally initialize twitter with created configs
        Twitter.initialize(config)
    }

    private fun setupRealm() {
        // Configure Realm for the application
        Realm.init(this)
        val realmConfiguration = RealmConfiguration.Builder()
            //                .name("appsters.realm")
            .build()
        //        Realm.deleteRealm(realmConfiguration); // Clean slate
        Realm.setDefaultConfiguration(realmConfiguration) // Make this Realm the default
    }


    private fun initStetho() {
        // Create an InitializerBuilder
        val initializerBuilder = Stetho.newInitializerBuilder(this)

        // Enable Chrome DevTools
        initializerBuilder.enableWebKitInspector(
            Stetho.defaultInspectorModulesProvider(this)
        ).enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
            .enableWebKitInspector(
                RealmInspectorModulesProvider.builder(this)
                    .build()
            )

        // Use the InitializerBuilder to generate an Initializer
        val initializer = initializerBuilder.build()

        // Initialize Stetho with the Initializer
        Stetho.initialize(initializer)
    }

    //Reusing Transformers - Singleton
    fun <T> applySchedulers(): ObservableTransformer<T, T> {
        return schedulersTransformer as ObservableTransformer<T, T>
    }

    fun defaultSubscribeScheduler(): Scheduler? {
        if (subscribeScheduler == null) {
            subscribeScheduler = Schedulers.io()
        }
        return subscribeScheduler
    }

    override fun activityInjector(): AndroidInjector<Activity>? {
        return activityDispatchingAndroidInjector
    }

    /**
     * API level 14 and higher
     * https://github.com/adjust/android_sdk#sdk-add
     */
    private class ActivityLifecycleHandler : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

        override fun onActivityStarted(activity: Activity) {}

        override fun onActivityResumed(activity: Activity) {}

        override fun onActivityPaused(activity: Activity) {}

        override fun onActivityStopped(activity: Activity) {}

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {}

        override fun onActivityDestroyed(activity: Activity) {}
    }

    companion object {

        private val TAG = AppsterApplication::class.java.simpleName

        lateinit var application: AppsterApplication
            private set

        lateinit var mAppPreferences: AppPreferences

        @JvmStatic
        @Throws(PackageManager.NameNotFoundException::class)
        fun getCurrentVersionName(context: Context): String {
            val pkg = get(context).packageName
            return get(context).packageManager.getPackageInfo(pkg, 0).versionName
        }

        @JvmStatic
        fun logout(context: Context) {
            application.multipleLiveData.postValue(GcoxException("", 0))
            mAppPreferences.clearAllParamLogin()
            SocialManager.getInstance().logOut()
            val intent = Intent(context.applicationContext, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            val cn = intent.component
            val mainIntent = Intent.makeRestartActivityTask(cn)
            context.applicationContext.startActivity(mainIntent)
            clearAllNotification(context)
            AppsterWebServices.resetAppsterWebserviceAPI()
            saveCurrentVersionCode()
        }

        private fun saveCurrentVersionCode() {
            AppsterApplication.mAppPreferences.saveCurrentVersionCode(BuildConfig.VERSION_CODE)
        }

        private fun clearAllNotification(context: Context) {
            // Clear all notification
            val nMgr =
                context.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nMgr.cancelAll()
        }

        operator fun get(context: Context): AppsterApplication {
            return context.applicationContext as AppsterApplication
        }

        fun getCurrentActivityRunning(context: Context): String {
            var activityName = ""
            val am = get(context).getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val taskInfo = am.getRunningTasks(1)
            //        ComponentName componentInfo = taskInfo.get(0).topActivity;
            //        Log.d("NCS", "CURRENT Activity ::" + taskInfo.get(0).topActivity.getClassName() + "   Package Name :  " + componentInfo.getPackageName());
            if (!taskInfo.isEmpty()) activityName = taskInfo[0].topActivity.className
            return activityName
        }

        /**
         * Checks if the application is being sent in the background (i.e behind
         * another application's Activity).
         *
         * @return `true` if another application will be above this one.
         */
        fun isApplicationSentToBackground(context: Context): Boolean {

            var isInBackground = true
            val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
                val runningProcesses = am.runningAppProcesses
                if (runningProcesses != null) {
                    for (processInfo in runningProcesses) {
                        if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                            for (activeProcess in processInfo.pkgList) {
                                if (activeProcess == context.packageName) {
                                    isInBackground = false
                                }
                            }
                        }
                    }
                }
            } else {
                val taskInfo = am.getRunningTasks(1)
                val componentInfo = taskInfo[0].topActivity
                if (componentInfo.packageName == context.packageName) {
                    isInBackground = false
                }
            }

            return isInBackground
        }

        @JvmStatic
        val deviceName: String
            get() {
                val manufacturer = Build.MANUFACTURER
                val model = Build.MODEL
                return if (model.startsWith(manufacturer)) {
                    capitalize(model)
                } else {
                    capitalize(manufacturer) + " " + model
                }
            }

        private fun capitalize(s: String?): String {
            if (s == null || s.length == 0) {
                return ""
            }
            val first = s[0]
            return if (Character.isUpperCase(first)) {
                s
            } else {
                Character.toUpperCase(first) + s.substring(1)
            }
        }
    }
}
