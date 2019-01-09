package com.myreevuuCoach.customViews;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NonSwipeable extends ViewPager {

    private boolean enabled;

    public NonSwipeable(Context context) {
        super(context);
        this.enabled = false;
    }

    public NonSwipeable(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.enabled = false;
    }

   @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        return this.enabled && super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        return this.enabled && super.onInterceptTouchEvent(event);
    }
    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
