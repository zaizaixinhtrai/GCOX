package com.gcox.fansmeet.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.gcox.fansmeet.util.UiUtils;

public class ChallengeItemImageView extends android.support.v7.widget.AppCompatImageView
{
	public ChallengeItemImageView(Context context)
	{
		super(context);
	}

	public ChallengeItemImageView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public ChallengeItemImageView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		int width = UiUtils.getRealScreenSize(getContext())[0]/3;
		setMeasuredDimension(width, width);
	}
}