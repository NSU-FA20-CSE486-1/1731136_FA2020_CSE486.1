package com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.R;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.auth.Authentication;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.auth.AuthenticationUser;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.auth.FirebaseEmailPasswordAuthentication;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.daos.ChefServiceChargePaymentDao;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.daos.firebaseDaos.ChefServiceChargePaymentFirebaseRealtimeDao;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.DatabaseOperationStatusListener;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.models.ChefServiceChargePayment;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils.InputValidatorUtil;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils.SessionUtil;

public class ChefSCPaymentActivity extends AppCompatActivity {

    private static final String TAG = "CSCPA-debug";

    // ui
    private TextView mPaymentAmountTextView;
    private EditText mTransactionCodeEditText;
    private Button mConfirmPaymentButton;

    // model
    private ChefServiceChargePayment mChefServiceChargePayment;

    // variables for user authentication
    private boolean isUserAuthenticated = false;
    private Authentication mAuth;
    private Authentication.AuthenticationCallbacks mAuthCallbacks = new Authentication.AuthenticationCallbacks() {
        @Override
        public void onAuthenticationSuccess(AuthenticationUser user) {

            isUserAuthenticated = true;

            mChefServiceChargePayment.setmChefUid(user.getmUid());
        }

        @Override
        public void onAuthenticationFailure(String message) {

            Toast.makeText(ChefSCPaymentActivity.this, R.string.hard_logout, Toast.LENGTH_SHORT)
                    .show();

            SessionUtil.logoutNow(ChefSCPaymentActivity.this, mAuth);

            Log.d(TAG, "onAuthenticationFailure: authentication failed -> "+message);
        }
    };

    // variables used to store payment record to database
    private ChefServiceChargePaymentDao mChefServiceChargePaymentDao;
    private DatabaseOperationStatusListener<Void, String> mPaymentRecordDatabaseOperationStatusListener =
            new DatabaseOperationStatusListener<Void, String>() {
                @Override
                public void onSuccess(Void successResponse) {

                    paymentSuccessUI();
                }

                @Override
                public void onFailed(String failedResponse) {

                    paymentFailedUI();

                    Log.d(TAG, "onFailed: payment record send to database error -> "+failedResponse);
                }
            };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_s_c_payment);

        init();
    }

    private void init() {

        setupToolbar();

        mPaymentAmountTextView = findViewById(R.id.chef_paymentAmount_TextView);
        mTransactionCodeEditText = findViewById(R.id.chef_transactionCode_EditText);
        mConfirmPaymentButton = findViewById(R.id.chef_confirmPayment_Button);

        mChefServiceChargePayment = new ChefServiceChargePayment();

        mChefServiceChargePaymentDao = new ChefServiceChargePaymentFirebaseRealtimeDao();

        mAuth = new FirebaseEmailPasswordAuthentication(mAuthCallbacks);
        mAuth.authenticateUser();
    }

    /*
    setup the toolbar with back button
     */
    private void setupToolbar() {

        Toolbar myChildToolbar = findViewById(R.id.chefSCPayment_Toolbar);
        setSupportActionBar(myChildToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        if(ab!=null) {
            // Enable the Up button
            ab.setDisplayHomeAsUpEnabled(true);
            // set toolbar title
            ab.setTitle(R.string.service_charge_payment_title);
        }
    }

    public void confirmPaymentClick(View view) {

        String transactionCode = mTransactionCodeEditText.getText().toString().trim();

        if(validateInputs(transactionCode)){

            if(!isUserAuthenticated){
                Toast.makeText(this, R.string.authenticating, Toast.LENGTH_SHORT)
                        .show();
                return;
            }

            paymentInProgressUI();

            mChefServiceChargePayment.setmTransactionCode(transactionCode);

            mChefServiceChargePaymentDao.createServiceChargePaymentRecord(
                    mChefServiceChargePayment,
                    mPaymentRecordDatabaseOperationStatusListener
            );

        }
    }

    private boolean validateInputs(String transactionCode) {

        if(!InputValidatorUtil.isTransactionCodeValid(transactionCode)){

            mTransactionCodeEditText.setError(getString(R.string.invalid_transaction_code));

            return false;
        }

        return true;
    }

    private void paymentInProgressUI(){

        mConfirmPaymentButton.setEnabled(false);
        mConfirmPaymentButton.setText(getString(R.string.confirming));
    }

    private void paymentSuccessUI(){

        Toast.makeText(this, R.string.payment_complete, Toast.LENGTH_SHORT)
                .show();

        finish();
    }

    private void paymentFailedUI(){

        Toast.makeText(this, R.string.payment_failed, Toast.LENGTH_SHORT)
                .show();

        mConfirmPaymentButton.setEnabled(true);
        mConfirmPaymentButton.setText(getString(R.string.confirmPayment_ButtonLabel));
    }
}