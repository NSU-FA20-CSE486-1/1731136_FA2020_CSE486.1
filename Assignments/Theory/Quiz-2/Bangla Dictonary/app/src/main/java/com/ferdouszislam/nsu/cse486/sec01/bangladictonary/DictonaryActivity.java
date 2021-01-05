package com.ferdouszislam.nsu.cse486.sec01.bangladictonary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.ferdouszislam.nsu.cse486.sec01.bangladictonary.models.DictonaryItem;
import com.ferdouszislam.nsu.cse486.sec01.bangladictonary.recyclerviewAdapter.DictonaryItemAdapter;

import java.util.ArrayList;

public class DictonaryActivity extends AppCompatActivity implements DictonaryItemAdapter.DeleteItemClickListener {

    // key to pass modified items list (if deleted) to MainActivity
    public static final String MODIFIED_DICT_ITEMS_KEY = "com.ferdouszislam.nsu.cse486.sec01.bangladictonary-modified_dict_items";

    // model
    private ArrayList<DictonaryItem> mDictonaryItems;

    // ui
    private RecyclerView mDictonaryItemsRecyclerView;
    private DictonaryItemAdapter mDictonaryItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictonary);

        init();
    }

    private void init() {

        mDictonaryItems = (ArrayList<DictonaryItem>) getIntent().getSerializableExtra(MainActivity.DICT_ITEM_LIST_KEY);

        mDictonaryItemsRecyclerView = findViewById(R.id.dictonary_RecyclerView);
        mDictonaryItemAdapter = new DictonaryItemAdapter(mDictonaryItems, this);

        mDictonaryItemsRecyclerView.setAdapter(mDictonaryItemAdapter);
        mDictonaryItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * on delete item from recycler view listener
     * @param dictonaryItem item to be removed
     * @param position position of item in the arraylist
     */
    @Override
    public void onDeleteItemClick(DictonaryItem dictonaryItem, int position) {

        mDictonaryItems.remove(position);
        mDictonaryItemAdapter.notifyItemRemoved(position);
    }

    @Override
    public void onBackPressed() {

        // pass the modified dictonary list to MainActivity
        Intent intent = new Intent();
        intent.putExtra(MODIFIED_DICT_ITEMS_KEY, mDictonaryItems);
        setResult(RESULT_OK, intent);

        // MUST CALL THIS LAST
        // call to super finishes the activity
        super.onBackPressed();
    }
}