package com.test.alarm.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kostya on 26.09.2016.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final int VERSION = 1;
    public static final String DATABASE_NAME = "/data/data/com.test.alarm/alarm.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // enable foreign keys
        sqLiteDatabase.execSQL("PRAGMA foreign_keys=ON;");

        // alarm table
        sqLiteDatabase.execSQL(
                "create table " + DBSchema.AlarmTable.NAME + "(" +
                        DBSchema.AlarmTable.Cols.ID + " integer primary key autoincrement, " +
                        DBSchema.AlarmTable.Cols.DATE + " date not null, " +
                        DBSchema.AlarmTable.Cols.MSG + " text not null, " +
                        DBSchema.AlarmTable.Cols.ACTIVATED + " integer default 0, " +
                        DBSchema.AlarmTable.Cols.TYPE+ " integer default 0, " +
                        DBSchema.AlarmTable.Cols.DIFFICULT + " integer default 0" + ")"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }
}
