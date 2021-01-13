package com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.daos.firebaseDaos;

import android.util.Log;


import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.daos.FoodOfferDao;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.models.FoodOffer;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.DatabaseOperationStatusListener;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils.NosqlDatabasePathsUtil;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
}
