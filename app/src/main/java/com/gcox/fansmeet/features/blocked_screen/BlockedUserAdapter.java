package com.gcox.fansmeet.features.blocked_screen;

import android.content.Context;
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
import com.gcox.fansmeet.core.adapter.BaseRecyclerViewLoadMore;
import com.gcox.fansmeet.customview.CircleImageView;
import com.gcox.fansmeet.util.ImageLoaderUtil;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by ThanhBan on 9/27/2016.
 */

public class BlockedUserAdapter extends BaseRecyclerViewLoadMore<BlockedUserAdapter.ViewHolder, BlockedUserModel> implements View.OnClickListener {

    private Context mContext;

    OnLikedUserItemClickListener recyclerItemCallBack;

    public BlockedUserAdapter(Context context, RecyclerView recyclerView, ArrayList<BlockedUserModel> mModels) {
        super(recyclerView, mModels);
        mContext = context.getApplicationContext();
    }

    @Override
    public void handleItem(ViewHolder viewHolder, BlockedUserModel item, int postiotn) {
//        viewHolder.btnUnblock.setTag(viewHolder);
//        viewHolder.userImage.setTag(viewHolder);
        viewHolder.userName.setTag(viewHolder);
        viewHolder.displayName.setTag(viewHolder);

        viewHolder.bindView(item,recyclerItemCallBack);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(mContext).inflate(
                    R.layout.item_blocked_user, parent, false);
            ViewHolder holder = new ViewHolder(v);
            holder.btnUnblock.setOnClickListener(this);
            holder.userImage.setOnClickListener(this);
            holder.userName.setOnClickListener(this);
            holder.displayName.setOnClickListener(this);
            return holder;
        } else {
            View v = getProgressBarLayout(parent);

            return new ProgressViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {

            final BlockedUserModel item = mModels.get(position);
            handleItem((ViewHolder) holder, item, position);

        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    void addNewList(List<BlockedUserModel> blockedUserModels) {
        int lastPosition = mModels.size();
        int length = blockedUserModels.size();
        mModels.addAll(blockedUserModels);
        notifyItemRangeInserted(lastPosition, length);
    }

    void removeItem(int position){
        if (mModels != null && mModels.size() > position){
            mModels.remove(position);
            notifyItemRemoved(position);
        }
    }

    @Override
    public void onClick(View v) {
//        ViewHolder holder = (ViewHolder) v.getTag();
//        if(v.getId()==R.id.btnUnblock) {
//            recyclerItemCallBack.onBlockButtonClick(holder.getBlockedUserModel(), holder.getAdapterPosition());
//        }else{
//            recyclerItemCallBack.onProfileImageClick(holder.getBlockedUserModel());
//        }
    }

    public void setRecyclerItemCallBack(OnLikedUserItemClickListener recyclerItemCallBack) {
        this.recyclerItemCallBack = recyclerItemCallBack;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.ivUserImage)
        CircleImageView userImage;
        @Bind(R.id.tvDisplayName)
        TextView displayName;
        @Bind(R.id.tvUserName)
        TextView userName;
        @Bind(R.id.btnUnblock)
        Button btnUnblock;

        private BlockedUserModel itemModel;


        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bindView(BlockedUserModel usersItemModel,OnLikedUserItemClickListener recyclerItemCallBack) {
            itemModel = usersItemModel;

            ImageLoaderUtil.displayUserImage(itemView.getContext(), itemModel.getUserImage(), userImage);
            displayName.setText(itemModel.getDisplayName());
            userName.setText(String.format("@%s", itemModel.getUserName()));

            if(AppsterApplication.mAppPreferences.getUserModel()!=null) {
                if (AppsterApplication.mAppPreferences.getUserModel().getUserId()== usersItemModel.getUserId()) {
                    btnUnblock.setVisibility(View.INVISIBLE);
                    userImage.setClickable(false);
                }
            }

            btnUnblock.setOnClickListener(v -> {
                if (recyclerItemCallBack !=null)recyclerItemCallBack.onBlockButtonClick(itemModel, getAdapterPosition());
            });

            userImage.setOnClickListener(v -> {
                if (recyclerItemCallBack !=null)recyclerItemCallBack.onProfileImageClick(itemModel);
            });
        }

        BlockedUserModel getBlockedUserModel() {
            return itemModel;
        }
    }

    interface OnLikedUserItemClickListener{
       void onProfileImageClick(BlockedUserModel usersItemModel);
       void onBlockButtonClick(BlockedUserModel usersItemModel, int position);
    }
}
