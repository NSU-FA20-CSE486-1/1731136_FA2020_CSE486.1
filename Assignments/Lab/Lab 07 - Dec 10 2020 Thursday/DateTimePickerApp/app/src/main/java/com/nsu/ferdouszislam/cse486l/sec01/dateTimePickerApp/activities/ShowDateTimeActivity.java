package com.nsu.ferdouszislam.cse486l.sec01.dateTimePickerApp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.nsu.ferdouszislam.cse486l.sec01.dateTimePickerApp.R;

public class ShowDateTimeActivity extends AppCompatActivity {

    // ui elements
    private TextView mPickedDateTimeTextView;

    // data
    private String mPickedTimeAndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_date_time);

        init();
    }

    private void init() {

        mPickedDateTimeTextView = findViewById(R.id.dateTimeTextView);

        mPickedTimeAndDate = getIntent().getStringExtra(MainActivity.TIME_DATE_KEY);

        mPickedDateTimeTextView.setText(mPickedTimeAndDate);
    }

}