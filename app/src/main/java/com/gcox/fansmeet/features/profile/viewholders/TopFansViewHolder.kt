package com.gcox.fansmeet.features.profile.celebrityprofile.delegates.viewholders

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.appster.extensions.inflate
import com.gcox.fansmeet.features.profile.FansModel
import kotlinx.android.synthetic.main.top_fans_item_layout.view.*


class TopFansViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var showItem: FansModel? = null

    companion object {

        @JvmStatic
        fun create(parent: ViewGroup, @LayoutRes layout: Int): TopFansViewHolder {
            return TopFansViewHolder(parent.inflate(layout))
        }
    }

    init {
    }

    fun bindTo(item: FansModel, listener: OnClickListener?) {
        with(itemView) {
            itemView.viewMoreClick.setOnClickListener { listener?.onViewMoreClicked() }
        }
    }


    interface OnClickListener {
        fun onImageClicked(item: FansModel)
        fun onViewMoreClicked()
    }
}