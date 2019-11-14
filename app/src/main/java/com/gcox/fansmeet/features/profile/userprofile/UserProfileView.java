package com.gcox.fansmeet.features.profile.userprofile;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.*;

import com.gcox.fansmeet.AppsterApplication;
import com.gcox.fansmeet.R;
import com.gcox.fansmeet.core.dialog.DialogInfoUtility;
import com.gcox.fansmeet.customview.BioTextView;
import com.gcox.fansmeet.customview.CircleImageView;
import com.gcox.fansmeet.customview.ShowMoreTextView;
import com.gcox.fansmeet.customview.autolinktextview.AutoLinkMode;
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.CelebrityProfileModel;
import com.gcox.fansmeet.features.topfans.TopFanModel;
import com.gcox.fansmeet.models.UserModel;
import com.gcox.fansmeet.util.ImageLoaderUtil;
import com.gcox.fansmeet.util.StringUtil;
import com.gcox.fansmeet.util.Utils;

import timber.log.Timber;

import java.util.List;

import static com.gcox.fansmeet.util.CustomTabUtils.openChromeTab;


/**
 * Created by ngoc on 9/25/15.
 */
public class UserProfileView extends LinearLayout {
    Activity activity;
    TextView tvFollwoersCount;
    TextView txtFollowerTitle;
    TextView textView_gift_count;
    TextView tvChallengesCount;
    TextView tvFollowing;
    TextView txtFollowingTitle;
    Button btFollow;
    ImageButton btnTabList;
    ImageButton btnTabGrid;
    ImageButton btnTabGift;
    private TextView txtEmpty;

    CircleImageView useImage;

    private TextView notificationMessage;
    private BioTextView txtAbout;
    private TextView txtUserName;
    private TextView txtDisplayName;
    private RelativeLayout rlLiveNotification;
    private LinearLayout viewMoreClick;
    private ConstraintLayout topFansLayout;

    private ImageView useImage1;
    private ImageView useImage2;
    private ImageView useImage3;
    private ImageView useImage4;

    private TextView userName1;
    private TextView userName2;
    private TextView userName3;
    private TextView userName4;

    private LinearLayout llUseImage1;
    private LinearLayout llUseImage2;
    private LinearLayout llUseImage3;
    private LinearLayout llUseImage4;

    private UserModel mUserProfileDetails;
    boolean isViewMe = false;
    private DialogInfoUtility utility;

    // variable to track event time
    int currentIndicatorSelected = FragmentMe.TAB_LIST_INDEX;
    OnUserProfileChangeView onTabChange;

    boolean hasShowCopyPopup = false;
    private boolean mIsStreaming;
    private boolean isAppOwner;
    private CelebrityProfileModel userProfileDetails;
    private String oldAbout;

    public UserProfileView(Activity activity, boolean isAppOwner, BioTextView.DisallowTouchEventCallback disallowTouchEventCallback) {
        super(activity);
        this.activity = activity;
        this.isAppOwner = isAppOwner;
        LayoutInflater.from(getContext()).inflate(R.layout.user_profile_header, this, true);

        tvFollwoersCount = findViewById(R.id.followersCount);
        tvChallengesCount = findViewById(R.id.tvChallengesCount);
        tvFollowing = findViewById(R.id.tvFollowing);

        btFollow = findViewById(R.id.btFollow);
        btnTabGrid = findViewById(R.id.btnTabGrid);
        btnTabList = findViewById(R.id.btnTabList);
        btnTabGift = findViewById(R.id.btnTabGift);

        useImage = findViewById(R.id.useImage);

        txtAbout = findViewById(R.id.txtAbout);
        txtUserName = findViewById(R.id.tvUsername);
        txtDisplayName = findViewById(R.id.tvDisplayName);
        viewMoreClick = findViewById(R.id.viewMoreClick);
        topFansLayout = findViewById(R.id.topFansLayout);

        useImage1 = findViewById(R.id.useImage1);
        useImage2 = findViewById(R.id.useImage2);
        useImage3 = findViewById(R.id.useImage3);
        useImage4 = findViewById(R.id.useImage4);

        userName1 = findViewById(R.id.userName1);
        userName2 = findViewById(R.id.userName2);
        userName3 = findViewById(R.id.userName3);
        userName4 = findViewById(R.id.userName4);

        llUseImage1 = findViewById(R.id.llUseImage1);
        llUseImage2 = findViewById(R.id.llUseImage2);
        llUseImage3 = findViewById(R.id.llUseImage3);
        llUseImage4 = findViewById(R.id.llUseImage4);
        txtEmpty = findViewById(R.id.txtEmpty);

        findViewById(R.id.rowView).setVisibility(INVISIBLE);
        initOnclick();
        if (isAppOwner) {
            btFollow.setVisibility(View.INVISIBLE);
            btnTabGift.setVisibility(View.GONE);
        } else {
            btFollow.setVisibility(View.VISIBLE);
        }
        txtAbout.addAutoLinkMode(AutoLinkMode.MODE_URL);
        txtAbout.setRequestDisallowParentView(disallowTouchEventCallback);
    }

    public UserProfileView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public UserProfileView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Timber.d("dispatchTouchEvent " + ev.getAction());
        return super.dispatchTouchEvent(ev);
    }

    public void setOnTabChange(OnUserProfileChangeView onTabChange) {
        this.onTabChange = onTabChange;
    }

    public void setUserProfileDetails(final CelebrityProfileModel userProfileDetails) {
        if (userProfileDetails != null) {
            this.userProfileDetails = userProfileDetails;
            ImageLoaderUtil.displayUserImage(getContext().getApplicationContext(), userProfileDetails.getUserImage(), useImage);
            txtDisplayName.setVisibility(View.VISIBLE);
            txtDisplayName.setText(StringUtil.decodeString(userProfileDetails.getDisplayName()));
            txtUserName.setText("@" + userProfileDetails.getUserName());

            setAboutText(userProfileDetails.getAbout());
            updateFollowStatus(userProfileDetails.isFollow(), userProfileDetails.getFollowersCount());
            tvFollowing.setText(Utils.shortenNumber(userProfileDetails.getFollowingCount()));
            tvChallengesCount.setText(Utils.shortenNumber(userProfileDetails.getChallengeCount()));
//            setViewMoreAbout(StringUtil.decodeString(userProfileDetails.getAbout()));
            if (userProfileDetails.getType() == 0)
                txtDisplayName.setCompoundDrawables(null, null, null, null);

//            txtAbout.post(new Runnable() {
//                @Override
//                public void run() {
//                    int lineCount = txtAbout.getLineCount();
//                    if (lineCount < 5) {
//                    } else {
//                        makeTextViewResizable(txtAbout, 5, "...more", true);
//                    }
//                }
//            });
        }
    }

    private void setAboutText(String about) {
        if (txtAbout != null) {
            String bioText = StringUtil.decodeString(about);
            txtAbout.setAutoLinkText(bioText);
        }
    }

    private void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                String text;
                int lineEndIndex;
                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    lineEndIndex = tv.getLayout().getLineEnd(0);
                    text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                } else {
                    lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                }

                SpannableString truncatedSpannableString = new SpannableString(text);
                int startIndex = text.indexOf(expandText);
                truncatedSpannableString.setSpan(new ForegroundColorSpan(getContext().getColor(android.R.color.white)), startIndex, startIndex + expandText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv.setText(truncatedSpannableString);
            }
        });
    }

    public void setTopFans(List<TopFanModel> topFans) {
        if (topFans.size() > 0) {
            viewMoreClick.setVisibility(View.VISIBLE);
            txtEmpty.setVisibility(View.GONE);
        } else {
            txtEmpty.setVisibility(View.VISIBLE);
            viewMoreClick.setVisibility(View.GONE);
            if (isAppOwner) txtEmpty.setText(getContext().getString(R.string.me_topfan_empty_text));
            else txtEmpty.setText(getContext().getString(R.string.topfan_empty_text));
        }

        for (int i = 0; i < topFans.size(); i++) {
            switch (i) {
                case 0:
                    ImageLoaderUtil.displayUserImage(getContext().getApplicationContext(), topFans.get(0).getUserImage(), useImage1);
                    userName1.setText("@" + topFans.get(0).getUserName());
                    llUseImage1.setVisibility(View.VISIBLE);
                    llUseImage1.setOnClickListener(v -> {
                        if (onTabChange != null)
                            onTabChange.onTopFansImageClicked(topFans.get(0).getUserId(), topFans.get(0).getUserName());
                    });
                    break;
                case 1:
                    ImageLoaderUtil.displayUserImage(getContext().getApplicationContext(), topFans.get(1).getUserImage(), useImage2);
                    userName2.setText("@" + topFans.get(1).getUserName());
                    llUseImage2.setVisibility(View.VISIBLE);
                    llUseImage2.setOnClickListener(v -> {
                        if (onTabChange != null)
                            onTabChange.onTopFansImageClicked(topFans.get(1).getUserId(), topFans.get(1).getUserName());
                    });
                    break;
                case 2:
                    ImageLoaderUtil.displayUserImage(getContext().getApplicationContext(), topFans.get(2).getUserImage(), useImage2);
                    userName3.setText("@" + topFans.get(2).getUserName());
                    llUseImage3.setVisibility(View.VISIBLE);
                    llUseImage3.setOnClickListener(v -> {
                        if (onTabChange != null)
                            onTabChange.onTopFansImageClicked(topFans.get(2).getUserId(), topFans.get(2).getUserName());
                    });
                    break;
                case 3:
                    ImageLoaderUtil.displayUserImage(getContext().getApplicationContext(), topFans.get(3).getUserImage(), useImage4);
                    userName4.setText("@" + topFans.get(3).getUserName());
                    llUseImage4.setVisibility(View.VISIBLE);
                    llUseImage4.setOnClickListener(v -> {
                        if (onTabChange != null)
                            onTabChange.onTopFansImageClicked(topFans.get(3).getUserId(), topFans.get(3).getUserName());
                    });
                    break;
            }
        }
    }

    private void initOnclick() {
        btnTabGift.setOnClickListener(v -> {
//            if (FragmentMe.TAB_GIFT_INDEX != currentIndicatorSelected) {
//                changeTabIcon(FragmentMe.TAB_GIFT_INDEX);
            if (onTabChange != null) onTabChange.onChangeToGift();
//            }
        });
        btnTabList.setOnClickListener(v -> {
            if (FragmentMe.TAB_LIST_INDEX != currentIndicatorSelected) {
                changeTabIcon(FragmentMe.TAB_LIST_INDEX);
                if (onTabChange != null) onTabChange.onChangeToListView();
            }
        });
        btnTabGrid.setOnClickListener(v -> {
            if (FragmentMe.TAB_GRID_INDEX != currentIndicatorSelected) {
                changeTabIcon(FragmentMe.TAB_GRID_INDEX);
                if (onTabChange != null) onTabChange.onChangeToGridView();
            }
        });

        viewMoreClick.setOnClickListener(v -> {
            if (onTabChange != null) onTabChange.onViewMoreClick();
        });

        btFollow.setOnClickListener(v -> {
            if (userProfileDetails == null) return;
            if (onTabChange != null) onTabChange.onFollowClicked(!userProfileDetails.isFollow());
        });

        findViewById(R.id.llFollower).setOnClickListener(v -> {
            if (onTabChange != null) onTabChange.onFollowerClicked();
        });

        findViewById(R.id.llFollowing).setOnClickListener(v -> {
            if (onTabChange != null) onTabChange.onFollowingClicked();
        });
        txtAbout.setAutoLinkOnClickListener((autoLinkMode, matchedText) -> {
            if (!hasShowCopyPopup) {
                Timber.e("%s - %s", autoLinkMode, matchedText);
                openChromeTab(activity, matchedText);
            }
        });
    }

    public void changeTabIcon(int newPosition) {
        if (newPosition == currentIndicatorSelected) {
            return;
        }

        switch (currentIndicatorSelected) {
            case FragmentMe.TAB_LIST_INDEX:
                btnTabList.setImageResource(R.drawable.ic_profile_bar_list);
                break;

            case FragmentMe.TAB_GRID_INDEX:
                btnTabGrid.setImageResource(R.drawable.ic_profile_bar_grid);
                break;

            case FragmentMe.TAB_GIFT_INDEX:
                btnTabGift.setImageResource(R.drawable.ic_profile_bar_gift);
                break;
        }

        switch (newPosition) {
            case FragmentMe.TAB_LIST_INDEX:
                btnTabList.setImageResource(R.drawable.ic_profile_bar_list_selected);
                break;

            case FragmentMe.TAB_GRID_INDEX:
                btnTabGrid.setImageResource(R.drawable.ic_profile_bar_grid_selected);
                break;

            case FragmentMe.TAB_GIFT_INDEX:
                btnTabGift.setImageResource(R.drawable.ic_profile_bar_gift_selected);
                break;
        }
        currentIndicatorSelected = newPosition;
    }

    public void updateFollowStatus(boolean isFollow, long followerCount) {

        if (userProfileDetails == null) return;
        tvFollwoersCount.setText(Utils.shortenNumber(followerCount));
        userProfileDetails.setFollow(isFollow);
        if (userProfileDetails.isFollow()) {
            btFollow.setBackgroundResource(R.drawable.home_following_btn_selector);
            btFollow.setTextColor(ContextCompat.getColor(getContext(), R.color.color_2587E7));
            btFollow.setText(getContext().getString(R.string.profile_following_title));
        } else {
            btFollow.setBackgroundResource(R.drawable.home_follow_btn_selector);
            btFollow.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            btFollow.setText(getContext().getString(R.string.follow));
        }
    }

    public void updateInfo() {
        if (userProfileDetails != null && userProfileDetails.getUserId() == AppsterApplication.mAppPreferences.getUserModel().getUserId()) {
            userProfileDetails.setUserImage(AppsterApplication.mAppPreferences.getUserModel().getUserImage());
            userProfileDetails.setDisplayName(AppsterApplication.mAppPreferences.getUserModel().getDisplayName());
            userProfileDetails.setAbout(AppsterApplication.mAppPreferences.getUserModel().getAbout());
            userProfileDetails.setFollowingCount(AppsterApplication.mAppPreferences.getUserModel().getFollowingCount());

            ImageLoaderUtil.displayUserImage(getContext().getApplicationContext(), userProfileDetails.getUserImage(), useImage);
            txtDisplayName.setText(StringUtil.decodeString(userProfileDetails.getDisplayName()));
            tvFollowing.setText(Utils.shortenNumber(userProfileDetails.getFollowingCount()));
            setAboutText(userProfileDetails.getAbout());
        }
    }
}
