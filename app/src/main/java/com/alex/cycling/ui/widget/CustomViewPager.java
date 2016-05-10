package com.alex.cycling.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.alex.cycling.utils.DisplayUtil;

/**
 * Created by Administrator on 2016/5/10.
 */
public class CustomViewPager extends ViewPager {

    private int screenWidth;
    private int position = 0;
    private boolean canScroll = false;

    public CustomViewPager(Context context) {
        super(context);
        init();
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        screenWidth = DisplayUtil.getScreenWidth();
        canScroll = false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!canScroll) {
//            if (position == 0) {
//                if ((ev.getAction() == MotionEvent.ACTION_DOWN) && (ev.getX() < DisplayUtil.dip2px(50.0F))) {
//                    return true;
//                }
//            }
            return true;
        } else {
            if ((ev.getAction() != MotionEvent.ACTION_DOWN) || (ev.getX() <= screenWidth - DisplayUtil.dip2px(50.0F))) {
                return true;
            }
        }
        return true;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setScrollable(boolean canScroll) {
        this.canScroll = canScroll;
    }

    public void setScrollablePosition(int position) {
        this.position = position;
    }
}
