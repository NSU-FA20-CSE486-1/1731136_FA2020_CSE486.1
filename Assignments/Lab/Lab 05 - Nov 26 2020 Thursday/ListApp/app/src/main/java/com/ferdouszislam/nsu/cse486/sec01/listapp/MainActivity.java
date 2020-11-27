package com.ferdouszislam.nsu.cse486.sec01.listapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ferdouszislam.nsu.cse486.sec01.listapp.adapter.ISingleItemUserInteraction;
import com.ferdouszislam.nsu.cse486.sec01.listapp.adapter.ItemsAdapter;
import com.ferdouszislam.nsu.cse486.sec01.listapp.models.AllowedItemChoice;
import com.ferdouszislam.nsu.cse486.sec01.listapp.models.ImageResource;
import com.ferdouszislam.nsu.cse486.sec01.listapp.models.Item;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ISingleItemUserInteraction {
// controller for storing picked list

    private static final String TAG = "MainActivity-debug";

    // value passing key
    public static final String CHOSEN_ITEM_NAMES_KEY = "chosen-items-key";
    private static final int RECEIVE_SELECTED_ITEM_NAMES_KEY = 254;

    // model
    private ArrayList<Item> mChosenItems = new ArrayList<>();

    // list empty or not flag
    private boolean isListEmpty = true;

    // savedInstanceState keys
    private static final String LIST_EMPTY_FLAG_KEY = "list-empty-flag-key";
    private static final String SAVED_ITEM_NAMES_KEY = "saved-item-names-key";
    private static final String SAVED_ITEM_ICONS_KEY = "saved-item-icons-key";

    // ui
    private TextView tvListEmptyTeller;
    private RecyclerView rvChosenItems;
    private ItemsAdapter adapterChosenItems;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
    // save list data when instance is destroyed by screen rotation

        super.onSaveInstanceState(outState);

        outState.putBoolean(LIST_EMPTY_FLAG_KEY, isListEmpty);

        ArrayList<String> savedItemNames = new ArrayList<>();
        ArrayList<Integer> savedItemIcons = new ArrayList<>();
        for(Item item: mChosenItems){
            savedItemNames.add(item.getLabel());
            savedItemIcons.add(item.getImageResource().getResource_id());
        }

        outState.putStringArrayList(SAVED_ITEM_NAMES_KEY, savedItemNames);
        outState.putIntegerArrayList(SAVED_ITEM_ICONS_KEY, savedItemIcons);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
    // restore data stored in savedInstanceState
        super.onRestoreInstanceState(savedInstanceState);

        isListEmpty = savedInstanceState.getBoolean(LIST_EMPTY_FLAG_KEY, true);

        if(!isListEmpty) {

            if(tvListEmptyTeller==null)
                tvListEmptyTeller = findViewById(R.id.listEmptyTeller_TextView);

            tvListEmptyTeller.setVisibility(View.GONE);
        }

        loadSavedList(savedInstanceState);

    }

    private void loadSavedList(Bundle savedInstanceState) {
    // load list's save data on screen rotation

        ArrayList<String> savedItemNames = savedInstanceState.getStringArrayList(SAVED_ITEM_NAMES_KEY);
        ArrayList<Integer> savedItemIcons = savedInstanceState.getIntegerArrayList(SAVED_ITEM_ICONS_KEY);

        for(int i=0;i<savedItemNames.size();i++){

            String name = savedItemNames.get(i);
            int icon = savedItemIcons.get(i);

            Item item = new Item(name, new ImageResource(icon));

            addItemToModelAndUI(item);
        }

    }

    private void init() {
    // initialise UI components

        // init UI
        tvListEmptyTeller = findViewById(R.id.listEmptyTeller_TextView);
        rvChosenItems = findViewById(R.id.chosenItemList_RecyclerView);
        // Create adapter passing in the sample user data
        adapterChosenItems = new ItemsAdapter(mChosenItems, this);
        // Attach the adapter to the recyclerview to populate items
        rvChosenItems.setAdapter(adapterChosenItems);
        // Set layout manager to position the items
        rvChosenItems.setLayoutManager(new LinearLayoutManager(this));
    }


    private void addItemToModelAndUI(Item item) {
    // add item to model and update the UI

        mChosenItems.add(item);

        // update UI
        if(adapterChosenItems==null)
            init();
        adapterChosenItems.notifyItemInserted(mChosenItems.size()-1);
        isListEmpty = false;
        if(tvListEmptyTeller!=null)
            tvListEmptyTeller.setVisibility(View.GONE);
    }


    public void addItemClick(View view) {
    // launch activity to add new item
    // on that activity pass already selected items so they are not shown

        ArrayList<String> chosenItemNames = new ArrayList<>();
        for (Item item : mChosenItems){
            chosenItemNames.add(item.getLabel());
        }

        Intent intent = new Intent(this, SelectItemActivity.class);
        intent.putStringArrayListExtra(CHOSEN_ITEM_NAMES_KEY, chosenItemNames);

        startActivityForResult(intent, RECEIVE_SELECTED_ITEM_NAMES_KEY);
    }

    @Override
    public void onItemNameButtonClick(Item item) {
    // single item on list is clicked
    // open URL in browser
    // according to of item label

        // Get the URL text.
        String url = AllowedItemChoice.getUrl(item.getLabel());

        Log.d(TAG, "onItemNameButtonClick: opening url = "+url);

        // Parse the URI and create the intent.
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);

        // Find an activity to hand the intent and start that activity.
        if(intent.resolveActivity(getPackageManager())!=null)
            startActivity(intent);
        else {
            showToast("No browser app found!");
            Log.d(TAG, "onItemNameButtonClick: cannot open URL, no browser found!");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    // load data passed by the opened activity before it finished

        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode!=RESULT_OK)
            return;

        if(requestCode==RECEIVE_SELECTED_ITEM_NAMES_KEY){

            ArrayList<String> newChosenItemNames = data.getStringArrayListExtra(SelectItemActivity.NEW_CHOSEN_ITEM_NAMES_KEY);
            ArrayList<Integer> newChosenItemIcons = data.getIntegerArrayListExtra(SelectItemActivity.NEW_CHOSEN_ITEM_ICONS_KEY);

            loadNewChosenItems(newChosenItemNames, newChosenItemIcons);

        }


    }

    private void loadNewChosenItems(ArrayList<String> newChosenItemNames, ArrayList<Integer> newChosenItemIcons) {
    // load the items passed by the opened activity before closing

        for(int i=0;i<newChosenItemIcons.size();i++){

            String name = newChosenItemNames.get(i);
            int icon = newChosenItemIcons.get(i);

            Item item = new Item(name, new ImageResource(icon));

            addItemToModelAndUI(item);
        }
    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

}