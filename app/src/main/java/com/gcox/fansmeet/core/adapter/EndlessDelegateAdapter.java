package com.gcox.fansmeet.core.adapter;

import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by linh on 23/05/2017.
 */

public class EndlessDelegateAdapter extends ListDisplayableDelegationAdapter {
    public static final int LOAD_MORE = 99;


    public EndlessDelegateAdapter(@Nullable BaseDiffCallback diffCallback) {
        super(diffCallback);
        this.delegatesManager.addDelegate(LOAD_MORE, new LoadMoreDelegate());
    }

    @Override
    public int getItemCount() {
        return (items == null) ? 0 : items.size();
    }

    public void addLoadMoreItem() {
        if (!items.isEmpty() && items.get(this.items.size() - 1) instanceof LoadMoreItem) {
            return;
        }
        items.add(new LoadMoreItem());
        notifyItemInserted(items.size());
    }

    public void updateItems(List<DisplayableItem> newItems) {
            if (!items.isEmpty()) {
                int lastPosition = this.items.size() - 1;
                if (this.items.get(lastPosition) instanceof LoadMoreItem) {
                    this.items.remove(lastPosition);
                    notifyItemRemoved(lastPosition);
                }
//                int currentSize = getItemCount();
//                setItems(newItems);
//                notifyItemRangeInserted(currentSize, getItemCount());
            }
        super.updateItems(newItems);
    }

    public void removeLoadingItem() {
        if (items != null && !items.isEmpty()) {
            int lastPosition = items.size() - 1;
            if (items.get(lastPosition) instanceof EmptyModel || items.get(lastPosition) instanceof LoadMoreItem) {
                items.remove(lastPosition);
                notifyItemRemoved(lastPosition);
            }
        }
    }
}
