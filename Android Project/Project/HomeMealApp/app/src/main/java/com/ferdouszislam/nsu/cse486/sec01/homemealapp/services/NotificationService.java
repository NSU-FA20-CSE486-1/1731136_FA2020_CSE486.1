package com.ferdouszislam.nsu.cse486.sec01.homemealapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.appSettings.SettingsFragment;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.daos.FoodOrderDao;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.daos.firebaseDaos.FoodOrderFirebaseRealtimeDao;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.DatabaseOperationStatusListener;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.ListDataChangeListener;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.models.FoodOrder;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.sharedPreferences.UserAuthSharedPref;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils.OrderStatus;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils.UserType;

public class NotificationService extends Service {

    private static final String TAG = "NS-debug";

    // variables used to get the user information
    private String mUid;
    private String mUserType;
    private UserAuthSharedPref mUserAuthSharedPref;

    // variables used to detect chef newly placed orders
    private FoodOrderDao mChefFoodOrderDao;
    private ListDataChangeListener<FoodOrder> mChefOrdersListDataChangeListener =
            new ListDataChangeListener<FoodOrder>() {
                @Override
                public void onDataAdded(FoodOrder data) {

                    showNotification(data);
                }

                @Override
                public void onDataUpdated(FoodOrder data) {
                    // kept blank intentionally
                }

                @Override
                public void onDataRemoved(FoodOrder data) {
                    removeNotification();
                }
            };

    // variables used to detect customer rejected orders
    private FoodOrderDao mCustomerFoodOrderDao;
    private ListDataChangeListener<FoodOrder> mCustomerOrdersListDataChangeListener =
            new ListDataChangeListener<FoodOrder>() {
                @Override
                public void onDataAdded(FoodOrder data) {

                    if(data.getmOrderStatus().equals(OrderStatus.REJECTED)){
                        showNotification(data);
                    }
                }

                @Override
                public void onDataUpdated(FoodOrder data) {

                    if(data.getmOrderStatus().equals(OrderStatus.REJECTED)){
                        showNotification(data);
                    }
                }

                @Override
                public void onDataRemoved(FoodOrder data) {

                    showNotification(data);
                }
            };


    public NotificationService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mUid = intent.getStringExtra(SettingsFragment.NOTIFICATION_SERVICE_UID_KEY);

        if(mUid == null){
            Log.d(TAG, "onStartCommand: Notification Service stopped beacuse user uid not provided");
            stopSelf();
        }

        init();

        return Service.START_NOT_STICKY;
    }

    private void init() {

        mUserAuthSharedPref = UserAuthSharedPref.build(this);

        if(mUserAuthSharedPref.getUserType().equals(UserType.CUSTOMER)){

            mUserType = UserType.CUSTOMER;

            mCustomerFoodOrderDao = new FoodOrderFirebaseRealtimeDao();
            mCustomerFoodOrderDao.readFoodOrdersForCustomer(
                    mUid,
                    new DatabaseOperationStatusListener<Void, String>() {
                        @Override
                        public void onSuccess(Void successResponse) {
                            // intentionally kept blank
                        }

                        @Override
                        public void onFailed(String failedResponse) {
                            Log.d(TAG, "onFailed: failed to read customer rejected orders service -> "+failedResponse);
                        }
                    },
                    mCustomerOrdersListDataChangeListener
            );
        }

        else if(mUserAuthSharedPref.getUserType().equals(UserType.CHEF)){

            mUserType = UserType.CHEF;

            mChefFoodOrderDao = new FoodOrderFirebaseRealtimeDao();

            mChefFoodOrderDao.readFoodOrdersForChef(
                    mUid,
                    new DatabaseOperationStatusListener<Void, String>() {
                        @Override
                        public void onSuccess(Void successResponse) {
                            // intentionally kept blank
                        }

                        @Override
                        public void onFailed(String failedResponse) {
                            Log.d(TAG, "onFailed: failed to read chef pending orders service -> "+failedResponse);
                        }
                    },
                    mChefOrdersListDataChangeListener
            );
        }

        else{

            Log.d(TAG, "init: Notification Service stopped because user type is invalid!");
            stopSelf();
        }
    }

    private void showNotification(FoodOrder data) {
        // TODO: implement

        if(mUserType.equals(UserType.CHEF)){

            Toast.makeText(this, "new order "+ data.getmFoodName() +"!", Toast.LENGTH_SHORT)
                    .show();
        }

        else if(mUserType.equals(UserType.CUSTOMER)){

            Toast.makeText(this, "order rejected "+ data.getmFoodName() +"!", Toast.LENGTH_SHORT)
                    .show();
        }

        else{
            Log.d(TAG, "showNotification: Notification Service stopped, invalid user type");
            stopSelf();
        }
    }

    private void removeNotification() {
        // TODO: implement
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}