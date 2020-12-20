package com.nsu.ece.ferdouszislam.cse486l.sec01.secrettextapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.nsu.ece.ferdouszislam.cse486l.sec01.secrettextapp.models.Message;
import com.nsu.ece.ferdouszislam.cse486l.sec01.secrettextapp.models.SMS;

/**
 * Activity to encrypt message and send via default SMS app
 */
public class EncryptMessageActivity extends AppCompatActivity {

    private static final String TAG = "EMA-debug";
    
    // ui
    private EditText mPhoneNumberEditText,mMessageContentEditText, mEncryptionKeyEditText;

    // model
    private SMS mSms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypt_message);

        init();
    }

    private void init() {

        mPhoneNumberEditText = findViewById(R.id.editTextPhoneNumber);
        mMessageContentEditText = findViewById(R.id.messageContentEditText);
        mEncryptionKeyEditText = findViewById(R.id.encryptionKeyEditText);

        mSms = new SMS();
    }

    /*
    send message button click
     */
    public void sendButtonClick(View view) {

        setModelFromInputs();

        if(validate()) {

            if (encryptMessage()){

                sendMessageViaDefaultSMSApp();
            }
        }

        else showToast( getString(R.string.invalid_input) );

        Log.d(TAG, "sendButtonClick: "+mSms.toString());
    }

    /*
    setup the model mSms from user inputs
     */
    private void setModelFromInputs() {

        Message message = new Message();
        message.setmPlainMessage(mMessageContentEditText.getText().toString().trim());
        message.setmEncryptionKey(mEncryptionKeyEditText.getText().toString().trim().toLowerCase());

        mSms.setmMessage(message);
        mSms.setmRecipientPhoneNumber(
                getString(R.string.phoneCountryCode_textLabel) + mPhoneNumberEditText.getText().toString().trim()
        );
    }

    /*
    validate user input
     */
    private boolean validate() {

        return mSms.isValid();
    }

    /*
    encrypt the message
     */
    private boolean encryptMessage() {

        try {
            String message = mSms.getmMessage().getmPlainMessage();
            
            AESUtils.setEncryptionKey(mSms.getmMessage().getmEncryptionKey());
            
            String encryptedMessage = AESUtils.encrypt(message);
            
            mSms.getmMessage().setmEncryptedMessage(encryptedMessage);

            Log.d(TAG, "encryptMessage: message encryption success!");
            
            return true;
            
        } catch (Exception e) {
            
            showToast(getString(R.string.encryption_error));
            Log.d(TAG, "encryptMessage: error = "+e.getMessage());
            
            return false;
        }
    }

    /*
    open default sms app
    with user inputted phone number and message
     */
    private void sendMessageViaDefaultSMSApp(){

        Intent intent = new Intent(Intent.ACTION_SENDTO);

        intent.setData(Uri.parse("smsto:"+mSms.getmRecipientPhoneNumber()));  // This ensures only SMS apps respond

        intent.putExtra("sms_body", mSms.getmMessage().getmEncryptedMessage());

        if (intent.resolveActivity(getPackageManager()) != null){

            startActivity(intent);
        }

        else showToast( getString(R.string.no_default_sms_app_found) );
    }

    /*
    show toast message
     */
    private void showToast(String message){

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}