package com.stilt.stoytek.stilt.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by frodeja on 08/03/17.
 */

public class SoundlevelInfoSQLiteHelper extends SQLiteOpenHelper {
    public static final String TABLE_SLINFO = "slinfo";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_SOUNDLEVEL = "soundlevel";

    private static final String DATABASE_NAME = "slinfo_db";
    private static final int DATABASE_VERSION = 1;

    public SoundlevelInfoSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /* TODO: Copy the database from the assets folder */
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /* TODO: Delete the database file */
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SLINFO);
        onCreate(db);
    }
}
