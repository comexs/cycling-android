package com.alex.cycling.ui.track.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alex.cycling.R;
import com.alex.cycling.base.BaseFragment;
import com.alex.cycling.service.TrackManager;
import com.alex.cycling.ui.widget.SpeedLineChart;
import com.alex.greendao.WorkPoint;
import com.github.mikephil.charting.charts.LineChart;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by comexs on 16/4/24.
 */
public class ChartFragment extends BaseFragment {

    @Bind(R.id.speedChart)
    LineChart speedChart;
    @Bind(R.id.climbChart)
    LineChart climbChart;

    private SpeedLineChart speedLineChart;
    private SpeedLineChart climbLineChart;

    private String trackUUID;

    List<WorkPoint> workPointList;

    public static Fragment newInstance(String trackUUID) {
        ChartFragment fragment = new ChartFragment();
        Bundle bundle = new Bundle();
        bundle.putString("uuid", trackUUID);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_track_chart, null, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {

        trackUUID = getArguments().getString("uuid");

        workPointList = TrackManager.queryWorkPointByUUID(trackUUID);


        speedLineChart = new SpeedLineChart();
        climbLineChart = new SpeedLineChart();
        speedLineChart.initChart(speedChart);
        climbLineChart.initChart(climbChart);
    }

    @Override
    public void onPageStart() {
        super.onPageStart();
        speedLineChart.setData(speedChart, workPointList);
        climbLineChart.setData(climbChart, workPointList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
