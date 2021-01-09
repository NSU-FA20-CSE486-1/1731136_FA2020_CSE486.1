package com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.daos;

import android.util.Log;

import androidx.annotation.NonNull;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.models.ChefUser;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.DatabaseOperationStatusListener;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils.NosqlDatabasePaths;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;

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
}
