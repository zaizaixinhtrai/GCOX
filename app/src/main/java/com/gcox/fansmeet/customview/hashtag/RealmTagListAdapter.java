package com.gcox.fansmeet.customview.hashtag;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.gcox.fansmeet.R;
import com.gcox.fansmeet.customview.CircleImageView;
import com.gcox.fansmeet.customview.CustomFontTextView;
import com.gcox.fansmeet.util.ImageLoaderUtil;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;

public class RealmTagListAdapter extends RealmBasedRecyclerViewAdapter<FollowItem, RealmTagListAdapter.ViewHolder> {

    private ItemClickListener clickListener;
    private String mQuery;

    public void setQuery(String query) {
        mQuery = query;
    }

    public RealmTagListAdapter(Context context, RealmResults<FollowItem> realmResults, boolean automaticUpdate, boolean animateResults, ItemClickListener clickListener) {
        super(context, realmResults, automaticUpdate, animateResults);
        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int viewType) {
        View v = inflater.inflate(R.layout.item_taggable_user, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindRealmViewHolder(ViewHolder viewHolder, int position) {
        final FollowItem follower = realmResults.get(position);
        ImageLoaderUtil.displayUserImage(getContext(), follower.getProfilePic(), viewHolder.imgAvatar);
        viewHolder.txtDisplayName.setText(SpannableUtil.makeHighLightQuery(getContext(), follower.getDisplayName(), mQuery));
        viewHolder.txtUsername.setText(SpannableUtil.makeHighLightQuery(getContext(), follower.getUserName(), mQuery));
        viewHolder.itemView.setOnClickListener(v -> clickListener.onClick(viewHolder.itemView, realmResults.get(position)));
    }

    static class ViewHolder extends RealmViewHolder {

        @Bind(R.id.img_avatar)
        CircleImageView imgAvatar;
        @Bind(R.id.txt_display_name)
        CustomFontTextView txtDisplayName;
        @Bind(R.id.txt_username)
        CustomFontTextView txtUsername;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface ItemClickListener {
        void onClick(View caller, FollowItem task);
    }
}
