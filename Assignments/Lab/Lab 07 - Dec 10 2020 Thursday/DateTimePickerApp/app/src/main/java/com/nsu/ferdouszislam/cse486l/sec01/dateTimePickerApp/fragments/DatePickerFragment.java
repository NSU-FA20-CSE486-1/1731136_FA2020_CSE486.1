package com.nsu.ferdouszislam.cse486l.sec01.dateTimePickerApp.fragments;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    // calling activity
    private IDatePicker mIDatePicker;

    public DatePickerFragment(IDatePicker mIDatePicker) {
        this.mIDatePicker = mIDatePicker;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker.
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it.
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        mIDatePicker.usePickedDate(year, month, dayOfMonth);
    }

    /*
    Interface for using picked date and time
     */
    public interface IDatePicker{

        void usePickedDate(int year, int month, int dayOfMonth);
    }
}