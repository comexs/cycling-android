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
import com.alex.cycling.utils.LogUtil;
import com.alex.cycling.utils.VacuateUtil;
import com.alex.cycling.utils.thread.ExecutUtils;
import com.alex.greendao.WorkPoint;
import com.github.mikephil.charting.charts.LineChart;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by comexs on 16/4/24.
 */
public class TrackChartFragment extends BaseFragment {

    @Bind(R.id.speedChart)
    LineChart speedChart;
    @Bind(R.id.climbChart)
    LineChart climbChart;

    private SpeedLineChart speedLineChart;
    private SpeedLineChart climbLineChart;

    private String trackUUID;

    List<WorkPoint> cacheList = new ArrayList<WorkPoint>();

    public static Fragment newInstance(String trackUUID) {
        TrackChartFragment fragment = new TrackChartFragment();
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

        final List<WorkPoint> workPointList = TrackManager.queryWorkPointByUUID(trackUUID);
        if (workPointList.size() <= 200) {
            cacheList = workPointList;
        } else {
            ExecutUtils.runInBack(new Runnable() {
                @Override
                public void run() {
                    VacuateUtil vacuateUtil = new VacuateUtil();
                    vacuateUtil.vacuate(workPointList, 3);
                    List<WorkPoint> list = vacuateUtil.getResult();
                    for (WorkPoint workPoint : list) {
                        if (workPoint.getStatus() != -1) {
                            cacheList.add(workPoint);
                        }
                    }
                    LogUtil.e(cacheList.size() + "");
                }
            });
        }

        speedLineChart = new SpeedLineChart();
        climbLineChart = new SpeedLineChart();
        speedLineChart.initChart(speedChart);
        climbLineChart.initChart(climbChart);
    }

    @Override
    public void onPageStart() {
        super.onPageStart();
        speedLineChart.setData(speedChart, cacheList);
        climbLineChart.setData(climbChart, cacheList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
