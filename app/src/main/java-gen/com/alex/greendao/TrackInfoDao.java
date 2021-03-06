package com.alex.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.alex.greendao.TrackInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "TRACK_INFO".
*/
public class TrackInfoDao extends AbstractDao<TrackInfo, Long> {

    public static final String TABLENAME = "TRACK_INFO";

    /**
     * Properties of entity TrackInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property TrackId = new Property(0, Long.class, "trackId", true, "TRACK_ID");
        public final static Property TrackUUID = new Property(1, String.class, "trackUUID", false, "TRACK_UUID");
        public final static Property TrackName = new Property(2, String.class, "trackName", false, "TRACK_NAME");
        public final static Property StartTime = new Property(3, long.class, "startTime", false, "START_TIME");
        public final static Property EndTime = new Property(4, long.class, "endTime", false, "END_TIME");
        public final static Property TotalTime = new Property(5, long.class, "totalTime", false, "TOTAL_TIME");
        public final static Property TotalDis = new Property(6, double.class, "totalDis", false, "TOTAL_DIS");
        public final static Property AverageSpeed = new Property(7, double.class, "averageSpeed", false, "AVERAGE_SPEED");
        public final static Property MaxSpeed = new Property(8, double.class, "maxSpeed", false, "MAX_SPEED");
        public final static Property ClimbUp = new Property(9, double.class, "climbUp", false, "CLIMB_UP");
        public final static Property ClimbDown = new Property(10, double.class, "climbDown", false, "CLIMB_DOWN");
        public final static Property MaxSlope = new Property(11, double.class, "maxSlope", false, "MAX_SLOPE");
        public final static Property MinSlope = new Property(12, double.class, "minSlope", false, "MIN_SLOPE");
        public final static Property Calorie = new Property(13, double.class, "calorie", false, "CALORIE");
        public final static Property StartLat = new Property(14, double.class, "startLat", false, "START_LAT");
        public final static Property StartLon = new Property(15, double.class, "startLon", false, "START_LON");
        public final static Property StartGeoCode = new Property(16, String.class, "startGeoCode", false, "START_GEO_CODE");
        public final static Property EndLat = new Property(17, double.class, "endLat", false, "END_LAT");
        public final static Property EndLon = new Property(18, double.class, "endLon", false, "END_LON");
        public final static Property EndGeoCode = new Property(19, String.class, "endGeoCode", false, "END_GEO_CODE");
        public final static Property ImageUrl = new Property(20, String.class, "imageUrl", false, "IMAGE_URL");
        public final static Property Device = new Property(21, String.class, "device", false, "DEVICE");
        public final static Property Status = new Property(22, int.class, "status", false, "STATUS");
    };


    public TrackInfoDao(DaoConfig config) {
        super(config);
    }
    
    public TrackInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"TRACK_INFO\" (" + //
                "\"TRACK_ID\" INTEGER PRIMARY KEY ASC AUTOINCREMENT ," + // 0: trackId
                "\"TRACK_UUID\" TEXT NOT NULL ," + // 1: trackUUID
                "\"TRACK_NAME\" TEXT," + // 2: trackName
                "\"START_TIME\" INTEGER NOT NULL ," + // 3: startTime
                "\"END_TIME\" INTEGER NOT NULL ," + // 4: endTime
                "\"TOTAL_TIME\" INTEGER NOT NULL ," + // 5: totalTime
                "\"TOTAL_DIS\" REAL NOT NULL ," + // 6: totalDis
                "\"AVERAGE_SPEED\" REAL NOT NULL ," + // 7: averageSpeed
                "\"MAX_SPEED\" REAL NOT NULL ," + // 8: maxSpeed
                "\"CLIMB_UP\" REAL NOT NULL ," + // 9: climbUp
                "\"CLIMB_DOWN\" REAL NOT NULL ," + // 10: climbDown
                "\"MAX_SLOPE\" REAL NOT NULL ," + // 11: maxSlope
                "\"MIN_SLOPE\" REAL NOT NULL ," + // 12: minSlope
                "\"CALORIE\" REAL NOT NULL ," + // 13: calorie
                "\"START_LAT\" REAL NOT NULL ," + // 14: startLat
                "\"START_LON\" REAL NOT NULL ," + // 15: startLon
                "\"START_GEO_CODE\" TEXT," + // 16: startGeoCode
                "\"END_LAT\" REAL NOT NULL ," + // 17: endLat
                "\"END_LON\" REAL NOT NULL ," + // 18: endLon
                "\"END_GEO_CODE\" TEXT," + // 19: endGeoCode
                "\"IMAGE_URL\" TEXT," + // 20: imageUrl
                "\"DEVICE\" TEXT," + // 21: device
                "\"STATUS\" INTEGER NOT NULL );"); // 22: status
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"TRACK_INFO\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, TrackInfo entity) {
        stmt.clearBindings();
 
        Long trackId = entity.getTrackId();
        if (trackId != null) {
            stmt.bindLong(1, trackId);
        }
        stmt.bindString(2, entity.getTrackUUID());
 
        String trackName = entity.getTrackName();
        if (trackName != null) {
            stmt.bindString(3, trackName);
        }
        stmt.bindLong(4, entity.getStartTime());
        stmt.bindLong(5, entity.getEndTime());
        stmt.bindLong(6, entity.getTotalTime());
        stmt.bindDouble(7, entity.getTotalDis());
        stmt.bindDouble(8, entity.getAverageSpeed());
        stmt.bindDouble(9, entity.getMaxSpeed());
        stmt.bindDouble(10, entity.getClimbUp());
        stmt.bindDouble(11, entity.getClimbDown());
        stmt.bindDouble(12, entity.getMaxSlope());
        stmt.bindDouble(13, entity.getMinSlope());
        stmt.bindDouble(14, entity.getCalorie());
        stmt.bindDouble(15, entity.getStartLat());
        stmt.bindDouble(16, entity.getStartLon());
 
        String startGeoCode = entity.getStartGeoCode();
        if (startGeoCode != null) {
            stmt.bindString(17, startGeoCode);
        }
        stmt.bindDouble(18, entity.getEndLat());
        stmt.bindDouble(19, entity.getEndLon());
 
        String endGeoCode = entity.getEndGeoCode();
        if (endGeoCode != null) {
            stmt.bindString(20, endGeoCode);
        }
 
        String imageUrl = entity.getImageUrl();
        if (imageUrl != null) {
            stmt.bindString(21, imageUrl);
        }
 
        String device = entity.getDevice();
        if (device != null) {
            stmt.bindString(22, device);
        }
        stmt.bindLong(23, entity.getStatus());
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public TrackInfo readEntity(Cursor cursor, int offset) {
        TrackInfo entity = new TrackInfo( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // trackId
            cursor.getString(offset + 1), // trackUUID
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // trackName
            cursor.getLong(offset + 3), // startTime
            cursor.getLong(offset + 4), // endTime
            cursor.getLong(offset + 5), // totalTime
            cursor.getDouble(offset + 6), // totalDis
            cursor.getDouble(offset + 7), // averageSpeed
            cursor.getDouble(offset + 8), // maxSpeed
            cursor.getDouble(offset + 9), // climbUp
            cursor.getDouble(offset + 10), // climbDown
            cursor.getDouble(offset + 11), // maxSlope
            cursor.getDouble(offset + 12), // minSlope
            cursor.getDouble(offset + 13), // calorie
            cursor.getDouble(offset + 14), // startLat
            cursor.getDouble(offset + 15), // startLon
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // startGeoCode
            cursor.getDouble(offset + 17), // endLat
            cursor.getDouble(offset + 18), // endLon
            cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19), // endGeoCode
            cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20), // imageUrl
            cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21), // device
            cursor.getInt(offset + 22) // status
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, TrackInfo entity, int offset) {
        entity.setTrackId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTrackUUID(cursor.getString(offset + 1));
        entity.setTrackName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setStartTime(cursor.getLong(offset + 3));
        entity.setEndTime(cursor.getLong(offset + 4));
        entity.setTotalTime(cursor.getLong(offset + 5));
        entity.setTotalDis(cursor.getDouble(offset + 6));
        entity.setAverageSpeed(cursor.getDouble(offset + 7));
        entity.setMaxSpeed(cursor.getDouble(offset + 8));
        entity.setClimbUp(cursor.getDouble(offset + 9));
        entity.setClimbDown(cursor.getDouble(offset + 10));
        entity.setMaxSlope(cursor.getDouble(offset + 11));
        entity.setMinSlope(cursor.getDouble(offset + 12));
        entity.setCalorie(cursor.getDouble(offset + 13));
        entity.setStartLat(cursor.getDouble(offset + 14));
        entity.setStartLon(cursor.getDouble(offset + 15));
        entity.setStartGeoCode(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setEndLat(cursor.getDouble(offset + 17));
        entity.setEndLon(cursor.getDouble(offset + 18));
        entity.setEndGeoCode(cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19));
        entity.setImageUrl(cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20));
        entity.setDevice(cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21));
        entity.setStatus(cursor.getInt(offset + 22));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(TrackInfo entity, long rowId) {
        entity.setTrackId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(TrackInfo entity) {
        if(entity != null) {
            return entity.getTrackId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
