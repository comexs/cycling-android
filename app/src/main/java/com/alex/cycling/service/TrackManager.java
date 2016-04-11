package com.alex.cycling.service;


import android.text.TextUtils;

import com.alex.cycling.db.DbUtil;
import com.alex.cycling.utils.BaiduTool;
import com.alex.cycling.utils.LogUtils;
import com.alex.cycling.utils.VacuateUtil;
import com.alex.cycling.utils.thread.ExecutUtils;
import com.alex.greendao.TrackInfo;
import com.alex.greendao.TrackInfoDao;
import com.alex.greendao.WorkPoint;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.greenrobot.dao.query.CloseableListIterator;


/**
 * Created by comexs on 16/4/3.
 */
public class TrackManager {


    private static final String trackName = UUID.randomUUID().toString();

    private static boolean isExist = false;

    //当插入第一条时，写入对应的trackUUID进入数据库和对应的第一个点
    public static void openTrackDb(final WorkPoint workPoint) {
        List<TrackInfo> trackInfos = DbUtil.getTrackInfoService().queryBuilder().where(TrackInfoDao.Properties.TrackUUID.eq(trackName)).list();
        if (trackInfos.size() == 0) {
            isExist = true;
            TrackInfo trackInfo = new TrackInfo();
            trackInfo.setTrackUUID(trackName);
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
    }

    //最后一条进入时，关闭数据库，插入最后一个点
    public static void closeTrackDB(final WorkPoint workPoint) {
        List<TrackInfo> trackInfos = DbUtil.getTrackInfoService().queryBuilder().where(TrackInfoDao.Properties.Status.eq("0")).list();
        if (trackInfos.size() > 0) {
            isExist = false;
            TrackInfo trackInfo = trackInfos.get(0);
            trackInfo.setEndLat(workPoint.getLat());
            trackInfo.setEndLon(workPoint.getLon());
            trackInfo.setEndTime(workPoint.getTime());
            trackInfo.setStatus(1);  //对应的轨迹为完成状态
            DbUtil.getTrackInfoService().update(trackInfo);
            ExecutUtils.runInMain(new Runnable() {
                @Override
                public void run() {
                    BaiduTool.geoAddress(new LatLng(workPoint.getLat(), workPoint.getLon()), listener);
                }
            });
            vacuate();
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
            LogUtils.e(resultStr);
            if (isExist) {
                TrackInfo trackInfo = DbUtil.getTrackInfoService().queryBuilder().where(TrackInfoDao.Properties.TrackUUID.eq(trackName)).unique();
                trackInfo.setStartGeoCode(resultStr);
                DbUtil.getTrackInfoService().update(trackInfo);
            } else {
                TrackInfo trackInfo = DbUtil.getTrackInfoService().queryBuilder().where(TrackInfoDao.Properties.TrackUUID.eq(trackName)).unique();
                trackInfo.setEndGeoCode(resultStr);
                DbUtil.getTrackInfoService().update(trackInfo);
            }
        }
    };


    public static void saveWorkPoint(WorkPoint workPoint) {
        if (!isExist) {
            openTrackDb(workPoint);
        }
        DbUtil.creTrackDb(trackName).save(workPoint);
    }

    public static String getCurrentUUID() {
        List<TrackInfo> trackInfos = DbUtil.getTrackInfoService().queryBuilder().where(TrackInfoDao.Properties.Status.eq("0")).orderDesc(TrackInfoDao.Properties.StartTime).list();
//        if (trackInfos.size() > 0) {
//            return trackInfos.get(0).getTrackUUID();
//        }
        return trackName;
    }

    public static List<LatLng> getCacheList() {
        List<LatLng> cacheList = new ArrayList<LatLng>();
        CloseableListIterator<WorkPoint> workPoints = DbUtil.creTrackDb(TrackManager.getCurrentUUID()).queryBuilder().listIterator();
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

    public static boolean getCyclingStatus() {
        return isExist;
    }

    //是否可以恢复
    public static boolean hasRevovery() {
        List<TrackInfo> trackInfos = DbUtil.getTrackInfoService().queryBuilder().where(TrackInfoDao.Properties.Status.eq("0")).orderDesc(TrackInfoDao.Properties.StartTime).list();
        if (trackInfos.size() > 0) {
            return true;
        }
        return false;
    }

    public static void vacuate() {
        vacuate(trackName);
    }

    public static void vacuate(final String trackName) {
        ExecutUtils.runInBack(new Runnable() {
            @Override
            public void run() {
                StringBuffer stringBuffer = new StringBuffer();
                List<WorkPoint> list = DbUtil.creTrackDb(trackName).queryAll();
                LogUtils.e(list.size() + "");
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
                LogUtils.e("抽稀完了!");
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
        sb.append("&pathStyles=0xff0000,3,1");
        return sb.toString();
    }

}
