package com.ferdouszislam.nsu.cse486.sec01.homemealapp.daos;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.models.FoodOrder;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.DatabaseOperationStatusListener;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.ListDataChangeListener;

public interface FoodOrderDao {

    void createFoodOrder(FoodOrder foodOrder, DatabaseOperationStatusListener<Void, String> statusListener);

    void readFoodOrdersForCustomer(String customerUid, DatabaseOperationStatusListener<Void, String> statusListener,
                               ListDataChangeListener<FoodOrder> dataChangeListener);

    void readFoodOrdersForChef(String chefUid, DatabaseOperationStatusListener<Void, String> statusListener,
                               ListDataChangeListener<FoodOrder> dataChangeListener);

    void deleteFoodOrder(String id, DatabaseOperationStatusListener<Void, String> statusListener);

}
