package com.gcox.fansmeet.features.home;

import com.gcox.fansmeet.core.adapter.DisplayableItem;

import java.util.List;

/**
 * Created by thanhbc on 5/30/17.
 */

public class BannerModel implements DisplayableItem {

    public List<String> bannerItems;

    public BannerModel(){

    }

    public BannerModel(List<String> bannerItems) {
        this.bannerItems = bannerItems;
    }

}
