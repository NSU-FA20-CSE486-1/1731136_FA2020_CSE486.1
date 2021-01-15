package com.ferdouszislam.nsu.cse486.sec01.homemealapp.daos.firebaseDaos;

import android.util.Log;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.daos.ChefServiceChargePaymentDao;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.DatabaseOperationStatusListener;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.models.ChefServiceChargePayment;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils.NosqlDatabasePathsUtil;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChefServiceChargePaymentFirebaseRealtimeDao implements ChefServiceChargePaymentDao {

    private static final String TAG = "CSCPFRD-debug";

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

    @Override
    public void createServiceChargePaymentRecord(ChefServiceChargePayment chefServiceChargePayment,
                                                 DatabaseOperationStatusListener<Void, String> statusListener) {

        DatabaseReference ref = mDatabase.getReference().child(NosqlDatabasePathsUtil.CHEF_SERVICE_CHARGE_PAYMENT_RECORDS);

        String recordId = ref.push().getKey();

        chefServiceChargePayment.setId(recordId);

        ref.child(recordId).setValue(chefServiceChargePayment)

                .addOnSuccessListener(aVoid -> statusListener.onSuccess(null))

                .addOnFailureListener(e -> {
                    statusListener.onFailed(e.getMessage());
                    Log.d(TAG, "onFailure: create chef payment record error -> "+e.getStackTrace());
                });
    }
}
