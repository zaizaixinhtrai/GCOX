package com.gcox.fansmeet.core.itemdecorator;

import android.graphics.Rect;
import android.support.annotation.IntRange;
import android.support.annotation.Px;
import android.support.v7.widget.OrientationHelper;

/**
 * Created by thanhbc on 11/9/17.
 */

public class MarginDelegate {
    private final int mSpanCount;
    private final int mSpaceItem;

    MarginDelegate( int spanCount, @Px int spaceItem ){
        this.mSpanCount = spanCount;
        this.mSpaceItem = spaceItem;
    }

    void calculateMargin( Rect outRect,
                          int position,
                          int spanCurrent,
                          int itemCount,
                          @IntRange( from = 0, to = 1 ) int orientation,
                          boolean isReverse,
                          boolean isRTL ){
        if( orientation == OrientationHelper.VERTICAL ){
            outRect.left = spanCurrent * mSpaceItem / mSpanCount;
            outRect.right = mSpaceItem - ( spanCurrent + 1 ) * mSpaceItem / mSpanCount;
            if( isReverse ){
                if( position >= mSpanCount) outRect.bottom = mSpaceItem;
            }else{
                if( position >= mSpanCount) outRect.top = mSpaceItem;
            }
        }else if( orientation == OrientationHelper.HORIZONTAL ){
            outRect.top = spanCurrent * mSpaceItem / mSpanCount;
            outRect.bottom = mSpaceItem - ( spanCurrent + 1 ) * mSpaceItem / mSpanCount;
            if( isReverse ){
                if( position >= mSpanCount) outRect.right = mSpaceItem;
            }else{
                if( position >= mSpanCount) outRect.left = mSpaceItem;
            }
        }
    }
}
