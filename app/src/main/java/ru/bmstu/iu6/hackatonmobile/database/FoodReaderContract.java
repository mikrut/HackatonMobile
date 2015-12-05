package ru.bmstu.iu6.hackatonmobile.database;

import android.provider.BaseColumns;

/**
 * Created by mikrut on 04.12.15.
 */
public class FoodReaderContract {
    private FoodReaderContract() {}

    public static abstract class FoodEntry implements BaseColumns {
        public static final String TABLE_NAME = "food_entries";
        public static final String COLUMN_NAME_MAX_PRICE = "max_price";

        public static final String COLUMN_NAME_MIN_TIME_H = "min_time_h";
        public static final String COLUMN_NAME_MIN_TIME_M = "min_time_m";

        public static final String COLUMN_NAME_MAX_TIME_H = "max_time_h";
        public static final String COLUMN_NAME_MAX_TIME_M = "max_time_m";

        public static final String COLUMN_NAME_UPDATED = "updated_time";
        public static final String COLUMN_NAME_FOUND = "found_time";

        public static final String COLUMN_NAME_DAYMASK = "day_mask";
    }
}
