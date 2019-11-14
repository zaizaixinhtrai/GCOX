package com.gcox.fansmeet.core.adapter.holders;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gcox.fansmeet.R;


/**
 * Created by linh on 23/05/2017.
 */

public class LoadMoreViewHolder extends RecyclerView.ViewHolder {
    private LoadMoreViewHolder(View itemView) {
        super(itemView);
    }

    public static LoadMoreViewHolder create(ViewGroup parent){
        return new LoadMoreViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item_load_more_recycler_view, parent, false));
    }

    public void bindTo(){

    }
}
