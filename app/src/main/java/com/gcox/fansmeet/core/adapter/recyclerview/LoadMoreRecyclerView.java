package com.gcox.fansmeet.core.adapter.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.gcox.fansmeet.core.adapter.OnLoadMoreListenerRecyclerView;


import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by linh on 23/05/2017.
 */

public class LoadMoreRecyclerView extends RecyclerView {
    private static final int mLoadMoreThreshold = 5;
    private AtomicBoolean mLoading;


    public LoadMoreRecyclerView(Context context) {
        super(context);
        init();
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadMoreRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        mLoading = new AtomicBoolean(false);
    }

    public void setOnLoadMoreListener(final OnLoadMoreListenerRecyclerView onLoadMoreListener) {

        if (getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getLayoutManager();
            addOnScrollListener(new OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            if (dy > 0) {
                                int totalItemCount = linearLayoutManager.getItemCount();
                                int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                                if (!mLoading.get() && totalItemCount <= (lastVisibleItem + mLoadMoreThreshold) && onLoadMoreListener != null) {
                                    // End has been reached
                                    // Do something
                                    mLoading.set(true);
                                    onLoadMoreListener.onLoadMore();
                                }
                            }
                        }
                    });

        } else if (getLayoutManager() instanceof GridLayoutManager) {

            final GridLayoutManager gridLayoutManager = (GridLayoutManager) getLayoutManager();
            addOnScrollListener(new OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);
                            if (dy > 0) {
                                int totalItemCount = gridLayoutManager.getItemCount();
                                int lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();
                                if (!mLoading.get()  && totalItemCount <= (lastVisibleItem + mLoadMoreThreshold) && onLoadMoreListener != null) {
                                    // End has been reached
                                    // Do something
                                    mLoading.set(true);
                                    onLoadMoreListener.onLoadMore();
                                }
                            }
                        }
                    });
        }
    }

    public void setLoading(boolean loading) {
        this.mLoading.set(loading);
    }

}
