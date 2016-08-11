package com.alex.cycling.ui.main.util;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter;

/**
 * Created by philipp on 02/06/16.
 */
public class DayAxisValueFormatter implements AxisValueFormatter {

    private BarLineChartBase<?> chart;

    public DayAxisValueFormatter(BarLineChartBase<?> chart) {
        this.chart = chart;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        int days = (int) value;
        String formmat = "";
        switch (days) {
            case 1:
                formmat = "一";
                break;
            case 2:
                formmat = "二";
                break;
            case 3:
                formmat = "三";
                break;
            case 4:
                formmat = "四";
                break;
            case 5:
                formmat = "五";
                break;
            case 6:
                formmat = "六";
                break;
            case 7:
                formmat = "日";
                break;
            default:
                formmat = "";
                break;
        }
        return formmat;
    }

    @Override
    public int getDecimalDigits() {
        return 0;
    }
}
