package com.gcox.fansmeet.util;

import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Created by thanhbc on 8/25/17.
 * ImageSpan don't have onClick so we implement it by our self.
 */

public abstract class ClickableImageSpan extends CenteredImageSpan{

    public ClickableImageSpan(Drawable d) {
        super(d);
    }

    public abstract void onClick(View view);
}
