package com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.daos;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.models.FoodOffer;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.DatabaseOperationStatusListener;

public interface FoodOfferDao {

    void createFoodOffer(FoodOffer foodOffer, DatabaseOperationStatusListener<Void, String> statusListener);

}
