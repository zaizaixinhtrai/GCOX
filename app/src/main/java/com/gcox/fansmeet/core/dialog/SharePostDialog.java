package com.gcox.fansmeet.core.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.gcox.fansmeet.R;
import com.gcox.fansmeet.customview.CustomFontTextView;
import com.gcox.fansmeet.customview.SquareImageView;
import com.gcox.fansmeet.features.profile.userprofile.gift.DiaLogDismissListener;
import com.gcox.fansmeet.util.ImageLoaderUtil;

/**
 * Created by User on 5/9/2016.
 */
public class SharePostDialog extends DialogFragment {

    private static final String IS_TRANSPARENT_BG = "isTransparentBackground";

    ChooseShareListenner chooseShareListenner;

    CustomFontTextView tvShareTitle;
    LinearLayout llCategories;
    LinearLayout llShareDialogContainer;

    private boolean isHideInstagram = false;
    private boolean mIsTransparentBackground = false;
    private DiaLogDismissListener mDialogDismisListener;
    private boolean isHideCopyLink = false;

    public static SharePostDialog newInstance() {
        return new SharePostDialog();
    }

    public static SharePostDialog newInstance(boolean isTransparentBackground) {
        SharePostDialog sharePostDialog = new SharePostDialog();
        Bundle bundle = new Bundle();

        bundle.putBoolean(IS_TRANSPARENT_BG, isTransparentBackground);
        sharePostDialog.setArguments(bundle);
        return sharePostDialog;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        final DialogFragment fragment = (DialogFragment) manager.findFragmentByTag(tag);
        if(fragment!=null && fragment.isAdded()) return;
        if(fragment!=null && fragment.getDialog()!=null && fragment.getDialog().isShowing()) return;
        super.show(manager, tag);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);

//        // request a window without the title
        Window window = dialog.getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        if (getArguments() != null) {
            mIsTransparentBackground = getArguments().getBoolean(IS_TRANSPARENT_BG, false);
        }
        View view = inflater.inflate(R.layout.popup_menu_share, container);
        tvShareTitle = view.findViewById(R.id.tvShareTitle);
        llCategories = view.findViewById(R.id.llCategories);
        llShareDialogContainer = view.findViewById(R.id.llShareDialogContainer);
        inti();

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (getArguments() != null) {
            mIsTransparentBackground = getArguments().getBoolean(IS_TRANSPARENT_BG, false);
            if (mIsTransparentBackground) {
                WindowManager.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
                layoutParams.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                getDialog().getWindow().setAttributes(layoutParams);
            }
        }

    }

    @Override
    public int getTheme() {
        return R.style.Theme_DialogCustom;
    }

    public void setChooseShareListenner(ChooseShareListenner chooseShareListenner) {
        this.chooseShareListenner = chooseShareListenner;
    }

    private void inti() {
        llCategories.removeAllViews();
        if (mIsTransparentBackground) {
//            llShareDialogContainer.setBackground(ContextCompat.getDrawable(getContext(), R.color.color_242424_80));
            tvShareTitle.setTextSize(14);
            tvShareTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.color_d8d8d8));
        }
//        if (isHideInstagram) llInstagram.setVisibility(View.GONE);
//        if (isHideCopyLink) {
//            txtCopylink.setVisibility(View.GONE);
//        } else {
//            txtCopylink.setVisibility(View.VISIBLE);
//        }
//        llFacebook.setOnClickListener(v -> {
//            if (chooseShareListenner != null) {
//                chooseShareListenner.chooseShareFacebook();
//                getDialog().dismiss();
//            }
//        });

        addView(R.drawable.icon_share_facebook, getContext().getString(R.string.share_live_facebook), view -> {
            if (chooseShareListenner != null) {
                chooseShareListenner.chooseShareFacebook();
                getDialog().dismiss();
            }
        });


        if (!isHideInstagram) {
            addView(R.drawable.icon_share_intagram, getContext().getString(R.string.share_live_instagram), view -> {
                if (chooseShareListenner != null) {
                    chooseShareListenner.chooseShareInstagram();
                    getDialog().dismiss();
                }
            });
        }

        addView(R.drawable.icon_share_whatsapp, getContext().getString(R.string.share_live_whatsapp), view -> {
            if (chooseShareListenner != null) {
                chooseShareListenner.chooseShareWhatApp();
                getDialog().dismiss();
            }
        });

        addView(R.drawable.icon_share_twitter, getContext().getString(R.string.share_live_twitter), view -> {
            if (chooseShareListenner != null) {
                chooseShareListenner.chooseShareTwtter();
                getDialog().dismiss();
            }
        });

        addView(R.drawable.icon_share_email, getContext().getString(R.string.share_live_email), view -> {
            if (chooseShareListenner != null) {
                chooseShareListenner.chooseShareEmail();
                getDialog().dismiss();
            }
        });

        if (!isHideCopyLink) {
            addView(R.drawable.post_share_copylink, getContext().getString(R.string.share_live_copy_link), view -> {
                if (chooseShareListenner != null) {
                    chooseShareListenner.copyLink();
                    getDialog().dismiss();
                }
            });
        }

        addView(R.drawable.icon_share_others, getContext().getString(R.string.share_live_others), view -> {
            if (chooseShareListenner != null) {
                chooseShareListenner.chooseShareOthers();
                getDialog().dismiss();
            }
        });


//        llTwitter.setOnClickListener(v -> {
//            if (chooseShareListenner != null) {
//                chooseShareListenner.chooseShareTwtter();
//                getDialog().dismiss();
//            }
//        });

//        llInstagram.setOnClickListener(v -> {
//            if (chooseShareListenner != null) {
//                chooseShareListenner.chooseShareInstagram();
//                getDialog().dismiss();
//            }
//        });

//        llEmail.setOnClickListener(v -> {
//            if (chooseShareListenner != null) {
//                chooseShareListenner.chooseShareEmail();
//            }
//        });
//
//
//        llWhatsapp.setOnClickListener(v -> {
//            if (chooseShareListenner != null) {
//                chooseShareListenner.chooseShareWhatApp();
//            }
//        });
//
//        txtCopylink.setOnClickListener(v -> {
//            if (chooseShareListenner != null) {
//                chooseShareListenner.copyLink();
//            }
//            getDialog().dismiss();
//        });
    }

    private void addView(int drawableId, String text, View.OnClickListener clickListener) {
        View categoryView = LayoutInflater.from(getContext()).inflate(
                R.layout.share_item, llCategories, false);
        llCategories.addView(categoryView);
        SquareImageView categoryImage = (SquareImageView) categoryView.findViewById(R.id.ivCategoryImage);
        ImageLoaderUtil.displayMediaImage(getContext(), drawableId, categoryImage);
        TextView categoryTitle = (TextView) categoryView.findViewById(R.id.tvCategoryTitle);
        categoryTitle.setText(text);

        if (clickListener != null) {
            categoryView.setOnClickListener(clickListener);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void setHideShareInstagramView() {
        isHideInstagram = true;

    }

    public void setHideCopyLinkView(boolean isHide) {
        isHideCopyLink = isHide;

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mDialogDismisListener != null) {
            mDialogDismisListener.onDiaLogDismissed();
        }
    }

    public void setDialogDismisListener(DiaLogDismissListener dialogDismisListener) {
        mDialogDismisListener = dialogDismisListener;
    }

    public interface ChooseShareListenner {

        void chooseShareFacebook();

        void chooseShareInstagram();

        void chooseShareTwtter();

        void chooseShareEmail();

        void chooseShareWhatApp();

        void copyLink();

        void chooseShareOthers();
    }
}
