package com.gcox.fansmeet.features.challenges;


import com.gcox.fansmeet.core.adapter.BaseDiffCallback;
import com.gcox.fansmeet.core.adapter.UpdateableItem;

/**
 * Created by ngoc on 5/24/18.
 */

public class ChallengesDiffCallBack extends BaseDiffCallback<UpdateableItem> {
    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldList.get(oldItemPosition).isSameItem(mNewList.get(newItemPosition));
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldList.get(oldItemPosition).isSameContent(mNewList.get(newItemPosition));
    }
}