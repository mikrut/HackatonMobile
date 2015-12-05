package ru.bmstu.iu6.hackatonmobile.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import ru.bmstu.iu6.hackatonmobile.database.RequirementReaderContract.RequirementEntry;
import ru.bmstu.iu6.hackatonmobile.models.RequirementModel;

/**
 * Created by mikrut on 04.12.15.
 */
public class RequirementHelper extends TableHelper<RequirementModel> {
    private final static String COMMA_SEP = ",";
    final static String CREATE_TABLE =
            "CREATE TABLE " + RequirementEntry.TABLE_NAME + "(" +
            RequirementEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
            RequirementEntry.COLUMN_NAME_MAX_PRICE  + " INTEGER NOT NULL" + COMMA_SEP +
            RequirementEntry.COLUMN_NAME_MAX_TIME_H + " INTEGER NOT NULL" + COMMA_SEP +
            RequirementEntry.COLUMN_NAME_MAX_TIME_M + " INTEGER NOT NULL" + COMMA_SEP +
            RequirementEntry.COLUMN_NAME_MIN_TIME_H + " INTEGER NOT NULL" + COMMA_SEP +
            RequirementEntry.COLUMN_NAME_MIN_TIME_M + " INTEGER NOT NULL" + COMMA_SEP +
            RequirementEntry.COLUMN_NAME_DAYMASK    + " INTEGER NOT NULL" + COMMA_SEP +
            RequirementEntry.COLUMN_NAME_CATEGORY   + " TEXT NOT NULL"    + COMMA_SEP +
            RequirementEntry.COLUMN_NAME_TYPE       + " INTEGER NOT NULL" + COMMA_SEP +
            RequirementEntry.COLUMN_NAME_FOUND + " DATETIME" + COMMA_SEP +
            RequirementEntry.COLUMN_NAME_UPDATED + " DATETIME" + ")";

    final static String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + RequirementEntry.TABLE_NAME;

    @Override
    protected String[] getProjection() {
        return FOOD_PROJECTION;
    }

    @Override
    protected String getTableName() {
        return RequirementEntry.TABLE_NAME;
    }

    @Override
    protected String getIdColumnName() {
        return RequirementEntry._ID;
    }

    final static String[] FOOD_PROJECTION = {
            RequirementEntry._ID,
            RequirementEntry.COLUMN_NAME_MAX_PRICE,
            RequirementEntry.COLUMN_NAME_MAX_TIME_M,
            RequirementEntry.COLUMN_NAME_MAX_TIME_M,
            RequirementEntry.COLUMN_NAME_MIN_TIME_H,
            RequirementEntry.COLUMN_NAME_MIN_TIME_M,
            RequirementEntry.COLUMN_NAME_FOUND,
            RequirementEntry.COLUMN_NAME_UPDATED,
            RequirementEntry.COLUMN_NAME_DAYMASK,
            RequirementEntry.COLUMN_NAME_TYPE,
            RequirementEntry.COLUMN_NAME_CATEGORY
    };

    public static final int PROJECTION_ID_INDEX = 0;
    public static final int PROJECTION_MAX_PRICE_INDEX = 1;
    public static final int PROJECTION_MAX_TIME_H_INDEX = 2;
    public static final int PROJECTION_MAX_TIME_M_INDEX = 3;
    public static final int PROJECTION_MIN_TIME_H_INDEX = 4;
    public static final int PROJECTION_MIN_TIME_M_INDEX = 5;
    public static final int PROJECTION_FOUND_INDEX = 6;
    public static final int PROJECTION_UPDATED_INDEX = 7;
    public static final int PROJECTION_DAYMASK_INDEX = 8;
    public static final int PROJECTION_TYPE_INDEX = 9;
    public static final int PROJECTION_CATEGORY_INDEX = 10;


    public RequirementHelper(Context context) {
        super(context, RequirementModel.class);
    }

    public Cursor getByType(int type) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = RequirementEntry.COLUMN_NAME_TYPE + " = ?";
        String[] selectionArgs = {String.valueOf(type)};

        return db.query(
                getTableName(),
                getProjection(),
                selection, // All alarms
                selectionArgs, // WHERE
                null, // GROUP BY
                null, // Filter groups
                null // Sort order
        );
    }

    public ArrayList<RequirementModel> getModelByType(int type) {
        return cursorToModelList(getByType(type));
    }

    public ArrayList<RequirementModel> getFoods() {
        return cursorToModelList(getByType(RequirementModel.TYPE_FOOD));
    }

    public ArrayList<RequirementModel> getStores() {
        return cursorToModelList(getByType(RequirementModel.TYPE_STORE));
    }

    public ArrayList<RequirementModel> getTransports() {
        return cursorToModelList(getByType(RequirementModel.TYPE_TRANSPORT));
    }

    public ArrayList<RequirementModel> getMemorials() {
        return cursorToModelList(getByType(RequirementModel.TYPE_MEMORIAL));
    }
}
