package com.ferdouszislam.nsu.cse486.sec01.homemealapp.daos;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.models.FoodOffer;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.DatabaseOperationStatusListener;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.ListDataChangeListener;

public interface FoodOfferDao {

    void createFoodOffer(FoodOffer foodOffer, DatabaseOperationStatusListener<Void, String> statusListener);

    void readFoodOffers(DatabaseOperationStatusListener<Void, String> statusListener,
                        ListDataChangeListener<FoodOffer> dataChangeListener);

    void readFoodOffersForChef(String chefUid, DatabaseOperationStatusListener<Void, String> statusListener,
                               ListDataChangeListener<FoodOffer> dataChangeListener);

    void deleteFoodItem(String id, DatabaseOperationStatusListener<Void, String> statusListener);
}
