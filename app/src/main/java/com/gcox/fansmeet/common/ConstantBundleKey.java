package com.gcox.fansmeet.common;

import android.os.Environment;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by USER on 10/12/2015.
 */
public class ConstantBundleKey {

    //Bundle key
    public static final String BUNDLE_NOTIFICATION_KEY = "notification";
    public static final String BUNDLE_POST_ID_KEY = "post_id";
    public static final String BUNDLE_POST_STREAM_SLUG = "BUNDLE_POST_STREAM_SLUG";
    public static final String BUNDLE_PROFILE_ID_KEY = "profile_id";
    public static final String BUNDLE_TYPE_KEY = "type";
    public static final String BUNDLE_TYPE_VIDEO = "share_video";
    public static final String BUNDLE_MEDIA_KEY = "media";
    public static final String BUNDLE_POST_DETAIL_KEY = "post_detail";
    public static final String BUNDLE_EDIT_POST_DETAIL_KEY = "edit_post_detail";
    public static final String BUNDLE_USER_PROFILE_DETAIL = "user_Profile_Details";
    public static final String BUNDLE_POCKET_TYPE = "pocket_type";
    public static final String BUNDLE_POCKET_VALUE = "pocket_value";

    public static final String BUNDLE_POST_DETAIL_POST_ID = "post_detail_post_id";
    public static final String BUNDLE_POST_DETAIL_USER_ID = "post_detail_user_id";

    public static final String BUNDLE_LIST_COMMENT = "ListComment";
    public static final String BUNDLE_COMMENT_COUNT = "commentCounts";
    public static final String BUNDLE_COMMENT_POSITION = "positionOnListview";
    public static final String BUNDLE_COMMENT_TYPE = "BUNDLE_COMMENT_TYPE";
    public static final String BUNDLE_COMMENT_OWNER_ID = "BUNDLE_COMMENT_OWER_ID";

    public static final String BUNDLE_NOTIFY_POSITION = "notify_position";

    public static final String BUNDLE_CHANGE_LANGUAGE = "change_language";

    public static final String BUNDLE_TITLE_EDIT_POST = "title_edit_post";
    public static final String BUNDLE_POSITION_EDIT_POST = "position_edit_post";
    public static final String BUNDLE_ADDRESS_EDIT_POST = "address_edit_post";
    public static final String BUNDLE_ID_EDIT_POST = "id_edit_post";

    public static final String BUNDLE_EDIT_ABLE_POST = "edit_able_post";
    public static final String BUNDLE_POSITION_ON_GRID = "position_grid";
    public static final String BUNDLE_DELETE_POST_ABLE = "delete_post_able";

    public static final String BUNDLE_IMAGE_USER_LINE_TRIM = "user_image";

    public static int PICK_IMAGE_REQUEST = 10;
    public static final int SHARE_INSTAGRAM_REQUEST = 1000;
    public static final String MEDIA_PATH = Environment.getExternalStorageDirectory() + "/Appster/Media";

    public static final String BUNDLE_CHAT_CHAT_WITH_AVATAR = "chat_with_avatar";
    public static final String BUNDLE_CHAT_CHAT_WITH_DISPLAY_NAME = "display_name";
    public static final String BUNDLE_CHAT_CHAT_WITH_USER_NAME = "user_name";
    public static final String BUNDLE_CHAT_CHAT_WITH_USER_ID = "with_user_id";
    public static final String BUNDLE_CHAT_USER_MESSAGING = "chat_user_messaging";

    public static final String BUNDLE_LOGIN_DISPLAY_NAME = "displayname";
    public static final String BUNDLE_LOGIN_ID = "login_id";
    /** the value should be one of {@link LOGIN_FROM#ARG_LOGIN_FACEBOOK , {@link LOGIN_FROM#ARG_LOGIN_GOOGLE }}, {@link LOGIN_FROM#ARG_LOGIN_INSTAGRAM}*/
    public static final String BUNDLE_LOGIN_FROM = "BUNDLE_LOGIN_FROM";//
    public static final String BUNDLE_LOGIN_PROFILE_PIC = "profile_Pic";
    public static final String BUNDLE_LOGIN_EMAIL = "email";
    public static final String BUNDLE_LOGIN_PLAYTOKEN_USER_ID = "PLAYTOKEN_USER_ID";
    public static final String BUNDLE_LOGIN_PLAYTOKEN_USER_ACCOUNT = "PLAYTOKEN_USER_ACCOUNT";
    public static final String BUNDLE_LOGIN_GENDER = "BUNDLE_LOGIN_GENDER";

    @StringDef({LOGIN_FROM.ARG_LOGIN_FACEBOOK, LOGIN_FROM.ARG_LOGIN_GOOGLE, LOGIN_FROM.ARG_LOGIN_INSTAGRAM, LOGIN_FROM.ARG_LOGIN_TWITTER, LOGIN_FROM.ARG_LOGIN_WECHAT,
            LOGIN_FROM.ARG_LOGIN_WEIBO, LOGIN_FROM.ARG_LOGIN_PHONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LOGIN_FROM{
        public static final String ARG_LOGIN_FACEBOOK = "ARG_LOGIN_FACEBOOK";
        public static final String ARG_LOGIN_GOOGLE = "ARG_LOGIN_GOOGLE";
        public static final String ARG_LOGIN_INSTAGRAM = "ARG_LOGIN_INSTAGRAM";
        public static final String ARG_LOGIN_TWITTER = "ARG_LOGIN_TWITTER";
        public static final String ARG_LOGIN_WECHAT = "ARG_LOGIN_WECHAT";
        public static final String ARG_LOGIN_WEIBO = "ARG_LOGIN_WEIBO";
        public static final String ARG_LOGIN_PHONE = "ARG_LOGIN_PHONE";
    }

    public static final String BUNDLE_CHANGE_PROFILE_IMAGE = "BUNDLE_CHANGE_PROFILE_IMAGE";
    public static final String BUNDLE_CHANGE_PROFILE_DISPLAY_NAME = "BUNDLE_CHANGE_PROFILE_DISPLAY_NAME";

    public static final String BUNDLE_DATA_LIST_COMMENT_FROM_PROFILE_ACTIVITY = "BUNDLE_DATA_LIST_COMMENT_FROM_PROFILE_ACTIVITY";
    public static final String BUNDLE_DATA_LIST_LIKE_FROM_PROFILE_ACTIVITY = "BUNDLE_DATA_LIST_LIKE_FROM_PROFILE_ACTIVITY";
    public static final String BUNDLE_DATA_FOLLOW_USER_FROM_PROFILE_ACTIVITY = "BUNDLE_DATA_FOLLOW_USER_FROM_PROFILE_ACTIVITY";
    public static final String BUNDLE_DATA_REPORT_FROM_PROFILE_ACTIVITY = "BUNDLE_DATA_REPORT_FROM_PROFILE_ACTIVITY";

    public static final String BUNDLE_PROFILE_CHANGE_FOLLOW_USER = "BUNDLE_PROFILE_CHANGE_FOLLOW_USER";

    public static final String BUNDLE_CHAT_URL_IMAGE = "BUNDLE_CHAT_URL_IMAGE";
    public static final String BUNDLE_CHAT_URL_VIDEO = "BUNDLE_CHAT_URL_VIDEO";

    public static final String BUNDLE_LINK_PARAMETER_TYPE = "type";
    public static final String BUNDLE_LINK_PARAMETER_POST_ID = "postId";
    public static final String BUNDLE_IS_LINK_PARAMETER = "BUNDLE_IS_LINK_PARAMETER";

    public static final String BUNDLE_IS_NEW_POST_OR_EDIT = "BUNDLE_IS_NEW_POST_OR_EDIT";
    public static final String BUNDLE_USERNAME = "user_name";
    public static final String BUNDLE_USER_ID = "user_id";

    public static final String BUNDLE_STREAM_DETAIL_SLUG = "BUNDLE_STREAM_DETAIL_SLUG";
    public static final String BUNDLE_STREAM_IS_RECORDED = "BUNDLE_STREAM_IS_RECORDED";
    public static final String BUNDLE_STREAM_DETAIL_PLAY_URL = "BUNDLE_STREAM_DETAIL_PLAY_URL";

    public static final String BUNDLE_HOME_LIST_EVENT = "home_list_event";
    public static final String BUNDLE_PROFILE = "bundle_profile";
    public static final String BUNDLE_LIST_TOP_FAN = "list_top_fans";
    public static final String BUNDLE_GO_PROFILE = "go_profile";
    public static final String BUNDLE_GO_HOME = "go_home";

    public static final String BUNDLE_VIEWER_CLOSE_STREAM = "ARG_VIEWER_CLOSE_STREAM";
    public static final String BUNDLE_TEXT_FOR_NEWLY_USER = "BUNDLE_TEXT_FOR_NEWLY_USER";

    public static final String BUNDLE_MAINTENANCE_MODLE = "BUNDLE_MAINTENANCE_MODLE";

    public static final String BUNDLE_OPEN_NEW_STREAM = "BUNDLE_OPEN_NEW_STREAM";

    public static final String BUNDLE_URL_FOR_WEBVIEW = "BUNDLE_URL_FOR_WEBVIEW";
    public static final String BUNDLE_TITLE_FOR_WEBVIEW = "BUNDLE_URL_FOR_WEBVIEW";

    public static final String BUNDLE_POST_DETAIL_SLUG_STREAM = "post_detail_slug_stream";
    public static final String BUNDLE_ENTRIES_MODEL = "entries_model";
    public static final String BUNDLE_URL_SELFIE_IMAGE = "selfie_image";

}
