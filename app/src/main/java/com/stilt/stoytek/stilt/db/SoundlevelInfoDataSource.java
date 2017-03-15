package com.stilt.stoytek.stilt.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by frodeja on 08/03/17.
 */

public class SoundlevelInfoDataSource {

    private SQLiteDatabase db;
    private SoundlevelInfoSQLiteHelper dbHelper;

    private static final String[] allColumns = {
            SoundlevelInfoSQLiteHelper.COLUMN_ID,
            SoundlevelInfoSQLiteHelper.COLUMN_TEXT,
            SoundlevelInfoSQLiteHelper.COLUMN_SOUNDLEVEL
    };

    public SoundlevelInfoDataSource(Context context) {
        /* TODO: Add a static method in SoundlevelInfoSQLiteHelper to check if DB exists */
        dbHelper = new SoundlevelInfoSQLiteHelper(context);
    }

    public void open() {
        db = dbHelper.getReadableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public String getFunFact(double eqdBLevel) {
        return "<placeholder>";
    }


}
