package ru.bmstu.iu6.hackatonmobile.models;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.GregorianCalendar;

/**
 * Created by mikrut on 05.12.15.
 */
public interface DBModel {
    public void setValues(Cursor cursor);
    public ContentValues getValues();

    public void setUpdatedTime(GregorianCalendar calendar);
    public int getId();
}
