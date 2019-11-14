package com.gcox.fansmeet.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import com.gcox.fansmeet.R;

import java.util.Locale;

/**
 * Created by lanna on 11/6/15.
 */
public class UiUtils {

    public static void hideSoftKeyboard(Activity activity) {
        try {
            if (!activity.isFinishing()) {
                InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class ListSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int space;
        private boolean includeEdge;

        public ListSpacingItemDecoration(int space, boolean includeEdge) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            if (includeEdge) {
                outRect.left = space;
                outRect.right = space;
                outRect.bottom = space;
            }
            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildLayoutPosition(view) != 0) {
                outRect.top = space;
            }
        }
    }

    public static class HorizontalListSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int space;
        private boolean includeEdge;

        public HorizontalListSpacingItemDecoration(int space, boolean includeEdge) {
            this.space = space;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            if (includeEdge) {
                outRect.left = space;
                outRect.right = space;
                outRect.bottom = space;
                outRect.top = space;
                return;
            }

            int position = parent.getChildAdapterPosition(view); // item position
            if (position != 0) {
                outRect.left = space;
            }
        }
    }

    public static class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private Rect mRect = new Rect();
        private Paint borderPaint;

        private int spanCount;
        private int spacing;
        private boolean includeEdge;
        private boolean noTopEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        public void setNoTopEdge(boolean noTopEdge) {
            this.noTopEdge = noTopEdge;
        }

        public void setupBorderPaint(int color, int strokeWidth) {
            borderPaint = new Paint();
            borderPaint.setAntiAlias(true);
            borderPaint.setColor(color);
            borderPaint.setStyle(Paint.Style.STROKE);
            borderPaint.setStrokeWidth(strokeWidth);
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {

            if (borderPaint == null) {
                super.onDraw(c, parent, state);
                return;
            }

            int save = c.save();
            for (int i = 0, size = parent.getChildCount(); i < size; i++) {
                View child = parent.getChildAt(i);
                parent.getDecoratedBoundsWithMargins(child, mRect);
                c.drawRect(mRect, borderPaint);
            }

            c.restoreToCount(save);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount && !noTopEdge) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    public static int[] getScreenSize(Context context) {
        int[] sizeOfScreen = new int[2];
        Point size = new Point();
        WindowManager w = ((Activity) context).getWindowManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            w.getDefaultDisplay().getSize(size);
            sizeOfScreen[0] = size.x;
            sizeOfScreen[1] = size.y;
        } else {
            Display d = w.getDefaultDisplay();
            sizeOfScreen[0] = d.getWidth();
            sizeOfScreen[1] = d.getHeight();
        }

        return sizeOfScreen;
    }

    /**
     * get the screen size which includes soft navigation bar and notification bar
     *
     * @param context
     */
    public static int[] getRealScreenSize(Context context) {
        int x, y, orientation = context.getResources().getConfiguration().orientation;
        WindowManager wm = ((WindowManager)
                context.getSystemService(Context.WINDOW_SERVICE));
        Display display = wm.getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point screenSize = new Point();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                display.getRealSize(screenSize);
                x = screenSize.x;
                y = screenSize.y;
            } else {
                display.getSize(screenSize);
                x = screenSize.x;
                y = screenSize.y;
            }
        } else {
            x = display.getWidth();
            y = display.getHeight();
        }

        int[] sizeOfScreen = new int[2];
        sizeOfScreen[0] = orientation == Configuration.ORIENTATION_PORTRAIT ? x : y;
        sizeOfScreen[1] = orientation == Configuration.ORIENTATION_PORTRAIT ? y : x;
        return sizeOfScreen;
    }

    public static int getDimenValueFromDimenXML(Context context, int dimenId) {
        return (int) (context.getResources().getDimension(dimenId) / context.getResources().getDisplayMetrics().density);
    }

    public static void setColorSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.setColorSchemeResources(R.color.path_primary, R.color.path_orange, R.color.path_green);
    }

    public static String getLocalization() {
        return Locale.getDefault().getLanguage();
    }

    public static int calculateTextViewContentHeight(TextView tv) {
        if (tv == null) return 0;
        TextPaint tp = tv.getPaint();
        if (tp == null) return 0;
        Paint.FontMetrics fm = tp.getFontMetrics();
        if (fm != null) {
            return Math.round(tv.getLineCount() * (fm.descent - fm.ascent));
        }
        return tv.getLineHeight() * tv.getLineCount();
    }

    public static void requestDisallowInterceptTouchEvent(View view, boolean disallow) {
        if (view != null && view.getParent() != null) {
            view.getParent().requestDisallowInterceptTouchEvent(disallow);
        }
    }
}
