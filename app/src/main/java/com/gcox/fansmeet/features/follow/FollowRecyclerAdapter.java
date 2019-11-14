package com.gcox.fansmeet.features.follow;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.gcox.fansmeet.AppsterApplication;
import com.gcox.fansmeet.R;
import com.gcox.fansmeet.common.Constants;
import com.gcox.fansmeet.core.adapter.BaseRecyclerViewLoadMore;
import com.gcox.fansmeet.customview.CircleImageView;
import com.gcox.fansmeet.models.FollowItemModel;
import com.gcox.fansmeet.models.UserModel;
import com.gcox.fansmeet.util.ImageLoaderUtil;

import java.util.ArrayList;

/**
 * Created by User on 5/5/2016.
 */
public class FollowRecyclerAdapter extends BaseRecyclerViewLoadMore<FollowRecyclerAdapter.FollowHolder, FollowItemModel> {

    private Context context;
    private ArrayList<FollowItemModel> arrFollowers;
    private FollowListener followListener;

    public FollowRecyclerAdapter(Context context, RecyclerView recyclerView, ArrayList<FollowItemModel> mModels, FollowListener followListener) {
        super(recyclerView, mModels);

        this.arrFollowers = mModels;
        this.context = context;
        this.followListener = followListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.follow_user_dapter_row, parent, false);

            vh = new FollowHolder(v);
        } else {
            View v = getProgressBarLayout(parent);
            vh = new ProgressViewHolder(v);
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof FollowHolder) {

            final FollowItemModel item = mModels.get(position);
            handleItem((FollowHolder) holder, item, position);

        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public void handleItem(final FollowHolder viewHolder, final FollowItemModel item, final int positiotn) {

        // Set username
        viewHolder.tvDisplayName.setText(item.getDisplayName());
        viewHolder.tvUserName.setText("@" + item.getUserName());

        if (AppsterApplication.mAppPreferences.getUserModel().getUserId() == item.getUserId()) {
            viewHolder.btnFollow.setVisibility(View.GONE);
        } else {
            viewHolder.btnFollow.setVisibility(View.VISIBLE);
        }

        // Set follow
        if (item.isFollow()) {
            viewHolder.btnFollow.setBackgroundResource(R.drawable.home_following_btn_selector);
            viewHolder.btnFollow.setTextColor(ContextCompat.getColor(context, R.color.color_2587E7));
            viewHolder.btnFollow.setText(context.getString(R.string.profile_following_title));
        } else {
            viewHolder.btnFollow.setBackgroundResource(R.drawable.home_follow_btn_selector);
            viewHolder.btnFollow.setTextColor(ContextCompat.getColor(context, R.color.white));
            viewHolder.btnFollow.setText(context.getString(R.string.follow));
        }

        ImageLoaderUtil.displayUserImage(context, item.getUserImage(),
                viewHolder.imvUserImage);

        viewHolder.imvUserImage.setOnClickListener(v -> {
            if (isOwnerProfile(AppsterApplication.mAppPreferences.getUserModel(), item.getUserId()))
                return;
            if (followListener != null) followListener.onUserImageClicked(item.getUserId(), item.getUserName());
        });

        viewHolder.tvDisplayName.setOnClickListener(v -> viewHolder.imvUserImage.performClick());
        viewHolder.tvUserName.setOnClickListener(v -> viewHolder.imvUserImage.performClick());
        viewHolder.btnFollow.setOnClickListener(v -> {
            if (followListener != null) followListener.onFollowClicked(positiotn, item.getUserId(), item.isFollow());
        });
    }

    private boolean isOwnerProfile(UserModel userProfile, int userId) {
        return userProfile != null &&
                userProfile.getUserId() == userId;
    }

    class FollowHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.imv_user_image)
        CircleImageView imvUserImage;
        @Bind(R.id.tvDisplayName)
        TextView tvDisplayName;
        @Bind(R.id.btn_follow)
        TextView btnFollow;
        @Bind(R.id.tvUserName)
        TextView tvUserName;

        public FollowHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
