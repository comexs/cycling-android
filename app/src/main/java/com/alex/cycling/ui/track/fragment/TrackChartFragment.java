package com.alex.cycling.ui.track.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alex.cycling.R;
import com.alex.cycling.base.BaseFragment;
import com.alex.cycling.db.DbUtil;
import com.alex.cycling.service.TrackManager;
import com.alex.cycling.ui.widget.ArcProgressBar;
import com.alex.cycling.ui.widget.SpeedLineChart;
import com.alex.cycling.utils.LogUtil;
import com.alex.cycling.utils.MathUtil;
import com.alex.cycling.utils.VacuateUtil;
import com.alex.cycling.utils.thread.ExecutUtils;
import com.alex.greendao.TrackInfo;
import com.alex.greendao.TrackInfoDao;
import com.alex.greendao.WorkPoint;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
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
    @Bind(R.id.arcProgressBar)
    ArcProgressBar arcProgressBar;

    private SpeedLineChart speedLineChart;

    private String trackUUID;
    private TrackInfo trackInfo;

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
        trackInfo = DbUtil.getTrackInfoService().queryBuilder().where(TrackInfoDao.Properties.TrackUUID.eq(trackUUID)).unique();

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
        test(workPointList);

        speedLineChart = new SpeedLineChart();
        speedLineChart.initChart(speedChart);
    }

    private void test(final List<WorkPoint> workPointList) {
        ExecutUtils.runInBack(new Runnable() {
            @Override
            public void run() {
                firstLatLng = null;
                allDistance = 0;
                climbup = 0;
                for (WorkPoint workPoint : workPointList) {
                    if (null == firstLatLng) {
                        firstLatLng = new LatLng(workPoint.getLat(), workPoint.getLon());
                        climbup = workPoint.getAlt();
                    } else {
                        LatLng latLng = new LatLng(workPoint.getLat(), workPoint.getLon());
                        double distance = DistanceUtil.getDistance(firstLatLng, latLng);
                        allDistance += distance;
                        firstLatLng = latLng;
                        if (workPoint.getAlt() - climbup > 0) {
                            climbup += (workPoint.getAlt() - climbup);
                        }
                    }
                }
                if (workPointList.size() < 2) {
                    return;
                }
                trackInfo.setTotalDis(MathUtil.decimal(allDistance));
                trackInfo.setAverageSpeed(allDistance / (workPointList.get(0).getTime() - workPointList.get(workPointList.size() - 1).getTime()));
                trackInfo.setTotalTime(workPointList.get(workPointList.size() - 1).getTime() - workPointList.get(0).getTime());
                trackInfo.setClimbUp(climbup);
                DbUtil.getTrackInfoService().update(trackInfo);
            }
        });
    }

    private LatLng firstLatLng;
    private double allDistance = 0;
    private double climbup = 0;

    @Override
    public void onPageStart() {
        super.onPageStart();
        speedLineChart.setData(speedChart, cacheList);
        arcProgressBar.startAnimation(50);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
