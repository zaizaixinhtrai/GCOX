package com.gcox.fansmeet.features.home.delegates;

import android.support.annotation.NonNull;
import android.view.ViewGroup;
import com.gcox.fansmeet.core.adapter.AbsListItemAdapterDelegate;
import com.gcox.fansmeet.core.adapter.DisplayableItem;
import com.gcox.fansmeet.features.home.BannerModel;
import com.gcox.fansmeet.features.home.viewholders.HomeBannerItemViewHolder;

import java.util.List;

/**
 * Created by thanhbc on 5/30/17.
 */

public class HomeBannerDelegate extends AbsListItemAdapterDelegate<BannerModel, DisplayableItem, HomeBannerItemViewHolder> {


    private final HomeBannerItemViewHolder.OnClickListener mHomeBannerItemListener;

    public HomeBannerDelegate(HomeBannerItemViewHolder.OnClickListener listener) {
        this.mHomeBannerItemListener = listener;
    }

    @Override
    protected boolean isForViewType(@NonNull DisplayableItem item, @NonNull List<DisplayableItem> items, int position) {
        return item instanceof BannerModel;
    }

    @NonNull
    @Override
    protected HomeBannerItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        return HomeBannerItemViewHolder.create(parent);
    }

    @Override
    protected void onBindViewHolder(@NonNull BannerModel item, @NonNull HomeBannerItemViewHolder viewHolder, @NonNull List<Object> payloads) {
        viewHolder.bindTo(item, mHomeBannerItemListener);
    }
}
