package com.ferdouszislam.nsu.cse486.sec01.homemealapp.customer.recyclerViewAdapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.R;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.daos.FoodOrderDao;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.daos.firebaseDaos.FoodOrderFirebaseRealtimeDao;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.DatabaseOperationStatusListener;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.ListDataChangeListener;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.models.FoodOrder;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.utils.OrderStatus;

import java.util.ArrayList;

public class CustomerPlacedOrdersAdapter extends RecyclerView.Adapter<CustomerPlacedOrdersAdapter.ViewHolder> {

    private static final String TAG = "CustPOAd-debug";

    // caller activity/fragment callbacks
    private CustomerPlacedOrdersAdapter.CallerCallback mCaller;

    private boolean isDataListEmpty = true;

    // model
    private ArrayList<FoodOrder> mFoodOrders;
    private String mCustomerUid;

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

                    if(data.getmOrderStatus().equals(OrderStatus.DELIVERED) || data.getmOrderStatus().equals(OrderStatus.REJECTED)){

                        mFoodOrders.add(data);
                        notifyItemInserted(mFoodOrders.size()-1);
                    }

                    else {
                        mFoodOrders.add(0, data);
                        notifyItemInserted(0);
                    }
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


    public CustomerPlacedOrdersAdapter(String customerUid, CustomerPlacedOrdersAdapter.CallerCallback mCaller) {
        this.mCaller = mCaller;
        this.mCustomerUid = customerUid;
        this.mFoodOrders = new ArrayList<>();

        loadFoodOrders();
    }

    private void loadFoodOrders() {

        mFoodOrderDao = new FoodOrderFirebaseRealtimeDao();
        mFoodOrderDao.readFoodOrdersForCustomer(

                mCustomerUid,

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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.customer_placed_order_item_view, parent, false);

        // Return a new holder instance
        return new CustomerPlacedOrdersAdapter.ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        FoodOrder foodOrder = mFoodOrders.get(position);

        holder.foodNameTextView.setText(foodOrder.getmFoodName());
        holder.orderStatusTextView.setText(foodOrder.getmOrderStatus());
        holder.priceTextView.setText(foodOrder.getmPaymentAmount());

        holder.chefPhoneTextView.setText(foodOrder.getmChefPhone());
        holder.chefPhoneTextView.setOnClickListener(v -> mCaller.contactVendorClick(foodOrder.getmChefPhone()));

        String quantity = foodOrder.getmQuantityUnitsSelectedByCustomer() + " X " + foodOrder.getmQuantityPerUnit();
        holder.quantityTextView.setText(quantity);

        holder.timeStampTextView.setText(foodOrder.getmTimeStamp());

        holder.payNowButton.setOnClickListener(v -> mCaller.onPayNowClick(foodOrder));

        if(!foodOrder.getmOrderStatus().equals(OrderStatus.ON_THE_WAY)){

            holder.payNowButton.setEnabled(false);

            if(foodOrder.getmOrderStatus().equals(OrderStatus.DELIVERED) || foodOrder.getmOrderStatus().equals(OrderStatus.REJECTED)){
                holder.payNowButton.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mFoodOrders.size();
    }


    public interface CallerCallback{

        void onPayNowClick(FoodOrder foodOrder);
        void contactVendorClick(String phoneNumber);
        void onPlacedOrdersListNotEmpty();
        void onFailedToLoadPlacedOrders();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView foodNameTextView, priceTextView, quantityTextView, chefPhoneTextView;
        public TextView orderStatusTextView, timeStampTextView;
        public Button payNowButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            foodNameTextView = itemView.findViewById(R.id.customer_foodName_orders_TextView);
            priceTextView = itemView.findViewById(R.id.customer_price_orders_TextView);
            quantityTextView = itemView.findViewById(R.id.customer_quantity_orders_TextView);
            orderStatusTextView = itemView.findViewById(R.id.customer_deliveryStatus_orders_TextView);
            chefPhoneTextView = itemView.findViewById(R.id.customer_orderPhone_orders_TextView);
            timeStampTextView = itemView.findViewById(R.id.customer_timeStamp_orders_TextView);
            payNowButton = itemView.findViewById(R.id.customer_payNow_orders_Button);
        }
    }
}
