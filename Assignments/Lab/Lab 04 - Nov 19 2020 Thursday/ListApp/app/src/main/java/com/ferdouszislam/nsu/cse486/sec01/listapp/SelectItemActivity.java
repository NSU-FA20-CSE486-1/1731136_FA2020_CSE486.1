package com.ferdouszislam.nsu.cse486.sec01.listapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Toast;

import com.ferdouszislam.nsu.cse486.sec01.listapp.adapter.ISingleItemUserInteraction;
import com.ferdouszislam.nsu.cse486.sec01.listapp.adapter.ItemsAdapter;
import com.ferdouszislam.nsu.cse486.sec01.listapp.models.AllowedItemChoice;
import com.ferdouszislam.nsu.cse486.sec01.listapp.models.ImageResource;
import com.ferdouszislam.nsu.cse486.sec01.listapp.models.Item;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SelectItemActivity extends AppCompatActivity implements ISingleItemUserInteraction {

    private static final String TAG = "SelectItemActivity";

    // value passing key
    public static final String NEW_CHOSEN_ITEM_NAMES_KEY = "new-chosen-item-names-key";
    public static final String NEW_CHOSEN_ITEM_ICONS_KEY = "new-chosen-item-icons-key";

    // model
    private ArrayList<Item> mItemChoiceList = new ArrayList<>();
    private ArrayList<Item> mChosenItemList = new ArrayList<>();

    // ui
    // recyclerview to show the item choice list
    private RecyclerView rvItemChoices;
    private ItemsAdapter adapterItemChoices;

    // savedInstanceState keys
    private static final String SAVED_ITEM_CHOICE_NAMES_KEY = "saved-item-choice-names-key";
    private static final String SAVED_ITEM_CHOICE_ICONS_KEY = "saved-item-choice-icons-key";
    private static final String SAVED_CHOSEN_ITEM_NAMES_KEY = "saved-chosen-item-names-key";
    private static final String SAVED_CHOSEN_ITEM_ICONS_KEY = "saved-chosen-item-icons-key";


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
    // save picked items when instance is destroyed by screen rotation
        super.onSaveInstanceState(outState);

        // save remaining item choices list
        ArrayList<String> savedItemChoiceNames = new ArrayList<>();
        ArrayList<Integer> savedItemChoiceIcons = new ArrayList<>();
        for(Item item: mItemChoiceList){
            savedItemChoiceNames.add(item.getLabel());
            savedItemChoiceIcons.add(item.getImageResource().getResource_id());
        }
        outState.putStringArrayList(SAVED_ITEM_CHOICE_NAMES_KEY, savedItemChoiceNames);
        outState.putIntegerArrayList(SAVED_ITEM_CHOICE_ICONS_KEY, savedItemChoiceIcons);

        // save chosen item list
        ArrayList<String> savedChosenItemNames = new ArrayList<>();
        ArrayList<Integer> savedChosenItemIcons = new ArrayList<>();
        for(Item item: mChosenItemList){
            savedChosenItemNames.add(item.getLabel());
            savedChosenItemIcons.add(item.getImageResource().getResource_id());
        }
        outState.putStringArrayList(SAVED_CHOSEN_ITEM_NAMES_KEY, savedChosenItemNames);
        outState.putIntegerArrayList(SAVED_CHOSEN_ITEM_ICONS_KEY, savedChosenItemIcons);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_item);

        initUIElements();

        if(savedInstanceState==null){
        // no previous saved state
        // load initial data
            initModel();
        }

    }

    private void initUIElements() {

        rvItemChoices = findViewById(R.id.itemChoice_RecyclerView);
        // Create adapter passing in the sample user data
        adapterItemChoices = new ItemsAdapter(mItemChoiceList, this);
        // Attach the adapter to the recyclerview to populate items
        rvItemChoices.setAdapter(adapterItemChoices);
        // Set layout manager to position the items
        rvItemChoices.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initModel() {
        // load choosable items into model

        ArrayList<Item> allItemChoices = AllowedItemChoice.getAllowedItemsList();

        ArrayList<String> alreadyChosenItemNamesList = getIntent().getStringArrayListExtra(MainActivity.CHOSEN_ITEM_NAMES_KEY);
        Set<String> alreadyChosenItemNamesSet = new HashSet<>();
        for(String chosenName: alreadyChosenItemNamesList){
            alreadyChosenItemNamesSet.add(chosenName);
        }

        for(Item itemChoice: allItemChoices){
            // set.contains() uses equals() to compare so it's safe to use
            if(!alreadyChosenItemNamesSet.contains(itemChoice.getLabel()))
                mItemChoiceList.add(itemChoice);
        }

    }


    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
    // restore data stored in savedInstanceState
        super.onRestoreInstanceState(savedInstanceState);

        loadModelData(savedInstanceState);
    }

    private void loadModelData(Bundle savedInstanceState) {
        // load model data from savedInstanceState that was destroyed on screen rotation

        // load remaining item choices list
        ArrayList<String> savedItemChoiceNames = savedInstanceState.getStringArrayList(SAVED_ITEM_CHOICE_NAMES_KEY);
        ArrayList<Integer> savedItemChoiceIcons = savedInstanceState.getIntegerArrayList(SAVED_ITEM_CHOICE_ICONS_KEY);
        for(int i=0; i<savedItemChoiceNames.size();i++){

            String name = savedItemChoiceNames.get(i);
            int icon = savedItemChoiceIcons.get(i);

            mItemChoiceList.add( new Item(name, new ImageResource(icon)) );

            // need to update UI list too
            if(adapterItemChoices==null)
                initUIElements();
            adapterItemChoices.notifyItemInserted(mItemChoiceList.size()-1);
        }

        // load chosen item list
        ArrayList<String> savedChosenItemNames = savedInstanceState.getStringArrayList(SAVED_CHOSEN_ITEM_NAMES_KEY);
        ArrayList<Integer> savedChosenItemIcons = savedInstanceState.getIntegerArrayList(SAVED_CHOSEN_ITEM_ICONS_KEY);
        for(int i=0; i<savedChosenItemNames.size();i++){

            String name = savedChosenItemNames.get(i);
            int icon = savedChosenItemIcons.get(i);

            mChosenItemList.add( new Item(name, new ImageResource(icon)) );
        }

    }


    @Override
    public void onItemNameButtonClick(Item item) {

        int removeIndex = -1;

        // remove item from list
        try {
        // sometimes user actions might be faster than array removal
        // causing IndexOutOfBoundException

            for(int i=0;i<mItemChoiceList.size();i++){

                if(mItemChoiceList.get(i).getLabel().equals(item.getLabel())){

                    removeIndex = i;
                    break;
                }

            }

            if(removeIndex==-1) return;

            // update models
            mItemChoiceList.remove(removeIndex);
            mChosenItemList.add(item);

            // update UI
            adapterItemChoices.notifyItemRemoved(removeIndex);

            Toast.makeText(this, item.getLabel()+" added!", Toast.LENGTH_SHORT)
                    .show();

        } catch (IndexOutOfBoundsException e){

            Log.e(TAG, "onNameButtonClick: trying to select position = "+(removeIndex)+"\nerror = "+e.getMessage());
        }

    }

    @Override
    public void onBackPressed() {
        // send back the chosen items to MainActivity
        ArrayList<String> chosenItemNames = new ArrayList<>();
        ArrayList<Integer> chosenItemIcons = new ArrayList<>();
        for(int i=0;i<mChosenItemList.size();i++){

            chosenItemNames.add(mChosenItemList.get(i).getLabel());
            chosenItemIcons.add(mChosenItemList.get(i).getImageResource().getResource_id());
        }

        Intent sendBackIntent = new Intent();

        sendBackIntent.putStringArrayListExtra(NEW_CHOSEN_ITEM_NAMES_KEY, chosenItemNames);
        sendBackIntent.putIntegerArrayListExtra(NEW_CHOSEN_ITEM_ICONS_KEY, chosenItemIcons);

        setResult(RESULT_OK, sendBackIntent);

        Log.d(TAG, "onBackPressed: chosen items sent: "+chosenItemNames);

        // MUST CALL THIS LAST
        // call to super finishes the activity
        super.onBackPressed();
    }
}