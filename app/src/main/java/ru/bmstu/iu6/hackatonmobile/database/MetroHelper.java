package ru.bmstu.iu6.hackatonmobile.database;

import android.content.Context;
import ru.bmstu.iu6.hackatonmobile.models.MetroModel;

/**
 * Created by mikrut on 05.12.15.
 */
public class MetroHelper extends TableHelper<MetroModel> {
    private final static String COMMA_SEP = ",";
    final static String CREATE_TABLE =
            "CREATE TABLE " + MetroReaderContract.MetroEntry.TABLE_NAME + "(" +
                    MetroReaderContract.MetroEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                    MetroReaderContract.MetroEntry.COLUMN_NAME_STATION_NAME + " TEXT NOT NULL" + COMMA_SEP +
                    MetroReaderContract.MetroEntry.COLUMN_NAME_MAX_TIME_H + " INTEGER NOT NULL" + COMMA_SEP +
                    MetroReaderContract.MetroEntry.COLUMN_NAME_MAX_TIME_M + " INTEGER NOT NULL" + COMMA_SEP +
                    MetroReaderContract.MetroEntry.COLUMN_NAME_MIN_TIME_H + " INTEGER NOT NULL" + COMMA_SEP +
                    MetroReaderContract.MetroEntry.COLUMN_NAME_MIN_TIME_M + " INTEGER NOT NULL" + COMMA_SEP +
                    MetroReaderContract.MetroEntry.COLUMN_NAME_FOUND + " DATETIME" + COMMA_SEP +
                    MetroReaderContract.MetroEntry.COLUMN_NAME_UPDATED + " DATETIME";

    final static String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MetroReaderContract.MetroEntry.TABLE_NAME;

    protected String[] getProjection() {
        return METRO_PROJECTION;
    }

    protected String getTableName() {
        return MetroReaderContract.MetroEntry.TABLE_NAME;
    }

    protected String getIdColumnName() {
        return MetroReaderContract.MetroEntry._ID;
    }

    final static String[] METRO_PROJECTION = {
            MetroReaderContract.MetroEntry._ID,
            MetroReaderContract.MetroEntry.COLUMN_NAME_STATION_NAME,
            MetroReaderContract.MetroEntry.COLUMN_NAME_MAX_TIME_M,
            MetroReaderContract.MetroEntry.COLUMN_NAME_MAX_TIME_M,
            MetroReaderContract.MetroEntry.COLUMN_NAME_MIN_TIME_H,
            MetroReaderContract.MetroEntry.COLUMN_NAME_MIN_TIME_M,
            MetroReaderContract.MetroEntry.COLUMN_NAME_FOUND,
            MetroReaderContract.MetroEntry.COLUMN_NAME_UPDATED
    };

    public static final int PROJECTION_ID_INDEX = 0;
    public static final int PROJECTION_STATION_NAME_INDEX = 1;
    public static final int PROJECTION_MAX_TIME_H_INDEX = 2;
    public static final int PROJECTION_MAX_TIME_M_INDEX = 3;
    public static final int PROJECTION_MIN_TIME_H_INDEX = 4;
    public static final int PROJECTION_MIN_TIME_M_INDEX = 5;
    public static final int PROJECTION_FOUND_INDEX = 6;
    public static final int PROJECTION_UPDATED_INDEX = 7;

    public MetroHelper(Context context) {
        super(context, MetroModel.class);
    }
}
