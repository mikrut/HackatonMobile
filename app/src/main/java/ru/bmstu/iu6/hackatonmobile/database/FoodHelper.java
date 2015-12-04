package ru.bmstu.iu6.hackatonmobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import ru.bmstu.iu6.hackatonmobile.database.FoodReaderContract.FoodEntry;
import ru.bmstu.iu6.hackatonmobile.models.FoodModel;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * Created by mikrut on 04.12.15.
 */
public class FoodHelper {
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

    private DBHelper dbHelper;

    public FoodHelper(Context context) {
        dbHelper = new DBHelper(context);
    }

    @NonNull
    public Cursor getFoodCursor() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        return db.query (
                FoodEntry.TABLE_NAME,
                FOOD_PROJECTION,
                null, // All alarms
                null, // WHERE
                null, // GROUP BY
                null, // Filter groups
                null // Sort order
        );
    }

    public long saveFoodRecord(@NonNull FoodModel food) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FoodEntry.COLUMN_NAME_MAX_PRICE, food.getMaxPrice());

        return db.insert(FoodEntry.TABLE_NAME, null, values);
    }

    @NonNull
    public ArrayList<FoodModel> getFoods() {
        ArrayList<FoodModel> foods = new ArrayList<>();
        Cursor cursor = getFoodCursor();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            FoodModel food = new FoodModel();
            food.setValues(cursor);
            foods.add(food);
        }

        return foods;
    }

    @NonNull
    public void deleteFood(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = FoodEntry._ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };
        db.delete(FoodEntry.TABLE_NAME, selection, selectionArgs);
    }

    @Nullable
    public FoodModel findFoodModel(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = FoodEntry._ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };
        Cursor c = db.query (
                FoodEntry.TABLE_NAME,
                FOOD_PROJECTION,
                selection,
                selectionArgs, // WHERE
                null, // GROUP BY
                null, // Filter groups
                null, // Sort order
                "1" // Limit 1
        );
        if (c.getCount() > 0) {
            c.moveToFirst();
            FoodModel food = new FoodModel();
            food.setValues(c);
            return food;
        } else {
            return null;
        }
    }

    public void updateFoodModel(FoodModel food) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = FoodEntry._ID + " = ?";
        String[] selectionArgs = { String.valueOf(food.getId()) };


        GregorianCalendar now = new GregorianCalendar();
        food.setUpdatedTime(now);

        ContentValues cv = food.getValues();
        db.update(FoodEntry.TABLE_NAME, cv, selection, selectionArgs);
    }
}
