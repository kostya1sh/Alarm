package com.test.alarm.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;


/**
 * Created by kostya on 15.08.2016.
 */
public class TimePickerFragment extends DialogFragment {
    private Fragment callbackFragment;

    public void setCallbackFragment(Fragment callbackFragment) {
        this.callbackFragment = callbackFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), (TimePickerDialog.OnTimeSetListener) callbackFragment, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

}
