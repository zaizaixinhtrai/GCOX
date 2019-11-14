package com.gcox.fansmeet.core.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import com.gcox.fansmeet.R;
import timber.log.Timber;

import java.util.List;

/**
 * Created by User on 1/25/2016.
 */
public abstract class BaseRecyclerViewLoadMore<T1 extends RecyclerView.ViewHolder, E> extends RecyclerView.Adapter {

    public static final int VIEW_ITEM = 1;
    public static final int VIEW_PROGRESS = 0;
    private static final int TIME_DELAY = 2000;

    protected List<E> mModels;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    protected RecyclerView recyclerView;

    public BaseRecyclerViewLoadMore(RecyclerView recyclerView, List<E> mModels) {
        this.mModels = mModels;
        this.recyclerView = recyclerView;
    }

    @Override
    public int getItemViewType(int position) {
        return mModels.get(position) != null ? VIEW_ITEM : VIEW_PROGRESS;
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {

        if (mModels == null) {
            return 0;
        }
        return mModels.size();
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public void setOnLoadMoreListener(final OnLoadMoreListenerRecyclerView onLoadMoreListener) {

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();

            recyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            if (dy > 0) {
                                totalItemCount = linearLayoutManager.getItemCount();
                                lastVisibleItem = linearLayoutManager
                                        .findLastVisibleItemPosition();
                                if (!loading
                                        && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                                    // End has been reached
                                    // Do something
                                    if (onLoadMoreListener != null) {
                                        onLoadMoreListener.onLoadMore();
                                    }
                                    loading = true;
                                }
                            }
                        }
                    });

        } else if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {

            final GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView
                    .getLayoutManager();

            recyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            if (dy > 0) {
                                totalItemCount = gridLayoutManager.getItemCount();
                                lastVisibleItem = gridLayoutManager
                                        .findLastVisibleItemPosition();
                                if (!loading
                                        && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                                    // End has been reached
                                    // Do something
                                    if (onLoadMoreListener != null) {
                                        onLoadMoreListener.onLoadMore();
                                    }
                                    loading = true;
                                }
                            }
                        }
                    });
        }
    }

    public void handleItem(T1 viewHolder, E item, int postiotn){

    }

    public void setVisibleThreshold(int visibleThreshold) {
        this.visibleThreshold = visibleThreshold;
    }


    public void removeProgressItem() {
        if (mModels != null && !mModels.isEmpty() && mModels.get(mModels.size() - 1) == null) {
            //remove progress item
            mModels.remove(mModels.size() - 1);
            notifyItemRemoved(mModels.size());
            Timber.d("removeProgressItem");
        }
    }

    public void addProgressItem() {
        if (mModels != null) {
            mModels.add(null);
            notifyItemInserted(mModels.size() - 1);
            Timber.d("addProgressItem");
        }
    }

    public int getTimeDelay() {
        return TIME_DELAY;
    }

    public boolean isViewProgress(int position) {

        return mModels != null && mModels.get(position) == null;

    }

    public View getProgressBarLayout(ViewGroup parent) {

        return LayoutInflater.from(parent.getContext()).inflate(
                R.layout.progress_item_load_more_recycler_view, parent, false);
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }
}
