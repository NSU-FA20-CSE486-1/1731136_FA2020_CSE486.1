package com.nsu.ferdouszislam.cse486l.sec01.dateTimePickerApp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.nsu.ferdouszislam.cse486l.sec01.dateTimePickerApp.R;
import com.nsu.ferdouszislam.cse486l.sec01.dateTimePickerApp.fragments.DatePickerFragment;
import com.nsu.ferdouszislam.cse486l.sec01.dateTimePickerApp.fragments.TimePickerFragment;
import com.nsu.ferdouszislam.cse486l.sec01.dateTimePickerApp.models.Date;
import com.nsu.ferdouszislam.cse486l.sec01.dateTimePickerApp.models.Time;

public class MainActivity extends AppCompatActivity
        implements DatePickerFragment.IDatePicker, TimePickerFragment.ITimePicker {

    // ui elements
    private EditText mDatePickerEditText, mTimePickerEditText;

    // models
    private Date mDate;
    private Time mTime;

    // data passing key
    public static final String TIME_DATE_KEY = "time-date-key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {

        loadToolbar();

        initUIElements();

        initModels();
    }

    private void initUIElements(){

        mDatePickerEditText = findViewById(R.id.editTextDate);
        mTimePickerEditText = findViewById(R.id.editTextTime);

        mDatePickerEditText.setVisibility(View.GONE);
        mTimePickerEditText.setVisibility(View.GONE);
    }

    private void initModels() {

        mDate = null;
        mTime = null;
    }

    private void loadToolbar() {

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /*
    sets custom menu as toolbar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.app_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*
    Appbar option click callback
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.action_proceed:

                proceed();

                return true;

            case R.id.action_reset:

                if(mTimePickerEditText!=null){

                    mTimePickerEditText.setText("");
                    mTimePickerEditText.setVisibility(View.GONE);
                }
                if(mDatePickerEditText!=null){

                    mDatePickerEditText.setText("");
                    mDatePickerEditText.setVisibility(View.GONE);
                }

                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    /*
    time pick edit text on click callback method
     */
    public void timePickClick(View view) {

        // show time picker fragment
        DialogFragment timePickerFragment = new TimePickerFragment(this);
        timePickerFragment.show(getSupportFragmentManager(), getString(R.string.timePickerDialogTag));
    }

    /*
    Time picked callback
     */
    @Override
    public void usePickedTime(int hourOfDay, int minute) {

        mTime = new Time(hourOfDay, minute);

        mTimePickerEditText.setText(mTime.toString());
        mTimePickerEditText.setVisibility(View.VISIBLE);
    }

    /*
    date pick edit text on click callback method
     */
    public void datePickClick(View view) {

        // show date picker fragment
        DialogFragment datePickerFragment = new DatePickerFragment(this);
        datePickerFragment.show(getSupportFragmentManager(), getString(R.string.datePickerDialogTag));
    }

    /*
    Date picked callback
     */
    @Override
    public void usePickedDate(int year, int month, int dayOfMonth) {

        mDate = new Date(month, dayOfMonth, year);
        mDatePickerEditText.setText(mDate.toString());
        mDatePickerEditText.setVisibility(View.VISIBLE);
    }

    /*
    proceed with selected date time values to ShowDateTimeActivity
     */
    private void proceed() {

        if(validDateAndTimeSelected()){
            // open next activity passing the selected date & time data

            Intent intent = new Intent(MainActivity.this, ShowDateTimeActivity.class);

            String showableDateTime = mTime.toString() + " " +mDate.toString();
            intent.putExtra(TIME_DATE_KEY, showableDateTime);

            startActivity(intent);
        }

        else showInvalidInputError();
    }

    /*
    validate selected date & time
     */
    private boolean validDateAndTimeSelected() {

        return mDate!=null && mTime!=null
                && Date.isValidDate(mDate.getDd(), mDate.getMm(), mDate.getYyyy())
                && Time.isValidTime(mTime.getHh(), mTime.getMm());
    }

    /*
    Notify user of error date/time input
     */
    private void showInvalidInputError() {

        Toast.makeText(this, R.string.invalid_dateTime_input_error, Toast.LENGTH_LONG).show();
    }

}