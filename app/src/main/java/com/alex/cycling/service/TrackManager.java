package com.alex.cycling.service;

import com.alex.cycling.db.DbUtil;
import com.alex.greendao.TrackInfo;
import com.alex.greendao.TrackInfoDao;
import com.alex.greendao.WorkPoint;

import java.util.List;
import java.util.UUID;


/**
 * Created by comexs on 16/4/3.
 */
public class TrackManager {

    private static String trackName = UUID.randomUUID().toString();

    private static boolean isExist = false;


    //当插入第一条时，写入对应的trackUUID进入数据库和对应的第一个点
    public static void openTrackDb(WorkPoint workPoint) {
        List<TrackInfo> trackInfos = DbUtil.getTrackInfoService().queryBuilder().where(TrackInfoDao.Properties.TrackUUID.eq(trackName)).list();
        if (trackInfos.size() == 0) {
            TrackInfo trackInfo = new TrackInfo();
            trackInfo.setTrackUUID(trackName);
            trackInfo.setStartLat(workPoint.getLat());
            trackInfo.setStartLon(workPoint.getLon());
            trackInfo.setStartTime(workPoint.getTime());
            trackInfo.setStatus(0); //对应的轨迹为未成完状态
            trackInfo.setCalorie(0.0);
            DbUtil.getTrackInfoService().save(trackInfo);
            isExist = true;
        }
    }

    //最后一条进入时，关闭数据库，插入最后一个点
    public static void closeTrackDB(WorkPoint workPoint) {
        List<TrackInfo> trackInfos = DbUtil.getTrackInfoService().queryBuilder().where(TrackInfoDao.Properties.Status.eq("0")).list();
        if (trackInfos.size() > 0) {
            TrackInfo trackInfo = trackInfos.get(0);
            trackInfo.setEndLat(workPoint.getLat());
            trackInfo.setEndLon(workPoint.getLon());
            trackInfo.setEndTime(workPoint.getTime());
            trackInfo.setStatus(1);  //对应的轨迹为完成状态
            DbUtil.getTrackInfoService().update(trackInfo);
        }
    }

    public static void saveWorkPoint(WorkPoint workPoint) {
        if (!isExist) {
            openTrackDb(workPoint);
        }
        DbUtil.creTrackDb(trackName).saveAsync(workPoint);
    }


}
