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
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.daos.ChefUserDao;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.daos.FoodOrderDao;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.daos.firebaseDaos.ChefUserFirebaseRealtimeDao;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.daos.firebaseDaos.FoodOrderFirebaseRealtimeDao;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.DatabaseOperationStatusListener;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.SingleDataChangeListener;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.models.ChefUser;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.models.FoodOffer;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.models.FoodOrder;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.sharedPreferences.CustomerUserProfileSharedPref;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils.InputValidatorUtil;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils.SessionUtil;

public class CustomerPlaceOrderActivity extends AppCompatActivity {

    private static final String TAG = "CustPOA-debug";

    // ui
    private EditText mSelectUnitsEditText, mBkashPhoneNumberEditText;
    private EditText mContactPhoneNumberEditText, mCustomerLocationEditText;
    private TextView mPaymentAmountTextView;
    private Button mConfirmOrderButton;

    // model
    private FoodOffer mFoodOffer;
    private FoodOrder mFoodOrder;

    // variables for user authentication
    private Authentication mAuth;
    private Authentication.AuthenticationCallbacks mAuthCallbacks = new Authentication.AuthenticationCallbacks() {
        @Override
        public void onAuthenticationSuccess(AuthenticationUser user) {

            mFoodOrder.setmCustomerUid(user.getmUid());
        }

        @Override
        public void onAuthenticationFailure(String message) {

            Toast.makeText(CustomerPlaceOrderActivity.this, R.string.hard_logout, Toast.LENGTH_SHORT)
                    .show();

            SessionUtil.logoutNow(CustomerPlaceOrderActivity.this, mAuth);

            Log.d(TAG, "onAuthenticationFailure: authentication failed -> "+message);
        }
    };

    // shared preference variable for updating local chef user profile
    private CustomerUserProfileSharedPref mCustomerUserProfileSharedPref;

    // variables to save order to database
    private FoodOrderDao mFoodOrderDao;
    private DatabaseOperationStatusListener<Void, String> mFoodOrderCreationStatus = new DatabaseOperationStatusListener<Void, String>() {
        @Override
        public void onSuccess(Void successResponse) {
            placeFoodOrderSuccessUI();
        }

        @Override
        public void onFailed(String failedResponse) {

            placeFoodOrderFailedUI();

            Log.d(TAG, "onFailed: order creation failed -> "+failedResponse);
        }
    };

    // variables to fetch vendor contact number
    private ChefUserDao mChefUserDao;
    private SingleDataChangeListener<ChefUser> mChefUserSingleDataChangeListener =
            new SingleDataChangeListener<ChefUser>() {
                @Override
                public void onDataChange(ChefUser data) {

                    mFoodOrder.setmChefPhone(data.getmPhoneNumber());

                    // now store the food offer to database`
                    mFoodOrderDao.createFoodOrder(mFoodOrder, mFoodOrderCreationStatus);
                }
            };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_place_order);

        init();
    }

    private void init() {

        setupToolbar();

        mSelectUnitsEditText = findViewById(R.id.quantity_customerPlaceOrder_EditText);
        mBkashPhoneNumberEditText = findViewById(R.id.bKashPhone_customerPlaceOrder_EditText);
        mContactPhoneNumberEditText = findViewById(R.id.callPhone_customerPlaceOrder_EditText);
        mCustomerLocationEditText = findViewById(R.id.address_customerPlaceOrder_EditText);
        mPaymentAmountTextView = findViewById(R.id.paymentAmount_customerPlaceOrder_TextView);
        mConfirmOrderButton = findViewById(R.id.confirmOrder_Button);

        mFoodOffer = (FoodOffer) getIntent().getSerializableExtra(CustomerFoodOfferDetailsActivity.FOOD_OFFER_ORDER_KEY);
        mFoodOrder = new FoodOrder();
        mFoodOrder.setmFoodOfferId(mFoodOffer.getId());
        mFoodOrder.setmFoodName(mFoodOffer.getmFoodName());
        mFoodOrder.setmChefUid(mFoodOffer.getmChefUid());
        mFoodOrder.setmChefLocation(mFoodOffer.getmRegion()+ " (" + mFoodOffer.getmAddress() + ")");
        mFoodOrder.setmQuantityPerUnit(mFoodOffer.getmQuantity());

        mCustomerUserProfileSharedPref = CustomerUserProfileSharedPref.build(this);

        mChefUserDao = new ChefUserFirebaseRealtimeDao();
        mFoodOrderDao = new FoodOrderFirebaseRealtimeDao();

        mAuth = new FirebaseEmailPasswordAuthentication(mAuthCallbacks);
        mAuth.authenticateUser();

        mContactPhoneNumberEditText.setText(mCustomerUserProfileSharedPref.getCustomerUser().getmPhoneNumber());
        String initialPayment = mFoodOffer.getmPrice() + " BDT";
        mPaymentAmountTextView.setText(initialPayment);
    }

    /*
    setup the toolbar with back button
     */
    private void setupToolbar() {

        Toolbar myChildToolbar = findViewById(R.id.customerMenu_Toolbar);
        setSupportActionBar(myChildToolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        if(ab!=null) {
            // Enable the Up button
            ab.setDisplayHomeAsUpEnabled(true);
            // set toolbar title
            ab.setTitle(R.string.placed_orders_title);
        }
    }

    public void incrementQuantityClick(View view) {

        int quantity = Integer.parseInt(mSelectUnitsEditText.getText().toString().trim());
        quantity++;
        mSelectUnitsEditText.setText(Integer.toString(quantity));
        int price = quantity * mFoodOffer.getmPrice();
        String paymentAmount = price + " BDT";
        mPaymentAmountTextView.setText(paymentAmount);
    }

    public void decrementQuantityClick(View view) {

        int quantity = Integer.parseInt(mSelectUnitsEditText.getText().toString().trim());
        quantity--;
        if(quantity<1) return;
        mSelectUnitsEditText.setText(Integer.toString(quantity));
        int price = quantity * mFoodOffer.getmPrice();
        String paymentAmount = price + " BDT";
        mPaymentAmountTextView.setText(paymentAmount);
    }

    public void confirmOrderClick(View view) {

        String quantitySpecifiedByCustomer = mSelectUnitsEditText.getText().toString().trim();
        String contactPhone = mContactPhoneNumberEditText.getText().toString().trim();
        String bkashPhone = mBkashPhoneNumberEditText.getText().toString().trim();
        String location = mCustomerLocationEditText.getText().toString().trim();
        String paymentAmount = mPaymentAmountTextView.getText().toString().trim();

        if(validateInputs(quantitySpecifiedByCustomer, contactPhone, bkashPhone, location)){

            mFoodOrder.setmQuantityUnitsSelectedByCustomer(quantitySpecifiedByCustomer);
            mFoodOrder.setmCustomerContactPhone(contactPhone);
            mFoodOrder.setmCustomerBkashPhone(bkashPhone);
            mFoodOrder.setmCustomerLocation(location);
            mFoodOrder.setmPaymentAmount(paymentAmount);

            placeFoodOrder(mFoodOrder);
        }

    }

    private boolean validateInputs(String quantitySpecifiedByCustomer, String contactPhone, String bkashPhone, String location) {

        boolean isValid = true;

        if(!InputValidatorUtil.isQuantitySpecifiedByCustomerValid(quantitySpecifiedByCustomer)){
            isValid = false;
            mSelectUnitsEditText.setError(getString(R.string.food_quantity_error));
        }
        if(!InputValidatorUtil.isValidPhoneNumber(contactPhone)){
            isValid = false;
            mContactPhoneNumberEditText.setError(getString(R.string.phone_input_error));
        }
        if(!InputValidatorUtil.isValidPhoneNumber(bkashPhone)){
            isValid = false;
            mBkashPhoneNumberEditText.setError(getString(R.string.phone_input_error));
        }
        if(!InputValidatorUtil.isValidHomeAddress(location)){
            isValid = false;
            mCustomerLocationEditText.setError(getString(R.string.address_input_error));
        }

        return isValid;
    }

    private void placeFoodOrder(FoodOrder mFoodOrder) {

        placeFoodOrderInProgressUI();

        // fetch chef user's phone first
        // then store order to database
        mChefUserDao.readWithId(

                mFoodOrder.getmChefUid(),

                mChefUserSingleDataChangeListener,

                new DatabaseOperationStatusListener<Void, String>() {
                    @Override
                    public void onSuccess(Void successResponse) {
                        // kept blank intentionally
                    }

                    @Override
                    public void onFailed(String failedResponse) {
                        placeFoodOrderFailedUI();
                        Log.d(TAG, "onFailed: failed to read chef user phone -> "+failedResponse);
                    }
                });
    }

    private void placeFoodOrderInProgressUI(){

        mConfirmOrderButton.setEnabled(false);
        mConfirmOrderButton.setText(getString(R.string.placing_order));
    }

    private void placeFoodOrderSuccessUI(){

        finish();

        Toast.makeText(this, R.string.order_placed, Toast.LENGTH_SHORT)
                .show();
    }

    private void placeFoodOrderFailedUI(){

        mConfirmOrderButton.setEnabled(true);
        mConfirmOrderButton.setText(getString(R.string.placeOrder_ButtonLabel));
    }

}