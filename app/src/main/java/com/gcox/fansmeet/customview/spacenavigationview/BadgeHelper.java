/*
 * Space Navigation library for Android
 * Copyright (c) 2016 Arman Chatikyan (https://github.com/armcha/Space-Navigation-View).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gcox.fansmeet.customview.spacenavigationview;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.IdRes;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.gcox.fansmeet.R;

public class BadgeHelper {

    /**
     * Show badge
     *
     * @param view      target badge
     * @param badgeItem BadgeItem object
     */
    public static void showBadge(RelativeLayout view, BadgeItem badgeItem, boolean shouldShowBadgeWithNinePlus) {

        Utils.changeViewVisibilityVisible(view);
        TextView badgeTextView = (TextView) view.findViewById(R.id.badge_text_view);
        if (shouldShowBadgeWithNinePlus)
            badgeTextView.setText(badgeItem.getBadgeText());
        else
            badgeTextView.setText(badgeItem.getFullBadgeText());

        view.setScaleX(0);
        view.setScaleY(0);

        ViewCompat.animate(view)
                .setDuration(200)
                .scaleX(1)
                .scaleY(1)
                .setListener(new ViewPropertyAnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(View view) {
                        Utils.changeViewVisibilityVisible(view);
                    }
                })
                .start();
    }

    /**
     * Show badge
     *
     * @param view target badge
     */
    public static void hideBadge(View view) {
        ViewCompat.animate(view)
                .setDuration(200)
                .scaleX(0)
                .scaleY(0)
                .setListener(new ViewPropertyAnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(final View view) {
                        Utils.changeViewVisibilityGone(view);
                    }
                })
                .start();
    }

    /**
     * Show badge
     *
     * @param view      target badge
     * @param badgeItem BadgeItem object
     */
    public static void showBadge(RelativeLayout view, BadgeItem badgeItem, boolean shouldShowBadgeWithNinePlus, @IdRes int tvResId) {

        Utils.changeViewVisibilityVisible(view);
        TextView badgeTextView = (TextView) view.findViewById(tvResId);
        if (shouldShowBadgeWithNinePlus)
            badgeTextView.setText(badgeItem.getBadgeText());
        else
            badgeTextView.setText(badgeItem.getFullBadgeText());

        view.setScaleX(0);
        view.setScaleY(0);

        ViewCompat.animate(view)
                .setDuration(200)
                .scaleX(1)
                .scaleY(1)
                .setListener(new ViewPropertyAnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(View view) {
                        Utils.changeViewVisibilityVisible(view);
                    }
                })
                .start();
    }

    /**
     * Force show badge without animation
     *
     * @param view      target budge
     * @param badgeItem BadgeItem object
     */
     static void forceShowBadge(RelativeLayout view, BadgeItem badgeItem, boolean shouldShowBadgeWithNinePlus) {
        Utils.changeViewVisibilityVisible(view);
        view.setBackground(makeShapeDrawable(badgeItem.getBadgeColor()));
        TextView badgeTextView = (TextView) view.findViewById(R.id.badge_text_view);
        if (shouldShowBadgeWithNinePlus)
            badgeTextView.setText(badgeItem.getBadgeText());
        else
            badgeTextView.setText(badgeItem.getFullBadgeText());
    }

    /**
     * Make circle drawable for badge background
     *
     * @param color background color
     * @return return colored circle drawable
     */
     static GradientDrawable makeShapeDrawable(int color) {
        GradientDrawable badgeBackground = new GradientDrawable();
        badgeBackground.setColor(color);
        badgeBackground.setShape(GradientDrawable.OVAL);
        badgeBackground.setStroke(com.gcox.fansmeet.util.Utils.dpToPx(1), Color.parseColor("#FFFFFF"));
        return badgeBackground;
    }
    /**
     * Make circle drawable for badge background
     *
     * @param color background color
     * @return return colored circle drawable
     */
    public static GradientDrawable makeShapeDrawableWithoutBorder(int color) {
        GradientDrawable badgeBackground = new GradientDrawable();
        badgeBackground.setColor(color);
        badgeBackground.setShape(GradientDrawable.OVAL);
        return badgeBackground;
    }
}
