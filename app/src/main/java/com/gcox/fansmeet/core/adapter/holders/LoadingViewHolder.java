package com.gcox.fansmeet.core.adapter.holders;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import butterknife.ButterKnife;
import com.gcox.fansmeet.R;

/**
 * Created by thanhbc on 5/19/17.
 */

public class LoadingViewHolder extends RecyclerView.ViewHolder {

    public ProgressBar progressBar;
    public LoadingViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public static LoadingViewHolder create(ViewGroup parent){
        ViewGroup itemView = (ViewGroup) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.progress_item_load_more_recycler_view,parent,false);

        return new LoadingViewHolder(itemView);
    }
}
