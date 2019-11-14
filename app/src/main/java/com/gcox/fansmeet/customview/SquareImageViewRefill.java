package com.gcox.fansmeet.customview;

import android.content.Context;
import android.util.AttributeSet;
import com.gcox.fansmeet.util.PixelUtil;

public class SquareImageViewRefill extends android.support.v7.widget.AppCompatImageView
{
	public SquareImageViewRefill(Context context)
	{
		super(context);
	}

	public SquareImageViewRefill(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public SquareImageViewRefill(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(widthMeasureSpec, widthMeasureSpec - PixelUtil.dpToPx(getContext(),45) );
	}
}