package com.nsu.ece.ferdouszislam.cse486l.sec01.secrettextapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.nsu.ece.ferdouszislam.cse486l.sec01.secrettextapp.models.Message;

/**
 * Activity to decrypt an encrypted message
 */
public class DecryptMessageActivity extends AppCompatActivity {

    private static final String TAG = "DMA-debug";

    // ui
    private TextInputLayout mTextInputLayout;
    private EditText mMessageContentEditText;
    private Button mDecryptButton;

    // model
    Message mMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decrypt_message);

        init();
    }

    private void init() {

        mTextInputLayout = findViewById(R.id.messageTextInputLayout);
        mMessageContentEditText = findViewById(R.id.messageContentEditText);
        mDecryptButton = findViewById(R.id.decryptButton);

        mMessage = new Message();
    }

    /*
    decrypt button click
     */
    public void decryptMessageClick(View view) {

        String receivedMessage = mMessageContentEditText.getText().toString();
        mMessage.extractEncryptedMessageAndKey(receivedMessage);

        if(!decryptMessage()) showToast("Error! failed to decrypt text.");

        else{

            showDecryptedMessage();
        }
    }

    private boolean decryptMessage() {

        try {

            String encrypted = mMessage.getmEncryptedMessage();

            AESUtils.setEncryptionKey(mMessage.getmEncryptionKey());

            String decrypted = AESUtils.decrypt(encrypted);

            mMessage.setmPlainMessage(decrypted);

            Log.d(TAG, "decryptMessage: decryption success!");

            return true;
        }
        catch (Exception e){

            Log.d(TAG, "decryptMessage: error = "+e.getMessage());
            return false;
        }
    }

    private void showDecryptedMessage() {

        mTextInputLayout.setHelperText("decryption complete");

        mDecryptButton.setText("Decrypted");
        mDecryptButton.setEnabled(false);

        mMessageContentEditText.setText(mMessage.getmPlainMessage());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mMessageContentEditText.setTextColor( getColor(R.color.design_default_color_primary_dark));
        }
    }

    /*
    show toast message
     */
    private void showToast(String message){

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}