package com.alex.cycling.ui.main;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import com.alex.cycling.R;
import com.alex.cycling.base.BaseActivity;
import com.jni.ActInfo;
import com.alex.cycling.client.TrackClient;
import com.alex.cycling.service.TrackManager;
import com.alex.cycling.utils.BaiduLocationUtils;
import com.alex.cycling.utils.BaiduTool;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by comexs on 16/3/27.
 */
public class MapActivity extends BaseActivity implements BDLocationListener {


    @BindView(R.id.frg_map)
    MapView mapView;
    @BindView(R.id.close)
    FloatingActionButton close;

    List<LatLng> latLngList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mapView.showScaleControl(false);
        mapView.showZoomControls(false);
        mapView.getMap().setMyLocationEnabled(true);
        mapView.getMap().getUiSettings().setRotateGesturesEnabled(false);
        //百度地图logo消失
        mapView.removeViewAt(1);
        MapStatusUpdate u = MapStatusUpdateFactory.zoomTo(20);
        mapView.getMap().setMapStatus(u);
        BaiduLocationUtils.instance.start(this);
        BaiduLocationUtils.instance.setListener(this);
        TrackClient.getInstance().addTrackListener(onCyclingListener);
    }


    private TrackClient.OnCyclingListener onCyclingListener = new TrackClient.OnCyclingListener() {

        @Override
        public void onCycling(ActInfo actInfo) {


        }

        @Override
        public void onNewLocation(Location pt, long time, int signal) {
            if (null == pt) {
                return;
            }
            LatLng bdPt = BaiduTool.wgs2bd(new LatLng(pt.getLatitude(), pt.getLongitude()));
            drawTrack();
            MyLocationData locData = new MyLocationData.Builder()
                    .direction(pt.getBearing()).latitude(bdPt.latitude)
                    .longitude(bdPt.longitude).build();

            // 设置定位数据
            if (mapView == null) {
                return;
            }
            mapView.getMap().setMyLocationData(locData);
            MapStatusUpdate u = MapStatusUpdateFactory
                    .newLatLng(bdPt);
            mapView.getMap().setMapStatus(u);
        }
    };

    private void drawTrack() {
        if (mapView == null || mapView.getMap() == null) {
            return;
        }
        latLngList = TrackManager.getCurrentCacheList();
        mapView.getMap().clear();
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.RED);
        polylineOptions.width(5);
        if (latLngList.size() < 3) {
            return;
        }
        polylineOptions.points(latLngList);
        mapView.getMap().addOverlay(polylineOptions);
    }


    @OnClick(R.id.close)
    void click(View v) {
        switch (v.getId()) {
            case R.id.close:
                finish();
                break;
        }
    }


    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        TrackClient.getInstance().addTrackListener(onCyclingListener);
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        TrackClient.getInstance().removeTranckListener(onCyclingListener);
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != mapView) {
            mapView.onDestroy();
            mapView = null;
        }
        BaiduLocationUtils.instance.end();
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        if (null == bdLocation) {
            return;
        }
        if (!TrackManager.getCyclingStatus()) {
            return;
        }
        LatLng bdPt = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
        MyLocationData locData = new MyLocationData.Builder()
                .direction(bdLocation.getDirection()).latitude(bdPt.latitude)
                .longitude(bdPt.longitude).build();
        // 设置定位数据
        mapView.getMap().setMyLocationData(locData);
        MapStatusUpdate u = MapStatusUpdateFactory
                .newLatLng(bdPt);
        mapView.getMap().setMapStatus(u);
    }
}
