package com.gcox.fansmeet.core.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.DrawableRes;
import android.support.design.widget.NavigationView;
import android.support.v4.app.*;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.*;
import android.widget.*;
import butterknife.ButterKnife;
import com.gcox.fansmeet.AppsterApplication;
import com.gcox.fansmeet.R;
import com.gcox.fansmeet.common.Constants;
import com.gcox.fansmeet.core.dialog.DialogbeLiveConfirmation;
import com.gcox.fansmeet.customview.CircleImageView;
import com.gcox.fansmeet.customview.CustomFontTextView;
import com.gcox.fansmeet.customview.CustomTypefaceSpan;
import com.gcox.fansmeet.features.editprofile.EditProfileActivity;
import com.gcox.fansmeet.features.invitefriend.InviteFriendActivity;
import com.gcox.fansmeet.features.loyalty.LoyaltyActivity;
import com.gcox.fansmeet.features.refill.ActivityRefill;
import com.gcox.fansmeet.features.rewards.RewardActivity;
import com.gcox.fansmeet.features.setting.SettingActivity;
import com.gcox.fansmeet.features.stars.StarsActivity;
import com.gcox.fansmeet.models.UserModel;
import com.gcox.fansmeet.services.RefreshFollowerListService;
import com.gcox.fansmeet.util.CheckNetwork;
import com.gcox.fansmeet.util.DialogManager;
import com.gcox.fansmeet.util.ImageLoaderUtil;
import com.gcox.fansmeet.util.StringUtil;
import com.gcox.fansmeet.webservice.AppsterWebServices;
import com.gcox.fansmeet.webservice.request.LogoutRequestModel;
import timber.log.Timber;

import java.text.DecimalFormat;


/**
 * Created by sonnguyen on 10/7/15.
 */

public abstract class BaseToolBarActivity extends BaseActivity {
    private CustomFontTextView txtTitle;
    private Toolbar mToolbar;
    private View homeView;
    public Activity mActivity;
    ImageButton mIBtnLeftToolbar;
    ImageButton imEditProfile;
    private DrawerLayout mDrawer;
    NavigationView navigationView;

    // Handle Profile in Sliding Menu Screen
    TextView tvUserName;
    TextView tvDisplayName;
    CircleImageView imgProfile;
    ImageView imgBackgroundProfile;
    View headerLayout;
    private TextView tvStart;
    private TextView tvBean;
    private TextView tvLoyalty;
    private Button btRight;

    Bitmap avatarBlur;

    protected FrameLayout contentLayout;

    private long mLastClickLeftMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_tool_bar);
        findViewByIdView();
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getEventClickBack().setOnClickListener(v -> mDrawer.openDrawer(GravityCompat.START));
        }
        mActivity = this;

        if (getLayoutContentId() != 0) {
            homeView = getLayoutInflater().inflate(getLayoutContentId(), contentLayout, false);
            contentLayout.addView(homeView);
            ButterKnife.bind(this);//bind the home view used in MainActivity
            init();
        }

        getScreenWidth();
        navigationView.setItemIconTintList(null);
        imgBackgroundProfile.setOnClickListener(view -> {
            mDrawer.closeDrawers();
        });

        setupDrawerContent(navigationView);
        setDrawerToggle();
        handleProfileMenuSliding();
        addMenuActionLayout();
    }

    private void findViewByIdView() {
        mToolbar = findViewById(R.id.toolbar);
        mDrawer = findViewById(R.id.drawer_layout);
        contentLayout = findViewById(R.id.content);
        navigationView = findViewById(R.id.nvView);
        // Inflate the header view at runtime
        headerLayout = navigationView.inflateHeaderView(R.layout.view_profile_draw);
        imgBackgroundProfile = headerLayout.findViewById(R.id.bgProfile);
        imgProfile = headerLayout.findViewById(R.id.profile_image);
        tvDisplayName = headerLayout.findViewById(R.id.display_name);
        tvUserName = headerLayout.findViewById(R.id.user_name);
        btRight = mToolbar.findViewById(R.id.btJoinChallenge);
        txtTitle = mToolbar.findViewById(R.id.txt_title_screen);
        mIBtnLeftToolbar = mToolbar.findViewById(R.id.slider_menu);
        imEditProfile = mToolbar.findViewById(R.id.imEditProfile);
    }

    public void handleProfileMenuSliding() {
        Menu menu = navigationView.getMenu();
        MenuItem nav_itemLogout = menu.findItem(R.id.nav_logout);
        String menuLoginLogout;
        tvUserName.setVisibility(View.VISIBLE);
        tvDisplayName.setVisibility(View.VISIBLE);
        imgProfile.setVisibility(View.VISIBLE);
        imgBackgroundProfile.setVisibility(View.VISIBLE);
        imgProfile.setVisibility(View.VISIBLE);
        menuLoginLogout = getString(R.string.logout_slider);
        UserModel user = AppsterApplication.mAppPreferences.getUserModel();
        tvDisplayName.setText(user.getDisplayName());
        tvUserName.setText(String.format("@%s", user.getUserName()));
        ImageLoaderUtil.displayUserImage(getApplicationContext(), user.getUserImage(), imgProfile);
        nav_itemLogout.setTitle(menuLoginLogout);
        applyFontToMenuItem(nav_itemLogout, -1);

    }

    private void addMenuActionLayout() {
        DecimalFormat myFormatter = new DecimalFormat("#,###");
        MenuItem income = navigationView.getMenu().findItem(R.id.navStart);
        View v = View.inflate(this, R.layout.menu_item_action_layout, null);
        tvStart = v.findViewById(R.id.tvStart);
        ImageView iconStart = v.findViewById(R.id.icon);
        iconStart.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_gift_price));
        income.setActionView(v);
        tvStart.setText(myFormatter.format(AppsterApplication.mAppPreferences.getUserModel().getStars()).replace(".", ","));

        MenuItem reGem = navigationView.getMenu().findItem(R.id.navGem);
        View viewGem = View.inflate(this, R.layout.menu_item_action_layout, null);
        tvBean = viewGem.findViewById(R.id.tvStart);
        ImageView iconBean = viewGem.findViewById(R.id.icon);
        iconBean.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.refill_gem_icon));
        reGem.setActionView(viewGem);
        tvBean.setText(myFormatter.format(AppsterApplication.mAppPreferences.getUserModel().getGems()).replace(".", ","));
//
//        MenuItem reLoyalty = navigationView.getMenu().findItem(R.id.navLoyalty);
//        View viewLoyalty = View.inflate(this, R.layout.menu_item_action_layout, null);
//        tvLoyalty = viewLoyalty.findViewById(R.id.tvStart);
//        ImageView iconPoints = viewLoyalty.findViewById(R.id.icon);
//        iconPoints.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.icon_loyalty));
//        reLoyalty.setActionView(viewLoyalty);
//        tvLoyalty.setText(myFormatter.format(AppsterApplication.mAppPreferences.getUserModel().getLoyalty()).replace(".", ","));
    }

    private void setDrawerToggle() {
        mDrawer.addDrawerListener(new DrawerLayout.DrawerListener() {

            private float last = 0;

            @Override
            public void onDrawerSlide(View arg0, float arg1) {

                boolean opening = arg1 > last;
                boolean closing = arg1 < last;

                if (opening) {
                    tvDisplayName.setText(AppsterApplication.mAppPreferences.getUserModel().getDisplayName());
                    ImageLoaderUtil.displayUserImage(getApplicationContext(), AppsterApplication.mAppPreferences.getUserModel().getUserImage(), imgProfile);
                    updateCreditsAfterReceiveGiftForLeftMenu();
                }

                last = arg1;
            }

            @Override
            public void onDrawerStateChanged(int arg0) {
            }

            @Override
            public void onDrawerOpened(View arg0) {
            }

            @Override
            public void onDrawerClosed(View arg0) {
            }

        });
    }

    public void updateCreditsAfterReceiveGiftForLeftMenu() {

        // Preventing multiple clicks, using threshold of 1 second
        if (SystemClock.elapsedRealtime() - mLastClickLeftMenu < 5000)
            return;
        mLastClickLeftMenu = SystemClock.elapsedRealtime();

        if (!AppsterApplication.mAppPreferences.isUserLogin() || !CheckNetwork.isNetworkAvailable(this)) {
            return;
        }

        compositeDisposable.add(AppsterWebServices.get().getUserCredits("Bearer " + AppsterApplication.mAppPreferences.getUserToken())
                .subscribe(creditsResponseModel -> {
                    if (creditsResponseModel == null) return;
                    if (creditsResponseModel.getCode() == Constants.RESPONSE_FROM_WEB_SERVICE_OK) {

                        AppsterApplication.mAppPreferences.getUserModel().setStars(creditsResponseModel.getData().getStars());
                        AppsterApplication.mAppPreferences.getUserModel().setGems(creditsResponseModel.getData().getGems());
                        AppsterApplication.mAppPreferences.getUserModel().setLoyalty(creditsResponseModel.getData().getLoyalty());
                        setStartAndBean();
                    } else {
                        handleError(creditsResponseModel.getMessage(), creditsResponseModel.getCode());
                    }
                }, error -> {
                    Timber.e(error);
                }));
    }

    void setStartAndBean() {
        if (tvStart != null && tvBean != null && tvLoyalty != null && AppsterApplication.mAppPreferences.getUserModel() != null) {
            DecimalFormat myFormatter = new DecimalFormat("#,###");
            tvStart.setText(myFormatter.format(AppsterApplication.mAppPreferences.getUserModel().getStars()).replace(".", ","));
            tvBean.setText(myFormatter.format(AppsterApplication.mAppPreferences.getUserModel().getGems()).replace(".", ","));
//            tvLoyalty.setText(myFormatter.format(AppsterApplication.mAppPreferences.getUserModel().getLoyalty()).replace(".", ","));
        }
    }

    private void setupDrawerContent(NavigationView navigationView) {

        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    selectDrawerItem(menuItem);

                    return false;
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        mDrawer.closeDrawers();
        Intent intent = null;
        ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(BaseToolBarActivity.this,
                R.anim.push_in_to_right, R.anim.push_in_to_left);

        switch (menuItem.getItemId()) {

            case R.id.nav_setting:
                intent = new Intent(this, SettingActivity.class);
                ActivityCompat.startActivityForResult(this, intent, Constants.SETTING_REQUEST, options.toBundle());
                break;

            case R.id.navGem:
                intent = new Intent(BaseToolBarActivity.this, ActivityRefill.class);
                startActivity(intent, options.toBundle());
                break;

            case R.id.nav_logout:
                confirmLogout();
                break;

            case R.id.navStart:
                // Do not allow to open star history
//                intent = new Intent(BaseToolBarActivity.this, StarsActivity.class);
//                startActivity(intent, options.toBundle());
                break;

            case R.id.navReward:
                startActivity(RewardActivity.createIntent(this));
                break;
            case R.id.navLoyalty:
                intent = new Intent(BaseToolBarActivity.this, LoyaltyActivity.class);
                startActivity(intent, options.toBundle());
                break;

            case R.id.nav_invite_friend:
                intent = new Intent(BaseToolBarActivity.this, InviteFriendActivity.class);
                startActivity(intent, options.toBundle());
                break;
        }
    }

    private void confirmLogout() {
        DialogbeLiveConfirmation.Builder builder = new DialogbeLiveConfirmation.Builder();
        builder.title(getString(R.string.app_name))
                .message(getString(R.string.are_you_sure_you_want_to_logout))
                .confirmText(getString(R.string.btn_text_ok))
                .singleAction(false)
                .onConfirmClicked(() -> {
                    DialogManager.getInstance().showDialog(this, getString(R.string.connecting_msg));
                    if (CheckNetwork.isNetworkAvailable(BaseToolBarActivity.this)) {
                        logoutAppWithServer();
                    } else {
                        finish();
                    }
                })
                .build().show(this);

    }

    private void logoutAppWithServer() {
        LogoutRequestModel request = new LogoutRequestModel();
        if (AppsterApplication.mAppPreferences.isUserLogin()) {
            request.setDevice_token(AppsterApplication.mAppPreferences.getDevicesToken());
        }

        compositeDisposable.add(AppsterWebServices.get().logoutApp("Bearer " + AppsterApplication.mAppPreferences.getUserToken(), request)
                .subscribe(logoutDataResponse -> {
                    AppsterApplication.mAppPreferences.saveUserToken(null);
                    RefreshFollowerListService.clearLastTimeSync();
                    AppsterApplication.logout(BaseToolBarActivity.this);
                    finish();
                }, error -> {
                    Timber.e(error.getMessage());
                    finish();
                }));
    }

    private void setTextFontDrawerLayout() {

        Menu m = navigationView.getMenu();
        if (m == null) return;
        for (int i = 0; i < m.size(); i++) {
            MenuItem mi = m.getItem(i);

            //for aapplying a font to subMenu ...
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu != null && subMenu.size() > 0) {
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem, j);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi, i);
        }
    }

    private void applyFontToMenuItem(MenuItem mi, int stt) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/opensanssemibold.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("sans-serif", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        if (stt == 2) {
            mNewTitle.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.silder_color_change)), 0, mNewTitle.length(), 0);
        }
        mi.setTitle(mNewTitle);
    }


    @TargetApi(21)
    private void changeStatusbarColor() {
        Window window = mActivity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(mActivity, R.color.new_background_tapbar));
    }

    public void setTxtTitleAsAppName() {

//        txtTitle.setTextColor(Color.parseColor("#9B9B9B"));
//        SpannableString contentLine = new SpannableString(getString(R.string.title_app_line));
//        contentLine.setSpan(new UnderlineSpan(), 2, contentLine.length(), 0);
//        contentLine.setSpan(new ForegroundColorSpan(Color.parseColor("#D8D8D8")), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        txtTitle.setText(contentLine);
        if (getCurrentTabPosition() == 3) {
            return;
        }
        txtTitle.setVisibility(View.GONE);
        findViewById(R.id.imv_title).setVisibility(View.VISIBLE);
    }

    public void removeToolbarTitle() {
        txtTitle.setVisibility(View.GONE);
        findViewById(R.id.imv_title).setVisibility(View.GONE);
    }

    public void usedToolbarImage() {
        txtTitle.setVisibility(View.GONE);
        findViewById(R.id.imv_title).setVisibility(View.VISIBLE);
    }

    public void handleToolbar(boolean isVisible) {
        if (mToolbar != null) {
            this.mToolbar.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        }

//        if (AppsterApplication.mAppPreferences.getUserModel() != null &&
//                !StringUtil.isNullOrEmptyString(AppsterApplication.mAppPreferences.getUserModel().getUserId())) {
//            imvNotyOrSetting.setVisibility(View.VISIBLE);
//            imvNotyOrSetting.setShowBadge(false);
//        } else {
//            imvNotyOrSetting.setVisibility(View.GONE);
//            imvNotyOrSetting.setShowBadge(false);
//        }
    }

    public void setToolbarColor(String color) {
        mToolbar.setBackgroundColor(Color.parseColor(color));
    }

    public void setToolbarColor(int color) {
        mToolbar.setBackgroundColor(color);
    }

    public int getCurrentTabPosition() {
        return 0;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);

        if (avatarBlur != null && !avatarBlur.isRecycled()) {
            avatarBlur.recycle();
            avatarBlur = null;
        }
    }

    @Override
    public void finish() {
        super.finish();
    }

    /**
     * Set color for tab primary
     */
//    public abstract void setStatusBarColor();

    /**
     * Return the id of the layout that is the content of your activity.
     *
     * @return the id of your layout.
     */
    public abstract int getLayoutContentId();

    /**
     * Perform you initialization here. This is called after on create.
     */
    public abstract void init();

    /**
     * Use this instead of findViewById. This method will search the layout resource file that you
     * provide in {@code getLayoutContentId}
     *
     * @param id the id of the view to search for.
     * @return the found view if it exists.
     */
    public View findViewInContentById(int id) {
        return homeView.findViewById(id);
    }

    /**
     * Returns the current toolbar.
     *
     * @return the current toolbar.
     */
    public Toolbar getToolbar() {
        return this.mToolbar;
    }

    public void setTopBarTile(String title) {
        if (txtTitle != null) {
            txtTitle.setText(title);
//            txtTitle.setTextColor(Color.parseColor("#9b9b9b"));
            txtTitle.setVisibility(View.VISIBLE);
            findViewById(R.id.imv_title).setVisibility(View.GONE);
        }
    }

    public void setTopBarTileNoCap(String title) {
        if (txtTitle != null) {
            txtTitle.setText(title);
//            txtTitle.setTextColor(Color.parseColor("#9b9b9b"));
            txtTitle.setVisibility(View.VISIBLE);
            txtTitle.setAllCaps(false);
            txtTitle.setTextDirection(View.TEXT_DIRECTION_LOCALE);
            findViewById(R.id.imv_title).setVisibility(View.GONE);
        }
    }

    public void setTopBarTitleWithFont(String title, String font, int textSize, String color) {
        if (txtTitle != null) {
            txtTitle.setText(title);
            txtTitle.setTextColor(Color.parseColor(color));
            txtTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
            txtTitle.setVisibility(View.VISIBLE);
            txtTitle.setCustomFont(this, font);
            txtTitle.setTypeface(txtTitle.getTypeface(), Typeface.BOLD);
            findViewById(R.id.imv_title).setVisibility(View.GONE);
        }
    }

    public void invisibleImageEditProfile() {
        imEditProfile.setVisibility(View.GONE);
    }

    public void setImageEditProfile() {
        imEditProfile.setImageResource(mBeLiveThemeHelper.getAppEditProfileIcon());
        imEditProfile.setVisibility(View.VISIBLE);
        imEditProfile.setOnClickListener(v -> {
            ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(mActivity, R.anim.push_in_to_right, R.anim.push_in_to_left);
            Intent intent1 = new Intent(mActivity, EditProfileActivity.class);
            mActivity.startActivityForResult(intent1, Constants.REQUEST_CODE_EDIT_PROFILE, options.toBundle());
        });
    }

    public Button getRightButton() {
        return btRight;
    }

    public void visibleRightButton() {
        btRight.setVisibility(View.VISIBLE);
    }

    public void useAppToolbarBackButton() {
        setLeftToolbarIcon(getMBeLiveThemeHelper().getToolbarBackIcon());
    }

    public void setLeftToolbarIcon(@DrawableRes int resId) {
        mIBtnLeftToolbar.setImageResource(resId);
    }

    public View getEventClickBack() {
        return mIBtnLeftToolbar;
    }


    public void defaultMenuIcon() {
        Timber.e("defaultMenuIcon");
        mIBtnLeftToolbar.setImageResource(getMBeLiveThemeHelper().getAppMenuIcon());
        getEventClickBack().setOnClickListener(v -> mDrawer.openDrawer(GravityCompat.START));
    }

    public void showFullFragmentScreen(Fragment fragment) {
        if (!mActivity.isFinishing()) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.push_in_to_right,
                    R.anim.push_in_to_left);
            transaction.addToBackStack(null);
            transaction.replace(R.id.content, fragment).commit();
        }
    }


    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager.getBackStackEntryCount() > 1) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setColorPrimary(int color) {

    }

    public void handleTurnoffMenuSliding() {
        mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public void handleTurnOnMenuSliding() {
        mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }


    public void defaultMenuIconOnHome() {
        mIBtnLeftToolbar.setImageResource(getMBeLiveThemeHelper().getAppMenuIconLight());
        getEventClickBack().setOnClickListener(v -> mDrawer.openDrawer(GravityCompat.START));
    }


    public void setTitleTextColor(int color) {

        txtTitle.setTextColor(color);

    }

    public boolean handleCloseOpenDraw() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawers();
            return true;
        }
        return false;
    }

    private void getScreenWidth() {
        if (AppsterApplication.Companion.getMAppPreferences().getScreenWidth() <= 0) {
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x;
            AppsterApplication.Companion.getMAppPreferences().setScreenWidth(width);
        }

    }
}
