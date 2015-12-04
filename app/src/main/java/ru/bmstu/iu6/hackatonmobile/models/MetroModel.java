package ru.bmstu.iu6.hackatonmobile.models;

import android.content.ContentValues;
import android.database.Cursor;

import java.text.ParseException;
import java.util.GregorianCalendar;

import ru.bmstu.iu6.hackatonmobile.database.DBHelper;
import ru.bmstu.iu6.hackatonmobile.database.FoodHelper;
import ru.bmstu.iu6.hackatonmobile.database.FoodReaderContract;
import ru.bmstu.iu6.hackatonmobile.database.MetroHelper;
import ru.bmstu.iu6.hackatonmobile.database.MetroReaderContract;

/**
 * Created by mikrut on 05.12.15.
 */
public class MetroModel implements DBModel {
    private String stationName;
    private int id;
    private GregorianCalendar updatedTime;
    private GregorianCalendar foundTime;
    private int minH;
    private int minM;

    private int maxH;
    private int maxM;

    public MetroModel(){}

    public void setValues(Cursor cursor) {
        setId(cursor.getInt(MetroHelper.PROJECTION_ID_INDEX));
        setStationName(cursor.getString(MetroHelper.PROJECTION_STATION_NAME_INDEX));

        try {
            GregorianCalendar t = new GregorianCalendar();
            java.util.Date dt = DBHelper.sdf.parse(cursor.getString(MetroHelper.PROJECTION_UPDATED_INDEX));
            t.setTime(dt);
            setUpdatedTime(t);

            dt = DBHelper.sdf.parse(cursor.getString(MetroHelper.PROJECTION_FOUND_INDEX));
            t.setTime(dt);
            setFoundTime(t);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        setMinH(cursor.getInt(MetroHelper.PROJECTION_MIN_TIME_H_INDEX));
        setMinM(cursor.getInt(MetroHelper.PROJECTION_MIN_TIME_M_INDEX));

        setMaxH(cursor.getInt(MetroHelper.PROJECTION_MAX_TIME_H_INDEX));
        setMaxM(cursor.getInt(MetroHelper.PROJECTION_MAX_TIME_M_INDEX));
    }
    
    public ContentValues getValues() {
        ContentValues v = new ContentValues();

        v.put(MetroReaderContract.MetroEntry.COLUMN_NAME_STATION_NAME, getStationName());

        v.put(MetroReaderContract.MetroEntry.COLUMN_NAME_MAX_TIME_H, getMaxH());
        v.put(MetroReaderContract.MetroEntry.COLUMN_NAME_MAX_TIME_M, getMaxM());

        v.put(MetroReaderContract.MetroEntry.COLUMN_NAME_MIN_TIME_H, getMinH());
        v.put(MetroReaderContract.MetroEntry.COLUMN_NAME_MIN_TIME_M, getMinM());

        v.put(MetroReaderContract.MetroEntry.COLUMN_NAME_FOUND, DBHelper.sdf.format(getFoundTime()));
        v.put(MetroReaderContract.MetroEntry.COLUMN_NAME_UPDATED, DBHelper.sdf.format(getUpdatedTime()));

        return v;
    }

    public int getMinH() {
        return minH;
    }

    public void setMinH(int minH) {
        this.minH = minH;
    }

    public int getMinM() {
        return minM;
    }

    public void setMinM(int minM) {
        this.minM = minM;
    }

    public int getMaxH() {
        return maxH;
    }

    public void setMaxH(int maxH) {
        this.maxH = maxH;
    }

    public int getMaxM() {
        return maxM;
    }

    public void setMaxM(int maxM) {
        this.maxM = maxM;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GregorianCalendar getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(GregorianCalendar updatedTime) {
        this.updatedTime = updatedTime;
    }

    public GregorianCalendar getFoundTime() {
        return foundTime;
    }

    public void setFoundTime(GregorianCalendar foundTime) {
        this.foundTime = foundTime;
    }
}
