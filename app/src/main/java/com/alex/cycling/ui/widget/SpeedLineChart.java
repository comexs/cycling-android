package com.alex.cycling.ui.widget;

import android.graphics.Color;

import com.alex.cycling.R;
import com.alex.greendao.WorkPoint;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.FillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by comexs on 16/4/24.
 */
public class SpeedLineChart {

    private boolean isAnimation = false;

    public void initChart(LineChart mChart) {
        mChart.setDescription("");
        mChart.setNoDataTextDescription("努力加载中...");

        // enable touch gestures
        mChart.setTouchEnabled(true);

        mChart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        mChart.setDragEnabled(false);
        mChart.setScaleEnabled(false);
        mChart.setDrawGridBackground(false);
        mChart.setHighlightPerDragEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
        mChart.setBackgroundColor(Color.LTGRAY);

        // add data

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.LINE);
        l.setTextSize(11f);
        l.setTextColor(Color.WHITE);
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
//        l.setYOffset(11f);

        mChart.setDrawBorders(true);
        mChart.setBorderColor(R.color.colorPrimary);
        mChart.setBorderWidth(0.5f);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawGridLines(true);
        xAxis.setDrawAxisLine(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setSpaceBetweenLabels(0);


        YAxis axisLeft = mChart.getAxisLeft();
        axisLeft.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        axisLeft.setAxisMinValue(0f);
        axisLeft.setDrawZeroLine(true);
        axisLeft.setDrawGridLines(true);
        axisLeft.setDrawAxisLine(true);
        axisLeft.setEnabled(true);


        YAxis axisRight = mChart.getAxisRight();
        axisRight.setEnabled(false);
    }

    public void setData(LineChart mChart, List<WorkPoint> workPointList) {
        if (workPointList.size() == 0) {
            return;
        }

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < workPointList.size()-1; i++) {
            xVals.add(i+ "");
        }

        ArrayList<Entry> yVals = new ArrayList<Entry>();

        for (int i = 0; i < workPointList.size()-1; i++) {
//            float mult = (range + 1);
//            float val = (float) (Math.random() * mult) + 20;// + (float)
            yVals.add(new Entry(workPointList.get(i).getSpeed(), i));
        }

        LineDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setYVals(yVals);
            mChart.getData().setXVals(xVals);
            mChart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(yVals, "里程");
            set1.setDrawCubic(true);
            set1.setCubicIntensity(0.2f);
            //set1.setDrawFilled(true);
            set1.setDrawCircles(false);
            set1.setLineWidth(1.8f);
            set1.setCircleRadius(4f);
            set1.setCircleColor(Color.WHITE);
            set1.setHighLightColor(Color.rgb(244, 117, 117));
            set1.setColor(Color.RED);
            set1.setFillColor(Color.WHITE);
            set1.setFillAlpha(100);
            set1.setDrawHorizontalHighlightIndicator(false);
            set1.setFillFormatter(new FillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return -10;
                }
            });

            // create a data object with the datasets
            LineData data = new LineData(xVals, set1);
            data.setValueTextSize(9f);
            data.setDrawValues(false);
            // set data
            mChart.setData(data);
        }

        if (!isAnimation) {
            mChart.animateX(2500);
            isAnimation = true;
        }

    }

}
