package com.gcox.fansmeet.util;

import android.graphics.PointF;

/**
 * Created by linh on 15/02/2017.
 */

public class TouchEventUtil {
    public static final int MOVE_RIGHT = 1;
    public static final int MOVE_LEFT = MOVE_RIGHT<<1;
    public static final int MOVE_DOWN = MOVE_RIGHT<<2;
    public static final int MOVE_UP = MOVE_RIGHT<<3;

    // Use dx and dy to determine the direction
    public static int determineDirection(float dx, float dy){
        if(Math.abs(dx) > Math.abs(dy)) {
            if(dx>0)
                return MOVE_RIGHT;
            else
                return MOVE_LEFT;
        } else {
            if(dy>0)
                return MOVE_DOWN;
            else
                return MOVE_UP;
        }
    }

    public static double calculateMovementDistance(PointF p1, PointF p2){
        return Math.sqrt(Math.pow(p1.x-p2.x, 2) + Math.pow(p1.y-p2.y, 2));
    }
}
