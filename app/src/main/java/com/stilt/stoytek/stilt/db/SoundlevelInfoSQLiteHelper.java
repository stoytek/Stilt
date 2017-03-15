package com.stilt.stoytek.stilt.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
    private static final String DB_ASSET_NAME =  "slinf_db.db";

    private String DB_PATH;
    private Context context;

    public SoundlevelInfoSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        DB_PATH = context.getDatabasePath(DATABASE_NAME).getPath();
        this.context = context;

        /* See if the database exists, if not copy it from assets */
        if (!context.getDatabasePath(DATABASE_NAME).exists()) {
            try {
                copyDBFromAssets();
            } catch (IOException e) {
                Log.wtf("SoundlevelInfoSQLiteHelper", e);
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        context.deleteDatabase(DATABASE_NAME);
        try {
            copyDBFromAssets();
        } catch (IOException e) {
            Log.wtf("SoundlevelInfoSQLiteHelper", e);
        }
    }

    private void copyDBFromAssets() throws IOException {
        /* Get input and output files as streams */
        /* TODO: Check if we need to prefix dbname with 'databases/' */
        InputStream input = context.getAssets().open(DB_ASSET_NAME);
        OutputStream output = new FileOutputStream(context.getDatabasePath(DATABASE_NAME));

        /* A buffer to read into and write from */
        byte[] buffer = new byte[1024];
        int length;

        /*
            Read into buffer while there is still data to be read in the input stream
            and write from buffer to the output stream.
         */
        while ((length = input.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }

        /* Flush and close streams */
        output.flush();
        output.close();
        input.close();
    }

}
