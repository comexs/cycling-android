package com.alex.cycling.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.alex.cycling.base.CSApplication;

/**
 * Created by comexs on 16/4/26.
 */
public class DisplayUtil {

    /**
     * 获取当前机器的屏幕信息对象<br/>
     * 另外：通过android.os.Build类可以获取当前系统的相关信息
     *
     * @return
     */
    public static DisplayMetrics getScreenInfo() {
        WindowManager windowManager = (WindowManager) CSApplication.getInstance().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        // dm.widthPixels;//寬度
        // dm.heightPixels; //高度
        // dm.density; //密度
        return dm;
    }

    public static int getScreenWidth() {
        return getScreenInfo().widthPixels;
    }

    public static int getScreenHeight() {
        return getScreenInfo().heightPixels;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        final float scale = CSApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        final float scale = CSApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
