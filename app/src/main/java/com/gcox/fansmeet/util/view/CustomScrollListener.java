package com.gcox.fansmeet.util.view;

import android.support.v7.widget.RecyclerView;

import timber.log.Timber;

/**
 * Created by Ngoc on 8/14/2017.
 */

public class CustomScrollListener extends RecyclerView.OnScrollListener {
    private ScrollListener mScrollListener;

    public CustomScrollListener(ScrollListener scrollListener) {
        mScrollListener = scrollListener;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        switch (newState) {
            case RecyclerView.SCROLL_STATE_IDLE:
                Timber.e("The RecyclerView is not scrolling");
                if (mScrollListener != null) mScrollListener.onNotScrolling();
                break;
            case RecyclerView.SCROLL_STATE_DRAGGING:
                Timber.e("Scrolling now");
                if (mScrollListener != null) mScrollListener.onScrolling();
                break;
            case RecyclerView.SCROLL_STATE_SETTLING:
                Timber.e("Scroll Settling");
                if (mScrollListener != null) mScrollListener.onSettling();
                break;
            default:
                break;
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (dx > 0) {
            Timber.e("Scrolled Right");
        } else if (dx < 0) {
            Timber.e("Scrolled Left");
        } else {
            if (mScrollListener != null) mScrollListener.onNotScrolling();
        }
        if (dy > 0) {
            Timber.e("Scrolled Downwards");
            if (mScrollListener != null) mScrollListener.onScrolledDownward();
        } else if (dy < 0) {
            Timber.e("Scrolled Upwards");
            if (mScrollListener != null) mScrollListener.onScrolledUp();
        } else {
            Timber.e("No Vertical Scrolled");
            if (mScrollListener != null) mScrollListener.onNotScrolling();
        }
    }

    public interface ScrollListener {
        void onScrolling();

        void onSettling();

        void onNotScrolling();

        void onScrolledDownward();

        void onScrolledUp();
    }
}
