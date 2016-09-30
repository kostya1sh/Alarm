package com.test.alarm.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.test.alarm.MainActivity;
import com.test.alarm.R;
import com.test.alarm.entities.AlarmEntity;
import com.test.alarm.db.DBManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by kostya on 26.09.2016.
 */

public class SetAlarmFragment extends Fragment
        implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private int day;
    private int month;
    private int year;
    private int hour;
    private int minute;
    private String msg = "";
    private int type;
    private int difficult;

    private TextView tvDatePicker;
    private TextView tvTimePicker;
    private RelativeLayout rlTypeAndDifficultOption;
    private EditText etMsg;
    private Button btnSave;

    private MainActivity mainActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainActivity = (MainActivity) getActivity();

        View rootView = inflater.inflate(R.layout.set_alarm_layout, container, false);

        restoreInstanceState(savedInstanceState);

        initMsgView(rootView);
        initTypeAndDifficultOption(rootView);
        initDatePicker(rootView);
        initTimePicker(rootView);
        initSaveButton(rootView);

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        mainActivity.setFabVisibility(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        mainActivity.setFabVisibility(false);
    }

    private void restoreInstanceState(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            year = savedInstanceState.getInt("year", 0);
            month = savedInstanceState.getInt("month", 0);
            day = savedInstanceState.getInt("day", 0);
            hour = savedInstanceState.getInt("hour", 0);
            minute = savedInstanceState.getInt("minute", 0);
            type = savedInstanceState.getInt("type", 0);
            difficult = savedInstanceState.getInt("difficult", 0);
            msg = savedInstanceState.getString("msg", "");

        }
    }

    private void initSaveButton(View rootView) {
        btnSave = (Button) rootView.findViewById(R.id.btnSave);


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.setFabVisibility(true);
                mainActivity.setActionBarTitle(getResources().getString(R.string.app_name));
                AlarmEntity alarmEntity = new AlarmEntity();
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day, hour, minute);

                alarmEntity.setDate(calendar.getTime());
                alarmEntity.setActivated(1);
                alarmEntity.setMsg(msg);
                alarmEntity.setDifficult(difficult);
                alarmEntity.setType(type);

                DBManager.getInstance(getContext()).save(alarmEntity);
                mainActivity.notifyAlarmDataChanged();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(SetAlarmFragment.this).commit();
            }
        });
    }

    private void initTypeAndDifficultOption(View rootView) {
        rlTypeAndDifficultOption = (RelativeLayout) rootView.findViewById(R.id.rlOptionTypeAndDifficult);

        rlTypeAndDifficultOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainActivity.setBackBtnVisibility(true);
                mainActivity.setActionBarTitle(getResources().getString(R.string.type_and_difficult_fragment));
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                TypeAndDifficultFragment fragment = new TypeAndDifficultFragment();
                fragmentManager.beginTransaction().add(R.id.activity_main, fragment).commit();
            }
        });


    }

    private void initTimePicker(View rootView) {
        tvTimePicker = (TextView) rootView.findViewById(R.id.tvTimePick);

        Calendar calendar = Calendar.getInstance();
        if (hour > 0 && minute > 0) {
            calendar.set(hour, minute);
        } else {
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
        }

        tvTimePicker.setText(new SimpleDateFormat("HH:mm     ", Locale.getDefault()).format(calendar.getTime()));

        tvTimePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                TimePickerFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.setCallbackFragment(SetAlarmFragment.this);
                timePickerFragment.show(fragmentManager, "timePicker");
            }
        });
    }

    private void initDatePicker(View rootView) {
        tvDatePicker = (TextView) rootView.findViewById(R.id.tvDatePick);
        Calendar calendar = Calendar.getInstance();
        if (year > 0 && month > 0 && day > 0) {
            calendar.set(year, month, day);
        } else {
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }
        tvDatePicker.setText(new SimpleDateFormat("EEE, MMM dd, yyyy", Locale.getDefault()).format(calendar.getTime()));

        tvDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                DatePickerFragment datePickerFragment = new DatePickerFragment();
                datePickerFragment.setCallbackFragment(SetAlarmFragment.this);
                datePickerFragment.show(fragmentManager, "datePicker");
            }
        });

    }

    private void initMsgView(View rootView) {
        etMsg = (EditText) rootView.findViewById(R.id.etAlarmDescription);

        if (msg != null && msg.length() > 0) {
            etMsg.setText(msg);
        }
        etMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                msg = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("year", year);
        outState.putInt("month", month);
        outState.putInt("day", day);
        outState.putInt("hour", hour);
        outState.putInt("minute", minute);
        outState.putInt("type", type);
        outState.putInt("difficult", difficult);
        outState.putString("msg", msg);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        tvDatePicker.setText(new SimpleDateFormat("EEE, MMM dd, yyyy", Locale.getDefault()).format(calendar.getTime()));
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        this.hour = hourOfDay;
        this.minute = minute;
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hourOfDay, minute);
        tvTimePicker.setText(new SimpleDateFormat("HH:mm     ", Locale.getDefault()).format(calendar.getTime()));
    }

    public void setDifficult(int difficult) {
        this.difficult = difficult;
    }

    public int getDifficult() {
        return difficult;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
