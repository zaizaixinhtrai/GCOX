package com.gcox.fansmeet.features.setting;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.*;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.gcox.fansmeet.AppsterApplication;
import com.gcox.fansmeet.R;
import com.gcox.fansmeet.common.ConstantBundleKey;
import com.gcox.fansmeet.common.Constants;
import com.gcox.fansmeet.core.activity.BaseToolBarActivity;
import com.gcox.fansmeet.features.blocked_screen.BlockedUserActivity;
import com.gcox.fansmeet.features.editprofile.EditProfileActivity;
import com.gcox.fansmeet.models.UserModel;
import com.gcox.fansmeet.util.*;
import com.gcox.fansmeet.webservice.AppsterWebServices;
import com.gcox.fansmeet.webview.ActivityViewWeb;

import timber.log.Timber;


/**
 * Created by User on 10/9/2015.
 */
public class SettingActivity extends BaseToolBarActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, SettingContract.SettingActivityView {

    private static final String TAG = SettingActivity.class.getSimpleName();

    @Bind(R.id.title_notify)
    TextView titleNotify;
    @Bind(R.id.txt_notifications)
    TextView txtNotifications;
    @Bind(R.id.txt_notification_sound)
    TextView txtNotificationSound;
    @Bind(R.id.txt_hide_message_details)
    TextView txtHideMessageDetails;

    @Bind(R.id.txt_Messaging)
    TextView txtMessaging;
    @Bind(R.id.fmRedemption)
    RelativeLayout fmRedemption;
    @Bind(R.id.title_profile)
    TextView titleProfile;
    @Bind(R.id.title_suport)
    TextView titleSuport;
    @Bind(R.id.txt_title_lang)
    TextView txtTitleLang;
    @Bind(R.id.txt_edit_prifile)
    TextView txtEditPrifile;
    @Bind(R.id.rl_blocked_list)
    RelativeLayout llBlockedList;
    @Bind(R.id.txt_about)
    TextView txtAbout;
    @Bind(R.id.txt_Terms_And_Conditions)
    TextView txtTermsAndConditions;
    @Bind(R.id.txt_Deactivate)
    TextView txtDeactivate;
    @Bind(R.id.rl_version)
    RelativeLayout mRlVersionItem;
    @Bind(R.id.txt_version)
    TextView tvVersion;

    private Switch sw_notifications;
    private Switch sw_notification_sound;
    private Switch sw_hide_message_details;
    private Switch sw_Messaging;
    @Bind(R.id.sw_live_notifications)
    Switch swLiveNotification;

    private TextView txt_language;

    private RelativeLayout fm_imv_arrow_language;
    private RelativeLayout fm_Edit_Profile;
    private RelativeLayout fm_About_Us;
    private RelativeLayout fm_Terms_And_Conditions;
    //    private RelativeLayout fm_Log_Out;
    private RelativeLayout fm_Deactivate_Account;

    private RelativeLayout layoutPlaytokenConnect;

    private UserModel userInforModel;

    private boolean isChangeLanguage = false;
    private boolean hasUnblockSomeone = false;

    private int languageCurrent;

    private SettingPresenter presenter;

    //============== inherited methods =============================================================
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        languageCurrent = 1;
        presenter = new SettingPresenter(this, AppsterWebServices.get());
        useAppToolbarBackButton();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.hideSoftKeyboard(this);
    }

    @Override
    public int getLayoutContentId() {
        return R.layout.activity_setting;
    }

    @Override
    public void init() {

        ButterKnife.bind(this);
        setToolbarColor(ContextCompat.getColor(this, R.color.color_00203B));
        setTopBarTile(getString(R.string.setting_slider));
        getEventClickBack().setOnClickListener(v -> finish());
        handleTurnoffMenuSliding();
        userInforModel = new UserModel();
        UserModel savedUserProfile = AppsterApplication.mAppPreferences.getUserModel();

        userInforModel.setSearchable(savedUserProfile.getSearchable());
        userInforModel.setNotificationSound(savedUserProfile.getNotificationSound());
        userInforModel.setNotification(savedUserProfile.getNotification());
        userInforModel.setLiveNotification(savedUserProfile.getLiveNotification());
        userInforModel.setUserId(savedUserProfile.getUserId());

        initID();

        setDefault(userInforModel);
        setClickSwitch();
    }

    @Override
    public View findViewInContentById(int id) {
        return super.findViewInContentById(id);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Constants.REQUEST_BLOCKED_USER_LIST:
                if (data != null) {
                }
                break;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // refresh your views here
        super.onConfigurationChanged(newConfig);

        getBaseContext().getResources().updateConfiguration(newConfig, getBaseContext().getResources().getDisplayMetrics());
        getLayoutContentId();
        initID();

        setTopBarTile(getString(R.string.settings_bold));
        titleNotify.setText(getString(R.string.setting_title_notifications));
        txtNotifications.setText(getString(R.string.setting_notifications));
        txtNotificationSound.setText(getString(R.string.setting_notification_sound));
        txtHideMessageDetails.setText(getString(R.string.setting_hide_message_details));
        txtMessaging.setText(getString(R.string.setting_Messaging));
        titleProfile.setText(getString(R.string.setting_Profile));
        txtEditPrifile.setText(getString(R.string.setting_Edit_Profile));
        titleSuport.setText(getString(R.string.setting_Support));
        txtAbout.setText(getString(R.string.setting_About_Us));
        txtTermsAndConditions.setText(getString(R.string.setting_Terms_And_Conditions));
        txtDeactivate.setText(getString(R.string.setting_Deactivate_Account));
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onDestroy() {

        if (isChangeLanguage) {
            savedChangeLanguage(isChangeLanguage);
        }
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    //================ implemented methods =========================================================
    @Override
    public Context getViewContext() {
        return this;
    }

    @Override
    public void loadError(String errorMessage, int code) {
        handleError(errorMessage, code);
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void showProgress(String message) {
        showDialog(this, message);
    }

    @Override
    public void hideProgress() {
        dismissDialog();
    }


    @Override
    public void onSettingUpdateSuccessfully() {

    }

    @Override
    public void onHasNewUpdates(boolean hasNewUpdates) {
        if (hasNewUpdates) {
            AppsterUtility.goToPlayStore(this, 500);
        } else {
            String title = getString(R.string.setting_check_update);
            String message = getString(R.string.setting_has_already_newest_version);
            String okButton = getString(R.string.btn_text_ok);
            DialogUtil.showConfirmDialogSingleAction(this, title, message, okButton, null);
        }
    }

    @Override
    public void onCheckUpdateFailed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    //========== event handlers ====================================================================
    @Override
    public void onClick(View v) {
        ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(mActivity, R.anim.push_in_to_right, R.anim.push_in_to_left);
        Intent intent;
        switch (v.getId()) {
            case R.id.fm_imv_arrow_language:
                openChooseLanguage();
                break;

            case R.id.txt_language:
                openChooseLanguage();
                break;

            case R.id.fm_Edit_Profile:
                intent = new Intent(SettingActivity.this, EditProfileActivity.class);
                startActivity(intent, options.toBundle());
                break;

            case R.id.rl_blocked_list:
                intent = new Intent(SettingActivity.this, BlockedUserActivity.class);
                ActivityCompat.startActivityForResult(this, intent, Constants.REQUEST_BLOCKED_USER_LIST, options.toBundle());
                break;

            case R.id.fm_About_Us:
                AppsterUtility.temporaryLockView(v);
                Intent intentViewWeb = new Intent(SettingActivity.this, ActivityViewWeb.class);
                intentViewWeb.putExtra(ConstantBundleKey.BUNDLE_URL_FOR_WEBVIEW, Constants.URL_ABOUT_US);
                startActivity(intentViewWeb, options.toBundle());
                break;

            case R.id.fm_Terms_And_Conditions:
                AppsterUtility.temporaryLockView(v);
                Intent intentTerms = new Intent(SettingActivity.this, ActivityViewWeb.class);
                intentTerms.putExtra(ConstantBundleKey.BUNDLE_URL_FOR_WEBVIEW, Constants.URL_TERMS_CONDITION);
                startActivity(intentTerms, options.toBundle());
                break;

            case R.id.fm_Deactivate_Account:
                presenter.confirmDeactivateAccount();
                break;

            case R.id.rl_version:
                AppsterUtility.temporaryLockView(v);
                checkUpdates();
                break;

            case R.id.fmRedemption:

                RedemptionDialog dialog = RedemptionDialog.newInstance();
                dialog.show(getSupportFragmentManager(), "RedemptionDialog");

                break;

//            case R.id.slider_menu:
//                onBackPressed();
//                break;
        }
    }

    //============== inner methods =================================================================
    private void savedChangeLanguage(boolean isChangeLanguage) {
        AppsterApplication.mAppPreferences.setChangeLanguage(isChangeLanguage);
    }

    private void initID() {
        sw_notifications = (Switch) findViewById(R.id.sw_notifications);
        sw_notification_sound = (Switch) findViewById(R.id.sw_notification_sound);
        sw_hide_message_details = (Switch) findViewById(R.id.sw_hide_message_details);
        sw_Messaging = (Switch) findViewById(R.id.sw_Messaging);

        txt_language = (TextView) findViewById(R.id.txt_language);

        fm_imv_arrow_language = (RelativeLayout) findViewById(R.id.fm_imv_arrow_language);
        fm_Edit_Profile = (RelativeLayout) findViewById(R.id.fm_Edit_Profile);
        fm_About_Us = (RelativeLayout) findViewById(R.id.fm_About_Us);
        fm_Terms_And_Conditions = (RelativeLayout) findViewById(R.id.fm_Terms_And_Conditions);
        fm_Deactivate_Account = (RelativeLayout) findViewById(R.id.fm_Deactivate_Account);

        fm_imv_arrow_language.setOnClickListener(this);
        txt_language.setOnClickListener(this);

        fm_Edit_Profile.setOnClickListener(this);
        llBlockedList.setOnClickListener(this);
        fm_About_Us.setOnClickListener(this);
        fm_Terms_And_Conditions.setOnClickListener(this);
        fm_Deactivate_Account.setOnClickListener(this);
        mRlVersionItem.setOnClickListener(this);
        fmRedemption.setOnClickListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()) {
            case R.id.sw_notifications:

                if (isChecked) {
                    userInforModel.setNotification(1);
                } else {
                    userInforModel.setNotification(0);
                }

                presenter.updateSetting(userInforModel);
                break;

            case R.id.sw_live_notifications:
                userInforModel.setLiveNotification(isChecked ? 1 : 0);
                presenter.updateSetting(userInforModel);
                break;

            case R.id.sw_notification_sound:

                if (isChecked) {
                    userInforModel.setNotificationSound(1);
                } else {
                    userInforModel.setNotificationSound(0);
                }

                presenter.updateSetting(userInforModel);
                break;

            case R.id.sw_hide_message_details:

            case R.id.sw_Messaging:

                presenter.updateSetting(userInforModel);
                break;

        }
    }

    private void checkUpdates() {
        presenter.checkUpdates();
    }

    private void setDefault(UserModel userInforModel) {

        if (userInforModel.getNotification() == 1) {
            sw_notifications.setChecked(true);
        }

        if (userInforModel.getNotificationSound() == 1) {
            sw_notification_sound.setChecked(true);
        }

        swLiveNotification.setChecked(userInforModel.getLiveNotification() == 1);

        setChangeLanguage(userInforModel);

        try {
            tvVersion.setText(getString(R.string.setting_version, AppsterApplication.getCurrentVersionName(this)));
        } catch (PackageManager.NameNotFoundException e) {
            Timber.e(e);
        }
    }

    private void setClickSwitch() {
        sw_notifications.setOnCheckedChangeListener(this);
        sw_notification_sound.setOnCheckedChangeListener(this);
        swLiveNotification.setOnCheckedChangeListener(this);
        sw_hide_message_details.setOnCheckedChangeListener(this);
        sw_Messaging.setOnCheckedChangeListener(this);
    }

    public void openChooseLanguage() {


    }

    private void setChangeLanguage(UserModel userInforModel) {

    }

}
