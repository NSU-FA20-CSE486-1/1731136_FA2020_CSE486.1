package com.ferdouszislam.nsu.cse486.sec01.homemealapp.customer.daos;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.customer.models.CustomerUser;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.DatabaseOperationStatusListener;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.SingleDataChangeListener;

public interface CustomerUserDao {

    void createWithId(CustomerUser customerUser, DatabaseOperationStatusListener<Void, String> listener);

    void updateWithId(CustomerUser customerUser, String id, DatabaseOperationStatusListener<Void, String> listener);

    void readWithId(String uid, SingleDataChangeListener<CustomerUser> dataListener,
                    DatabaseOperationStatusListener<Void, String> statusListener);
}
