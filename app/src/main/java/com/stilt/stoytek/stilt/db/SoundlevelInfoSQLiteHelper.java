package com.stilt.stoytek.stilt.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by frodeja on 08/03/17.
 */

public class SoundlevelInfoSQLiteHelper extends SQLiteAssetHelper{
    public static final String TABLE_SLINFO = "slinfo";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_SOUNDLEVEL = "soundlevel";

    private static final String DATABASE_NAME = "slinfo_db.db";
    private static final int DATABASE_VERSION = 1;

    public SoundlevelInfoSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, null, DATABASE_VERSION);
        setForcedUpgrade();
    }
}
