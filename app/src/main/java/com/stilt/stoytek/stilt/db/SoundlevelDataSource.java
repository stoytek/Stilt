package com.stilt.stoytek.stilt.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.stilt.stoytek.stilt.dtypes.SoundlevelMeasurement;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Random;

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
        db = null; /* Make db ready for garbage collection */
    }

    /**
     * Inserts the given sound level measurement into the database.
     * @param slm Sound level measurement object to be inserted.
     * @return A long specifying the automatic column id of the new row.
     */

    public long insertSoundlevelMeasurement(SoundlevelMeasurement slm) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SoundlevelSQLiteHelper.COLUMN_SOUNDLEVEL, slm.getdBval());
        contentValues.put(SoundlevelSQLiteHelper.COLUMN_TIMESTAMP, slm.getTimestampMillis());
        return db.insert(SoundlevelSQLiteHelper.TABLE_SOUNDLEVELS, null, contentValues);
    }

    /**
     * Fetches all sound level measurements from the 24-hour window specified by the given date.
     * Discards all HOUR_OF_DAY, MINUTE, SECOND, and MILLISECOND information in the given date.
     * @param date The date to fetch results from.
     * @return ArrayList of sound level measurements found at give date.
     */

    public ArrayList<SoundlevelMeasurement> getSoundlevelMeasurementsFromDate(GregorianCalendar date) {
        long start, end;
        /* Set date to <DATE> 00:00:00 */
        date.set(GregorianCalendar.HOUR_OF_DAY, 0);
        date.set(GregorianCalendar.MINUTE, 0);
        date.set(GregorianCalendar.SECOND, 0);
        date.set(GregorianCalendar.MILLISECOND, 0);

        start = date.getTimeInMillis();

        /* Move time forward one day */
        date.add(GregorianCalendar.DATE, 1);
        end = date.getTimeInMillis();

        Cursor cursor = db.query(
        /* Table */         SoundlevelSQLiteHelper.TABLE_SOUNDLEVELS,
        /* Columns */       allColumns,
        /* WHERE */         SoundlevelSQLiteHelper.COLUMN_TIMESTAMP + ">=" + String.valueOf(start) +
                            " AND " +
                            SoundlevelSQLiteHelper.COLUMN_TIMESTAMP + "<" + String.valueOf(end),
        /* SelectionArgs */ null,
        /* GROUP BY */      null,
        /* HAVING */        null,
        /* ORDER BY */      SoundlevelSQLiteHelper.COLUMN_TIMESTAMP + " ASC");

        ArrayList<SoundlevelMeasurement> results = new ArrayList<SoundlevelMeasurement>();

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            results.add(cursorToSoundLevelMeasurement(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return results;
    }

    /**
     * Fetches all sound level measurements taken in the 24 hours before the specified date.
     * @param windowEnd End of 24 hour window.
     * @return ArrayList of all sound level measurements found in the 24 hour window.
     */

    public ArrayList<SoundlevelMeasurement> getSoundlevelMeasurementsFrom24HourWindowBeforeDate(GregorianCalendar windowEnd) {
        GregorianCalendar windowStart = new GregorianCalendar();
        windowStart.setTimeInMillis(windowEnd.getTimeInMillis());
        windowStart.add(GregorianCalendar.DATE, -1); //Subtract 24 hours from date
        return getSoundLevelMeasurementsFromPreciseDateRange(windowStart, windowEnd);
    }

    /**
     * Fetches all sound level measurements in a specified date range. Disregards the HOUR_OF_DAY,
     * MINUTE, SECOND, and MILLISECOND fields of the specified date.
     * @param from Start of range (inclusive).
     * @param to End of range (exclusive).
     * @return ArrayList of all sound level measurements found in the date range.
     */

    public ArrayList<SoundlevelMeasurement> getSoundLevelMeasurementsFromDateRange(GregorianCalendar from, GregorianCalendar to) {
        long start, end;
        /* Set date to <DATE> 00:00:00 */
        from.set(GregorianCalendar.HOUR_OF_DAY, 0);
        to.set(GregorianCalendar.HOUR_OF_DAY, 0);
        from.set(GregorianCalendar.MINUTE, 0);
        to.set(GregorianCalendar.MINUTE, 0);
        from.set(GregorianCalendar.SECOND, 0);
        to.set(GregorianCalendar.SECOND, 0);
        from.set(GregorianCalendar. MILLISECOND, 0);
        to.set(GregorianCalendar. MILLISECOND, 0);
        start = from.getTimeInMillis();
        end = to.getTimeInMillis();

        Cursor cursor = db.query(
        /* Table */         SoundlevelSQLiteHelper.TABLE_SOUNDLEVELS,
        /* Columns */       allColumns,
        /* WHERE */         SoundlevelSQLiteHelper.COLUMN_TIMESTAMP + ">=" + String.valueOf(start) +
                        " AND " +
                        SoundlevelSQLiteHelper.COLUMN_TIMESTAMP + "<" + String.valueOf(end),
        /* SelectionArgs */ null,
        /* GROUP BY */      null,
        /* HAVING */        null,
        /* ORDER BY */      SoundlevelSQLiteHelper.COLUMN_TIMESTAMP + " ASC");

        ArrayList<SoundlevelMeasurement> results = new ArrayList<SoundlevelMeasurement>();

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            results.add(cursorToSoundLevelMeasurement(cursor));
            cursor.moveToNext();
        }

        cursor.close();
        return results;
    }

    /**
     * Returns sound level measurements from a certain date range, keeping the HOUR_OF_DAY,
     * MINUTE, SECOND, and MILLISECOND field of the specified dates intact, giving a precise
     * date range to fetch results from.
     * @param from Start of range (inclusive).
     * @param to End of range (exclusive).
     * @return ArrayList of sound level measurements found in the given range.
     */
    public ArrayList<SoundlevelMeasurement> getSoundLevelMeasurementsFromPreciseDateRange(GregorianCalendar from, GregorianCalendar to) {
        long start, end;

        start = from.getTimeInMillis();
        end = to.getTimeInMillis();

        Cursor cursor = db.query(
        /* Table */         SoundlevelSQLiteHelper.TABLE_SOUNDLEVELS,
        /* Columns */       allColumns,
        /* WHERE */         SoundlevelSQLiteHelper.COLUMN_TIMESTAMP + ">=" + String.valueOf(start) +
                        " AND " +
                        SoundlevelSQLiteHelper.COLUMN_TIMESTAMP + "<" + String.valueOf(end),
        /* SelectionArgs */ null,
        /* GROUP BY */      null,
        /* HAVING */        null,
        /* ORDER BY */      SoundlevelSQLiteHelper.COLUMN_TIMESTAMP + " ASC");

        ArrayList<SoundlevelMeasurement> results = new ArrayList<SoundlevelMeasurement>();

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            results.add(cursorToSoundLevelMeasurement(cursor));
            cursor.moveToNext();
        }

        cursor.close();
        return results;
    }

    /**
     * Converts a result row into a SoundlevelMeasurement object.
     * @param cursor DB query result reference.
     * @return SoundlevelMeasurement based on contents of result row
     */

    private SoundlevelMeasurement cursorToSoundLevelMeasurement(Cursor cursor) {
        SoundlevelMeasurement slm = new SoundlevelMeasurement();
        slm.setdBval(cursor.getDouble(cursor.getColumnIndex(SoundlevelSQLiteHelper.COLUMN_SOUNDLEVEL)));
        GregorianCalendar tmp = new GregorianCalendar();
        tmp.setTimeInMillis(cursor.getLong(cursor.getColumnIndex(SoundlevelSQLiteHelper.COLUMN_TIMESTAMP)));
        slm.setTimestamp(tmp);
        return slm;
    }

    /**
     * Generates random data for the last 24 hours.
     * @return Array of random SoundlevelMeasurements
     */

    private ArrayList<SoundlevelMeasurement> getRandomData() {
        Random rand = new Random();
        ArrayList<SoundlevelMeasurement> result = new ArrayList<SoundlevelMeasurement>();
        long now = System.currentTimeMillis();
        GregorianCalendar timestamp = new GregorianCalendar();
        timestamp.setTimeInMillis(now);
        timestamp.add(GregorianCalendar.DATE, -1);
        while (timestamp.getTimeInMillis() < now) {
            GregorianCalendar localTimestamp = new GregorianCalendar();
            localTimestamp.setTimeInMillis(timestamp.getTimeInMillis());
            result.add(new SoundlevelMeasurement(rand.nextDouble()*94, localTimestamp));
            timestamp.add(GregorianCalendar.MINUTE, 10);
        }

        return result;
    }

}
