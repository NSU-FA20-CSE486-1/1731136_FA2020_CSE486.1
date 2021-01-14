package com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.recyclerViewAdapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.R;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.customer.recyclerViewAdapters.PlacedOrdersAdapter;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.daos.FoodOrderDao;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.daos.firebaseDaos.FoodOrderFirebaseRealtimeDao;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.DatabaseOperationStatusListener;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.ListDataChangeListener;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.models.FoodOrder;

import java.util.ArrayList;

public class ChefPlacedOrdersAdapter extends RecyclerView.Adapter<ChefPlacedOrdersAdapter.ViewHolder> {

    private static final String TAG = "ChPOAd-debug";

    // caller activity/fragment callbacks
    private ChefPlacedOrdersAdapter.CallerCallback mCaller;

    private boolean isDataListEmpty = true;

    // model
    private ArrayList<FoodOrder> mFoodOrders;
    private String mChefUid;

    // variables to read food offers from database
    private FoodOrderDao mFoodOrderDao;
    private ListDataChangeListener<FoodOrder> mFoodOrderListDataChangeListener =
            new ListDataChangeListener<FoodOrder>() {
                @Override
                public void onDataAdded(FoodOrder data) {

                    if(isDataListEmpty){
                        isDataListEmpty = false;
                        mCaller.onPlacedOrdersListNotEmpty();
                    }

                    Log.d(TAG, "onDataAdded: new data -> "+data.toString());

                    mFoodOrders.add(0, data);
                    notifyItemInserted(0);
                }

                @Override
                public void onDataUpdated(FoodOrder data) {

                    int updatePosition = -1;
                    for(int i=0;i<mFoodOrders.size();i++){

                        if(mFoodOrders.get(i).getmFoodOrderId().equals(data.getmFoodOrderId())){
                            updatePosition = i;
                            break;
                        }
                    }
                    if(updatePosition==-1) return;

                    mFoodOrders.set(updatePosition, data);
                    notifyItemChanged(updatePosition);
                }

                @Override
                public void onDataRemoved(FoodOrder data) {

                    int deletePosition = -1;
                    for(int i=0;i<mFoodOrders.size();i++){

                        if(mFoodOrders.get(i).getmFoodOrderId().equals(data.getmFoodOrderId())){
                            deletePosition = i;
                            break;
                        }
                    }
                    if(deletePosition==-1) return;

                    mFoodOrders.remove(deletePosition);
                    notifyItemRemoved(deletePosition);
                }
            };


    public ChefPlacedOrdersAdapter(String chefUid, ChefPlacedOrdersAdapter.CallerCallback mCaller) {
        this.mCaller = mCaller;
        this.mChefUid = chefUid;
        this.mFoodOrders = new ArrayList<>();

        loadFoodOrders();
    }

    private void loadFoodOrders() {

        mFoodOrderDao = new FoodOrderFirebaseRealtimeDao();
        mFoodOrderDao.readFoodOrdersForChef(

                mChefUid,

                new DatabaseOperationStatusListener<Void, String>() {
                    @Override
                    public void onSuccess(Void successResponse) {
                        // kept blank intentionally
                    }

                    @Override
                    public void onFailed(String failedResponse) {

                        mCaller.onFailedToLoadPlacedOrders();

                        Log.d(TAG, "onFailed: food offer read failed -> "+failedResponse);
                    }
                },

                mFoodOrderListDataChangeListener
        );

    }


    @NonNull
    @Override
    public ChefPlacedOrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.chef_placed_order_item_view, parent, false);

        // Return a new holder instance
        return new ChefPlacedOrdersAdapter.ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChefPlacedOrdersAdapter.ViewHolder holder, int position) {

        FoodOrder foodOrder = mFoodOrders.get(position);

        holder.foodNameTextView.setText(foodOrder.getmFoodName());
        holder.customerLocationTextView.setText(foodOrder.getmCustomerLocation());
        holder.priceTextView.setText(foodOrder.getmPaymentAmount());

        holder.customerPhoneTextView.setText(foodOrder.getmCustomerContactPhone());
        holder.customerPhoneTextView.setOnClickListener(v -> mCaller.contactCustomerClick(foodOrder.getmCustomerContactPhone()));

        String quantity = foodOrder.getmQuantityUnitsSelectedByCustomer() + " X " + foodOrder.getmQuantityPerUnit();
        holder.quantityTextView.setText(quantity);

        holder.acceptButton.setOnClickListener(v -> mCaller.onAcceptOrderClick(foodOrder));
        holder.rejectButton.setOnClickListener(v -> mCaller.onRejectOrderClick(foodOrder));
    }

    @Override
    public int getItemCount() {
        return mFoodOrders.size();
    }



    public interface CallerCallback{

        void onAcceptOrderClick(FoodOrder foodOrder);
        void onRejectOrderClick(FoodOrder foodOrder);
        void onPlacedOrdersListNotEmpty();
        void onFailedToLoadPlacedOrders();
        void contactCustomerClick(String phoneNumber);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView foodNameTextView, priceTextView, quantityTextView, customerLocationTextView, customerPhoneTextView;
        public Button acceptButton, rejectButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            foodNameTextView = itemView.findViewById(R.id.chef_foodName_orders_TextView);
            priceTextView = itemView.findViewById(R.id.chef_foodPrice_orders_TextView);
            quantityTextView = itemView.findViewById(R.id.chef_quantity_orders_TextView);
            customerLocationTextView = itemView.findViewById(R.id.chef_customerLocation_orders_TextView);
            customerPhoneTextView = itemView.findViewById(R.id.chef_orderPhone_orders_TextView);
            acceptButton = itemView.findViewById(R.id.chef_acceptOrder_orders_Button);
            rejectButton = itemView.findViewById(R.id.chef_rejectOrder_orders_Button);
        }
    }
}
