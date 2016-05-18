package com.alex.cycling.ui.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.alex.cycling.utils.DisplayUtil;
import com.alex.cycling.utils.LogUtil;

/**
 * Created by Administrator on 2016/5/10.
 */
public class CustomViewPager extends ViewPager {

    private boolean noScroll = false;

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public CustomViewPager(Context context) {
        super(context);
    }

    public void setNoScroll(boolean noScroll) {
        this.noScroll = noScroll;
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /* return false;//super.onTouchEvent(arg0); */
        if (noScroll && getCurrentItem() == 0) {
            if (event.getAction() == MotionEvent.ACTION_DOWN && event.getX() > (DisplayUtil.getScreenWidth() - DisplayUtil.dip2px(50))) {
                return true;
            }
            return false;
        } else {
            return super.onTouchEvent(event);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (noScroll && getCurrentItem() == 0) {
            if (event.getAction() == MotionEvent.ACTION_DOWN && event.getX() > (DisplayUtil.getScreenWidth() - DisplayUtil.dip2px(50))) {
                return super.onInterceptTouchEvent(event);
            }
            return false;
        } else {
            return super.onInterceptTouchEvent(event);
        }
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item);
    }

}
