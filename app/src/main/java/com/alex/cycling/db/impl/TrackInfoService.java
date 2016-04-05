package com.alex.cycling.db.impl;

import com.alex.cycling.db.DbBaseService;
import com.alex.greendao.TrackInfo;

import de.greenrobot.dao.AbstractDao;

/**
 * Created by comexs on 16/4/3.
 */
public class TrackInfoService extends DbBaseService<TrackInfo, Long> {

    public TrackInfoService(AbstractDao dao) {
        super(dao);
    }
}
