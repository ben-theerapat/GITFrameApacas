package com.admin.gitframeapacas.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DBFavorite extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "myFavorite.db";
    public static final String CONTACTS_TABLE_NAME = "my_favorite";
    public static final String CONTACTS_COLUMN_SNAME = "sname";
    public static final String CONTACTS_COLUMN_SCODE = "scode";
    public static final String CONTACTS_COLUMN_AQI = "aqi";
    public static final String CONTACTS_COLUMN_CO = "co";
    public static final String CONTACTS_COLUMN_NO2 = "no2";
    public static final String CONTACTS_COLUMN_O3 = "o3";
    public static final String CONTACTS_COLUMN_SO2 = "so2";
    public static final String CONTACTS_COLUMN_PM25 = "pm25";
    public static final String CONTACTS_COLUMN_RAD = "rad";
    public static final String CONTACTS_COLUMN_TSTAMP = "tstamp";



    public DBFavorite(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table " + CONTACTS_TABLE_NAME +
                        "(" +
                        CONTACTS_COLUMN_SNAME + " text," +
                        CONTACTS_COLUMN_SCODE + " integer," +
                        CONTACTS_COLUMN_AQI + " text," +
                        CONTACTS_COLUMN_CO + " text," +
                        CONTACTS_COLUMN_NO2 + " text," +
                        CONTACTS_COLUMN_O3 + " text," +
                        CONTACTS_COLUMN_SO2 + " text," +
                        CONTACTS_COLUMN_PM25 + " text," +
                        CONTACTS_COLUMN_RAD + " text," +
                        CONTACTS_COLUMN_TSTAMP + " text" +
                        ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + CONTACTS_TABLE_NAME);
        onCreate(db);
    }

    public void drop() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CONTACTS_TABLE_NAME, null, null);
    }

    public boolean insertData(String sname, String scode, String aqi, String co, String no2, String o3, String so2, String pm25, String rad, String tstamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_COLUMN_SNAME, sname);
        contentValues.put(CONTACTS_COLUMN_SCODE, scode);
        contentValues.put(CONTACTS_COLUMN_AQI, aqi);
        contentValues.put(CONTACTS_COLUMN_CO, co);
        contentValues.put(CONTACTS_COLUMN_NO2, no2);
        contentValues.put(CONTACTS_COLUMN_O3, o3);
        contentValues.put(CONTACTS_COLUMN_SO2, so2);
        contentValues.put(CONTACTS_COLUMN_PM25, pm25);
        contentValues.put(CONTACTS_COLUMN_RAD, rad);
        contentValues.put(CONTACTS_COLUMN_TSTAMP, tstamp);
        db.insert(CONTACTS_TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
        return numRows;
    }
    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + CONTACTS_TABLE_NAME, null);
        return res;
        /*
            how to use
          Cursor res = dbGrid.getAllData();
                if (res.getCount() == 0) {
                    Log.i("griddata", "Nothing found");
                } else {

                    while (res.moveToNext()) {
                        String sName = res.getString(0);
                    }

         */
    }

    public long UpdateData(String scode, String sname, String aqi, String co, String no2, String o3, String so2, String pm25, String rad, String tstamp) {
        try {
            SQLiteDatabase db;
            db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(CONTACTS_COLUMN_SNAME, sname);
            contentValues.put(CONTACTS_COLUMN_SCODE, scode);
            contentValues.put(CONTACTS_COLUMN_AQI, aqi);
            contentValues.put(CONTACTS_COLUMN_CO, co);
            contentValues.put(CONTACTS_COLUMN_NO2, no2);
            contentValues.put(CONTACTS_COLUMN_O3, o3);
            contentValues.put(CONTACTS_COLUMN_SO2, so2);
            contentValues.put(CONTACTS_COLUMN_PM25, pm25);
            contentValues.put(CONTACTS_COLUMN_RAD, rad);
            contentValues.put(CONTACTS_COLUMN_TSTAMP, tstamp);
            long rows = db.update(CONTACTS_TABLE_NAME, contentValues, " scode = ?",
                    new String[]{String.valueOf(scode)});

            db.close();
            return rows; // return rows updated.

        } catch (Exception e) {
            return -1;
        }

    }

    public void deleteData(String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + CONTACTS_TABLE_NAME + " WHERE " + CONTACTS_COLUMN_SCODE + "='" + value + "'");
        db.close();
    }

}