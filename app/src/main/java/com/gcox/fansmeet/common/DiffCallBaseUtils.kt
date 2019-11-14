package com.gcox.fansmeet.common

import com.gcox.fansmeet.core.adapter.BaseDiffCallback
import com.gcox.fansmeet.core.adapter.UpdateableItem


class DiffCallBaseUtils : BaseDiffCallback<UpdateableItem>() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldList[oldItemPosition].isSameItem(mNewList[newItemPosition])
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldList[oldItemPosition].isSameContent(mNewList[newItemPosition])
    }
}