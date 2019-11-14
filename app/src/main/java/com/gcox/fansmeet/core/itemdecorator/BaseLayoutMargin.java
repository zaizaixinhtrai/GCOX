package com.gcox.fansmeet.core.itemdecorator;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Px;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import static android.view.View.SCROLLBARS_OUTSIDE_OVERLAY;

/**
 * Created by thanhbc on 11/9/17.
 */

public abstract class BaseLayoutMargin extends RecyclerView.ItemDecoration {
    private final MarginDelegate mMarginDelegate;
    private final int mSpanCount;
    private final int mSpacing;
    private OnClickLayoutMarginItemListener listener;

    BaseLayoutMargin( int spanCount, @Px int spacing ){
        this.mSpanCount = spanCount;
        this.mSpacing = spacing;
        this.mMarginDelegate = new MarginDelegate( spanCount, spacing );
    }

    void setOnClickLayoutMarginItemListener( OnClickLayoutMarginItemListener listener ){
        this.listener = listener;
    }

    void setPadding( RecyclerView rv, @Px int margin ){
        this.setPadding( rv, margin, margin, margin, margin );
    }

    void setPadding( RecyclerView rv,
                     @Px int top,
                     @Px int bottom,
                     @Px int left,
                     @Px int right ){
        rv.setClipToPadding( false );
        rv.setScrollBarStyle( SCROLLBARS_OUTSIDE_OVERLAY );
        rv.setPadding( left, top, right, bottom );
    }

    MarginDelegate getMarginDelegate(){
        return mMarginDelegate;
    }

    void calculateMargin(Rect outRect, int position, int spanCurrent, int itemCount, int orientation, boolean isReverse, boolean isRTL ){
        mMarginDelegate.calculateMargin( outRect, position, spanCurrent, itemCount, orientation, isReverse, isRTL );
    }

    public int getSpacing(){
        return mSpacing;
    }

    public int getSpanCount(){
        return mSpanCount;
    }

    void setupClickLayoutMarginItem( Context context,
                                     View view,
                                     int position,
                                     int spanCurrent,
                                     RecyclerView.State state ){
        if( listener != null )
            view.setOnClickListener( onClickItem( context, view, position, spanCurrent, state ) );
    }

    @NonNull
    private View.OnClickListener onClickItem( final Context context, final View view, final int position, final int currentSpan, final RecyclerView.State state ){
        return new View.OnClickListener(){
            @Override
            public void onClick( View v ){
                if( listener != null )
                    listener.onClick( context, view, position, currentSpan, state );
            }
        };
    }

    @Override
    public void getItemOffsets( Rect outRect, View view, RecyclerView parent, RecyclerView.State state ){
        super.getItemOffsets( outRect, view, parent, state );
    }
}
