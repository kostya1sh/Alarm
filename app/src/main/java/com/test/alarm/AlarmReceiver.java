package com.test.alarm;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.test.alarm.db.DBManager;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by kostya on 30.09.2016.
 */

public class AlarmReceiver extends BroadcastReceiver {
    private static int id = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("AlarmReceiver", "received alarm !");
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder
                .setSmallIcon(R.mipmap.ic_launcher, 1)
                .setContentInfo(new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(intent.getExtras().getLong("date")))
                .setContentTitle(context.getResources().getString(R.string.app_name))
                .setContentText(intent.getExtras().getString("msg"));

        notificationManager.notify(id, notificationBuilder.build());

        DBManager.getInstance(context).setAlarmActivated(intent.getExtras().getLong("alarmId"), false);

        // notify activity to change data in alarm adapter
        Intent dataToActivity = new Intent("alarmActivity");
        context.sendBroadcast(dataToActivity);
        id++;
    }
}
