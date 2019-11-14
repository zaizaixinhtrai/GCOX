package com.gcox.fansmeet.customview;

import android.content.Context;
import android.graphics.PointF;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import com.gcox.fansmeet.util.TouchEventUtil;

/**
 * Created by linh on 22/12/2016.
 */

public class InterceptViewpager extends ViewPager {
    private PointF pointDown;
    private PointF pointMoving;

    private int interceptTabIndex;
    private int interceptDirection;

    public void setInterceptTabIndex(int interceptTabIndex) {
        this.interceptTabIndex = interceptTabIndex;
    }

    public void setInterceptDirection(int interceptDirection) {
        this.interceptDirection = interceptDirection;
    }

    public InterceptViewpager(Context context) {
        super(context);
        init();
    }

    public InterceptViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                pointDown.x = ev.getX();
                pointDown.y = ev.getY();
            } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
                pointMoving.x = ev.getX();
                pointMoving.y = ev.getY();
                float deltaX = pointDown.x - pointMoving.x;
                float deltaY = pointDown.y - pointMoving.y;
                int direction = TouchEventUtil.determineDirection(deltaX, deltaY);

                if (TouchEventUtil.calculateMovementDistance(pointDown, pointMoving) > 15 || direction == TouchEventUtil.MOVE_RIGHT || direction == TouchEventUtil.MOVE_LEFT){
                    getParent().requestDisallowInterceptTouchEvent(true);
                }

                direction &= interceptDirection;
                if (getCurrentItem() == interceptTabIndex && (direction == TouchEventUtil.MOVE_RIGHT || direction == TouchEventUtil.MOVE_LEFT || direction == TouchEventUtil.MOVE_DOWN || direction == TouchEventUtil.MOVE_UP)) {
                    return true;
                }
            }

        return super.dispatchTouchEvent(ev);
    }

    private void init(){
        pointDown = new PointF();
        pointMoving = new PointF();
    }
}
