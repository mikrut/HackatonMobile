package ru.bmstu.iu6.hackatonmobile.database;

import android.provider.BaseColumns;

/**
 * Created by mikrut on 05.12.15.
 */
public class MetroReaderContract {
    private MetroReaderContract() {}

    public static abstract class MetroEntry implements BaseColumns {
        public static final String TABLE_NAME = "metro_entries";
        public static final String COLUMN_NAME_STATION_NAME = "station_name";

        public static final String COLUMN_NAME_MIN_TIME_H = "min_time_h";
        public static final String COLUMN_NAME_MIN_TIME_M = "min_time_m";

        public static final String COLUMN_NAME_MAX_TIME_H = "max_time_h";
        public static final String COLUMN_NAME_MAX_TIME_M = "max_time_m";

        public static final String COLUMN_NAME_UPDATED = "updated_time";
        public static final String COLUMN_NAME_FOUND = "found_time";
    }
}
