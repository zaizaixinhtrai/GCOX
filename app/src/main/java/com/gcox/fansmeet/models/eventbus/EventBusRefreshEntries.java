package com.gcox.fansmeet.models.eventbus;

/**
 * Created by User on 7/11/2016.
 */
public class EventBusRefreshEntries {
    public boolean isRefresh = false;

    public EventBusRefreshEntries(boolean isRefresh) {
        this.isRefresh = isRefresh;
    }

    public EventBusRefreshEntries() {
    }
}
