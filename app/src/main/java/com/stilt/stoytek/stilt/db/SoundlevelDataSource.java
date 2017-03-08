package com.stilt.stoytek.stilt.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.stilt.stoytek.stilt.dtypes.SoundlevelMeasurement;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by frodeja on 08/03/17.
 */

public class SoundlevelDataSource {

    private SQLiteDatabase db;
    private SoundlevelSQLiteHelper dbHelper;
    private static final String[] allColumns = {
            SoundlevelSQLiteHelper.COLUMN_ID,
            SoundlevelSQLiteHelper.COLUMN_SOUNDLEVEL,
            SoundlevelSQLiteHelper.COLUMN_TIMESTAMP
    };

    public SoundlevelDataSource(Context context) {
        dbHelper = new SoundlevelSQLiteHelper(context);
    }

    public void open() throws SQLiteException {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long insertSoundlevelMeasurement(SoundlevelMeasurement slm) {
        return 0;
    }

    public ArrayList<SoundlevelMeasurement> getSoundlevelMeasurementsFromDate(Date date) {
        return null;
    }

    public ArrayList<SoundlevelMeasurement> getSoundLevelMeasurementsFromDateRange(Date from, Date to) {
        return null;
    }

}
