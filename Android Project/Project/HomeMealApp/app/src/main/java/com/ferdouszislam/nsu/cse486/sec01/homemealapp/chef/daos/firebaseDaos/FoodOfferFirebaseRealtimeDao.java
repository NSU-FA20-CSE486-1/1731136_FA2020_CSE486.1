package com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.daos.firebaseDaos;

import android.util.Log;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.daos.FoodOfferDao;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.models.ChefUser;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.models.FoodOffer;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.DatabaseOperationStatusListener;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.ListDataChangeListener;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils.NosqlDatabasePathsUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class FoodOfferFirebaseRealtimeDao implements FoodOfferDao {

    private static final String TAG = "FOFRD-debug";

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

    @Override
    public void createFoodOffer(FoodOffer foodOffer, DatabaseOperationStatusListener<Void, String> statusListener) {

        DatabaseReference ref = mDatabase.getReference().child(NosqlDatabasePathsUtil.FOOD_OFFERS_NODE);

        String foodOfferId = ref.push().getKey();

        foodOffer.setId(foodOfferId);

        ref.child(foodOfferId).setValue(foodOffer)

                .addOnSuccessListener(aVoid -> statusListener.onSuccess(null))

                .addOnFailureListener(e -> {
                    statusListener.onFailed(e.getMessage());
                    Log.d(TAG, "onFailure: create food offer error -> "+e.getStackTrace());
                });
    }

    @Override
    public void readFoodOffers(DatabaseOperationStatusListener<Void, String> statusListener,
                               ListDataChangeListener<FoodOffer> dataChangeListener) {

        DatabaseReference ref = mDatabase.getReference().child(NosqlDatabasePathsUtil.FOOD_OFFERS_NODE);

        final boolean[] dataReadSuccess = {false};

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if(!dataReadSuccess[0]){
                    dataReadSuccess[0] = true;
                    statusListener.onSuccess(null);
                }

                FoodOffer foodOffer = snapshot.getValue(FoodOffer.class);

                dataChangeListener.onDataAdded(foodOffer);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                dataChangeListener.onDataUpdated(snapshot.getValue(FoodOffer.class));
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                dataChangeListener.onDataRemoved(snapshot.getValue(FoodOffer.class));
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.d(TAG, "onCancelled: read chef food offers error -> "+error.getDetails());

                statusListener.onFailed(error.getMessage());
            }
        });

    }

    @Override
    public void readFoodOffersForChef(String chefUid, DatabaseOperationStatusListener<Void, String> statusListener,
                                      ListDataChangeListener<FoodOffer> dataChangeListener) {

        Query query = mDatabase.getReference().child(NosqlDatabasePathsUtil.FOOD_OFFERS_NODE)
                .orderByChild(NosqlDatabasePathsUtil.FOOD_OFFERS_CHEF_UID).equalTo(chefUid);

        final boolean[] dataReadSuccess = {false};

        query.getRef().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if(!dataReadSuccess[0]){
                    dataReadSuccess[0] = true;
                    statusListener.onSuccess(null);
                }

                FoodOffer foodOffer = snapshot.getValue(FoodOffer.class);

                dataChangeListener.onDataAdded(foodOffer);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                dataChangeListener.onDataUpdated(snapshot.getValue(FoodOffer.class));
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                dataChangeListener.onDataRemoved(snapshot.getValue(FoodOffer.class));
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.d(TAG, "onCancelled: read chef food offers error -> "+error.getDetails());

                statusListener.onFailed(error.getMessage());
            }
        });

    }

    @Override
    public void deleteFoodItem(String id, DatabaseOperationStatusListener<Void, String> statusListener) {

        mDatabase.getReference().child(NosqlDatabasePathsUtil.FOOD_OFFERS_NODE + "/" + id).setValue(null)

                .addOnSuccessListener(aVoid -> statusListener.onSuccess(null))

                .addOnFailureListener(e -> {
                    statusListener.onFailed(e.getMessage());
                    Log.d(TAG, "onFailure: error deleting food item -> "+e.getStackTrace());
                });
    }
}
