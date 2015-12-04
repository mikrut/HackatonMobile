package ru.bmstu.iu6.hackatonmobile.database;

import android.content.Context;
import ru.bmstu.iu6.hackatonmobile.database.FoodReaderContract.FoodEntry;
import ru.bmstu.iu6.hackatonmobile.models.FoodModel;

/**
 * Created by mikrut on 04.12.15.
 */
public class FoodHelper extends TableHelper<FoodModel> {
    private final static String COMMA_SEP = ",";
    final static String CREATE_TABLE =
            "CREATE TABLE " + FoodEntry.TABLE_NAME + "(" +
            FoodEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
            FoodEntry.COLUMN_NAME_MAX_PRICE + " SMALLINT NOT NULL" + COMMA_SEP +
            FoodEntry.COLUMN_NAME_MAX_TIME_H + " INTEGER NOT NULL" + COMMA_SEP +
            FoodEntry.COLUMN_NAME_MAX_TIME_M + " INTEGER NOT NULL" + COMMA_SEP +
            FoodEntry.COLUMN_NAME_MIN_TIME_H + " INTEGER NOT NULL" + COMMA_SEP +
            FoodEntry.COLUMN_NAME_MIN_TIME_M + " INTEGER NOT NULL" + COMMA_SEP +
            FoodEntry.COLUMN_NAME_FOUND + " DATETIME" + COMMA_SEP +
            FoodEntry.COLUMN_NAME_UPDATED + " DATETIME";

    final static String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FoodEntry.TABLE_NAME;

    @Override
    protected String[] getProjection() {
        return FOOD_PROJECTION;
    }

    @Override
    protected String getTableName() {
        return FoodEntry.TABLE_NAME;
    }

    @Override
    protected String getIdColumnName() {
        return FoodEntry._ID;
    }

    final static String[] FOOD_PROJECTION = {
            FoodEntry._ID,
            FoodEntry.COLUMN_NAME_MAX_PRICE,
            FoodEntry.COLUMN_NAME_MAX_TIME_M,
            FoodEntry.COLUMN_NAME_MAX_TIME_M,
            FoodEntry.COLUMN_NAME_MIN_TIME_H,
            FoodEntry.COLUMN_NAME_MIN_TIME_M,
            FoodEntry.COLUMN_NAME_FOUND,
            FoodEntry.COLUMN_NAME_UPDATED
    };

    public static final int PROJECTION_ID_INDEX = 0;
    public static final int PROJECTION_MAX_PRICE_INDEX = 1;
    public static final int PROJECTION_MAX_TIME_H_INDEX = 2;
    public static final int PROJECTION_MAX_TIME_M_INDEX = 3;
    public static final int PROJECTION_MIN_TIME_H_INDEX = 4;
    public static final int PROJECTION_MIN_TIME_M_INDEX = 5;
    public static final int PROJECTION_FOUND_INDEX = 6;
    public static final int PROJECTION_UPDATED_INDEX = 7;


    public FoodHelper(Context context) {
        super(context, FoodModel.class);
    }
}
