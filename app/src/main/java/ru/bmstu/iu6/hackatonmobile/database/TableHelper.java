package ru.bmstu.iu6.hackatonmobile.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.GregorianCalendar;

import ru.bmstu.iu6.hackatonmobile.models.DBModel;

/**
 * Created by mikrut on 05.12.15.
 */
public abstract class TableHelper<Model extends DBModel> {
    protected DBHelper dbHelper;
    private Class<Model> modelClass;

    public TableHelper(Context context, Class<Model> modelClass) {
        dbHelper = new DBHelper(context);
        this.modelClass = modelClass;
    }

    protected abstract String[] getProjection();
    protected abstract String getTableName();
    protected abstract String getIdColumnName();

    @NonNull
    public Cursor getModelCursor() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        return db.query(
                getTableName(),
                getProjection(),
                null, // All alarms
                null, // WHERE
                null, // GROUP BY
                null, // Filter groups
                null // Sort order
        );
    }

    public long saveModel(@NonNull Model model) {
        Log.e("save model", "save model");
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        GregorianCalendar now = new GregorianCalendar();
        model.setUpdatedTime(now);
        ContentValues values = model.getValues();
        return db.insert(getTableName(), null, values);
    }

    protected ArrayList<Model> cursorToModelList(Cursor cursor) {
        ArrayList<Model> models = new ArrayList<>();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            try {
                Model metro = modelClass.newInstance();
                metro.setValues(cursor);
                models.add(metro);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return models;
    }

    @NonNull
    public ArrayList<Model> getModels() {
        Cursor cursor = getModelCursor();
        return cursorToModelList(cursor);
    }

    @NonNull
    public void deleteModel(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = getIdColumnName() + " = ?";
        String[] selectionArgs = { String.valueOf(id) };
        db.delete(getTableName(), selection, selectionArgs);
    }

    @Nullable
    public Model findModel(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = getIdColumnName() + " = ?";
        String[] selectionArgs = { String.valueOf(id) };
        Cursor c = db.query (
                getTableName(),
                getProjection(),
                selection,
                selectionArgs, // WHERE
                null, // GROUP BY
                null, // Filter groups
                null, // Sort order
                "1" // Limit 1
        );
        if (c.getCount() > 0) {
            c.moveToFirst();
            Model model = null;
            try {
                model = modelClass.newInstance();
                model.setValues(c);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } finally {
                return model;
            }
        } else {
            return null;
        }
    }

    public void updateModel(Model model) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = getIdColumnName() + " = ?";
        String[] selectionArgs = { String.valueOf(model.getId()) };


        GregorianCalendar now = new GregorianCalendar();
        model.setUpdatedTime(now);

        ContentValues cv = model.getValues();
        db.update(getTableName(), cv, selection, selectionArgs);
    }
}
