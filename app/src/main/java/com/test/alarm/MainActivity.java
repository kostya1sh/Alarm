package com.test.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.test.alarm.adapters.AlarmsAdapter;
import com.test.alarm.entities.AlarmEntity;
import com.test.alarm.db.DBManager;
import com.test.alarm.fragments.SetAlarmFragment;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public static final String SET_ALARM_FRAG_TAG = "setAlarmFragment";

    private TextView actionBarTitle;
    private ImageButton backBtn;
    private ListView lvAlarms;
    private String currentTitle = "";
    private boolean isFabVisible = true;
    private boolean isBackBtnVisible = false;
    private List<AlarmEntity> alarmEntities = new ArrayList<>();
    private AlarmsAdapter alarmsAdapter;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            currentTitle = savedInstanceState.getString("title", "");
            isBackBtnVisible = savedInstanceState.getBoolean("backVis", true);
            isFabVisible = savedInstanceState.getBoolean("fabVis", false);
        } else {
            currentTitle = getResources().getString(R.string.app_name);
            isBackBtnVisible = false;
            isFabVisible = true;
        }

        initActionBar(currentTitle, isBackBtnVisible);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.btnAddAlarm);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SetAlarmFragment fragment = new SetAlarmFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.activity_main, fragment, SET_ALARM_FRAG_TAG).commit();
                setFabVisibility(false);
                setActionBarTitle(getResources().getString(R.string.set_alarm_fragment));
            }
        });

        initAlarmList();

        registerReceiver(broadcastReceiver, new IntentFilter("alarmActivity"));
    }

    // update listview if receive intent
    BroadcastReceiver broadcastReceiver =  new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("Updater", "received update intent!");
            notifyAlarmDataChanged();
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("title", currentTitle);
        outState.putBoolean("fabVis", isFabVisible);
        outState.putBoolean("backVis", isBackBtnVisible);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setFabVisibility(isFabVisible);
        setBackBtnVisibility(isBackBtnVisible);
        setActionBarTitle(currentTitle);
        registerReceiver(broadcastReceiver, new IntentFilter("alarmActivity"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (!fragments.isEmpty()) {
            for (Fragment fragment : fragments) {
                if (fragment != null) {
                    fragmentManager.beginTransaction().remove(fragment).commit();
                }
            }
            setActionBarTitle(getResources().getString(R.string.app_name));
            setBackBtnVisibility(false);
        } else {
            super.onBackPressed();
        }
    }

    private void initAlarmList() {
        lvAlarms = (ListView) findViewById(R.id.lvAlarms);
        alarmEntities = DBManager.getInstance(this).getAll();
        if (!alarmEntities.isEmpty()) {
            TextView tvNoAlarms = (TextView) findViewById(R.id.tvNoAlarms);
            tvNoAlarms.setVisibility(View.GONE);
        }
        alarmsAdapter = new AlarmsAdapter(this, alarmEntities);
        lvAlarms.setAdapter(alarmsAdapter);
    }

    /**
     * Synchronize data in db and listview
     */
    public void notifyAlarmDataChanged() {
        if (alarmsAdapter != null) {
            alarmEntities = DBManager.getInstance(this).getAll();
            alarmsAdapter.setAlarmEntityList(alarmEntities);
            TextView tvNoAlarms = (TextView) findViewById(R.id.tvNoAlarms);
            tvNoAlarms.setVisibility(View.GONE);
        }
    }

    public void initActionBar(String title, boolean isBackVisible) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View actionBarView = layoutInflater.inflate(R.layout.action_bar_layout, null);
            actionBarTitle = (TextView) actionBarView.findViewById(R.id.abTitle);
            actionBarTitle.setText(title);
            currentTitle = title;
            backBtn = (ImageButton) actionBarView.findViewById(R.id.abBtnBack);
            if (isBackVisible) {
                backBtn.setVisibility(View.VISIBLE);
                isBackBtnVisible = true;
            } else {
                backBtn.setVisibility(View.INVISIBLE);
                isBackBtnVisible = false;
            }
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setCustomView(actionBarView);
            actionBar.setDisplayShowCustomEnabled(true);
        }
    }

    public void setActionBarTitle(String title) {
        actionBarTitle.setText(title);
        currentTitle = title;
    }

    public void setFabVisibility(boolean isVisible) {
        if (isVisible) {
            floatingActionButton.setVisibility(View.VISIBLE);
            isFabVisible = true;
        } else {
            floatingActionButton.setVisibility(View.GONE);
            isFabVisible = false;
        }
    }

    public void setBackBtnVisibility(boolean isVisible) {
        if (isVisible) {
            backBtn.setVisibility(View.VISIBLE);
            isBackBtnVisible = true;
        } else {
            backBtn.setVisibility(View.INVISIBLE);
            isBackBtnVisible = false;
        }
    }

    public void setBackBtnOnClickListener(View.OnClickListener onClickListener) {
        backBtn.setOnClickListener(onClickListener);
    }

    public void setDifficult(int difficult) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        SetAlarmFragment setAlarmFragment = (SetAlarmFragment) fragmentManager.findFragmentByTag(SET_ALARM_FRAG_TAG);
        setAlarmFragment.setDifficult(difficult);
    }

    public void setType(int type) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        SetAlarmFragment setAlarmFragment = (SetAlarmFragment) fragmentManager.findFragmentByTag(SET_ALARM_FRAG_TAG);
        setAlarmFragment.setType(type);
    }

    public int getDifficult() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        SetAlarmFragment setAlarmFragment = (SetAlarmFragment) fragmentManager.findFragmentByTag(SET_ALARM_FRAG_TAG);
        return setAlarmFragment.getDifficult();
    }

    public int getType() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        SetAlarmFragment setAlarmFragment = (SetAlarmFragment) fragmentManager.findFragmentByTag(SET_ALARM_FRAG_TAG);
        return setAlarmFragment.getType();
    }

}
