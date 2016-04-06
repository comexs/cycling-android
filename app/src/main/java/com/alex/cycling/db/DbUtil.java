package com.alex.cycling.db;

import com.alex.cycling.db.impl.TrackInfoService;
import com.alex.cycling.db.impl.WorkPointService;
import com.alex.greendao.TrackInfoDao;
import com.alex.greendao.WorkPointDao;


/**
 * Created by comexs on 16/3/30.
 */
public class DbUtil {

    private static WorkPointService workPointService;
    private static TrackInfoService trackInfoService;
    private static String trackName = null;


    private static WorkPointDao getWorkPointDao(String trackUUID) {
        return DbCore.createTrackSession(trackUUID).getWorkPointDao();
    }

    public static WorkPointService creTrackDb(String trackUUID) {
        if (workPointService == null) {
            workPointService = new WorkPointService(getWorkPointDao(trackUUID));
            trackName = trackUUID;
        } else {
            if (!trackName.equals(trackUUID)) {
                workPointService = new WorkPointService(getWorkPointDao(trackUUID));
                trackName = trackUUID;
            }
        }
        return workPointService;
    }

    private static TrackInfoDao getTrackInfoDao() {
        return DbCore.getDaoSession().getTrackInfoDao();
    }

    public static TrackInfoService getTrackInfoService() {
        if (trackInfoService == null) {
            trackInfoService = new TrackInfoService(getTrackInfoDao());
        }
        return trackInfoService;
    }

}
