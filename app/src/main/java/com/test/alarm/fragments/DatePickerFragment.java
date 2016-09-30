package com.test.alarm.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.widget.DatePicker;

import java.util.Calendar;


/**
 * Created by kostya on 15.08.2016.
 */
public class DatePickerFragment extends DialogFragment {
    private Fragment callbackFragment;

    public void setCallbackFragment(Fragment callbackFragment) {
        this.callbackFragment = callbackFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) callbackFragment, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {

    }
}
