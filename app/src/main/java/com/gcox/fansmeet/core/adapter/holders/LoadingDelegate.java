package com.gcox.fansmeet.core.adapter.holders;

import android.support.annotation.NonNull;
import android.view.ViewGroup;
import com.gcox.fansmeet.core.adapter.AbsListItemAdapterDelegate;
import com.gcox.fansmeet.core.adapter.DisplayableItem;

import java.util.List;

/**
 * Created by thanhbc on 5/19/17.
 */

public class LoadingDelegate extends AbsListItemAdapterDelegate<LoadingItem,DisplayableItem,LoadingViewHolder> {

    @Override
    protected boolean isForViewType(@NonNull DisplayableItem item, @NonNull List<DisplayableItem> items, int position) {
        return item instanceof LoadingDelegate;
    }

    @NonNull
    @Override
    protected LoadingViewHolder onCreateViewHolder(@NonNull ViewGroup parent) {
        return LoadingViewHolder.create(parent);
    }

    @Override
    protected void onBindViewHolder(@NonNull LoadingItem item, @NonNull LoadingViewHolder viewHolder, @NonNull List<Object> payloads) {

    }
}
