package com.ferdouszislam.nsu.cse486.sec01.bangladictonary;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ferdouszislam.nsu.cse486.sec01.bangladictonary.models.DictonaryItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // key to pass user input arraylist to 'DictonaryActivity'
    public static final String DICT_ITEM_LIST_KEY = "com.ferdouszislam.nsu.cse486.sec01.bangladictonary-dict_item_list";

    // to track of which data were deleted in 'DictonaryActivity'
    private static final int DICT_ITEMS_STATE_REQUEST_CODE = 76;

    // ui
    private EditText mEnglishWordTextView, mBanglaMeaningTextView;

    // model
    private ArrayList<DictonaryItem> mAddedDictonaryItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {

        mEnglishWordTextView = findViewById(R.id.englishWord_EditText);
        mBanglaMeaningTextView = findViewById(R.id.banglaWord_EditText);

        mAddedDictonaryItems = new ArrayList<>();
    }

    /*
    "save" word to dictonary click listener
     */
    public void saveClick(View view) {

        DictonaryItem dictonaryItem = readUserInput();

        if(dictonaryItem.validateLanguage()){

            mAddedDictonaryItems.add(dictonaryItem);

            showWordSavedUI();
        }

        else {

            showInvalidInputUI(dictonaryItem);
        }
    }


    /**
     * Take inputs from edit text and initialize model
     * @return object of DictonaryItem class from user input
     */
    private DictonaryItem readUserInput() {

        String wordInEnglish = mEnglishWordTextView.getText().toString().trim();
        String wordInBangla = mBanglaMeaningTextView.getText().toString().trim()    ;

        return new DictonaryItem(wordInEnglish, wordInBangla);
    }

    /*
    let user know that word was saved
     */
    private void showWordSavedUI() {

        Toast.makeText(this, R.string.saved_toast, Toast.LENGTH_SHORT)
                .show();

        mEnglishWordTextView.setText("");
        mBanglaMeaningTextView.setText("");
    }

    /**
     * Alert user of invalid input field
     * @param dictonaryItem model created with invalid input
     */
    private void showInvalidInputUI(DictonaryItem dictonaryItem) {

        if(!dictonaryItem.isValidBanglaWord()) mBanglaMeaningTextView.setError(getString(R.string.not_bangla_word));
        if(!dictonaryItem.isValidEnglishWord()) mEnglishWordTextView.setError(getString(R.string.not_english_word));
    }

    /*
    show dictonary activity button click listener
     */
    public void showDictonaryClick(View view) {

        Intent intent = new Intent(this, DictonaryActivity.class);
        intent.putExtra(DICT_ITEM_LIST_KEY, mAddedDictonaryItems);

        startActivityForResult(intent, DICT_ITEMS_STATE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode!=RESULT_OK) return;

        switch (requestCode){

            case DICT_ITEMS_STATE_REQUEST_CODE:

                if (data != null) {
                    mAddedDictonaryItems = (ArrayList<DictonaryItem>) data.getSerializableExtra(DictonaryActivity.MODIFIED_DICT_ITEMS_KEY);
                }

                break;
        }
    }

}
