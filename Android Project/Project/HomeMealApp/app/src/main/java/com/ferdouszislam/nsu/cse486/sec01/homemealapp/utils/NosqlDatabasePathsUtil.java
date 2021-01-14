package com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils;

/**
 * Class to contain public constant strings of database paths
 */
public abstract class NosqlDatabasePathsUtil {

    public static final String CHEF_USERS_NODE = "chefUsers";

    public static final String FOOD_OFFERS_NODE = "foodOffers";
    public static final String FOOD_OFFERS_CHEF_UID = "mChefUid";

    public static final String CUSTOMER_USERS_NODE = "customerUsers";

    public static final String FOOD_ORDERS_NODE = "foodOrders";
    public static final String FOOD_ORDERS_CUSTOMER_UID = "mCustomerUid";
    public static final String FOOD_ORDERS_CHEF_UID = "mChefUid";

    public static final String COMPLETED_FOOD_ORDERS_NODE = "completedFoodOrders";

    public static final String REJECTED_FOOD_ORDERS_NODE = "rejectedFoodOrders";

}
