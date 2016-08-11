package com.alex.cycling.service;


import android.location.Location;

import com.alex.cycling.db.DbUtil;
import com.alex.cycling.utils.BaiduTool;
import com.alex.cycling.utils.LogUtil;
import com.alex.cycling.utils.VacuateUtil;
import com.alex.cycling.utils.thread.ExecutUtils;
import com.alex.greendao.TrackInfo;
import com.alex.greendao.TrackInfoDao;
import com.alex.greendao.WorkPoint;
import com.alex.greendao.WorkPointDao;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.jni.ActInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.greenrobot.dao.query.CloseableListIterator;


/**
 * Created by comexs on 16/4/3.
 */
public class TrackManager {

    private static String trackUUID = null;

    public static final String RECOVERY = "recovery";

    private static boolean hasRecovery = false; //是否恢复

    private static boolean isFirst = true;

    private static List<WorkPoint> workPoints = new ArrayList<WorkPoint>();

    //当插入第一条时，写入对应的trackUUID进入数据库和对应的第一个点
    public static void openTrackDb(final WorkPoint workPoint) {
        isFirst = false;
        TrackInfo trackInfo = new TrackInfo();
        trackInfo.setTrackUUID(getCurrentUUID());
        trackInfo.setStartLat(workPoint.getLat());
        trackInfo.setStartLon(workPoint.getLon());
        trackInfo.setStartTime(workPoint.getTime());
        trackInfo.setStatus(0); //对应的轨迹为未成完状态
        trackInfo.setCalorie(0.0);
        DbUtil.getTrackInfoService().save(trackInfo);
        ExecutUtils.runInMain(new Runnable() {
            @Override
            public void run() {
                BaiduTool.geoAddress(new LatLng(workPoint.getLat(), workPoint.getLon()), listener);
            }
        });
    }


    //最后一条进入时，关闭数据库，插入最后一个点
    public static void closeTrackDB() {
        isFirst = true;
        List<TrackInfo> trackInfos = DbUtil.getTrackInfoService().queryBuilder().where(TrackInfoDao.Properties.Status.eq("0")).list();
        if (trackInfos.size() == 0) {
            return;
        }
        for (TrackInfo trackInfo : trackInfos) {
            trackInfo.setStatus(1);  //对应的轨迹为完成状态
            DbUtil.getTrackInfoService().update(trackInfo);
//            vacuate();
        }
    }

    private static OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {

        @Override
        public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
            ReverseGeoCodeResult.AddressComponent address = result.getAddressDetail();
            String resultStr = String.format("%s#%s#%s#%s#%s", address.province, address.city, address.district, address.street, address.streetNumber);
            LogUtil.e(resultStr);
            if (!isFirst) {
                TrackInfo trackInfo = DbUtil.getTrackInfoService().queryBuilder().where(TrackInfoDao.Properties.TrackUUID.eq(getCurrentUUID())).unique();
                trackInfo.setStartGeoCode(resultStr);
                DbUtil.getTrackInfoService().update(trackInfo);
            } else {
                TrackInfo trackInfo = DbUtil.getTrackInfoService().queryBuilder().where(TrackInfoDao.Properties.TrackUUID.eq(getCurrentUUID())).unique();
                trackInfo.setEndGeoCode(resultStr);
                DbUtil.getTrackInfoService().update(trackInfo);
            }
        }
    };


    public static void saveWorkPoint(Location location, boolean isEnd) {
        WorkPoint workPoint = new WorkPoint();
        workPoint.setLat(location.getLatitude());
        workPoint.setLon(location.getLongitude());
        workPoint.setTime(location.getTime());
        workPoint.setSpeed(location.getSpeed());
        workPoint.setAlt(location.getAltitude());
        if (isFirst && !hasRevovery()) {
            openTrackDb(workPoint);
        }
        workPoints.add(workPoint);
        if (workPoints.size() == 5) {
            DbUtil.creTrackDb(getCurrentUUID()).save(workPoints);
//            saveTrackInfo(workPoints);
            workPoints.clear();
        }
        if (isEnd) {
            TrackManager.closeTrackDB();
            if (workPoints.size() > 0) {
                DbUtil.creTrackDb(getCurrentUUID()).save(workPoints);
                workPoints.clear();
            }
            return;
        }
    }

//    public static void saveTrackInfo(List<WorkPoint> workPoints) {
//        ActInfo actInfo = new ActInfo();
//        double distance = BaiduTool.getDis(new LatLng(mLocation.getLatitude(), mLocation.getLongitude()), new LatLng(location.getLatitude(), location.getLongitude())) / 1000;
//        actInfo.setDistance((actInfo.getDistance() + distance));
//        actInfo.setClimbed(actInfo.getClimbed() + Math.abs(actInfo.getClimbed() - location.getAltitude()));
//        if (0 != (tempTime - startTime) / 1000) {
//            double aSpeed = actInfo.getDistance() / ((tempTime - startTime) / 1000) * 1000;
//            actInfo.setAverSpeed(aSpeed * 3.6);
//        }
//    }


    //恢复最后轨迹最后一个点
    public static WorkPoint recoveryLastPoint() {
        return DbUtil.creTrackDb(getCurrentUUID()).queryBuilder().orderDesc(WorkPointDao.Properties.Time).list().get(0);
    }

    //恢复最后轨迹第一个点
    public static WorkPoint getLastTrackFirstPoint() {
        return DbUtil.creTrackDb(getCurrentUUID()).queryBuilder().orderAsc(WorkPointDao.Properties.Time).list().get(0);
    }


    //获取最后轨迹的时间间隔
    public static long getLastTrackMillTime() {
        return (recoveryLastPoint().getTime() - getLastTrackFirstPoint().getTime()) * 1000;
    }


    //获取当前的轨迹uuid
    public static String getCurrentUUID() {
        if (null == trackUUID) {
            if (hasRevovery()) {
                TrackInfo trackInfo = DbUtil.getTrackInfoService().queryBuilder().where(TrackInfoDao.Properties.Status.eq("0")).orderDesc(TrackInfoDao.Properties.StartTime).list().get(0);
                return trackUUID = trackInfo.getTrackUUID();
            } else {
                trackUUID = UUID.randomUUID().toString();
            }
        }
        return trackUUID;
    }


    public static List<LatLng> getCurrentCacheList() {
        return getCacheList(getCurrentUUID());
    }


    public static List<LatLng> getCacheList(String trackUUID) {
        List<LatLng> cacheList = new ArrayList<LatLng>();
        CloseableListIterator<WorkPoint> workPoints = DbUtil.creTrackDb(trackUUID).queryBuilder().listIterator();
        try {
            while (workPoints.hasNext()) {
                WorkPoint workPoint = workPoints.next();
                LatLng bdPt = BaiduTool.wgs2bd(new LatLng(workPoint.getLat(), workPoint.getLon()));
                cacheList.add(bdPt);
            }
            workPoints.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cacheList;
    }

    public static List<WorkPoint> queryWorkPointByUUID(String trackUUID) {
        List<WorkPoint> workPointList = new ArrayList<WorkPoint>();
        CloseableListIterator<WorkPoint> workPoints = DbUtil.creTrackDb(trackUUID).queryBuilder().listIterator();
        try {
            while (workPoints.hasNext()) {
                WorkPoint workPoint = workPoints.next();
                workPointList.add(workPoint);
            }
            workPoints.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return workPointList;
    }


    public static boolean getCyclingStatus() {
        return isFirst;
    }

    //是否可以恢复
    public static boolean hasRevovery() {
        List<TrackInfo> lastTrackInfo = DbUtil.getTrackInfoService().queryBuilder().where(TrackInfoDao.Properties.Status.eq("0")).orderDesc(TrackInfoDao.Properties.StartTime).list();
        if (lastTrackInfo.size() > 0) {
            return hasRecovery = true;
        }
        return hasRecovery = false;
    }

    public static void vacuate() {
        vacuate(trackUUID);
    }

    public static void vacuate(final String trackName) {
        ExecutUtils.runInBack(new Runnable() {
            @Override
            public void run() {
                StringBuffer stringBuffer = new StringBuffer();
                List<WorkPoint> list = DbUtil.creTrackDb(getCurrentUUID()).queryAll();
                LogUtil.e(list.size() + "");
                VacuateUtil vacuateUtil = new VacuateUtil();
                vacuateUtil.vacuate(list, 3);
                List<WorkPoint> resultList = vacuateUtil.getResult();
                for (WorkPoint workPoint : resultList) {
                    if (workPoint.getStatus() != -1) {
                        LatLng latLng = BaiduTool.wgs2bd(new LatLng(workPoint.getLat(), workPoint.getLon()));
                        stringBuffer.append(latLng.longitude);
                        stringBuffer.append(",");
                        stringBuffer.append(latLng.latitude);
                        stringBuffer.append(";");
                    }
                }
                TrackInfo trackInfo = DbUtil.getTrackInfoService().queryBuilder().where(TrackInfoDao.Properties.TrackUUID.eq(trackName)).unique();
                trackInfo.setImageUrl(stringBuffer.toString());
                DbUtil.getTrackInfoService().update(trackInfo);
                LogUtil.e("抽稀完了!");
            }
        });
    }

    public static String getBaiduUrlByDes(String bdpath, int width, int height) {
        if (width == 0 || height == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("http://api.map.baidu.com/staticimage?");
        sb.append("width=");
        sb.append(width);
        sb.append("&height=");
        sb.append(height);
        sb.append("&copyright=1");
        sb.append("&paths=");
        sb.append(bdpath);
        sb.append("&pathStyles=0x2CAF61,3,1");
        return sb.toString();
    }

}
