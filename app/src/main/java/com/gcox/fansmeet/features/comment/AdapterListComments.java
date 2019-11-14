package com.gcox.fansmeet.features.comment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.gcox.fansmeet.AppsterApplication;
import com.gcox.fansmeet.R;
import com.gcox.fansmeet.core.activity.BaseActivity;
import com.gcox.fansmeet.customview.CustomTypefaceSpan;
import com.gcox.fansmeet.customview.autolinktextview.AutoLinkTextView;
import com.gcox.fansmeet.customview.autolinktextview.AutoLinkUtil;
import com.gcox.fansmeet.models.ItemClassComments;
import com.gcox.fansmeet.util.*;

import java.util.ArrayList;

/**
 * Created by Ngoc on 9/4/2015.
 */
public class AdapterListComments extends RecyclerView.Adapter<AdapterListComments.CommentsAdapterHolderView> {

    ArrayList<ItemClassComments> listComment;
    private Context activity;
    private SpannableString spannablecontent;
    private int mUserIdOwnerPost;
    private CommentCallback mCommentCallback;

    public AdapterListComments(Context activity, ArrayList<ItemClassComments> listComment, int userIdOwnerPost) {

        this.activity = activity;
        this.listComment = listComment;
        this.mUserIdOwnerPost = userIdOwnerPost;
    }

    public void setCommentCallback(CommentCallback mCommentCallback) {
        this.mCommentCallback = mCommentCallback;
    }

    // inflates the row layout from xml when needed
    @Override
    public AdapterListComments.CommentsAdapterHolderView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_comment_row, parent, false);
        return new AdapterListComments.CommentsAdapterHolderView(view);
    }

    @Override
    public void onBindViewHolder(CommentsAdapterHolderView holder, int position) {
        final ItemClassComments item = listComment.get(position);

        AutoLinkUtil.addAutoLinkMode(holder.txt_displayname);
        holder.txt_displayname.setText("");
        ClickableSpan clickablePP = new ClickableSpan() {

            @Override
            public void onClick(View widget) {
                if (preventShowUserProfile(item)) {

                    return;
                }

                mCommentCallback.onUserImageClicked(position, item);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(Color.parseColor("#6b6c6e"));
            }
        };
        Typeface opensansbold = Typeface.createFromAsset(activity.getAssets(),
                "fonts/opensansbold.ttf");
        String contentComment = StringUtil.decodeString(item.getMessage());
        String displayName = StringUtil.decodeString(item.getDisplayName());
        ArrayList<int[]> hashtagSpans = StringUtil.getSpans(contentComment, '#');
        SpannableString commentsContentSpan =
                new SpannableString(contentComment);

        StringUtil.setSpanComment(commentsContentSpan, hashtagSpans, activity);
        String message = displayName + " ";
        spannablecontent = new SpannableString(message);
        int start = message.indexOf(displayName);
        int end = message.indexOf(displayName) + displayName.length();
        if (start < 0) start = 0;
        spannablecontent.setSpan(new RelativeSizeSpan(1.0f), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannablecontent.setSpan(new CustomTypefaceSpan("sans-serif", Typeface.create(opensansbold, Typeface.BOLD)),
                start, end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannablecontent.setSpan(clickablePP, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        StringBuilder builder = new StringBuilder();
        builder.append(spannablecontent);

        holder.txt_displayname.append(spannablecontent);
        holder.txt_displayname.appendAutoLinkText(contentComment);
        holder.txt_displayname.setAutoLinkOnClickListener(AutoLinkUtil.newListener((BaseActivity) activity));
        holder.txt_displayname.setMovementMethod(LinkMovementMethod.getInstance());

        boolean showDeleteOption = AppsterApplication.mAppPreferences.getUserModel().getUserId() == mUserIdOwnerPost
                || AppsterApplication.mAppPreferences.getUserModel().getUserId() == item.getUserId();
        holder.txt_displayname.setOnLongClickListener(v -> {
            showStreamOption(v, contentComment, position, showDeleteOption);
            return true;
        });

        // set User Image
        ImageLoaderUtil.displayUserImage(activity, item.getUserImage(),
                holder.imageView_user);

        holder.txt_timeAgo.setText(SetDateTime.partTimeForFeedItem(item
                .getCreated(), activity));

        holder.imageView_user.setOnClickListener(v -> {
            if (preventShowUserProfile(item)) {
                return;
            }
            mCommentCallback.onUserImageClicked(position, item);
        });

        holder.rootView.setOnClickListener(view -> {
            if (mCommentCallback != null) mCommentCallback.onRowClick(position);
        });

        if (position == 0) {
            holder.fmPadding.setVisibility(View.VISIBLE);
        } else {
            holder.fmPadding.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        if (listComment != null) {
            return listComment.size();
        }
        return 0;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    private CustomTypefaceSpan getCustomTypefaceSpanForName() {
        return new CustomTypefaceSpan("sans-serif", Typeface.create(Typeface.createFromAsset(activity.getAssets(),
                "fonts/opensansbold.ttf"), Typeface.BOLD));
    }

    private void showStreamOption(View view, String textCopy, int position, boolean showDeleteOption) {
        PopupMenu popupMenuOption = new PopupMenu(activity, view);
        popupMenuOption.getMenu().add(0, 0, 0, activity.getString(R.string.copy_stream_comment));
        if (showDeleteOption) {
            popupMenuOption.getMenu().add(0, 0, 1, activity.getString(R.string.stream_comment_delete_text));
        }
        popupMenuOption.setOnMenuItemClickListener(item -> {
            switch (item.getOrder()) {
                case 0:
                    CopyTextUtils.CopyClipboard(activity, textCopy, "");
                    break;
                case 1:
                    if (showDeleteOption) {
                        DialogUtil.showConfirmDialog((Activity) activity,
                                activity.getString(R.string.delete_comment_title),
                                activity.getString(R.string.delete_comment_body),
                                activity.getString(R.string.delete_comment_delete_button),
                                () -> {
                                    if (mCommentCallback != null)
                                        mCommentCallback.onDeleteComment(position);
                                }
                        );
                    }
                    break;
            }
            return true;
        });

        popupMenuOption.show();
    }

    private boolean preventShowUserProfile(ItemClassComments item) {
        return AppsterApplication.mAppPreferences.getUserModel().getUserId() == item.getUserId();
    }

    public static class CommentsAdapterHolderView extends RecyclerView.ViewHolder {

        private ImageView imageView_user;
        private AutoLinkTextView txt_displayname;
        private TextView txt_timeAgo;
        private ItemClassComments item;
        private RelativeLayout rootView;
        private FrameLayout fmPadding;

        CommentsAdapterHolderView(View item) {
            super(item);
            imageView_user = item.findViewById(R.id.userImage);
            txt_displayname = item.findViewById(R.id.tvSenderDisplayName);
            txt_timeAgo = item.findViewById(R.id.txt_timeAgo);
            rootView = item.findViewById(R.id.root_view);
            fmPadding = item.findViewById(R.id.fm_padding);
        }
    }

    public interface CommentCallback {
        void onDeleteComment(int position);

        void onRowClick(int position);

        void onUserImageClicked(int position, ItemClassComments item);
    }
}