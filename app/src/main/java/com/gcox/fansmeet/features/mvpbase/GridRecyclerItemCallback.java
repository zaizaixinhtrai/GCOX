package com.gcox.fansmeet.features.mvpbase;

/**
 * Created by linh on 18/05/2017.
 */

public interface GridRecyclerItemCallback<T> {
    void onItemClicked(T item, int rowPosition, int colPosition);
}
