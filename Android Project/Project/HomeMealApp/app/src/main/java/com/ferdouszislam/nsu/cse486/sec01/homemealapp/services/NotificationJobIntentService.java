package com.ferdouszislam.nsu.cse486.sec01.homemealapp.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.R;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.appSettings.SettingsFragment;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.activities.ChefPlacedOrdersActivity;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.customer.activities.CustomerPlacedOrdersActivity;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.daos.FoodOrderDao;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.daos.firebaseDaos.FoodOrderFirebaseRealtimeDao;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.DatabaseOperationStatusListener;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.ListDataChangeListener;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.models.FoodOrder;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.sharedPreferences.UserAuthSharedPref;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils.OrderStatus;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils.UserType;

public class NotificationJobIntentService extends JobIntentService {

    private static final String TAG = "NJIS-debug";
    static final int JOB_ID = 503;
    private static final String NOTIFICATION_CHANNEL_ID = "com.ferdouszislam.nsu.cse486.sec01.homemealapp.services-notificationChannel";
    private static final int NOTIFICATION_ID = 288;

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

                    Log.d(TAG, "onDataAdded: new order chef");

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

                    Log.d(TAG, "onDataAdded: new order customer");

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

    public static void enqueueWork(Context context, Intent work){

        enqueueWork(context, NotificationJobIntentService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        Log.d(TAG, "onStartCommand: job started!");

        mUid = intent.getStringExtra(SettingsFragment.NOTIFICATION_SERVICE_UID_KEY);

        if(mUid == null){
            Log.d(TAG, "onStartCommand: Notification job Service stopped beacuse user uid not provided");
            stopSelf();
        }

        init();

    }

    private void init() {

        createNotificationChannel();

        mUserAuthSharedPref = UserAuthSharedPref.build(this);
        mUserType = mUserAuthSharedPref.getUserType();

        if(mUserType.equals(UserType.CUSTOMER)){

            mCustomerFoodOrderDao = new FoodOrderFirebaseRealtimeDao(this);
            mCustomerFoodOrderDao.readFoodOrdersForCustomer(
                    mUid,
                    new DatabaseOperationStatusListener<Void, String>() {
                        @Override
                        public void onSuccess(Void successResponse) {
                            // intentionally kept blank
                        }

                        @Override
                        public void onFailed(String failedResponse) {
                            Log.d(TAG, "onFailed: failed to read customer rejected orders job -> "+failedResponse);
                        }
                    },
                    mCustomerOrdersListDataChangeListener
            );
        }

        else if(mUserType.equals(UserType.CHEF)){

            mChefFoodOrderDao = new FoodOrderFirebaseRealtimeDao(this);

            mChefFoodOrderDao.readFoodOrdersForChef(
                    mUid,
                    new DatabaseOperationStatusListener<Void, String>() {
                        @Override
                        public void onSuccess(Void successResponse) {
                            // intentionally kept blank
                        }

                        @Override
                        public void onFailed(String failedResponse) {
                            Log.d(TAG, "onFailed: failed to read chef pending orders job -> "+failedResponse);
                        }
                    },
                    mChefOrdersListDataChangeListener
            );
        }

        else{

            Log.d(TAG, "init: Notification job Service stopped because user type is invalid!");
            stopSelf();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "onDestroy: notification job destroyed");
    }

    private void showNotification(FoodOrder data) {

        Toast.makeText(this, "notification!", Toast.LENGTH_SHORT)
                .show();

        String notificationTitle, notificationDescription;
        Intent intent;

        if(mUserType.equals(UserType.CHEF)){

            notificationTitle = getString(R.string.new_order);
            notificationDescription =
                    data.getmFoodName() + ", " + data.getmQuantityUnitsSelectedByCustomer() + " X " + data.getmQuantityPerUnit();

            intent = new Intent(this, ChefPlacedOrdersActivity.class);
        }

        else if(mUserType.equals(UserType.CUSTOMER)){

            notificationTitle = getString(R.string.order_rejected);
            notificationDescription =
                    data.getmFoodName() + " X " + data.getmQuantityUnitsSelectedByCustomer() + " " + getString(R.string.was_rejected);

            intent = new Intent(this, CustomerPlacedOrdersActivity.class);
        }

        else{
            Log.d(TAG, "showNotification: Notification Job Service stopped, invalid user type");
            stopSelf();

            return;
        }

        displayNotification(notificationTitle, notificationDescription, intent);
    }

    private void removeNotification() {

        NotificationManagerCompat.from(this).cancel(NOTIFICATION_ID);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.enable_notifications_channel);
            String description = getString(R.string.enable_notifications_channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void displayNotification(String notificationTitle, String notificationDescription, Intent intent) {

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_action_notification)
                .setContentTitle(notificationTitle)
                .setContentText(notificationDescription)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, builder.build());
    }
}
