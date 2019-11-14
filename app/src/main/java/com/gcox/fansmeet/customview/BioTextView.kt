package com.gcox.fansmeet.customview

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.gcox.fansmeet.customview.autolinktextview.AutoLinkTextView
import com.gcox.fansmeet.util.UiUtils

class BioTextView : AutoLinkTextView {

    interface DisallowTouchEventCallback {
        fun requestDisallowTouchEvent(disallow: Boolean)
    }

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    private var gestureDetector: GestureDetector? = null
    var requestDisallowParentView: DisallowTouchEventCallback? = null
    var longClickListener: View.OnLongClickListener? = null

    init {
        gestureDetector = GestureDetector(context, TextViewTouchEvent())
        setOnTouchListener { v, event ->
            if (event == null) {
                false
            } else {
                if (event.actionMasked == MotionEvent.ACTION_DOWN || event.actionMasked == MotionEvent.ACTION_UP) {
                    // let the default touch event handle link click
                    super.onTouchEvent(event)
                }
                val result = gestureDetector?.onTouchEvent(event) == true
                if (event.actionMasked == MotionEvent.ACTION_UP) {
                    UiUtils.requestDisallowInterceptTouchEvent(this, false)
                    requestDisallowParentView?.requestDisallowTouchEvent(false)
                }
                result
            }
        }
    }

    private inner class TextViewTouchEvent : GestureDetector.SimpleOnGestureListener() {

        internal var downY = 0f
        internal var startScrollY = 0f
        internal var scrollMaxRange = 0f
        internal var shouldConsumed: Boolean = false

        override fun onDown(e: MotionEvent): Boolean {
            shouldConsumed = true
            downY = e.y
            startScrollY = scrollY.toFloat()

            val tvHeight = UiUtils.calculateTextViewContentHeight(this@BioTextView).toFloat()
            scrollMaxRange = tvHeight - height
            if (scrollMaxRange <= 5) {
                scrollMaxRange = 0f
                shouldConsumed = false
            } else {
                UiUtils.requestDisallowInterceptTouchEvent(this@BioTextView, true)
                requestDisallowParentView?.requestDisallowTouchEvent(true)
                shouldConsumed = true
            }
            return shouldConsumed
        }

        override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            if (!shouldConsumed) {
                return false
            }
            val y = e2.y
            var delta = downY - y + startScrollY
            if (delta < 0) {
                delta = 0f
            }
            if (delta > scrollMaxRange) {
                delta = scrollMaxRange
            }
            scrollTo(0, delta.toInt())
            return true
        }

        override fun onLongPress(e: MotionEvent) {
            longClickListener?.onLongClick(this@BioTextView)
        }
    }
}