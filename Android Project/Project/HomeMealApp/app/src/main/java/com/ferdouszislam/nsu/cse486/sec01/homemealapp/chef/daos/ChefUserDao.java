package com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.daos;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.models.ChefUser;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.DatabaseOperationStatusListener;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.SingleDataChangeListener;

public interface ChefUserDao {

    void createWithId(ChefUser chefUser, DatabaseOperationStatusListener<Void, String> listener);

    void readWithId(String uid, SingleDataChangeListener<ChefUser> dataListener,
                    DatabaseOperationStatusListener<Void, String> statusListener);

}
