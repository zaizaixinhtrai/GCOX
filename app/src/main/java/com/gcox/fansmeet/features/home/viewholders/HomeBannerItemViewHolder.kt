package com.gcox.fansmeet.features.home.viewholders

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gcox.fansmeet.R
import com.gcox.fansmeet.core.adapter.EndlessPagerAdapter
import com.gcox.fansmeet.features.home.BannerModel
import com.gcox.fansmeet.features.home.EventPagerAdapter
import com.gcox.fansmeet.features.home.HomeCurrentEventModel
import kotlinx.android.synthetic.main.home_adapter_banner_row.view.*
import com.gcox.fansmeet.features.home.HomeSliderAdapter


/**
 * Created by thanhbc on 5/30/17.
 */

class HomeBannerItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    internal var mBannerModel: BannerModel? = null

    private var mEventPagerAdapter: EventPagerAdapter? = null

    fun bindTo(model: BannerModel, onClickListener: OnClickListener) {
        this.mBannerModel = model
        itemView?.apply {
            mEventPagerAdapter =
                EventPagerAdapter(itemView.context.applicationContext, createListBanner(model.bannerItems))
            if (model.bannerItems.size > 1) {
                pager.isCycle = true
                pager.startAutoScroll()
                pager.setScrollDuration(400)
                val endlessPagerAdapter = EndlessPagerAdapter(mEventPagerAdapter)
                pager.adapter = endlessPagerAdapter
                pager.currentItem = model.bannerItems.size * (Integer.MAX_VALUE / 2 / model.bannerItems.size)

            } else {
                pager.adapter = mEventPagerAdapter
            }

//            itemView.pager.adapter = HomeSliderAdapter(itemView.context, model.bannerItems)
//            itemView.tabDots.setupWithViewPager(itemView.pager, true)
        }
    }

    private fun createListBanner(bannerItems: List<String>?): List<HomeCurrentEventModel> {
        val elems = arrayListOf<HomeCurrentEventModel>()
        if (bannerItems != null) {
            for (i in 0 until bannerItems.size) {
                val item = HomeCurrentEventModel()
                item.image = bannerItems[i]
                elems.add(item)
            }
        }
        return elems
    }

    interface OnClickListener {
        fun onBannerItemClicked(eventModel: HomeCurrentEventModel)
    }

    companion object {

        @JvmStatic
        fun create(parent: ViewGroup): HomeBannerItemViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(
                R.layout.home_adapter_banner_row, parent, false
            ) as ViewGroup
            return HomeBannerItemViewHolder(itemView)
        }
    }
}
