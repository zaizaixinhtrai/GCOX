package com.gcox.fansmeet.features.home

import com.gcox.fansmeet.core.adapter.BaseDiffCallback
import com.gcox.fansmeet.core.adapter.DisplayableItem
import com.gcox.fansmeet.core.adapter.EndlessDelegateAdapter
import com.gcox.fansmeet.features.home.delegates.HomeBannerDelegate
import com.gcox.fansmeet.features.home.delegates.HomeDelegate
import com.gcox.fansmeet.features.home.viewholders.HomeBannerItemViewHolder
import com.gcox.fansmeet.features.home.viewholders.HomeViewHolder

/**
 * Created by thanhbc on 5/16/18.
 */

class HomeScreenAdapter(
    diffCallback: BaseDiffCallback<*>?, items: List<DisplayableItem>,
    val listener: HomeViewHolder.OnClickListener?, val listenerHomeBanner: HomeBannerItemViewHolder.OnClickListener?
) : EndlessDelegateAdapter(diffCallback) {
    init {
        this.delegatesManager.addDelegate(HomeDelegate(listener))
        this.delegatesManager.addDelegate(HomeBannerDelegate(listenerHomeBanner))
        setItems(items)
    }
}
