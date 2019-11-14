package com.gcox.fansmeet.features.topfans;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.gcox.fansmeet.R;
import com.gcox.fansmeet.core.adapter.BaseRecyclerViewLoadMore;
import com.gcox.fansmeet.customview.CircleImageView;
import com.gcox.fansmeet.models.UserModel;
import com.gcox.fansmeet.util.ImageLoaderUtil;


import java.util.ArrayList;

/**
 * Created by User on 8/31/2016.
 */
public class TopFansAdapter extends BaseRecyclerViewLoadMore<TopFansAdapter.Holder, TopFanModel> {
    Context context;
    Holder currentHolder;
    ArrayList<TopFanModel> mModels;
    private boolean isViewLiveStream;
    private boolean isViewer;
    private TopFansListener listener;

    public TopFansAdapter(Context context, RecyclerView recyclerView, ArrayList<TopFanModel> mModels, TopFansListener listener) {
        super(recyclerView, mModels);
        this.context = context;
        this.mModels = mModels;
        this.listener = listener;
    }

    @Override
    public void handleItem(final Holder viewHolder, final TopFanModel item, final int postiotn) {
        if (postiotn == 0 || postiotn == 1 || postiotn == 2) {
            viewHolder.topImage.setVisibility(View.VISIBLE);
            viewHolder.numberOrder.setVisibility(View.GONE);
            viewHolder.borderStt.setVisibility(View.VISIBLE);

            if (postiotn == 0) {
                viewHolder.topImage.setBackgroundResource(R.drawable.top_fans_one);
                viewHolder.borderStt.setBackgroundResource(R.drawable.topfan_silver_1th);
            } else if (postiotn == 1) {
                viewHolder.topImage.setBackgroundResource(R.drawable.top_fans_two);
                viewHolder.borderStt.setBackgroundResource(R.drawable.topfan_silver_2th);
            } else if (postiotn == 2) {
                viewHolder.topImage.setBackgroundResource(R.drawable.top_fans_three);
                viewHolder.borderStt.setBackgroundResource(R.drawable.topfan_silver_3th);
            }

        } else {
            viewHolder.topImage.setVisibility(View.GONE);
            viewHolder.numberOrder.setVisibility(View.VISIBLE);
            viewHolder.numberOrder.setText(postiotn + 1 + "");
            viewHolder.borderStt.setBackgroundResource(R.drawable.topfan_silver_normal);
        }

        ImageLoaderUtil.displayUserImage(context, item.getUserImage(), viewHolder.userImage);
        viewHolder.displayName.setText(item.getDisplayName());
        viewHolder.totalStars.setText(String.valueOf(item.getGiftCount()));
        viewHolder.tvUserName.setText("@" + item.getUserName());
        viewHolder.userImage.setOnClickListener(v -> {
            if (listener != null) listener.onUserImageClickedListener(item);
        });
        viewHolder.tvUserName.setOnClickListener(v -> viewHolder.userImage.performClick());
        viewHolder.displayName.setOnClickListener(v -> viewHolder.userImage.performClick());

    }

    private boolean isOwnerProfile(UserModel userProfile, int userId) {
        return userProfile != null &&
                userProfile.getUserId() == userId;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(context).inflate(
                    R.layout.top_pan_dapter_row, parent, false);

            vh = new Holder(v);
        } else {
            View v = getProgressBarLayout(parent);

            vh = new ProgressViewHolder(v);
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof Holder) {

            final TopFanModel item = mModels.get(position);
            handleItem((Holder) holder, item, position);

        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }

    }

    public static class Holder extends RecyclerView.ViewHolder {

        ImageView topImage;
        TextView numberOrder;
        CircleImageView userImage;
        TextView displayName;
        TextView totalStars;
        LinearLayout borderStt;
        TextView tvUserName;

        public Holder(View itemView) {
            super(itemView);

            topImage = itemView.findViewById(R.id.top_image);
            numberOrder = itemView.findViewById(R.id.numberOrder);
            userImage = itemView.findViewById(R.id.userImage);
            displayName = itemView.findViewById(R.id.displayName);
            totalStars = itemView.findViewById(R.id.totalStars);
            borderStt = itemView.findViewById(R.id.border_stt);
            tvUserName = itemView.findViewById(R.id.tvUserName);
        }
    }

    public interface TopFansListener {
        void onUserImageClickedListener(TopFanModel item);
    }
}
