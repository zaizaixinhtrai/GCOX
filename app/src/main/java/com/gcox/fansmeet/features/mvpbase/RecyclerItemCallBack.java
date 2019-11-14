package com.gcox.fansmeet.features.mvpbase;

/**
 * Created by ThanhBan on 9/16/2016.
 */
public interface RecyclerItemCallBack<T> {
    void onItemClicked(T item, int position);
}
