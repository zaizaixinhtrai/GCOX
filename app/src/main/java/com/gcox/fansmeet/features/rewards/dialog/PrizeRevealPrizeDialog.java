package com.gcox.fansmeet.features.rewards.dialog;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.gcox.fansmeet.R;
import com.gcox.fansmeet.common.Constants;
import com.gcox.fansmeet.core.dialog.DialogbeLiveConfirmation;
import com.gcox.fansmeet.core.dialog.NoTitleDialogFragment;
import com.gcox.fansmeet.customview.CustomFontButton;
import com.gcox.fansmeet.customview.CustomFontEditText;
import com.gcox.fansmeet.customview.CustomFontTextView;
import com.gcox.fansmeet.features.rewards.models.PrizeCollectModel;
import com.gcox.fansmeet.util.AppsterUtility;
import com.gcox.fansmeet.util.DialogManager;
import com.gcox.fansmeet.util.EmailUtil;
import com.gcox.fansmeet.util.ImageLoaderUtil;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.gcox.fansmeet.webservice.AppsterWebServices;
import com.gcox.fansmeet.webservice.request.BagRedeemRequestModel;
import com.jakewharton.rxbinding2.widget.RxTextView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;


/**
 * Created by linh on 07/11/2017.
 */

public class PrizeRevealPrizeDialog extends NoTitleDialogFragment implements ImageLoaderUtil.ImageLoaderCallback {
    private static final String PRIZE_ITEM = "PRIZE_ITEM";
    private static final int GLOW_ANIMATION_DURATION = 3600;
    private static final int SPARKLE_SCALE_UP_DURATION = 1500;
    private static final int SPARKLE_FADE_IN_DURATION = 1000;
    private static final int SPARKLE_FADE_OUT_DURATION = 500;
    private static final int SPARKLE_DELAYED_TIME = 1000;

    @Bind(R.id.edtEmail)
    CustomFontEditText edtEmail;
    @Bind(R.id.img_glow)
    ImageView mImgGlow;
    @Bind(R.id.img_sparkle)
    ImageView mImgSparkle;
    @Bind(R.id.img_gift)
    ImageView mImgPrize;
    @Bind(R.id.tvPrizeDesc)
    CustomFontTextView tvPrizeDesc;
    @Bind(R.id.tvContent)
    CustomFontTextView tvContent;
    @Bind(R.id.tvInvalidEmail)
    CustomFontTextView tvInvalidEmail;
    @Bind(R.id.txt_daily_treat_message)
    CustomFontTextView mTxtRevealPrizeMessage;
    @Bind(R.id.fmImageGift)
    FrameLayout fmImageGift;
    @Bind(R.id.btn_go_to_bag)
    CustomFontButton btRedeem;


    private PrizeCollectModel mDailyTreat;

    ObjectAnimator mShowGlowAnimator;
    ObjectAnimator sparkleScaleXAnimator;
    ObjectAnimator sparkleScaleYAnimator;
    ObjectAnimator sparkleFadeInAnimator;
    ObjectAnimator sparkleFadeOutAnimator;
//    AnimatorSet mAnimatorSet;

    CompositeDisposable mCompositeSubscription;

    private PublishSubject<Long> mIntervalSubject = PublishSubject.create();
    private DaylyTreatPrizeListner daylyTreatPrizeListner;

    public static PrizeRevealPrizeDialog newInstance(PrizeCollectModel dailyTreatType) {

        Bundle args = new Bundle();
        args.putParcelable(PRIZE_ITEM, dailyTreatType);
        PrizeRevealPrizeDialog fragment = new PrizeRevealPrizeDialog();
        fragment.setArguments(args);
        return fragment;
    }

    //======= lifecycle ============================================================================
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        if (b != null) {
            //noinspection WrongConstant
            mDailyTreat = b.getParcelable(PRIZE_ITEM);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        setCancelable(false);
        initAnimators();
        bindView();
//        mPresenter.getAppConfig();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        if (mAnimatorSet != null){
//            mAnimatorSet.removeAllListeners();
//            mAnimatorSet.cancel();
//        }
        if (mCompositeSubscription != null) mCompositeSubscription.clear();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIntervalSubject.onNext(-1L);
    }

    @Override
    protected int getRootLayoutResource() {
        return R.layout.dialog_prize_reveal;
    }

    @Override
    protected boolean isDimDialog() {
        return true;
    }

    @Override
    protected float dimAmount() {
        return 0.9f;
    }

    //======= event handlers =======================================================================

    @OnClick(R.id.btn_back)
    void onBackButtonClicked() {
        dismiss();
    }

    @OnClick(R.id.btn_go_to_bag)
    void onGoToBagButtonClicked() {

        if (edtEmail.getText() == null) return;
        if (mDailyTreat != null) {
            DialogManager.getInstance().showDialog(getContext(), getString(R.string.connecting_msg));
            mCompositeSubscription.add(AppsterWebServices.get().bagRedeem(AppsterUtility.getAuth(), mDailyTreat.getId(),
                    new BagRedeemRequestModel(mDailyTreat.getName(), edtEmail.getText().toString().trim()))
                    .subscribe(response -> {
                        DialogManager.getInstance().dismisDialog();
                        if (response.getCode() == Constants.RESPONSE_FROM_WEB_SERVICE_OK) {
                            if (daylyTreatPrizeListner != null) daylyTreatPrizeListner.sendEmailSuccess(mDailyTreat.getId());
                            dismiss();
                        } else {
                            handleErrorCode(response.getMessage());
                        }
                    }, throwable -> {
                        DialogManager.getInstance().dismisDialog();
                        Timber.e(throwable.getMessage());
                        handleErrorCode(throwable.getMessage());
                    }));
        }

    }

    private void handleErrorCode(String message) {
        if (getContext() != null) {
            new DialogbeLiveConfirmation.Builder()
                    .title(getString(R.string.app_name))
                    .singleAction(true)
                    .message(message)
                    .build().show(getContext());
        }

    }

    //======= mvp callbacks ========================================================================
//    @Override
//    public void onGetAppConfigSuccessfully(int nextTreatTimeLeft) {
//        if (getViewContext() != null)
//            DailyBonusUtils.setupDailyBonusNotification(Integer.parseInt(mAppPreferences.getUserId()), nextTreatTimeLeft, DailyBonusJobCreator.DAILY);
////        setupNextTreatCountDown(nextTreatTimeLeft);
//    }
//
//    @Override
//    public void onGetAppConfigSuccessFailed() {
//
//    }

    //========= picasso callbacks ==================================================================
    @Override
    public void onFailed(Exception e) {
        if (mImgPrize != null)
            mImgPrize.setBackgroundColor(Color.parseColor("#FFFFFF"));
    }

    @Override
    public void onSuccess(Bitmap bitmap) {
        if (isFragmentUIActive()) {
            setupViewsByType();
            startAnimation();
        }
    }

    public void setDaylyTreatPrizeListner(DaylyTreatPrizeListner listner) {
        this.daylyTreatPrizeListner = listner;
    }

    //======= inner methods ========================================================================
    private void init() {
        mCompositeSubscription = new CompositeDisposable();
    }

    private void bindView() {
        tvPrizeDesc.setText(mDailyTreat.getName());
//        tvContent.setText(mDailyTreat.getDescription());
        if (!mDailyTreat.getImage().isEmpty()) {
            ImageLoaderUtil.displayUserImage(getContext(), mDailyTreat.getImage(), mImgPrize, false, this);
        } else {
            mImgPrize.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

        mCompositeSubscription.add(RxTextView.textChangeEvents(edtEmail)
                .observeOn(Schedulers.computation())
                .debounce(300, TimeUnit.MILLISECONDS) // default Scheduler is Computation
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(textViewTextChangeEvent -> checkEmailValid(textViewTextChangeEvent.text().toString()), Timber::e));
    }

    private void checkEmailValid(String email) {
        if (!TextUtils.isEmpty(email)) {
            if (EmailUtil.isEmail(email)) {
                btRedeem.setBackgroundResource(R.drawable.send_gift_button_background);
                btRedeem.setEnabled(true);
                tvInvalidEmail.setVisibility(View.INVISIBLE);
                edtEmail.replaceCustomDrawableEnd(0);
            } else {
                tvInvalidEmail.setVisibility(View.VISIBLE);
                edtEmail.replaceCustomDrawableEnd(R.drawable.ic_id_name_invalid);
                btRedeem.setBackgroundResource(R.drawable.redeem_button_bg);
                btRedeem.setEnabled(false);
            }
        } else {
            tvInvalidEmail.setVisibility(View.INVISIBLE);
            edtEmail.replaceCustomDrawableEnd(0);
            btRedeem.setEnabled(false);
            btRedeem.setBackgroundResource(R.drawable.redeem_button_bg);
        }
    }

    private void setupViewsByType() {
        @DrawableRes int sparkleRes = 0;
        mImgGlow.setImageResource(R.drawable.ic_glow_blue);
        mImgSparkle.setImageResource(sparkleRes);
    }

    private void initAnimators() {
        mShowGlowAnimator = ObjectAnimator.ofFloat(mImgGlow, "rotation", 0f, 360f)
                .setDuration(GLOW_ANIMATION_DURATION);
        mShowGlowAnimator.setRepeatMode(ValueAnimator.RESTART);
        mShowGlowAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mShowGlowAnimator.setInterpolator(null);
        mShowGlowAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (isFragmentUIActive()) {
                    mImgGlow.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        sparkleFadeInAnimator = ObjectAnimator.ofFloat(mImgSparkle, "alpha", 0f, 1f);
        sparkleFadeInAnimator.setDuration(SPARKLE_FADE_IN_DURATION);
        sparkleFadeInAnimator.setRepeatMode(ValueAnimator.RESTART);
        //        sparkleFadeInAnimator.setRepeatCount(ValueAnimator.INFINITE);

        sparkleFadeOutAnimator = ObjectAnimator.ofFloat(mImgSparkle, "alpha", 1f, 0f);
        sparkleFadeOutAnimator.setDuration(SPARKLE_FADE_OUT_DURATION);
        sparkleFadeOutAnimator.setRepeatMode(ValueAnimator.RESTART);
        sparkleFadeOutAnimator.setStartDelay(SPARKLE_SCALE_UP_DURATION - SPARKLE_FADE_OUT_DURATION);
        //        sparkleFadeOutAnimator.setRepeatCount(ValueAnimator.INFINITE);
        sparkleFadeOutAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (isFragmentUIActive()) {
                    startSparkleAnimationWidthDelay();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        sparkleScaleXAnimator = ObjectAnimator.ofFloat(mImgSparkle, "scaleX", 0f, 1f);
        sparkleScaleXAnimator.setDuration(SPARKLE_SCALE_UP_DURATION);
        sparkleScaleXAnimator.setRepeatMode(ValueAnimator.RESTART);
//        sparkleScaleXAnimator.setRepeatCount(ValueAnimator.INFINITE);

        sparkleScaleYAnimator = ObjectAnimator.ofFloat(mImgSparkle, "scaleY", 0f, 1f);
        sparkleScaleYAnimator.setDuration(SPARKLE_SCALE_UP_DURATION);
        sparkleScaleYAnimator.setRepeatMode(ValueAnimator.RESTART);
//        sparkleScaleYAnimator.setRepeatCount(ValueAnimator.INFINITE);
    }

    private void startAnimation() {
        mShowGlowAnimator.start();
        startSparkleAnimation();
    }

    private void startSparkleAnimationWidthDelay() {
        mCompositeSubscription.add(Observable.fromCallable(() -> null)
                .delay(SPARKLE_DELAYED_TIME, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> startSparkleAnimation(), Timber::e));
    }

    private void startSparkleAnimation() {
        return;
//        sparkleScaleXAnimator.start();
//        sparkleScaleYAnimator.start();
//        sparkleFadeInAnimator.start();
//        sparkleFadeOutAnimator.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    //==== inner classes ===========================================================================
    public interface DaylyTreatPrizeListner {
        void sendEmailSuccess(int boxId);
    }
}
