package com.gcox.fansmeet.common;

import android.os.Environment;
import android.support.annotation.IntDef;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class Constants {

    // public static String google_project_id = "975251251047";
    public static String device_token = "";
    public static String language = "";

    /**
     * ********** twitter ****************************
     */
    public static String CONSUMER_KEY = "80X2LrLzEJsv1Q5hNLxPp2yqt";
    public static String CONSUMER_SECRET = "goVH6NfYbhBeP9pe0LUOxAyGzZhjfyU5z4oP4jhB7DmxD8S7So";

    public static final String PREF_KEY_ACCESS_TOKEN = "469990572-OCswtxQ0vXNj5J9oTllTr1jPoxpeyb9YIVx1kpXS";
    public static final String PREF_KEY_ACCESS_TOKEN_SECRET = "Wf9BiiOsnlrVTZ8NxFjRAIfKIQP56cvO0mkuSaQrXeV25";

    public static final String INSTAGRAM_CLIENT_ID = "a1271b5bc6f24337b98a27005bbbfe0c";
    public static final String INSTAGRAM_CLIENT_SECRET = "10484067785a464fa6b4190fef58f470";
    public static final String INSTAGRAM_CALLBACK_URL = "https://famousmeetapi.acclaim.io";

    public static final String WECHAT_APP_ID = "wxb7f9bbfadd3b7ea6";
    public static final String WECHAT_SECRET = "c660c2f1fcdfc4fc411add201132deda";

    public static final String WEIBO_APP_KEY = "2201065481";
    public static final String WEIBO_REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
    public static final String WEIBO_SCOPE = "friendships_groups_write";

    public static final String PREF_NAME = "com.example.android-twitter-oauth-demo";
    public static final String CALLBACK_URL = "https://sgintractive.com";
    public static final String IEXTRA_AUTH_URL = "auth_url";
    public static final String IEXTRA_OAUTH_VERIFIER = "oauth_verifier";
    public static final String IEXTRA_OAUTH_TOKEN = "oauth_token";

    public final static String FILE_START_NAME = "Appsters_";
    public final static String VIDEO_EXTENSION = ".mp4";
    public final static String IMAGE_EXTENSION = ".jpg";
    public final static String WATERMARK_NAME = "video_watermark";
    public final static String WATERMARK_EXTENSION = ".png";
    public final static String DCIM_FOLDER = "/DCIM";
    public final static String IMAGE_FOLDER = "/image";
    public final static String VIDEO_FOLDER = "/video";
    public final static String TEMP_FOLDER = "/Temp";


    public final static String TEMP_FOLDER_PATH = Environment.getExternalStorageDirectory().toString() + DCIM_FOLDER + VIDEO_FOLDER + TEMP_FOLDER;
    public final static String VIDEO_CONTENT_URI = "content://media/external/video/media";

    public static final int VIDEO_RECORDED_POST_STATUS_REQUEST = 1371;
    public static final int PICK_VIDEO_FROM_LIBRARY_REQUEST = 1370;
    public static final int CAMERA_VIDEO_REQUEST = 1373;
    public static final int VIDEO_TRIMMED_REQUEST = 1372;
    public static final String VIDEO_CACHE_FOLDER = Environment.getExternalStorageDirectory() + File.separator + ".AppstersVideoCachingNew";
    public static final String FILE_CACHE_FOLDER = Environment.getExternalStorageDirectory() + File.separator + ".AppstersFileCachingNew";
    public static final int MAX_VIDEO_CACHE_NUMBER = 10;


    public final static int RESOLUTION_HIGH_VALUE = 2;
    public final static int RESOLUTION_MEDIUM_VALUE = 1;
    public final static int RESOLUTION_LOW_VALUE = 0;

    public final static int OUTPUT_WIDTH = 480;
    public final static int OUTPUT_HEIGHT = 480;

    public final static boolean DO_YOU_WANT_WATER_MARK_ON_VIDEO = true;

    public static String address = "";

    public static String APP_LANGUAGE_CODE = "en";
    public static Boolean vote = true;

    public static final int PAGE_LIMITED = 20;
    public static final int PAGE_LIMITED_1000 = 1000;
    public static final int MAX_VIDEO_DURATION_TIME = 15; //Second

    public static final int MIN_VIDEO_DURATION_TIME = 3; //Second

    public static final int VIDEOS_FEED = 2;
    public static final int IMAGE_FEED = 1;
    public static final int QUOTES_FEED = 0;
    public static final int MIN_USER_NAME = 3;
    public static final int MAX_USER_NAME = 20;
    public static final int ITEM_PAGE_LIMITED = 10;
    public static final int ITEM_PAGE_LIMITED_FOR_GRID = 21;

    public static final int VERIFICATION_CODE_LENGTH = 6;
    public static final int MIN_LENGTH_USER_PASSWORD = 6;
    public static final int MAX_LENGTH_USER_PASSWORD = 20;
    public static final int MIX_DISPLAY_NAME = 3;
    public static final int MAX_DISPLAY_NAME = 16;

    public static final String GENDER_MALE = "Male";
    public static final String GENDER_FEMALE = "Female";
    public static final String GENDER_SECRET = "Secret";

    public static String getFBImageProfile(String fbid) {
        return " https://graph.facebook.com/" + fbid + "/picture?type=large&height=800&width=800";
    }

    public static final int HEIGHT_OF_VIDEOS_VIEW = 350;
    public static final int MESSAGE_LOGIN_LIVE_STREAM_SUCCESS = 1001;
    public static final int MESSAGE_LOGIN_LIVE_STREAM_ERROR = 1002;
    public static final int MESSAGE_GET_CONFIG_SERVER_LIVE_STREAM_SUCCESS = 1003;
    public static final int MESSAGE_GET_CONFIG_SERVER_LIVE_STREAM_ERROR = 1004;
    public static final int MESSAGE_SHOW_DIALOG_PROGRESS = 1005;

    public static final String VIDEO_PATH = "VIDEO_PATH";
    public static final String ACTIVITY_FEED_ID = "ACTIVITY_FEED_ID";
    public static final String VIDEO_SEEK_TO_POSITION = "VIDEO_SEEK_TO_POSITION";
    public static final String EMPTY_STRING = "";

    public static final String videoPath = Environment
            .getExternalStorageDirectory().getPath() + "/tmpVideo.mp4";

    public static final String videoPathTrim = Environment
            .getExternalStorageDirectory().getPath() + "/tmpVideo_trim.mp4";

    public static final String videoPathReduceVideoQuality = Environment
            .getExternalStorageDirectory().getPath() + "/tmpVideo_reduce_video_quality.mp4";

    public static final String RETURN_DATA = "return-data";

    public static final int REQUEST_PIC_FROM_CAMERA = 1360;
    public static final int REQUEST_PIC_FROM_LIBRARY = 1361;
    public static final int REQUEST_PIC_FROM_CROP = 1365;
    public static final int REQUEST_COVER_FROM_CAMERA = 1366;
    public static final int REQUEST_COVER_FROM_LIBRARY = 1367;


    // Request code Comment
    public static final int COMMENT_REQUEST = 2001;

    // Request code Setting
    public static final int SETTING_REQUEST = 2002;

    public static final int POST_REQUEST = 2003;

    public static final int POST_CHALLENGE_REQUEST = 2004;

    public static final int REQUEST_EDIT_POST = 2010;

    public static final int REQUEST_EDIT_SUBMISSION = 2034;

    public static final int REQUEST_EDIT_CHALLENGE = 2033;

    public static final int REQUEST_VIEW_NOTIFY = 2011;

    public static final int REQUEST_MENU = 2012;

    public static final int REQUEST_NOTIFICATION = 2013;

    public static final int REQUEST_FOLLOW = 2014;

    public static final int REQUEST_SETTING_LOCATION = 2015;

    public static final int REQUEST_CODE_EDIT_PROFILE = 999;

    public static final int REQUEST_CODE_VIEW_USER_PROFILE = 2017;

    public static final int REQUEST_CODE_VIEW_POST_DETAIL = 2018;

    public static final int REQUEST_CODE_SHARE_FEED = 2019;
    public static final int REQUEST_CODE_REFILL_SCREEN = 2020;

    public static final int REQUEST_MEDIA_PLAYER_STREAM = 2021;

    public static final int REQUEST_BLOCKED_USER_LIST = 2022;

    public static final int REQUEST_SEARCH_ACTIVITY = 2023;

    public static final int REQUEST_LIKED_USERS_LIST_ACTIVITY = 2024;

    public static final int REQUEST_TOPFAN_ACTIVITY = 2025;

    public static final int REQUEST_MESSAGE_LIST_ACTIVITY = 2026;
    public static final int REQUEST_CATEGORY_DETAIL_ACTIVITY = 2027;
    public static final int REQUEST_SOCIAL_INVITE_FRIEND_ACTIVITY = 2028;

    public static final int REQUEST_REDEMPTION = 2028;
    public static final int TWEET_SHARE_REQUEST_CODE = 2029;
    public static final int REQUEST_PRIZE_BAG = 2030;

    public static final int REQUEST_CHALLENGE_DETAIL_ACTIVITY = 2031;
    public static final int REQUEST_CHALLENGE_ENTRIES_ACTIVITY = 2032;

    public static final int REQUEST_PHOTO_EDITOR_ACTIVITY = 2035;

    // Web Url
    public static final String URL_ABOUT_US = "https://gcoxgroup.com/about.html";
    public static final String URL_PAYPAL = "https://www.paypal.com/sg/webapps/mpp/account-selection";

    public static final String URL_TERMS_CONDITION = "https://gcoxgroup.com/edutainment-app-term.html";
    public static final String URL_FAQ = "http://appsters.net/home/faq";
    public static final String URL_HELP_CENTER = "http://appsters.net/home/customer_support";
    public static final String URL_PRIVACY_POLICY = "https://gcoxgroup.com/edutainment-app-policy.html";
    public static final String URL_REWARDS_HOW_TO_PLAY_END_LINK = "prize/howtoplay";

    public static final int RETROFIT_ERROR = -1;

    public static final String USER_PROFILE_ID = "USER_PROFILE_ID";
    public static final String ARG_USER_NAME = "ARG_USER_NAME";
    public static final String USER_PROFILE_DISPLAYNAME = "USER_PROFILE_DISPLAYNAME";
    static final int MIN_AGE = 17;
    static final int MAX_AGE = 80;
    public static final int REQUEST_CODE_LOGIN = 3000;
    public static final int REQUEST_CODE_LOGOUT = 4000;
    public static final String CHALLENGE_ID = "challenge_id";
    public static final String ENTRIES_ID = "entries_id";

    // App language
    public static final String APP_LANGUAGE_ENGLISH_EN = "en";
    public static final String APP_LANGUAGE_TRADITIONAL_ZH = "zh";
    public static final String APP_LANGUAGE_SIMPLIFIED_SP = "sp";

    public static final int APP_LANGUAGE_TYPE_ENGLISH = 0;
    public static final int APP_LANGUAGE_TYPE_TRADITIONAL = 1;
    public static final int APP_LANGUAGE_TYPE_SIMPLIFIED = 2;

    //======= push notification types synced from server side
    public static final int NOTIFYCATION_TYPE_MESSAGE = 1;
    public static final int NOTIFYCATION_TYPE_RECEIVE_GIFT = 2;
    public static final int NOTIFYCATION_TYPE_LIKE = 3;
    public static final int NOTIFYCATION_TYPE_FOLLOW = 4;
    public static final int NOTIFYCATION_TYPE_THANKS = 5;//not used. for referral code
    public static final int NOTIFYCATION_TYPE_COMMENT = 6;
    public static final int NOTIFYCATION_TYPE_COMISSION = 7;
    public static final int NOTIFYCATION_TYPE_POST = 14;
    public static final int NOTIFYCATION_TYPE_TAG = 26;
    public static final int NOTIFYCATION_TYPE_CASHBACK = 8;//not used. for cash out
    public static final int NOTIFYCATION_TYPE_LIVESTREAM = 9;
    public static final int NOTIFYCATION_TYPE_FROMADMINMSG = 10;
    public static final int NOTIFYCATION_TYPE_FROMADMINCREDIT = 11;
    public static final int NOTIFYCATION_TYPE_UNKNOWN = 12;//not used
    public static final int NOTIFYCATION_TYPE_LIKE_STREAM = 13;
    public static final int NOTIFYCATION_TYPE_NEWPOST = 14;//for RED DOT in wallfeed
    public static final int NOTIFYCATION_TYPE_ADMINBONUSGIFT = 15;
    public static final int NOTIFYCATION_TYPE_AWARD_FROM_CAMPAIGN = 16;//for voting bar
    public static final int NOTIFYCATION_TYPE_MAINTENANCE = 17; //not used
    public static final int NOTIFYCATION_TYPE_WITHDRAW = 18;//for cash out, when Admin user approved request on Admin
    public static final int NOTIFYCATION_TYPE_END_STREAM = 19;//not used
    public static final int NOTIFYCATION_TYPE_REPORTED_STREAM = 20;//not used
    public static final int NOTIFYCATION_TYPE_MAINTENACE_STANDBY = 21;//not used
    public static final int NOTIFYCATION_TYPE_MAINTENACE_START = 22;//not used
    public static final int NOTIFYCATION_TYPE_COMMENT_STREAM = 23;
    public static final int NOTIFYCATION_TYPE_REMINDER = 24;//For Remind in Admin Push
    public static final int NOTIFYCATION_TYPE_TAG_USER_ON_POST = 25;
    public static final int NOTIFYCATION_TYPE_TAG_USER_ON_COMMENT_POST = 26;
    public static final int NOTIFYCATION_TYPE_TAG_USER_ON_COMMENT_STREAM = 27;
    public static final int NOTIFYCATION_TYPE_NEW_RECORD = 28;//not used
    public static final int NOTIFICATION_DAILY_BONUS_NEW_DAY = 29;
    public static final int NOTIFICATION_DAILY_BONUS_RESET_WEEK = 30;
    //==== end push notification types

    public static final int NOTIFYCATION_TYPE_YOU = 1;
    public static final int NOTIFYCATION_TYPE_FOLLOWING = 2;
    public static final int NOTIFYCATION_TYPE_ENDSTREAM = 19;
    public static final int NOTIFYCATION_TYPE_SERVER_MAINTENANCE_STANDBY = 21;
    public static final int NOTIFYCATION_TYPE_SERVER_MAINTENANCE_START = 22;
    public static final int NOTIFYCATION_USER_TAGGED_IN_POST = 25;
    public static final int NOTIFYCATION_USER_TAGGED_IN_POST_COMMENT = 26;
    public static final int NOTIFYCATION_USER_TAGGED_IN_STREAM_COMMENT = 27;


    //amplitude constants
    public static final String AMPLITUDE_EVENT_PUSH_TYPE_DAILY_BONUS_NEW_DAY = "DAILY_BONUS_NEW_DAY";
    public static final String AMPLITUDE_EVENT_PUSH_TYPE_DAILY_BONUS_RESET_WEEK = "DAILY_BONUS_NEW_WEEK";

    // For intent push
    public static final String PUSH_NOTIFICATION_INTENT = "new_push_notification";

    public static final int ANDROID_DEVICE_TYPE = 0;//0 for Android, 1 for iOS

    // Trending
    public static final int TRENDING_THIS_WEEK_TYPE = 0;
    public static final int TRENDING_THIS_MONTH_TYPE = 1;
    public static final int TRENDING_THIS_ALL_TIME_TYPE = 2;

    public static final int BITMAP_THUMBNAIL_HEIGHT = 480;
    public static final int BITMAP_THUMBNAIL_WIDTH = 480;

    public static int HEIGHT_TEXTVIEW_NAME_HOME = 0;
    public static final String INTENT_MEDIA_URL_PATH = "media_path";

    // For Camera record
    public final static File SDROOT = Environment.getExternalStorageDirectory();
    private final static String APPSTERS_FOLDER = "/Appsters";
    public final static String CAMERA_DIR = APPSTERS_FOLDER + "/camera/";
    public final static String WORKING_VIDEO_PATH = Environment.getExternalStorageDirectory() + APPSTERS_FOLDER + "/video/";
    public final static String VIDEO_KIT_FOLDER = Environment.getExternalStorageDirectory() + "/videokit/";
    public final static String CONFIGURATION_URL = SDROOT + APPSTERS_FOLDER + "/config/";
    public final static String USER_UNIQUE_ID_NAME = "/uniqueUser.txt";


    public static final int LIMIT_TEXT_LENG_POST = 300;
    public static final int LIMIT_DISPLAY_NAME = 16;
    public static final int LIMIT_BIO = 80;

    public static int HEIGHT_PROGRESS_LOAD_MORE_GRID = 0;

    public static final byte TYPE_LIVE_FOLLOWING = 1;
    public static final byte TYPE_LIVE_TRENDING = 2;

    public static byte PERCENT_MIN_VISIBLE = 20;
    public static byte PERCENT_MAX_GONE = 28;

    public static String LOGIN_PLAYTOKEN_GAME_ID = "13b090fd-5508-4af3-93ee-e9bd4424e014";
    public static final String REFERRAL_ID = "referralId";//used in branch io to transfer the sharing user's referral code.

    public enum LoginType {
        SOCIAL_TYPE,
        PLAYTOKEN_TYPE,
        PHONE_NUMBER_TYPE
    }

    // NewsFeet Screen
    public static final byte NEWS_FEED_TRENDING_POST_TYPE = 2;
    public static final byte NEWS_FEED_FOLLOWING_TYPE = 1;
    public static final byte TYPE_PROFILE_ME = 100;

    public static final String BUNDLE_LIVE_STREAM_MESSAGE = "tweetMsg";
    public static final String BUNDLE_LIVE_STEAM_DORECORD = "doRecord";
    public static final String BUNDLE_LIVE_STREAM_CATEGORY_ID = "categoryId";
    public static final String BUNDLE_LIE_STREAM_MESSAGE_ORG = "tweetMsgOrg";


    // Live show type
    public static final int LIVE_SHOW_TYPE_SPORT = 1;
    public static final int LIVE_SHOW_TYPE_FASHION = 2;
    public static final int LIVE_SHOW_TYPE_GAME = 3;
    public static final int LIVE_SHOW_TYPE_MUSIC = 4;

    public static final int INSTAGRAM_SHARE_RETURN = 2;
    public static final int TWITTER_SHARE_RETURN = 3;
    public static final int WHATSAPP_SHARE_RETURN = 4;


    public final static String APPSTERS_IMAGE_SHARE = Environment.getExternalStorageDirectory() + "/Appsters/Picture";
    public  static final int MAX_IMAGE_SHARE_CACHE_NUMBER = 10;

    public static final int IS_FOLLOWING_USER = 1;
    public static final int UN_FOLLOW_USER = 0;

    // NewsFeet Screen
    public static final int NEWS_FEED_LIKE = 1;
    public static final int NEWS_FEED_UNLIKE = 0;

    public static final int RESPONSE_FROM_WEB_SERVICE_OK = 1;
    public static final int RESPONSE_FROM_WEB_SERVICE_404 = 404;
    public static final int RESPONSE_FROM_WEB_SERVICE_1404 = 1404;
    public static final int HAS_BEEN_REPORT_POST = 1;
    public static final int NOT_REPORT_POST = 0;
    public static final int RESPONSE_DUPLICATE_LOGIN = 1341;

    public static final String KEYBOARD_HEIGHT = "KEYBOARD_HEIGHT";

    public static class StreamStatus {
        public static final int StreamCreated = 0;
        public static final int StreamStart = 1;
        public static final int StreamEnd = 2;
    }

    public static final int IS_LIVING_STREAM = 1;

    public static class TYPES_SCHEME_SCREEN {
        public static final String SCHEME_POST = "post";
        public static final String SCHEME_STREAM = "stream";
        public static final String SCHEME_USER_DETAIL = "profile";
    }

    public static final int HOME_BANNER_ITEM = 0;
    public static final int HOME_LIVE_STREAM_ITEM = 1;
    public static final int HOME_TAG_ID_HOT = 0;

    public static final int LIMIT_TEXT_ABOUT = 300;

    public static final String WEB_VIEW_URL = "url";
    public static final String MESSAGE_TYPE_GIFT = "gift";

    public static final int LIMIT_TOTAL_STARS_IN_VIEW = 10000000;

    public static final int LIST_USER_POST_LIVE_STREAM = 1;
    public static final int LIST_USER_POST_NOMAL = 2;

    public static final int EVENT_TYPE_LINK_USER = 0;
    public static final int EVENT_TYPE_LINK_EVENT = 1;
    public static final int EVENT_TYPE_LINK_CATEGORY = 2;

    public static final int STREAM_HAS_ENDED = 2;

    public static final int NUMBER_COMMENT_SHOW = 2;
    public static final int NUMBER_LINE_COMMENT_SHOW = 5;
    public static final int WATCH_VIDEOS_TIME = 5000;

    public static final int ID_FOR_SEARCH_FRAGMENT = 999;

    public static final int UN_KNOW_ERROR = 9999999;

    public static final String TWITTER_KEY = "pvJrIqNYo37mPY7PhuZG2xamB";
    public static final String TWITTER_SECRET = "3QGY1yhqo5w5WrWvgAsBhoxC7itFvqApX62eSxkGRTWzFxOQyD";
    public static final int LIVE_STREAM_REQUEST = 2022;

    /*****************
     * stream
     ***************/
    public static final String STREAM_BLOCKED_LIST = "blocked_list";
    public static final String STREAM_MUTE_LIST = "muted_list";
    //    public static final String GUEST_USER_NAME = BuildConfig.NAME_USER_DEFAULT_XMPP;
    public static final String GUEST_DISPLAY_NAME = "Guest";
    public static final String GUEST_ADDRESS = "It\'s a secret";

    public static String AWS_S3_SERVER_LINK = "";
    public static int HOURS_CACHE_STATIS_RESOURCES = 0;
    public static boolean VIDEO_CALL_ENABLE = false;
    public static String INVITE_FRIENDS_CONTENT = "";
    public static final String WEB_URL_REGEX = "\\(?\\b(http://|www[.]|https://|Www[.]|Https://|Http://)[-A-Za-z0-9+&@#/%?=~_()|!:,.;]*[-A-Za-z0-9+&@#/%=~_()|]";

    public static final int MAX_LENGTH_INPUT_USER_ID = 16;
    public static final int MIN_LENGTH_INPUT_USER_ID = 3;

    public static final int MAINTENANCE_MODE_STOP = 0;
    public static final int MAINTENANCE_MODE_STANDBY = 1;
    public static final int MAINTENANCE_MODE_START = 2;

    public static final int DELAYED_TIME_SHOWN_NEXT_TUTORIAL = 500;//milliseconds


    //== comment activity
    public static final int COMMENT_TYPE_POST = 0;
    public static final int COMMENT_TYPE_STREAM = 1;

    @IntDef({COMMENT_TYPE_POST, COMMENT_TYPE_STREAM})
    @Retention(RetentionPolicy.SOURCE)
    public @interface COMMENT_TYPE {
    }


    /*****************
     * tracking stream
     ***************/
    public static final String TRACKING_STREAM_FORMAT = "%s, %s, %s, %s , %s";
    public static final String LIVE_STREAM = "live";
    public static final String RECORDED_STREAM = "recorded";
    public static final String TARGET_HOST = "host";
    public static final String TARGET_VIEWER = "viewer";
    public static final String TRIGGER_END_BY_ADMIN_BANNED_API = "admin banned from API";
    public static final String TRIGGER_END_BY_ADMIN_BANNED_XMPP = "admin banned from XMPP";
    public static final String TRIGGER_END_BY_ADMIN_DELETED_XMPP = "admin deleted from XMPP";
    public static final String TRIGGER_END_BY_RECEIVIED_STATUS_END_STATISTIC_API = "recieved stream status END from Statistic API";
    public static final String TRIGGER_END_BY_USER_MANUAL = "user ended manual";
    public static final String TRIGGER_END_BY_ERROR_START_STREAM = "error when start stream (device logined same account to start stream)";
    public static final String TRIGGER_END_BY_RECORDED_ENDED_NOT_SAVED = "recorded stream has been ended but don't record in server database (user don't tap on Save button)";
    public static final String TRIGGER_END_BY_RECORDED_SCRUB_TO_END = "user scrub to end recorded stream";
    public static final String TRIGGER_END_BY_PLAYER_RETRY_CONNECT_FAILED = "KSY player retry connect failed 20times ";
    public static final String TRIGGER_END_BY_PLAYER_RECORDED_ENDED_CAUSE_ERROR = "recorded stream ended due to error";
    public static final String TRIGGER_END_BY_PLAYER_RECORDED_ENDED_NORMALLY = "recorded stream ended normally";
    public static final String TRIGGER_END_BY_GET_VIEWER_NETWORK_SPEED_FAILED = "get viewer network speed failed within 20s";
    public static final String TRIGGER_END_BY_RELOAD_FAILED = "reload stream failed by viewer network";
    public static final String TRIGGER_END_BY_NOT_ALLOW_CREATE_SECOND_STREAM = "not allow create second stream";

    public static final int PAGE_LATEST_POST_LIMITED = 50;

    public static final String COUNTRY_CODE_VN_FROM_SERVER_RETURN = "vn";

    public static final String VIETNAMESE_LANGUAGE_PHONE = "vi";

    public static final int FACEBOOK_SHARE_REQUEST_CODE = 609;
    public static final int FACEBOOK_SHARE_TYPE = 0;
    public static final int WHATSAPP_SHARE_TYPE = 1;
    public static final int TWITTWER_SHARE_TYPE = 2;
    public static final int EMAIL_SHARE_TYPE = 3;
    public static final int COPY_LINK_TYPE = 4;
    public static final int OTHER_SHARE_TYPE = 5;

    public static final int POST_TYPE_IMAGE = 1;
    public static final int POST_TYPE_VIDEO = 2;
    public static final int POST_TYPE_QUOTES = 0;
    public static final int POST_TYPE_CHALLENGE_SELFIE = 3;

    @IntDef({POST_TYPE_IMAGE, POST_TYPE_VIDEO, POST_TYPE_QUOTES})
    @Retention(RetentionPolicy.SOURCE)
    public @interface POST_TYPE {
    }

    @IntDef({FACEBOOK_SHARE_TYPE, WHATSAPP_SHARE_TYPE, TWITTWER_SHARE_TYPE, EMAIL_SHARE_TYPE, OTHER_SHARE_TYPE, COPY_LINK_TYPE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface EARN_POINTS_SHARE_TYPE {
    }

    public static final int USER_POST_NORMAL = 1;
    public static final int CHALLENGE_SUBMISSION = 2;
    public static final int POST_TYPE_CHALLENGE = 0;

    public static final int WEB_SERVICE_START_PAGE = 1;

    public static final int REPORT_TYPE = 0;
    public static final int UNREPORT_TYPE = 1;

    public static final int HOME_IMAGE_CAN_CLICK_ABLE = 1;

}
