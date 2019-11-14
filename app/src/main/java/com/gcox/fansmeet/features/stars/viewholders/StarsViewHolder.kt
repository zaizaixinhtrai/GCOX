package com.gcox.fansmeet.features.stars.viewholders

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.appster.extensions.inflate
import com.gcox.fansmeet.features.stars.StarsModel
import kotlinx.android.synthetic.main.stars_row_layout.view.*


class StarsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {

        @JvmStatic
        fun create(parent: ViewGroup, @LayoutRes layout: Int): StarsViewHolder {
            return StarsViewHolder(parent.inflate(layout))
        }
    }

    init {
    }

    fun bindTo(item: StarsModel, listener: OnClickListener?) {
        with(itemView) {
            tvTitle.text = item.title
            if (item.star!! < 0) {
                tvStars.text = "- " + tvStars.text.toString()
            } else {
                tvStars.text = item.star.toString()
            }
        }
    }

    interface OnClickListener
}