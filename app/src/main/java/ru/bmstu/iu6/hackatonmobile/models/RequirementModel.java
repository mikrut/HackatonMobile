package ru.bmstu.iu6.hackatonmobile.models;

import android.content.ContentValues;
import android.database.Cursor;

import java.text.ParseException;
import java.util.GregorianCalendar;

import ru.bmstu.iu6.hackatonmobile.database.DBHelper;
import ru.bmstu.iu6.hackatonmobile.database.RequirementHelper;
import ru.bmstu.iu6.hackatonmobile.database.RequirementReaderContract.RequirementEntry;

/**
 * Created by mikrut on 04.12.15.
 */
public class RequirementModel implements DBModel {
    public static final int TYPE_FOOD = 0;
    public static final int TYPE_STORE = 1;
    public static final int TYPE_TRANSPORT = 2;
    public static final int TYPE_MEMORIAL = 3;

    private short maxPrice;
    private int id;
    private GregorianCalendar updatedTime;
    private GregorianCalendar foundTime;

    private int minH;
    private int minM;

    private int maxH;
    private int maxM;

    private byte daysMask;

    private int type;
    private String category;


    public RequirementModel() {}

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public byte getDaysMask() {
        return daysMask;
    }

    public void setDaysMask(byte daysMask) {
        this.daysMask = daysMask;
    }

    public void setValues(Cursor cursor) {
        setId(cursor.getInt(RequirementHelper.PROJECTION_ID_INDEX));
        setMaxPrice(cursor.getShort(RequirementHelper.PROJECTION_MAX_PRICE_INDEX));

        try {
            GregorianCalendar t = new GregorianCalendar();
            java.util.Date dt = DBHelper.sdf.parse(cursor.getString(RequirementHelper.PROJECTION_UPDATED_INDEX));
            t.setTime(dt);
            setUpdatedTime(t);

            dt = DBHelper.sdf.parse(cursor.getString(RequirementHelper.PROJECTION_FOUND_INDEX));
            t.setTime(dt);
            setFoundTime(t);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        setMinH(cursor.getInt(RequirementHelper.PROJECTION_MIN_TIME_H_INDEX));
        setMinM(cursor.getInt(RequirementHelper.PROJECTION_MIN_TIME_M_INDEX));

        setMaxH(cursor.getInt(RequirementHelper.PROJECTION_MAX_TIME_H_INDEX));
        setMaxM(cursor.getInt(RequirementHelper.PROJECTION_MAX_TIME_M_INDEX));

        setDaysMask((byte) cursor.getInt(RequirementHelper.PROJECTION_DAYMASK_INDEX));

        setType(cursor.getInt(RequirementHelper.PROJECTION_TYPE_INDEX));
        setCategory(cursor.getString(RequirementHelper.PROJECTION_CATEGORY_INDEX));
    }

    public ContentValues getValues() {
        ContentValues v = new ContentValues();

        v.put(RequirementEntry.COLUMN_NAME_MAX_PRICE, getMaxPrice());

        v.put(RequirementEntry.COLUMN_NAME_MAX_TIME_H, getMaxH());
        v.put(RequirementEntry.COLUMN_NAME_MAX_TIME_M, getMaxM());

        v.put(RequirementEntry.COLUMN_NAME_MIN_TIME_H, getMinH());
        v.put(RequirementEntry.COLUMN_NAME_MIN_TIME_M, getMinM());

        if (getFoundTime() != null)
            v.put(RequirementEntry.COLUMN_NAME_FOUND, DBHelper.sdf.format(getFoundTime().getTime()));
        else
            v.put(RequirementEntry.COLUMN_NAME_FOUND, (String) null);
        if (getUpdatedTime() != null)
           v.put(RequirementEntry.COLUMN_NAME_UPDATED, DBHelper.sdf.format(getUpdatedTime().getTime()));
        else
            v.put(RequirementEntry.COLUMN_NAME_UPDATED, (String) null);

        v.put(RequirementEntry.COLUMN_NAME_DAYMASK, getDaysMask());

        v.put(RequirementEntry.COLUMN_NAME_CATEGORY, getCategory());
        v.put(RequirementEntry.COLUMN_NAME_TYPE, getType());

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
