package com.gcox.fansmeet.features.profile.userprofile.gift;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.domain.interactors.giftstore.GetGiftStoreUseCase;
import com.gcox.fansmeet.AppsterApplication;
import com.gcox.fansmeet.R;
import com.gcox.fansmeet.common.Constants;
import com.gcox.fansmeet.core.activity.BaseToolBarActivity;
import com.gcox.fansmeet.core.dialog.DialogInfoUtility;
import com.gcox.fansmeet.core.dialog.DialogbeLiveConfirmation;
import com.gcox.fansmeet.core.fragment.BaseFragment;
import com.gcox.fansmeet.customview.WrapContentHeightViewPager;
import com.gcox.fansmeet.data.repository.GiftStoreDataRepository;
import com.gcox.fansmeet.data.repository.datasource.GiftStoreDataSource;
import com.gcox.fansmeet.data.repository.datasource.cloud.CloudGiftStoreDataSource;
import com.gcox.fansmeet.domain.repository.GiftStoreRepository;
import com.gcox.fansmeet.features.refill.ActivityRefill;
import com.gcox.fansmeet.models.UserModel;
import com.gcox.fansmeet.util.AppsterUtility;
import com.gcox.fansmeet.util.DialogManager;
import com.gcox.fansmeet.webservice.AppsterWebServices;
import com.gcox.fansmeet.webservice.request.SendGiftRequestModel;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import java.util.List;

/**
 * Created by linh on 14/12/2016.
 */

public class GiftFragment extends BaseFragment {

    private View rootView;
    @Bind(R.id.viewPager)
    WrapContentHeightViewPager viewPager;
    @Bind(R.id.txt_total_gem)
    TextView txtTotalGem;// ~ bean
    @Bind(R.id.viewPagerCountDots)
    LinearLayout dotsLayout;
    @Bind(R.id.btnSend)
    ImageButton btn_send;
    @Bind(R.id.btnRefill)
    ImageButton btn_refill;

    ImageView[] dots;

    private GiftPagerAdapter myViewPagerAdapter;
    // page change listener
    private ViewPager.OnPageChangeListener viewPagerPageChangeListener;
//    private Dialog dialogSendGift;

    Context context;
    DialogInfoUtility utility;

    GiftRecyclerViewAdapter.CompleteSendGift completeSendGift;

    UserModel appOwner;
    private UserModel userProfileDetails;
    private int userID;

    private String gift_id = "";
    long totalGem;
    int dotsCount;
    private int streamID;
    boolean isShowAlertSendSucessFull = true;
    protected CompositeDisposable compositeDisposable ;

    private boolean itemTransparent = true;
    private GetGiftStoreUseCase mGetGiftStoreUseCase;
    //================ inherited methods ===========================================================
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appOwner = AppsterApplication.mAppPreferences.getUserModel();
        compositeDisposable =new CompositeDisposable();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView != null) {
            return rootView;
        }
        rootView = inflater.inflate(R.layout.dialog_send_gif, container, false);
        ButterKnife.bind(this, rootView);
        handleButtonSend();
        initUseCase();
//        getListGift();
        utility = new DialogInfoUtility();
        return rootView;
    }
    private void initUseCase() {
        GiftStoreDataSource giftStoreDataSource = new CloudGiftStoreDataSource(AppsterWebServices.get(), AppsterUtility.getAuth());
        GiftStoreRepository giftStoreRepository = new GiftStoreDataRepository(giftStoreDataSource);
        Scheduler uiThread = AndroidSchedulers.mainThread();
        Scheduler ioThread = Schedulers.io();
        mGetGiftStoreUseCase = new GetGiftStoreUseCase(uiThread, ioThread, giftStoreRepository);
    }
    @Override
    public void onResume() {
        super.onResume();
        refresh();
        if (AppsterApplication.mAppPreferences.isUserLogin()) {
            txtTotalGem.setText(String.valueOf(totalGem));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        if (!compositeDisposable.isDisposed()) compositeDisposable.dispose();
    }

    //=========== implemented methods ==============================================================
    @OnClick(R.id.btnSend)
    void onSendButtonClick() {

    }
    private void showToastOutOfUnpurchaseGift() {
        if(getActivity()==null) return;
        Toast.makeText(getActivity(), getActivity().getString(R.string.out_of_unpurchasable_gift), Toast.LENGTH_SHORT).show();
    }
    @OnClick(R.id.btnRefill)
    void onRefillButtonClick() {
        openRefillScreen();
    }

    //=========== inner methods ====================================================================
    public void setUserProfileDetails(UserModel userProfileDetails) {
        this.userProfileDetails = userProfileDetails;
        userID = userProfileDetails.getUserId();
    }

    public void setBackgroundTransparent(boolean itemTransparent) {
        this.itemTransparent = itemTransparent;
    }

    private void handleButtonSend() {
        if (AppsterApplication.mAppPreferences.isUserLogin() && appOwner.getUserId()== userID) {
            btn_send.setVisibility(View.GONE);
        } else {
            btn_send.setVisibility(View.VISIBLE);
        }
    }

    private void showDialognotChoseGift() {

        DialogbeLiveConfirmation.Builder builder = new DialogbeLiveConfirmation.Builder();
        builder.title(getString(R.string.app_name))
                .message(getString(R.string.sendgift_no_choose_gift))
                .confirmText(getString(R.string.btn_text_ok))
                .singleAction(true)
                .onConfirmClicked(() -> {
                    //action
                })
                .build().show(getContext());
    }

    private void showMessageNotEnoughStart() {
//        utility.showMessagePurchasedMore(context.getString(R.string.app_name),
//                context.getString(R.string.sendgift_no_choose_gift), context, v -> openRefillScreen());

        DialogbeLiveConfirmation.Builder builder = new DialogbeLiveConfirmation.Builder();
        builder.title(getString(R.string.app_name))
                .message(getString(R.string.sendgift_no_enough_bean))
                .confirmText(getString(R.string.btn_text_ok))
                .singleAction(true)
                .onConfirmClicked(this::openRefillScreen)
                .build().show(getContext());
    }

    private void openRefillScreen() {
        Intent i = new Intent(context, ActivityRefill.class);
        getActivity().startActivityForResult(i, Constants.REQUEST_CODE_REFILL_SCREEN);
    }

    public void setCompleteSendGift(GiftRecyclerViewAdapter.CompleteSendGift completeSendGift) {
        this.completeSendGift = completeSendGift;
    }


    private void sendGift(GiftItemModel item) {
        if (null == item) return;

        DialogManager.getInstance().showDialog(context, context.getResources().getString(R.string.connecting_msg));
        SendGiftRequestModel request = new SendGiftRequestModel();
        request.setReceiver_user_id(userID);
        request.setGift_id(item.getId());

        if (streamID > 0) {
            request.setStream_id(streamID);
        }

    }

    private void getListGift() {
        DialogManager.getInstance().showDialog(context, context.getResources().getString(R.string.connecting_msg));

        compositeDisposable.add(mGetGiftStoreUseCase.execute(null)
                .subscribe(giftStoreModel -> {
                    DialogManager.getInstance().dismisDialog();
                    if (giftStoreModel == null) return;
                    if (!giftStoreModel.getGiftItems().isEmpty()) {
                            totalGem = giftStoreModel.getGems();

                            setViewPagerItemsWithAdapter(giftStoreModel.getGiftItems());
                            setViewpagerIndicator();
                            if (AppsterApplication.mAppPreferences.isUserLogin()) {
                                appOwner.setGems(totalGem);
                                txtTotalGem.setText(String.valueOf(totalGem));
                            }
                        }
                }, error -> DialogManager.getInstance().dismisDialog()));
    }


    void refresh() {
        getListGift();
    }

    void setViewPagerItemsWithAdapter(List<GiftItemModel> giftList) {

        if (myViewPagerAdapter == null && viewPagerPageChangeListener == null) {

            int residual = giftList.size() % 8;
            if (residual == 0) {
                dotsCount = giftList.size() / 8;
            } else {
                dotsCount = giftList.size() / 8 + 1;
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
                    //don't need to implement
                }

                @Override
                public void onPageScrollStateChanged(int arg0) {
                    //don't need to implement
                }
            };


        }

        initViewPager(giftList);
    }

    private void initViewPager(List<GiftItemModel> giftList) {
        myViewPagerAdapter = new GiftPagerAdapter(getContext(), dotsCount, giftList);
        myViewPagerAdapter.setBackgroudTransparent(itemTransparent);
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        viewPager.setOffscreenPageLimit(dotsCount);
    }

    void setViewpagerIndicator() {
        dotsLayout.removeAllViews();
        dots = new ImageView[dotsCount];
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(8, 0, 0, 0);

        for (int i = 0; i < dotsCount; i++) {

            dots[i] = new ImageView(context);
            dots[i].setLayoutParams(lp);
            dots[i].setBackgroundResource(R.drawable.circle_dot_gift);
            dotsLayout.addView(dots[i]);
        }

        dots[0].setBackgroundResource(R.drawable.circle_dot_gift_red);
    }

    //region inner classes
    class GemChangeEvent {
        long gemCount;

        GemChangeEvent(long gemCount) {
            this.gemCount = gemCount;
        }
    }
    //endregion
}
