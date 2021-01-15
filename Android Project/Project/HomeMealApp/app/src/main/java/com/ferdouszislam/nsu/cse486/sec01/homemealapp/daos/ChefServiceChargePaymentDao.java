package com.ferdouszislam.nsu.cse486.sec01.homemealapp.daos;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.DatabaseOperationStatusListener;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.models.ChefServiceChargePayment;

public interface ChefServiceChargePaymentDao {

    void createServiceChargePaymentRecord(ChefServiceChargePayment chefServiceChargePayment,
                                          DatabaseOperationStatusListener<Void, String> statusListener);

}
