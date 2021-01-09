package com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.daos;

import android.util.Log;

import androidx.annotation.NonNull;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.models.ChefUser;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.DatabaseOperationStatusListener;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.SingleDataChangeListener;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils.NosqlDatabasePaths;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChefUserFirebaseRealtimeDao implements ChefUserDao {

    private static final String TAG = "CUFRD-debug";

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

    @Override
    public void createWithId(ChefUser chefUser, DatabaseOperationStatusListener<Void, String> listener) {

        mDatabase.getReference().child(NosqlDatabasePaths.CHEF_USER_NODE + "/" + chefUser.getmUid())
                .setValue(chefUser)

                .addOnSuccessListener(listener::onSuccess)

                .addOnFailureListener(e -> {

                    listener.onFailed(e.getMessage());
                    Log.d(TAG, "onFailure: failed to create chefuser -> "+e.getStackTrace());
                });

    }

    @Override
    public void readWithId(String id, SingleDataChangeListener<ChefUser> dataListener,
                           DatabaseOperationStatusListener<Void, String> statusListener) {

        mDatabase.getReference().child(NosqlDatabasePaths.CHEF_USER_NODE + "/" + id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(!snapshot.exists()){

                            statusListener.onFailed("data with id not found");
                        }

                        ChefUser chefUser = snapshot.getValue(ChefUser.class);
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
