package com.ferdouszislam.nsu.cse486.sec01.homemealapp.daos.firebaseDaos;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.models.CompletedFoodOrder;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.models.FoodOrder;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.daos.FoodOrderDao;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.DatabaseOperationStatusListener;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.ListDataChangeListener;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.models.RejectedFoodOrder;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils.NosqlDatabasePathsUtil;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class FoodOrderFirebaseRealtimeDao implements FoodOrderDao {

    private static final String TAG = "FOrFRD-debug";

    private FirebaseDatabase mDatabase;

    public FoodOrderFirebaseRealtimeDao() {

        mDatabase = FirebaseDatabase.getInstance();
    }

    public FoodOrderFirebaseRealtimeDao(Context context) {

        try {
            mDatabase = FirebaseDatabase.getInstance();
        } catch (IllegalStateException e){

            FirebaseApp.initializeApp(context);
            mDatabase = FirebaseDatabase.getInstance();
        }
    }

    @Override
    public void createFoodOrder(FoodOrder foodOrder, DatabaseOperationStatusListener<Void, String> statusListener) {

        DatabaseReference ref = mDatabase.getReference().child(NosqlDatabasePathsUtil.FOOD_ORDERS_NODE);

        String foodOrderId = ref.push().getKey();

        foodOrder.setmFoodOrderId(foodOrderId);

        ref.child(foodOrderId).setValue(foodOrder)

                .addOnSuccessListener(aVoid -> statusListener.onSuccess(null))

                .addOnFailureListener(e -> {
                    statusListener.onFailed(e.getMessage());
                    Log.d(TAG, "onFailure: create food order error -> "+e.getStackTrace());
                });
    }

    @Override
    public void createCompletedFoodOrder(CompletedFoodOrder foodOrder, DatabaseOperationStatusListener<Void, String> statusListener) {

        DatabaseReference ref = mDatabase.getReference().child(NosqlDatabasePathsUtil.COMPLETED_FOOD_ORDERS_NODE);

        ref.child(foodOrder.getmFoodOrderId()).setValue(foodOrder)

                .addOnSuccessListener(aVoid -> statusListener.onSuccess(null))

                .addOnFailureListener(e -> {
                    statusListener.onFailed(e.getMessage());
                    Log.d(TAG, "onFailure: create complete food order error -> "+e.getStackTrace());
                });
    }

    @Override
    public void createRejectedFoodOrder(RejectedFoodOrder foodOrder, DatabaseOperationStatusListener<Void, String> statusListener) {

        DatabaseReference ref = mDatabase.getReference().child(NosqlDatabasePathsUtil.REJECTED_FOOD_ORDERS_NODE);

        ref.child(foodOrder.getmFoodOrderId()).setValue(foodOrder)

                .addOnSuccessListener(aVoid -> statusListener.onSuccess(null))

                .addOnFailureListener(e -> {
                    statusListener.onFailed(e.getMessage());
                    Log.d(TAG, "onFailure: create rejected food order error -> "+e.getStackTrace());
                });
    }

    @Override
    public void readFoodOrdersForCustomer(String customerUid, DatabaseOperationStatusListener<Void, String> statusListener,
                                          ListDataChangeListener<FoodOrder> dataChangeListener) {

        Query query = mDatabase.getReference().child(NosqlDatabasePathsUtil.FOOD_ORDERS_NODE)
                .orderByChild(NosqlDatabasePathsUtil.FOOD_ORDERS_CUSTOMER_UID).equalTo(customerUid);

        final boolean[] dataReadSuccess = {false};

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if(!dataReadSuccess[0]){
                    dataReadSuccess[0] = true;
                    statusListener.onSuccess(null);
                }

                FoodOrder foodOrder = snapshot.getValue(FoodOrder.class);

                dataChangeListener.onDataAdded(foodOrder);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                dataChangeListener.onDataUpdated(snapshot.getValue(FoodOrder.class));
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                dataChangeListener.onDataRemoved(snapshot.getValue(FoodOrder.class));
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.d(TAG, "onCancelled: read customer food orders error -> "+error.getDetails());

                statusListener.onFailed(error.getMessage());
            }
        });

    }

    @Override
    public void updateWithId(FoodOrder foodOrder, String id, DatabaseOperationStatusListener<Void, String> listener) {

        mDatabase.getReference().child(NosqlDatabasePathsUtil.FOOD_ORDERS_NODE + "/" + id)
                .setValue(foodOrder)

                .addOnCompleteListener(task -> listener.onSuccess(null))

                .addOnFailureListener(e -> {

                    Log.d(TAG, "onFailure: order update failed -> "+e.getStackTrace());

                    listener.onFailed(e.getMessage());
                });
    }


    @Override
    public void readFoodOrdersForChef(String chefUid, DatabaseOperationStatusListener<Void, String> statusListener,
                                      ListDataChangeListener<FoodOrder> dataChangeListener) {

        Query query = mDatabase.getReference().child(NosqlDatabasePathsUtil.FOOD_ORDERS_NODE)
                .orderByChild(NosqlDatabasePathsUtil.FOOD_ORDERS_CHEF_UID).equalTo(chefUid);

        final boolean[] dataReadSuccess = {false};

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if(!dataReadSuccess[0]){
                    dataReadSuccess[0] = true;
                    statusListener.onSuccess(null);
                }

                FoodOrder foodOrder = snapshot.getValue(FoodOrder.class);

                dataChangeListener.onDataAdded(foodOrder);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                dataChangeListener.onDataUpdated(snapshot.getValue(FoodOrder.class));
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                dataChangeListener.onDataRemoved(snapshot.getValue(FoodOrder.class));
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.d(TAG, "onCancelled: read chef food order error -> "+error.getDetails());

                statusListener.onFailed(error.getMessage());
            }
        });

    }

    @Override
    public void deleteFoodOrder(String id, DatabaseOperationStatusListener<Void, String> statusListener) {

        mDatabase.getReference().child(NosqlDatabasePathsUtil.FOOD_ORDERS_NODE + "/" + id).setValue(null)

                .addOnSuccessListener(aVoid -> statusListener.onSuccess(null))

                .addOnFailureListener(e -> {
                    statusListener.onFailed(e.getMessage());
                    Log.d(TAG, "onFailure: error deleting food item -> "+e.getStackTrace());
                });
    }
}
