package com.gcox.fansmeet.core.adapter;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import java.util.List;

import timber.log.Timber;

/**
 * Created by thanhbc on 5/19/17.
 */

public class ListDisplayableDelegationAdapter extends ListDelegationAdapter<List<DisplayableItem>> {

    final BaseDiffCallback mDiffCallback;

    public ListDisplayableDelegationAdapter(@Nullable BaseDiffCallback diffCallback) {
        mDiffCallback = diffCallback;
    }

    public void updateItems(List<DisplayableItem> newItems) {
        if(mDiffCallback!=null) {
            Timber.e("update with diff util");
            Timber.e("newItem update %s",newItems.toString());
            mDiffCallback.setOldList(getItems()).setNewList(newItems);
            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(mDiffCallback);
            items.clear();
            items.addAll(newItems);
            diffResult.dispatchUpdatesTo(this);
            Timber.e("dispatchUpdatesTo adapter");
        }else{
            Timber.e("notifyDataSetChanged");
            items.clear();
            items.addAll(newItems);
//            setItems(newItems);
            notifyDataSetChanged();
        }
    }
}
