package com.gcox.fansmeet.features.notification;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.gcox.fansmeet.R;
import com.gcox.fansmeet.core.adapter.BaseRecyclerViewLoadMore;
import com.gcox.fansmeet.customview.CircleImageView;
import com.gcox.fansmeet.customview.CustomTypefaceSpan;
import com.gcox.fansmeet.util.ImageLoaderUtil;
import com.gcox.fansmeet.util.SetDateTime;
import com.gcox.fansmeet.util.StringUtil;

import java.util.List;

/**
 * Created by User on 5/4/2016.
 */
public class NotifyRecyclerviewAdapter extends BaseRecyclerViewLoadMore<NotifyRecyclerviewAdapter.NotifyHolder, NotificationItemModel> {

    private Context context;
    private int typeFragment;
    Typeface americanFont;

    public NotifyRecyclerviewAdapter(Context context, RecyclerView recyclerView,
                                     List<NotificationItemModel> mModels,
                                     int typeFragment) {
        super(recyclerView, mModels);
        this.context = context;
        this.typeFragment = typeFragment;
        americanFont = Typeface.createFromAsset(context.getAssets(),
                "fonts/opensansbold.ttf");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.cardview_notify, parent, false);

            vh = new NotifyHolder(v);
        } else {
            View v = getProgressBarLayout(parent);

            vh = new ProgressViewHolder(v);
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof NotifyHolder) {

            final NotificationItemModel item = mModels.get(position);
            handleItem((NotifyHolder) holder, item, position);

        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }

    }

    @Override
    public void handleItem(NotifyHolder viewHolder, NotificationItemModel model, final int postiotn) {

        viewHolder.tvNotifyUser.setText(StringUtil.decodeString(model.getMessage()));
        viewHolder.tvNotifyTime.setText(SetDateTime.partTimeForFeedItem(model.getCreated(), context));
        ImageLoaderUtil.displayUserImage(context, model.getSenderImage(),
                viewHolder.ivNotifyAvatar);

//        String message;
//
//        if (model.getMessage() == null) {
//            message = "";
//        } else {
//            message = model.getMessage();
//        }
//
//        ImageLoaderUtil.displayUserImage(context, model.getActionUser().getUserImage(),
//                viewHolder.ivNotifyAvatar);
//
//        viewHolder.tvNotifyTime.setText(SetDateTime.partTimeNotification(model.getCreated(), context));
//
//        String displayName = model.getActionUser().getDisplayName();
//        String actionUserDisplayName = model.getReceiver() != null ? model.getReceiver().getDisplayName() : "";
//
//        if (model.isIsRead()) {
//
//            if (!StringUtil.isNullOrEmptyString(message)) {
//
//                message = StringUtil.decodeString(message);
//
//                viewHolder.tvNotifyUser.setTypeface(null, Typeface.NORMAL);
//                SpannableString spannablecontent = new SpannableString(message);
//
//                if (!StringUtil.isNullOrEmptyString(displayName) && message.contains(displayName)) {
//                    spannablecontent.setSpan(getCustomTypefaceSpanForName(),
//                            message.indexOf(displayName), message.indexOf(displayName) + displayName.length(),
//                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    spannablecontent.setSpan(getForegroundColorSpanForName(),
//                            message.indexOf(displayName), message.indexOf(displayName) + displayName.length()
//                            , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                }
//
//                if (!StringUtil.isNullOrEmptyString(actionUserDisplayName) && message.contains(actionUserDisplayName)) {
//                    spannablecontent.setSpan(getCustomTypefaceSpanForName(),
//                            message.indexOf(actionUserDisplayName), message.indexOf(actionUserDisplayName) + actionUserDisplayName.length(),
//                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    spannablecontent.setSpan(getForegroundColorSpanForName(),
//                            message.indexOf(actionUserDisplayName), message.indexOf(actionUserDisplayName) + actionUserDisplayName.length()
//                            , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                }
//
//                viewHolder.tvNotifyUser.setText(spannablecontent);
//            }
//
//        } else {
//            viewHolder.tvNotifyUser.setTypeface(null, Typeface.BOLD);
//            viewHolder.tvNotifyUser.setText(StringUtil.decodeString(message));
//        }

        viewHolder.ivNotifyAvatar.setOnClickListener(view -> {

        });

    }

    private CustomTypefaceSpan getCustomTypefaceSpanForName() {

        return new CustomTypefaceSpan("sans-serif", Typeface.create(americanFont, Typeface.BOLD));
    }

    private ForegroundColorSpan getForegroundColorSpanForName() {
        return new ForegroundColorSpan(Color.parseColor("#6b6c6e"));
    }

    static class NotifyHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.ivNotifyAvatar)
        CircleImageView ivNotifyAvatar;
        @Bind(R.id.tvNotifyUser)
        TextView tvNotifyUser;
        @Bind(R.id.tvNotifyTime)
        TextView tvNotifyTime;
        @Bind(R.id.ivNotifyMessage)
        ImageView ivNotifyMessage;
        @Bind(R.id.card_view)
        RelativeLayout cardView;

        public NotifyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
