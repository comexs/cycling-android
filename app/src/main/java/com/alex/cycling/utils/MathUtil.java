package com.alex.cycling.utils;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2016/5/13.
 */
public class MathUtil {

    public static double decimal(double number) {
        BigDecimal bg = new BigDecimal(number / 1000);
        return bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static String getTimeIntervalFormat(long diff) {
        long time = diff;
        int h = (int) (time / (60 * 60));
        time = time % (60 * 60);
        int m = (int) (time / (60));
        time = time % (60);
        int s = (int) time;
        return String.format("%02d:%02d:%02d", h, m, s);
    }

}
