package com.nsu.ferdouszislam.cse486l.sec01.dateTimePickerApp.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import com.nsu.ferdouszislam.cse486l.sec01.dateTimePickerApp.R;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    private ITimePicker mITimePicker;

    public TimePickerFragment(ITimePicker mITimePicker) {
        this.mITimePicker = mITimePicker;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        mITimePicker.usePickedTime(hourOfDay, minute);
    }

    public interface ITimePicker{

        void usePickedTime(int hourOfDay, int minute);
    }

}