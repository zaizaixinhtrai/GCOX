package com.gcox.fansmeet.features.profile.userprofile.gift;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.domain.interactors.giftstore.GetGiftStoreUseCase;
import com.gcox.fansmeet.AppsterApplication;
import com.gcox.fansmeet.R;
import com.gcox.fansmeet.common.Constants;
import com.gcox.fansmeet.core.dialog.DialogInfoUtility;
import com.gcox.fansmeet.core.dialog.DialogbeLiveConfirmation;
import com.gcox.fansmeet.customview.CustomFontTextView;
import com.gcox.fansmeet.customview.WrapContentHeightViewPager;
import com.gcox.fansmeet.data.repository.GiftStoreDataRepository;
import com.gcox.fansmeet.data.repository.datasource.GiftStoreDataSource;
import com.gcox.fansmeet.data.repository.datasource.cloud.CloudGiftStoreDataSource;
import com.gcox.fansmeet.domain.interactors.useraction.SendStartUseCase;
import com.gcox.fansmeet.domain.repository.GiftStoreRepository;
import com.gcox.fansmeet.exception.GcoxException;
import com.gcox.fansmeet.features.refill.ActivityRefill;
import com.gcox.fansmeet.models.eventbus.EventBusRefreshEntries;
import com.gcox.fansmeet.util.AppsterUtility;
import com.gcox.fansmeet.util.DialogManager;
import com.gcox.fansmeet.util.Utils;
import com.gcox.fansmeet.webservice.AppsterWebServices;
import com.gcox.fansmeet.webservice.request.SendStartRequestModel;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import org.greenrobot.eventbus.EventBus;
import timber.log.Timber;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by User on 8/21/2015.
 */
public class DialogSendGift implements DialogInterface.OnDismissListener {


    @Bind(R.id.viewPager)
    WrapContentHeightViewPager viewPager;
    @Bind(R.id.btnSend)
    Button btnSend;
    @Bind(R.id.txt_total_gem)
    CustomFontTextView txt_total_gems;
    @Bind(R.id.btnRefill)
    ImageButton btn_refill;
    @Bind(R.id.viewPagerCountDots)
    LinearLayout dotsLayout;
    @Bind(R.id.llGiftContainer)
    LinearLayout llGiftContainer;

    private Dialog dialogSendGift;
    private Activity activity;
    private ArrayList<GiftItemModel> arrayList_sendGiftGridView = new ArrayList<GiftItemModel>();
    private DialogInfoUtility utility;


    GiftPagerAdapter myViewPagerAdapter;

    private ImageView[] dots;

    int dotsCount;

    private long totalGems;
    private int receiverId;
    private boolean isSendForUser = true;

    private boolean isShowAlertSendSucessFull = true;
    private AtomicBoolean dismissEnable = new AtomicBoolean(true);
    private GiftRecyclerViewAdapter.CompleteSendGift completeSendGift;
    private DiaLogDismissListener mDialogDismisListener;
    private boolean mIsPrivateChat;
    private GetGiftStoreUseCase mGetGiftStoreUseCase;
    private SendStartUseCase mSendStartUseCase;
    protected CompositeDisposable compositeDisposable;

    public DialogSendGift(final Activity activity) {
        this.activity = activity;
        dialogSendGift = new Dialog(activity, R.style.DialogSlideToUpAnim);
        dialogSendGift.setCanceledOnTouchOutside(true);
        dialogSendGift.requestWindowFeature(Window.FEATURE_NO_TITLE);

        Window window = dialogSendGift.getWindow();
        if (window != null) {
            window.getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE
            );
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT);
            WindowManager.LayoutParams layoutParams = dialogSendGift.getWindow().getAttributes();
            layoutParams.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(layoutParams);
        }
        mIsPrivateChat = true;
        dialogSendGift.setContentView(R.layout.dialog_send_gif);

        ButterKnife.bind(this, dialogSendGift.getWindow().getDecorView());
        ViewGroup.LayoutParams params = llGiftContainer.getLayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        llGiftContainer.setLayoutParams(params);
        utility = new DialogInfoUtility();
        isShowAlertSendSucessFull = true;

        viewPager.setOnClickListener(v -> dialogSendGift.dismiss());
        dialogSendGift.setOnDismissListener(this);

        initUseCase();
    }

    public DialogSendGift(final Activity activity, int receiverId, boolean isSendForUser) {
        this.activity = activity;
        this.receiverId = receiverId;
        this.isSendForUser = isSendForUser;
        initDialogFullScreen();
        utility = new DialogInfoUtility();
        isShowAlertSendSucessFull = false;
        dialogSendGift.setOnDismissListener(this);

        initUseCase();
    }

    private void initUseCase() {
        GiftStoreDataSource giftStoreDataSource = new CloudGiftStoreDataSource(AppsterWebServices.get(), AppsterUtility.getAuth());
        GiftStoreRepository giftStoreRepository = new GiftStoreDataRepository(giftStoreDataSource);
        Scheduler uiThread = AndroidSchedulers.mainThread();
        Scheduler ioThread = Schedulers.io();
        mGetGiftStoreUseCase = new GetGiftStoreUseCase(uiThread, ioThread, giftStoreRepository);
        mSendStartUseCase = new SendStartUseCase(uiThread, ioThread, giftStoreRepository);
    }

    private void showToastOutOfUnpurchaseGift() {
        if (activity == null) return;
        Toast.makeText(activity, activity.getString(R.string.out_of_unpurchasable_gift), Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.btnRefill)
    public void onRefillClicked() {
        openRefillScreen();
    }

    @OnClick(R.id.llGiftContainer)
    public void onllGiftContainerClicked() {
//        closeGiftDialog();
    }

    private void closeGiftDialog() {
//        if (!dismissEnable.get()) return;
        arrayList_sendGiftGridView.clear();
        if (dialogSendGift != null) dialogSendGift.dismiss();
    }

    @OnClick(R.id.imClose)
    public void close() {
        closeGiftDialog();
    }

    @OnClick(R.id.btnSend)
    public void sendGift() {
        if (isSendForUser) {
            sendGiftForUser(myViewPagerAdapter.getSelectedItem());
        } else {
            sendGiftForEntries(myViewPagerAdapter.getSelectedItem());
        }
    }

//    @OnClick(R.id.root_view)
//    public void onOutSideClicked() {
//        closeGiftDialog();
//    }
    //endregion


    public void initDialogFullScreen() {
        dialogSendGift = new Dialog(activity);
        dialogSendGift.getWindow().getAttributes().windowAnimations = R.style.DialogZoomAnimation;
        dialogSendGift.getWindow().setBackgroundDrawableResource(R.color.transparent);
        Window window = dialogSendGift.getWindow();
        dialogSendGift.setCanceledOnTouchOutside(true);
        dialogSendGift.setContentView(R.layout.dialog_send_gif);
        dialogSendGift.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams layoutParams = dialogSendGift.getWindow().getAttributes();
        layoutParams.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(layoutParams);
        ButterKnife.bind(this, dialogSendGift.getWindow().getDecorView());
//        dialogSendGift.findViewById(R.id.llGiftContainer).setBackgroundColor(ContextCompat.getColor(activity, R.color.color_242424_80));
        ViewGroup.LayoutParams params = llGiftContainer.getLayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        llGiftContainer.setLayoutParams(params);

    }


    private void openRefillScreen() {
        Intent i = new Intent(activity, ActivityRefill.class);
        activity.startActivityForResult(i, Constants.REQUEST_CODE_REFILL_SCREEN);
    }

    public void setCompleteSendGift(GiftRecyclerViewAdapter.CompleteSendGift completeSendGift) {
        this.completeSendGift = completeSendGift;
    }

    private void showDialognotChoseGift() {
        utility.showMessage(activity.getString(R.string.app_name),
                activity.getString(R.string.sendgift_no_choose_gift),
                activity);
    }

    private void showMessageNotEnoughStart() {
    }

    public void show() {
        dialogSendGift.show();
        getListGift();
    }

    public void show(boolean hideButtonSend) {

        if (hideButtonSend) {
            btnSend.setVisibility(View.GONE);
        }

        dialogSendGift.show();
//        getListGift();

    }


    // page change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener;

    private void setViewPagerItemsWithAdapter() {

        if (myViewPagerAdapter == null && viewPagerPageChangeListener == null) {

            int residual = arrayList_sendGiftGridView.size() % 8;
            if (residual == 0) {
                dotsCount = arrayList_sendGiftGridView.size() / 8;
            } else {
                dotsCount = arrayList_sendGiftGridView.size() / 8 + 1;
            }
            viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageSelected(int position) {
                    for (int i = 0; i < dotsCount; i++) {
                        dots[i].setBackgroundResource(R.drawable.circle_dot_gift);
                    }
                    dots[position].setBackgroundResource(R.drawable.circle_dot_gift_red);
                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {

                }

                @Override
                public void onPageScrollStateChanged(int arg0) {

                }
            };
        }
        myViewPagerAdapter = new GiftPagerAdapter(activity, dotsCount, arrayList_sendGiftGridView, mIsPrivateChat);
        if (mIsPrivateChat) myViewPagerAdapter.setBackgroudTransparent(false);
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        viewPager.setOffscreenPageLimit(dotsCount);
    }

    private void setUiPageViewController() {
        dots = new ImageView[dotsCount];

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        params.height = 5;

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(Utils.dpToPx(5), 0, 0, 0);

        for (int i = 0; i < dotsCount; i++) {

            dots[i] = new ImageView(activity);
            dots[i].setLayoutParams(lp);
            dots[i].setBackgroundResource(R.drawable.circle_dot_gift);
            dotsLayout.addView(dots[i]);
        }

        dots[0].setBackgroundResource(R.drawable.circle_dot_gift_red);

        dotsLayout.setVisibility(View.GONE);
    }


    private void getListGift() {
        if (mGetGiftStoreUseCase == null) return;
        DialogManager.getInstance().showDialog(activity, activity.getResources().getString(R.string.connecting_msg));
        if (compositeDisposable == null) compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(mGetGiftStoreUseCase.execute(null)
                .subscribe(giftStoreModel -> {
                    DialogManager.getInstance().dismisDialog();
                    if (giftStoreModel == null) return;
                    if (!giftStoreModel.getGiftItems().isEmpty()) {
                        totalGems = giftStoreModel.getGems();
                        arrayList_sendGiftGridView.clear();
                        arrayList_sendGiftGridView.addAll(giftStoreModel.getGiftItems());
                        setViewPagerItemsWithAdapter();
                        setUiPageViewController();

                        if (AppsterApplication.mAppPreferences.isUserLogin()) {
                            AppsterApplication.mAppPreferences.getUserModel().setGems(totalGems);
                        }
                        txt_total_gems.setText(String.valueOf(totalGems));
                    }
                }, error -> DialogManager.getInstance().dismisDialog()));
    }

    public void dimissDialog() {
        dismissEnable.set(true);
        closeGiftDialog();
    }

    public void resume() {
        if (dialogSendGift != null && dialogSendGift.isShowing()) {
            Timber.e("call update bean");
            updatePurchaseBean();
        }
    }

    private void updatePurchaseBean() {
        if (mGetGiftStoreUseCase == null) return;
        compositeDisposable.add(mGetGiftStoreUseCase.execute(null)
                .subscribe(giftStoreModel -> {
                    if (giftStoreModel == null) return;

                    totalGems = giftStoreModel.getGems();
                    if (AppsterApplication.mAppPreferences.isUserLogin()) {
                        AppsterApplication.mAppPreferences.getUserModel().setGems(totalGems);
                        Timber.e("total bean %d", totalGems);
                        if (txt_total_gems != null) {
                            Timber.e("total bean update %d", totalGems);
                            txt_total_gems.setText(String.valueOf(totalGems));
                        }

                    }

                }, error -> Timber.e(error.getMessage())));
    }

    public void handleErrorCode(int code, String message) {
        if (activity != null) {
            if (code == 1096) {
                new DialogbeLiveConfirmation.Builder()
                        .title(activity.getString(R.string.app_name))
                        .singleAction(true)
                        .message(message)
                        .onConfirmClicked(this::handleIfUserNotEnoughGem)
                        .build().show(activity);
            } else {
                new DialogbeLiveConfirmation.Builder()
                        .title(activity.getString(R.string.app_name))
                        .singleAction(true)
                        .message(message)
                        .build().show(activity);
            }
        }
    }

    private void handleIfUserNotEnoughGem() {
        dimissDialog();
        ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(activity,
                R.anim.push_in_to_right, R.anim.push_in_to_left);
        Intent intent = new Intent(activity, ActivityRefill.class);
        activity.startActivity(intent, options.toBundle());
    }

    private void sendGiftForUser(GiftItemModel item) {
        if (item == null) return;

        DialogManager.getInstance().showDialog(activity, activity.getResources().getString(R.string.connecting_msg));
        if (btnSend != null) btnSend.setEnabled(false);

        compositeDisposable.add(mSendStartUseCase.execute(SendStartUseCase.Params.load(item.getId(), receiverId))
                .subscribe(giftStoreModel -> {
                    if (btnSend != null) btnSend.setEnabled(true);
                    DialogManager.getInstance().dismisDialog();
                    if (giftStoreModel != null) {
                        AppsterApplication.mAppPreferences.getUserModel().setGems(giftStoreModel.getSenderGems());
                        totalGems = giftStoreModel.getSenderGems();
                        if (txt_total_gems != null) {
                            txt_total_gems.setText(String.valueOf(totalGems));
                        }
                    }

                }, error -> {
                    DialogManager.getInstance().dismisDialog();
                    if (btnSend != null) btnSend.setEnabled(true);
                    if (error instanceof GcoxException) {
                        GcoxException serverException = (GcoxException) error;
                        handleErrorCode(serverException.getCode(), serverException.getMessage());
                    } else {
                        handleErrorCode(Constants.RETROFIT_ERROR, error.getMessage());
                    }

                }));
    }

    private void sendGiftForEntries(GiftItemModel item) {
        if (item == null) return;

        DialogManager.getInstance().showDialog(activity, activity.getResources().getString(R.string.connecting_msg));
        if (btnSend != null) btnSend.setEnabled(false);

        compositeDisposable.add(AppsterWebServices.get().sendStart("Bearer " + AppsterApplication.mAppPreferences.getUserToken(), receiverId,
                new SendStartRequestModel(item.getId()))
                .subscribe(giftStoreModel -> {
                    if (btnSend != null) btnSend.setEnabled(true);
                    DialogManager.getInstance().dismisDialog();
                    if (giftStoreModel.getCode() == Constants.RESPONSE_FROM_WEB_SERVICE_OK) {
                        if (giftStoreModel.getData() != null) {
                            AppsterApplication.mAppPreferences.getUserModel().setGems(giftStoreModel.getData().getSenderGems());
                            totalGems = giftStoreModel.getData().getSenderGems();
                            if (txt_total_gems != null) {
                                txt_total_gems.setText(String.valueOf(totalGems));
                            }
                        }
                        EventBus.getDefault().post(new EventBusRefreshEntries(true));
                    } else {
                        Timber.e(giftStoreModel.getMessage());
                        handleErrorCode(giftStoreModel.getCode(), giftStoreModel.getMessage());
                    }
                }, error -> {
                    Timber.e(error);
                    DialogManager.getInstance().dismisDialog();
                    if (btnSend != null) btnSend.setEnabled(true);
                    if (error instanceof GcoxException) {
                        GcoxException serverException = (GcoxException) error;
                        handleErrorCode(serverException.getCode(), serverException.getMessage());
                    } else {
                        handleErrorCode(Constants.RETROFIT_ERROR, error.getMessage());
                    }
                }));
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (mDialogDismisListener != null) mDialogDismisListener.onDiaLogDismissed();
        arrayList_sendGiftGridView.clear();
        ButterKnife.unbind(this);
        if (!compositeDisposable.isDisposed()) compositeDisposable.dispose();
    }


    public void setOnDismissListener(DiaLogDismissListener dismissListener) {
        this.mDialogDismisListener = dismissListener;
    }

}
