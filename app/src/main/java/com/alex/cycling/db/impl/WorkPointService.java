package com.alex.cycling.db.impl;

import com.alex.cycling.db.DbBaseService;
import com.alex.greendao.WorkPoint;
import com.alex.greendao.WorkPointDao;


/**
 * Created by comexs on 16/3/30.
 */
public class WorkPointService extends DbBaseService<WorkPoint, Long> {

    public WorkPointService(WorkPointDao dao) {
        super(dao);
    }
}
