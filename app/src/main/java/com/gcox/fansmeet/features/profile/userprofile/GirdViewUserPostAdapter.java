package com.gcox.fansmeet.features.profile.userprofile;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.gcox.fansmeet.R;
import com.gcox.fansmeet.common.Constants;
import com.gcox.fansmeet.core.adapter.BaseRecyclerViewLoadMore;
import com.gcox.fansmeet.features.profile.celebrityprofile.delegates.GridModel;
import com.gcox.fansmeet.models.UserModel;
import com.gcox.fansmeet.models.UserPostModel;
import com.gcox.fansmeet.util.ImageLoaderUtil;
import com.gcox.fansmeet.util.RecycleItemClickSupport;
import com.gcox.fansmeet.util.StringUtil;

import java.util.List;


/**
 * Created by SonNguyen on 22/04/2016.
 */
public class GirdViewUserPostAdapter extends BaseRecyclerViewLoadMore<GirdViewUserPostAdapter.NewsFeedGridViewHolder, GridModel> {

    private List<GridModel> arrayListNewFeeds;
    final Context context;
    private RecycleItemClickSupport.OnItemClickListener onItemClickListener;
    UserModel userProfileDetails;

    public GirdViewUserPostAdapter(RecyclerView recyclerView, Context mContext, List<GridModel> arrayListNewFeeds, RecycleItemClickSupport.OnItemClickListener clickListener) {
        super(recyclerView, arrayListNewFeeds);
        this.context = mContext;
        this.onItemClickListener = clickListener;
        setItemsAndNotify(arrayListNewFeeds);
    }

    public void setUserProfileDetails(UserModel userProfileDetails) {
        this.userProfileDetails = userProfileDetails;
    }

    public List<GridModel> getItems() {
        return arrayListNewFeeds;
    }

    public void setItemsAndNotify(List<GridModel> arrayListNewFeeds) {
        this.arrayListNewFeeds = arrayListNewFeeds;// filteredData(arrayListNewFeeds);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v;
        if (viewType == VIEW_ITEM) {
            v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_grid_newsfeed, parent, false);

            vh = new NewsFeedGridViewHolder(v, onItemClickListener);
        } else {
            v = getProgressBarLayout(parent);
            vh = new ProgressViewHolder(v);

        }
        v.setTag(vh);

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NewsFeedGridViewHolder) {
            ((NewsFeedGridViewHolder) holder).bindView(arrayListNewFeeds.get(position), position);
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        if (arrayListNewFeeds != null) return arrayListNewFeeds.size();
        return 0;
    }

    @Override
    public void handleItem(NewsFeedGridViewHolder viewHolder, GridModel item, int postiotn) {

    }

    public class NewsFeedGridViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        ImageView image_media;
        FrameLayout media_image_fl;
        TextView tvChallenge;

        private RecycleItemClickSupport.OnItemClickListener onItemClickListener;

        public NewsFeedGridViewHolder(View itemView, RecycleItemClickSupport.OnItemClickListener onItemClickListener) {
            super(itemView);
            this.onItemClickListener = onItemClickListener;
            image_media = itemView.findViewById(R.id.mediaImage);
            tvChallenge = itemView.findViewById(R.id.tvChallenge);
            media_image_fl = itemView.findViewById(R.id.media_image_fl);
        }

        public void bindView(final GridModel itemFeed, final int position) {
            ImageLoaderUtil.displayMediaImage(context, itemFeed.getImage(), image_media);
            tvChallenge.setVisibility(View.GONE);
            if (itemFeed.getPostType() != null && itemFeed.getMediaType() != null) {
                showGridType(itemFeed.getPostType(), itemFeed.getMediaType());
            }
            media_image_fl.setOnClickListener(v -> {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClicked(recyclerView, getAdapterPosition(), media_image_fl);
            });
        }

        private void showGridType(int type, int mediaType) {
            if (type == Constants.POST_TYPE_CHALLENGE) {
                tvChallenge.setText(getString(R.string.gid_challenge_button));
                tvChallenge.setBackgroundResource(R.drawable.user_profile_challenge_btn);
                tvChallenge.setVisibility(View.VISIBLE);
                return;
            }
            if (type == Constants.USER_POST_NORMAL) {
                if (mediaType == Constants.POST_TYPE_VIDEO) {
                    tvChallenge.setText(getString(R.string.newsfeed_btn_video));
                    tvChallenge.setBackgroundResource(R.drawable.home_btn_recorded);
                    tvChallenge.setVisibility(View.VISIBLE);
                    return;
                }
            }
            tvChallenge.setText(null);
            tvChallenge.setVisibility(View.GONE);
        }

        private String getString(@StringRes int id) {
            if (itemView.getContext() != null) {
                return itemView.getContext().getString(id);
            }
            return "";
        }

        @Override
        public void onClick(View view) {

        }
    }
}
