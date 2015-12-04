package ru.bmstu.iu6.hackatonmobile.models;

import android.content.ContentValues;
import android.database.Cursor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

import ru.bmstu.iu6.hackatonmobile.database.DBHelper;
import ru.bmstu.iu6.hackatonmobile.database.FoodHelper;
import ru.bmstu.iu6.hackatonmobile.database.FoodReaderContract.FoodEntry;

/**
 * Created by mikrut on 04.12.15.
 */
public class FoodModel {
    private short maxPrice;
    private int id;
    private GregorianCalendar updatedTime;
    private GregorianCalendar foundTime;

    private int minH;
    private int minM;

    private int maxH;
    private int maxM;


    public FoodModel() {}

    public void setValues(Cursor cursor) {
        setId(cursor.getInt(FoodHelper.PROJECTION_ID_INDEX));
        setMaxPrice(cursor.getShort(FoodHelper.PROJECTION_MAX_PRICE_INDEX));

        try {
            GregorianCalendar t = new GregorianCalendar();
            java.util.Date dt = DBHelper.sdf.parse(cursor.getString(FoodHelper.PROJECTION_UPDATED_INDEX));
            t.setTime(dt);
            setUpdatedTime(t);

            dt = DBHelper.sdf.parse(cursor.getString(FoodHelper.PROJECTION_FOUND_INDEX));
            t.setTime(dt);
            setFoundTime(t);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        setMinH(cursor.getInt(FoodHelper.PROJECTION_MIN_TIME_H_INDEX));
        setMinM(cursor.getInt(FoodHelper.PROJECTION_MIN_TIME_M_INDEX));

        setMaxH(cursor.getInt(FoodHelper.PROJECTION_MAX_TIME_H_INDEX));
        setMaxM(cursor.getInt(FoodHelper.PROJECTION_MAX_TIME_M_INDEX));
    }

    public ContentValues getValues() {
        ContentValues v = new ContentValues();

        v.put(FoodEntry.COLUMN_NAME_MAX_PRICE, getMaxPrice());

        v.put(FoodEntry.COLUMN_NAME_MAX_TIME_H, getMaxH());
        v.put(FoodEntry.COLUMN_NAME_MAX_TIME_M, getMaxM());

        v.put(FoodEntry.COLUMN_NAME_MIN_TIME_H, getMinH());
        v.put(FoodEntry.COLUMN_NAME_MIN_TIME_M, getMinM());

        v.put(FoodEntry.COLUMN_NAME_FOUND, DBHelper.sdf.format(getFoundTime()));
        v.put(FoodEntry.COLUMN_NAME_UPDATED, DBHelper.sdf.format(getUpdatedTime()));

        return v;
    }

    public short getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(short maxPrice) {
        this.maxPrice = maxPrice;
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

}
