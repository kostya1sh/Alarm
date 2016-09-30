package com.test.alarm.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.test.alarm.entities.AlarmEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by kostya on 26.09.2016.
 */

public class DBManager {
    private static DBManager sDBManager;
    private SQLiteDatabase mDatabase;

    private DBManager(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        mDatabase = dbHelper.getWritableDatabase();
    }

    public static DBManager getInstance(Context context) {
        if (sDBManager == null) {
            sDBManager = new DBManager(context);
        }
        return sDBManager;
    }

    private boolean checkCursor(Cursor cursor) {
        return cursor != null && cursor.getCount() > 0 && cursor.getColumnCount() > 0;
    }

    private ContentValues getContentValues(AlarmEntity alarmEntity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBSchema.AlarmTable.Cols.DATE, alarmEntity.getDate().getTime());
        contentValues.put(DBSchema.AlarmTable.Cols.MSG, alarmEntity.getMsg());
        contentValues.put(DBSchema.AlarmTable.Cols.ACTIVATED, alarmEntity.getActivated());
        contentValues.put(DBSchema.AlarmTable.Cols.DIFFICULT, alarmEntity.getDifficult());
        contentValues.put(DBSchema.AlarmTable.Cols.TYPE, alarmEntity.getType());
        return contentValues;
    }

    public List<AlarmEntity> getAll() {
        List<AlarmEntity> alarmEntities = new ArrayList<>();
        Cursor cursor = mDatabase.query(
                DBSchema.AlarmTable.NAME,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if (checkCursor(cursor)) {
            cursor.moveToFirst();
            do {
                AlarmEntity alarmEntity = new AlarmEntity();
                alarmEntity.setId(cursor.getLong(cursor.getColumnIndex(DBSchema.AlarmTable.Cols.ID)));
                alarmEntity.setDate(new Date(cursor.getLong(cursor.getColumnIndex(DBSchema.AlarmTable.Cols.DATE))));
                alarmEntity.setMsg(cursor.getString(cursor.getColumnIndex(DBSchema.AlarmTable.Cols.MSG)));
                alarmEntity.setActivated(cursor.getInt(cursor.getColumnIndex(DBSchema.AlarmTable.Cols.ACTIVATED)));
                alarmEntity.setDifficult(cursor.getInt(cursor.getColumnIndex(DBSchema.AlarmTable.Cols.DIFFICULT)));
                alarmEntity.setType(cursor.getInt(cursor.getColumnIndex(DBSchema.AlarmTable.Cols.TYPE)));

                alarmEntities.add(alarmEntity);
            } while (cursor.moveToNext());
        }

        return alarmEntities;
    }

    public Long save(AlarmEntity alarmEntity) {
        ContentValues contentValues = getContentValues(alarmEntity);
        return mDatabase.insert(DBSchema.AlarmTable.NAME, null, contentValues);
    }

    public void setAlarmActivated(AlarmEntity alarmActivated, boolean isActivated) {
        if (alarmActivated.getId() != null) {
            ContentValues contentValues = new ContentValues();
            if (isActivated) {
                contentValues.put(DBSchema.AlarmTable.Cols.ACTIVATED, 1);
            } else {
                contentValues.put(DBSchema.AlarmTable.Cols.ACTIVATED, 0);
            }
            mDatabase.update(
                    DBSchema.AlarmTable.NAME,
                    contentValues,
                    DBSchema.AlarmTable.Cols.ID + "=?",
                    new String[]{alarmActivated.getId().toString()}
            );
        }
    }

    public void setAlarmActivated(Long id, boolean isActivated) {
        if (id != null) {
            ContentValues contentValues = new ContentValues();
            if (isActivated) {
                contentValues.put(DBSchema.AlarmTable.Cols.ACTIVATED, 1);
            } else {
                contentValues.put(DBSchema.AlarmTable.Cols.ACTIVATED, 0);
            }
            mDatabase.update(
                    DBSchema.AlarmTable.NAME,
                    contentValues,
                    DBSchema.AlarmTable.Cols.ID + "=?",
                    new String[]{id.toString()}
            );
        }
    }

    public void deleteAlarm(Long id) {
        mDatabase.delete(
                DBSchema.AlarmTable.NAME,
                DBSchema.AlarmTable.Cols.ID + "=?",
                new String[]{id.toString()}
        );
    }

}
