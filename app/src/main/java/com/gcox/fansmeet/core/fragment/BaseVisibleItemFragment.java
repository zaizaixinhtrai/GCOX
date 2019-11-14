package com.gcox.fansmeet.core.fragment;

import android.graphics.Rect;
import android.view.View;


/**
 * Created by Son Nguyen on 3/7/2016.
 */
public class BaseVisibleItemFragment extends BaseFragment {
    public final Rect mCurrentViewRect = new Rect();

    public int getVisibilityPercents(View view) {

        if (view == null) return 0;

        int percents = 100;

        view.getLocalVisibleRect(mCurrentViewRect);

        int height = view.getHeight();

        if (viewIsPartiallyHiddenTop()) {
            // view is partially hidden behind the top edge
            percents = Math.abs((height - mCurrentViewRect.top) * 100 / height);
        } else if (viewIsPartiallyHiddenBottom(height)) {
            percents = mCurrentViewRect.bottom * 100 / height;
        }
        return percents;
    }

    private boolean viewIsPartiallyHiddenBottom(int height) {
        return mCurrentViewRect.bottom > 0 && mCurrentViewRect.bottom < height;
    }

    private boolean viewIsPartiallyHiddenTop() {
        return mCurrentViewRect.top > 0;
    }

}
