package com.gcox.fansmeet.core.adapter;

import android.support.v7.util.DiffUtil;

import java.util.List;

/**
 * Created by thanhbc on 4/3/17.
 */

public abstract class BaseDiffCallback<E> extends DiffUtil.Callback {
    protected List<E> mOldList;
    protected List<E> mNewList;

    @SuppressWarnings("unchecked")
    public <T extends BaseDiffCallback<E>>T setOldList(List<E> oldList) {
        mOldList = oldList;
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public <T extends BaseDiffCallback<E>>T setNewList(List<E> newList) {
        mNewList = newList;
        return (T) this;
    }

    public BaseDiffCallback() {
    }

    public BaseDiffCallback(List<E> oldList, List<E> newList) {
        this.mOldList = oldList;
        this.mNewList = newList;
    }

    @Override
    public int getOldListSize() {
        if (mOldList == null){
            return 0;
        }else {
            return mOldList.size();
        }
    }

    @Override
    public int getNewListSize() {
        if (mNewList == null){
            return 0;
        }else {
            return mNewList.size();
        }
    }

}
