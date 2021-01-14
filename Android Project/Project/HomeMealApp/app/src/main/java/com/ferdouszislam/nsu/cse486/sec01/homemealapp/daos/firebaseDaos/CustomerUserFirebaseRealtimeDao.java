package com.ferdouszislam.nsu.cse486.sec01.homemealapp.daos.firebaseDaos;

import android.util.Log;

import androidx.annotation.NonNull;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.daos.CustomerUserDao;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.models.CustomerUser;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.DatabaseOperationStatusListener;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.SingleDataChangeListener;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils.NosqlDatabasePathsUtil;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CustomerUserFirebaseRealtimeDao implements CustomerUserDao {

    private static final String TAG = "CustUFRD-debug";

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

    @Override
    public void createWithId(CustomerUser customerUser, DatabaseOperationStatusListener<Void, String> listener) {

        mDatabase.getReference().child(NosqlDatabasePathsUtil.CUSTOMER_USERS_NODE + "/" + customerUser.getmUid())
                .setValue(customerUser)

                .addOnSuccessListener(listener::onSuccess)

                .addOnFailureListener(e -> {

                    listener.onFailed(e.getMessage());
                    Log.d(TAG, "onFailure: failed to create chefuser -> "+e.getStackTrace());
                });

    }

    @Override
    public void updateWithId(CustomerUser customerUser, String id, DatabaseOperationStatusListener<Void, String> listener) {

        mDatabase.getReference().child(NosqlDatabasePathsUtil.CUSTOMER_USERS_NODE + "/" + id)
                .setValue(customerUser)

                .addOnCompleteListener(task -> listener.onSuccess(null))

                .addOnFailureListener(e -> {

                    Log.d(TAG, "onFailure: user update failed -> "+e.getStackTrace());

                    listener.onFailed(e.getMessage());
                });
    }

    @Override
    public void readWithId(String id, SingleDataChangeListener<CustomerUser> dataListener,
                           DatabaseOperationStatusListener<Void, String> statusListener) {

        mDatabase.getReference().child(NosqlDatabasePathsUtil.CUSTOMER_USERS_NODE + "/" + id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(!snapshot.exists()){

                            statusListener.onFailed("data with id not found");
                            return;
                        }

                        CustomerUser chefUser = snapshot.getValue(CustomerUser.class);
                        dataListener.onDataChange(chefUser);
                        statusListener.onSuccess(null);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        statusListener.onFailed(error.getMessage());

                        Log.d(TAG, "onCancelled: user with id read->"+error.getDetails());
                    }
                });

    }
}
