package com.ferdouszislam.nsu.cse486.sec01.bangladictonary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.ferdouszislam.nsu.cse486.sec01.bangladictonary.models.DictonaryItem;

import java.util.ArrayList;

public class DictonaryActivity extends AppCompatActivity {

    // model
    private ArrayList<DictonaryItem> mDictonaryItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictonary);

        init();
    }

    private void init() {

        mDictonaryItems = (ArrayList<DictonaryItem>) getIntent().getSerializableExtra(MainActivity.DICT_ITEM_LIST_KEY);


    }
}