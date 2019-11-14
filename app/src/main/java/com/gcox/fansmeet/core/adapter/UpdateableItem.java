package com.gcox.fansmeet.core.adapter;

/**
 * Created by thanhbc on 5/22/17.
 */

public interface UpdateableItem extends DisplayableItem {
    boolean isSameItem(DisplayableItem item);

    boolean isSameContent(DisplayableItem item);
}
