package com.ferdouszislam.nsu.cse486.sec01.homemealapp.customer;

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
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.daos.FoodOrderDao;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.daos.firebaseDaos.CustomerUserFirebaseRealtimeDao;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.daos.firebaseDaos.FoodOrderFirebaseRealtimeDao;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.DatabaseOperationStatusListener;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.models.CompletedFoodOrder;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.models.FoodOrder;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils.InputValidatorUtil;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils.OrderStatus;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils.SessionUtil;

public class CustomerPaymentActivity extends AppCompatActivity {

    private static final String TAG = "CustPA-debug";

    // ui
    private TextView mPaymentAmountTextView, mVendorBkashNumberTextView;
    private EditText mTransactionCodeEditText;
    private Button mConfirmPaymentButton;

    // model
    private CompletedFoodOrder mCompletedFoodOrder;

    // variables for user authentication
    private boolean isUserAuthenticated = false;
    private Authentication mAuth;
    private Authentication.AuthenticationCallbacks mAuthCallbacks = new Authentication.AuthenticationCallbacks() {
        @Override
        public void onAuthenticationSuccess(AuthenticationUser user) {

            isUserAuthenticated = true;
        }

        @Override
        public void onAuthenticationFailure(String message) {

            Toast.makeText(CustomerPaymentActivity.this, R.string.hard_logout, Toast.LENGTH_SHORT)
                    .show();

            SessionUtil.logoutNow(CustomerPaymentActivity.this, mAuth);

            Log.d(TAG, "onAuthenticationFailure: authentication failed -> "+message);
        }
    };

    // variables used to insert complete order to database
    private FoodOrderDao mFoodOrderDao;
    private DatabaseOperationStatusListener<Void, String> mCompleteOrderDatabaseOperationStatusListener =
            new DatabaseOperationStatusListener<Void, String>() {
                @Override
                public void onSuccess(Void successResponse) {

                    mFoodOrderDao.updateWithId(
                            mCompletedFoodOrder,
                            mCompletedFoodOrder.getmFoodOrderId(),
                            mUpdateOrderStatusDatabaseOperationStatusListener
                    );
                }

                @Override
                public void onFailed(String failedResponse) {

                    paymentFailedUI();
                }
            };
    private DatabaseOperationStatusListener<Void, String> mUpdateOrderStatusDatabaseOperationStatusListener =
            new DatabaseOperationStatusListener<Void, String>() {
                @Override
                public void onSuccess(Void successResponse) {
                    paymentSuccessUI();
                }

                @Override
                public void onFailed(String failedResponse) {

                    paymentFailedUI();
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_payment);

        init();
    }

    private void init() {

        setupToolbar();

        mPaymentAmountTextView = findViewById(R.id.customer_paymentAmount_TextView);
        mVendorBkashNumberTextView = findViewById(R.id.vendorBkashNumber_TextView);
        mTransactionCodeEditText = findViewById(R.id.customer_transactionCode_EditText);
        mConfirmPaymentButton = findViewById(R.id.customer_confirmPayment_Button);

        FoodOrder foodOrder = (FoodOrder) getIntent().getSerializableExtra(CustomerPlacedOrdersActivity.FOOD_ORDER_KEY);
        mCompletedFoodOrder = new CompletedFoodOrder(foodOrder);

        mVendorBkashNumberTextView.setText(mCompletedFoodOrder.getmChefPhone());
        mPaymentAmountTextView.setText(mCompletedFoodOrder.getmPaymentAmount());

        mFoodOrderDao = new FoodOrderFirebaseRealtimeDao();

        mAuth = new FirebaseEmailPasswordAuthentication(mAuthCallbacks);
        mAuth.authenticateUser();
    }

    /*
    setup the toolbar with back button
     */
    private void setupToolbar() {

        Toolbar myChildToolbar = findViewById(R.id.customerPayment_Toolbar);
        setSupportActionBar(myChildToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        if(ab!=null) {
            // Enable the Up button
            ab.setDisplayHomeAsUpEnabled(true);
            // set toolbar title
            ab.setTitle(R.string.customer_payment_title);
        }
    }

    public void confirmPaymentClick(View view) {

        String transactionCode = mTransactionCodeEditText.getText().toString().trim();

        if(validateInputs(transactionCode)) {

            if(!isUserAuthenticated){
                Toast.makeText(this, R.string.authenticating, Toast.LENGTH_SHORT)
                        .show();
                return;
            }

            mCompletedFoodOrder.setmTransactionCode(transactionCode);

            // create a entry in 'completeFoodOrders' then update 'foodOrders' node
            mFoodOrderDao.createCompletedFoodOrder(mCompletedFoodOrder, mCompleteOrderDatabaseOperationStatusListener);

            paymentInProgressUI();
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