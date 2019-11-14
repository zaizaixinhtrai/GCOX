package com.gcox.fansmeet.manager;

import android.content.Context;
import com.gcox.fansmeet.R;
import com.gcox.fansmeet.core.dialog.DialogInfoUtility;

/**
 * Created by User on 10/9/2015.
 */
public class ShowErrorManager {

    // Status/System
    public static final int authentication_error = 401;
    private static final int system_error = 600;
    private static final int parameter_missing = 601;
    private static final int model_state_error = 602;
    public static final int account_deactivated_or_suspended = 603;
    public static final int account_deactivate_or_invalid_email = 604;
    public static final int  VERIFICATION_CODE_INVALID = 1315;
    public static final int REQUEST_VERIFICATION_CODE_REACHED_LIMITED = 1317;
    public static final int  SIGN_IN_INVALID_PASSWORD = 1327;
    private static final int insufficient_gold = 605;
    private static final int email_has_already_been_verified = 606;
    private static final int Media_content_type_is_not_support = 607;
    private static final int Transaction_Error = 608;
    private static final int Register_Xmpp_accrount_failed = 609;
    public static final int ADMIN_BLOCKED = 1099;

    //Invalid/Missing Data
    private static final int invalid_data = 700;
    private static final int invalid_user_id = 701;
    private static final int filter_is_invalid = 702;
    private static final int media_type_is_invalid = 703;
    private static final int invalid_login_credentials = 704;
    private static final int out_of_length_bound = 705;
    private static final int invalid_datetime = 706;
    private static final int invalid_data_type = 707;
    private static final int invalid_email_format = 708;
    private static final int message_content_missing = 709;
    private static final int email_is_missing = 710;
    private static final int display_name_is_missing = 711;
    private static final int invalid_device_type = 712;
    private static final int invalid_credit_type = 713;
    private static final int invalid_post_id = 714;
    private static final int invalid_follower_id = 715;
    private static final int invalid_owner_id = 716;
    private static final int invalid_report_user_id = 717;
    private static final int invalid_notification_status = 718;
    private static final int invalid_save_push_notification_id = 719;
    private static final int invalid_receiver_id = 720;
    private static final int invalid_user_role = 721;
    private static final int invalid_referral_id = 722;
    private static final int dob_is_out_of_accepted_age_range = 723;

    private static final int missing_required_data = 750;
    private static final int email_already_existed = 751;
    private static final int not_exist = 752;
    private static final int data_has_been_deleted = 754;
    private static final int missing_paypal_email = 755;
    private static final int missing_account_number = 756;
    private static final int missing_branch_code = 757;
    private static final int missing_bank_name = 758;
    public static final int username_has_been_used = 759;
    private static final int email_has_been_used = 760;
    private static final int Referral_ID_already_exist = 761;
    private static final int Email_has_been_used_with_another_facebook_id = 762;
    private static final int User_does_not_have_any_email = 763;
    private static final int Email_has_not_been_verified = 764;
    private static final int Already_like_that_post = 765;
    private static final int Stream_already_exists = 766;

    //Data Not Found
    private static final int no_data_found = 651;
    public static final int user_not_found = 652;
    private static final int no_page_found = 653;
    private static final int transaction_info_not_found = 654;

    //CRUD Error
    private static final int update_fail = 800;
    private static final int create_fail = 801;
    private static final int delete_fail = 802;
    private static final int can_not_edit_comment = 803;
    private static final int can_not_create_comment = 804;
    private static final int can_not_delete_comment = 805;
    private static final int can_not_create_post = 806;
    private static final int can_not_update_post = 807;
    private static final int can_not_delete_post = 808;
    private static final int can_not_delete_conversation = 809;
    private static final int save_cashback_error = 810;
    private static final int send_receive_gift_error = 811;
    private static final int Request_cashback_fail = 812;

    //Authority/Privacy
    private static final int unauthorized = 900;
    private static final int user_rejected = 901;
    private static final int not_in_leaderboard = 903;
    private static final int post_not_belong_to_user = 904;
    private static final int User_turn_off_chat = 905;
    private static final int Notification_not_belong_to_user = 906;

    //Communication Error
    private static final int can_not_send_email = 951;
    private static final int self_communication = 952;
    private static final int Can_not_upload_media_content = 953;

    // Block message
    public static final int message_turn_off = 1002;

    public static final int stream_block = 1071;
    public static final int not_enough_star = 1076;

    public static final int stream_create_second = 1003;

    public static final int un_know_error = 999999;
    public static final int userHaveBeenKickedOutStream = 1120;
    public static final int userHaveBeenBlockedStream = 1309;

    public static void show(int errorCode, Context context) {
        DialogInfoUtility utility = new DialogInfoUtility();

        if (errorCode == system_error) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.system_error), context);
        } else if (errorCode == parameter_missing) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.parameter_missing), context);
        } else if (errorCode == model_state_error) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.model_state_error), context);
        } else if (errorCode == account_deactivated_or_suspended) {

            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.account_deactivated_or_suspended), context);
        } else if (errorCode == account_deactivate_or_invalid_email) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.account_deactivate_or_invalid_email), context);
        } else if (errorCode == insufficient_gold) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.insufficient_gold), context);
        } else if (errorCode == email_has_already_been_verified) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.email_has_already_been_verified), context);

        } else if (errorCode == invalid_data) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.invalid_data), context);

        } else if (errorCode == invalid_user_id) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.invalid_user_id), context);

        } else if (errorCode == filter_is_invalid) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.filter_is_invalid), context);

        } else if (errorCode == media_type_is_invalid) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.media_type_is_invalid), context);

        } else if (errorCode == invalid_login_credentials) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.invalid_login_credentials), context);

        } else if (errorCode == out_of_length_bound) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.out_of_length_bound), context);

        } else if (errorCode == invalid_datetime) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.invalid_datetime), context);

        } else if (errorCode == invalid_data_type) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.invalid_data_type), context);

        } else if (errorCode == invalid_email_format) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.invalid_email_format), context);

        } else if (errorCode == message_content_missing) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.message_content_missing), context);

        } else if (errorCode == email_is_missing) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.email_is_missing), context);

        } else if (errorCode == display_name_is_missing) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.display_name_is_missing), context);

        } else if (errorCode == invalid_device_type) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.invalid_device_type), context);

        } else if (errorCode == invalid_credit_type) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.invalid_credit_type), context);

        } else if (errorCode == invalid_post_id) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.invalid_post_id), context);

        } else if (errorCode == invalid_follower_id) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.invalid_follower_id), context);

        } else if (errorCode == invalid_owner_id) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.invalid_owner_id), context);

        } else if (errorCode == invalid_report_user_id) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.invalid_report_user_id), context);

        } else if (errorCode == invalid_notification_status) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.invalid_notification_status), context);

        } else if (errorCode == invalid_save_push_notification_id) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.invalid_save_push_notification_id), context);

        } else if (errorCode == invalid_receiver_id) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.invalid_receiver_id), context);

        } else if (errorCode == invalid_user_role) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.invalid_user_role), context);

        } else if (errorCode == invalid_referral_id) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.invalid_referral_id), context);

        } else if (errorCode == dob_is_out_of_accepted_age_range) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.dob_is_out_of_accepted_age_range), context);

        } else if (errorCode == missing_required_data) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.missing_required_data), context);

        } else if (errorCode == email_already_existed) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.email_already_existed), context);

        } else if (errorCode == not_exist) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.not_exist), context);

        } else if (errorCode == data_has_been_deleted) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.data_has_been_deleted), context);

        } else if (errorCode == missing_paypal_email) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.missing_paypal_email), context);

        } else if (errorCode == missing_account_number) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.missing_account_number), context);

        } else if (errorCode == missing_branch_code) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.missing_branch_code), context);

        } else if (errorCode == missing_bank_name) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.missing_bank_name), context);

        } else if (errorCode == username_has_been_used) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.username_has_been_used), context);

        } else if (errorCode == email_has_been_used) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.email_has_been_used), context);

        } else if (errorCode == no_data_found) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.no_data_found), context);

        } else if (errorCode == user_not_found) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.user_not_found), context);

        } else if (errorCode == no_page_found) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.no_page_found), context);

        } else if (errorCode == transaction_info_not_found) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.transaction_info_not_found), context);

        } else if (errorCode == update_fail) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.update_fail), context);

        } else if (errorCode == create_fail) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.create_fail), context);

        } else if (errorCode == delete_fail) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.delete_fail), context);

        } else if (errorCode == can_not_edit_comment) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.can_not_edit_comment), context);

        } else if (errorCode == can_not_create_comment) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.can_not_create_comment), context);

        } else if (errorCode == can_not_delete_comment) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.can_not_delete_comment), context);

        } else if (errorCode == can_not_create_post) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.can_not_create_post), context);

        } else if (errorCode == can_not_update_post) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.can_not_update_post), context);

        } else if (errorCode == can_not_delete_post) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.can_not_delete_post), context);

        } else if (errorCode == can_not_delete_conversation) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.can_not_delete_conversation), context);

        } else if (errorCode == save_cashback_error) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.save_cashback_error), context);

        } else if (errorCode == send_receive_gift_error) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.send_receive_gift_error), context);

        } else if (errorCode == unauthorized) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.unauthorized), context);

        } else if (errorCode == user_rejected) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.user_rejected), context);

        } else if (errorCode == not_in_leaderboard) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.not_in_leaderboard), context);

        } else if (errorCode == post_not_belong_to_user) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.post_not_belong_to_user), context);

        } else if (errorCode == can_not_send_email) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.can_not_send_email), context);

        } else if (errorCode == self_communication) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.self_communication), context);

        } else if (errorCode == Media_content_type_is_not_support) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.Media_content_type_is_not_support), context);

        } else if (errorCode == Transaction_Error) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.Transaction_Error), context);

        } else if (errorCode == Register_Xmpp_accrount_failed) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.Register_Xmpp_accrount_failed), context);

        } else if (errorCode == Referral_ID_already_exist) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.Referral_ID_already_exist), context);

        } else if (errorCode == Email_has_been_used_with_another_facebook_id) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.Email_has_been_used_with_another_facebook_id), context);

        } else if (errorCode == User_does_not_have_any_email) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.User_does_not_have_any_email), context);

        } else if (errorCode == Email_has_not_been_verified) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.Error_Email_has_not_been_verified), context);

        } else if (errorCode == Already_like_that_post) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.Already_like_that_post), context);

        } else if (errorCode == Stream_already_exists) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.Stream_already_exists), context);

        } else if (errorCode == Request_cashback_fail) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.Request_cashback_fail), context);

        } else if (errorCode == User_turn_off_chat) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.User_turn_off_chat), context);

        } else if (errorCode == Notification_not_belong_to_user) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.Notification_not_belong_to_user), context);

        } else if (errorCode == Can_not_upload_media_content) {
            utility.showMessage(context.getString(R.string.app_name), context.getString(R.string.Can_not_upload_media_content), context);

        }
    }
}
