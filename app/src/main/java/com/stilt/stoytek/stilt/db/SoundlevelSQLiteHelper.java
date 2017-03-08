package com.stilt.stoytek.stilt.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by frodeja on 08/03/17.
 */

public class SoundlevelSQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_SOUNDLEVELS = "soundlevels";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_SOUNDLEVEL = "soundlevel";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private static final String DATABASE_NAME = "soundlevels";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "create table " +
            TABLE_SOUNDLEVELS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_SOUNDLEVEL + " REAL, " +
            COLUMN_TIMESTAMP + " LONG NOT NULL);";

    public SoundlevelSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SOUNDLEVELS);
        onCreate(db);
    }
}
