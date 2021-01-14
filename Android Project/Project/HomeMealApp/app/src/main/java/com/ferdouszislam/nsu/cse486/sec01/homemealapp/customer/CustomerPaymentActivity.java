package com.ferdouszislam.nsu.cse486.sec01.homemealapp.customer;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.R;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.daos.FoodOrderDao;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.daos.firebaseDaos.FoodOrderFirebaseRealtimeDao;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.DatabaseOperationStatusListener;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.models.CompletedFoodOrder;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.models.FoodOrder;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils.InputValidatorUtil;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils.OrderStatus;

public class CustomerPaymentActivity extends AppCompatActivity {

    // ui
    private TextView mPaymentAmountTextView;
    private EditText mTransactionCodeEditText;
    private Button mConfirmPaymentButton;

    // model
    private CompletedFoodOrder mCompletedFoodOrder;

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
        mTransactionCodeEditText = findViewById(R.id.customer_transactionCode_EditText);
        mConfirmPaymentButton = findViewById(R.id.customer_confirmPayment_Button);

        FoodOrder foodOrder = (FoodOrder) getIntent().getSerializableExtra(CustomerPlacedOrdersActivity.FOOD_ORDER_KEY);
        mCompletedFoodOrder = new CompletedFoodOrder(foodOrder);

        mPaymentAmountTextView.setText(mCompletedFoodOrder.getmPaymentAmount());

        mFoodOrderDao = new FoodOrderFirebaseRealtimeDao();
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
            ab.setTitle(R.string.service_charge_payment_title);
        }
    }

    public void confirmPaymentClick(View view) {

        String transactionCode = mTransactionCodeEditText.getText().toString().trim();

        if(validateInputs(transactionCode)) {

            mCompletedFoodOrder.setmTransactionCode(transactionCode);
            mCompletedFoodOrder.setmOrderStatus(OrderStatus.DELIVERED);

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