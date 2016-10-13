package com.test.alarm.adapters;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.test.alarm.AlarmReceiver;
import com.test.alarm.MainActivity;
import com.test.alarm.R;
import com.test.alarm.entities.AlarmEntity;
import com.test.alarm.db.DBManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by kostya on 26.09.2016.
 */

public class AlarmsAdapter extends BaseAdapter {
    private MainActivity mainActivity;
    private List<AlarmEntity> alarmEntityList;
    private List<PendingIntent> pendingIntents;

    public AlarmsAdapter(MainActivity mainActivity, List<AlarmEntity> alarmEntities) {
        this.alarmEntityList = alarmEntities;
        this.mainActivity = mainActivity;
        removeAllAlarms();
        setAlarms();
    }

    private void setAlarms() {
        pendingIntents = new ArrayList<>();
        for (AlarmEntity alarmEntity : alarmEntityList) {
            if (alarmEntity.getActivated() == 1) {
                pendingIntents.add(setAlarm(alarmEntity));
            } else {
                pendingIntents.add(null);
            }
        }
    }

    public void setAlarmEntityList(List<AlarmEntity> alarmEntityList) {
        this.alarmEntityList = alarmEntityList;
        removeAllAlarms();
        setAlarms();
        notifyDataSetChanged();
    }

    @TargetApi(24)
    private PendingIntent setAlarm(AlarmEntity alarmEntity) {
        Intent i = new Intent(mainActivity, AlarmReceiver.class);

        i.putExtra("alarmId", alarmEntity.getId());
        i.putExtra("msg", alarmEntity.getMsg());
        i.putExtra("date", alarmEntity.getDate().getTime());

        PendingIntent pi = PendingIntent.getBroadcast(mainActivity, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) mainActivity.getSystemService(Context.ALARM_SERVICE);

        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT && currentApiVersion < Build.VERSION_CODES.M){
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmEntity.getDate().getTime(), pi);
        } else if (currentApiVersion >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmEntity.getDate().getTime(), pi);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmEntity.getDate().getTime(), pi);
        }

        return pi;
    }

    private void removeAllAlarms() {
        AlarmManager alarmManager = (AlarmManager) mainActivity.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(mainActivity, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(mainActivity, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pi);
    }

    private void removeAlarm(PendingIntent pendingIntent) {
        AlarmManager alarmManager = (AlarmManager) mainActivity.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    @Override
    public int getCount() {
        return alarmEntityList.size();
    }

    @Override
    public Object getItem(int i) {
        return alarmEntityList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private static class ViewHolder {
        int position;
        ImageView imgType;
        ImageView imgDifficult;
        TextView tvTime;
        TextView tvDate;
        TextView tvDayOfWeek;
        TextView tvMsg;
        Switch switchAlarmActivated;
        LinearLayout llDivider;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mainActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_alarm_layout, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.position = i;
            viewHolder.imgType = (ImageView) view.findViewById(R.id.imgAlarmType);
            viewHolder.imgDifficult = (ImageView) view.findViewById(R.id.imgAlarmDifficult);
            viewHolder.tvTime = (TextView) view.findViewById(R.id.tvAlarmTime);
            viewHolder.tvDate = (TextView) view.findViewById(R.id.tvAlarmDate);
            viewHolder.tvDayOfWeek = (TextView) view.findViewById(R.id.tvAlarmDayOfWeek);
            viewHolder.tvMsg = (TextView) view.findViewById(R.id.tvAlarmMsg);
            viewHolder.switchAlarmActivated = (Switch) view.findViewById(R.id.switchAlarmActivated);
            viewHolder.llDivider = (LinearLayout) view.findViewById(R.id.llAlarmDivider);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        ImageView imgType = viewHolder.imgType;
        ImageView imgDifficult = viewHolder.imgDifficult;
        TextView tvTime = viewHolder.tvTime;
        TextView tvDate = viewHolder.tvDate;
        TextView tvDayOfWeek = viewHolder.tvDayOfWeek;
        TextView tvMsg = viewHolder.tvMsg;
        Switch switchAlarmActivated = viewHolder.switchAlarmActivated;
        LinearLayout llDivider = viewHolder.llDivider;

        final AlarmEntity alarmEntity = alarmEntityList.get(i);
        switch (alarmEntity.getType()) {
            case 0:
                imgType.setImageBitmap(BitmapFactory.decodeResource(mainActivity.getResources(),
                        R.drawable.expression));
                break;
            case 1:
                imgType.setImageBitmap(BitmapFactory.decodeResource(mainActivity.getResources(),
                        R.drawable.connection));
                break;
            case 2:
                imgType.setImageBitmap(BitmapFactory.decodeResource(mainActivity.getResources(),
                        R.drawable.random));
                break;
        }

        switch (alarmEntity.getDifficult()) {
            case 0:
                imgDifficult.setImageBitmap(BitmapFactory.decodeResource(mainActivity.getResources(),
                        R.drawable.low_difficult));
                break;
            case 1:
                imgDifficult.setImageBitmap(BitmapFactory.decodeResource(mainActivity.getResources(),
                        R.drawable.medium_difficult));
                break;
            case 2:
                imgDifficult.setImageBitmap(BitmapFactory.decodeResource(mainActivity.getResources(),
                        R.drawable.high_difficult));
                break;
        }

        if (alarmEntity.getActivated() == 0) {
            switchAlarmActivated.setChecked(false);
        } else {
            switchAlarmActivated.setChecked(true);
        }
        switchAlarmActivated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Switch s = (Switch) view;
                if (!s.isChecked()) {
                    DBManager.getInstance(mainActivity).setAlarmActivated(alarmEntity, false);
                    alarmEntity.setActivated(0);
                    removeAlarm(pendingIntents.get(i));
                } else {
                    DBManager.getInstance(mainActivity).setAlarmActivated(alarmEntity, true);
                    alarmEntity.setActivated(1);
                    pendingIntents.set(i, setAlarm(alarmEntity));
                }
            }
        });

        tvTime.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(alarmEntity.getDate()));
        tvDate.setText(new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(alarmEntity.getDate()));
        tvDayOfWeek.setText(new SimpleDateFormat("EEEE", Locale.getDefault()).format(alarmEntity.getDate()));
        tvMsg.setText(alarmEntity.getMsg());


        if (i == alarmEntityList.size() - 1) {
            llDivider.setVisibility(View.GONE);
        } else {
            llDivider.setVisibility(View.VISIBLE);
        }

        return view;
    }
}
