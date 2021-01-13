package com.ferdouszislam.nsu.cse486.sec01.homemealapp.customer.recyclerViewAdapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.R;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.daos.FoodOfferDao;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.daos.firebaseDaos.FoodOfferFirebaseRealtimeDao;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.models.FoodOffer;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.recyclerViewAdapters.ChefFoodOffersAdapter;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.DatabaseOperationStatusListener;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.ListDataChangeListener;

import java.util.ArrayList;

public class FoodOffersAdapter extends RecyclerView.Adapter<FoodOffersAdapter.ViewHolder> {

    private static final String TAG = "CustFOAd-debug";

    // caller activity/fragment
    Context mContext;

    // caller activity/fragment callbacks
    private FoodOffersAdapter.CallerCallback mCaller;

    private boolean isDataListEmpty = true;

    // model
    private ArrayList<FoodOffer> mFoodOffers;

    // variables to read food offers from database
    private FoodOfferDao mFoodOfferDao;
    private ListDataChangeListener<FoodOffer> mFoodOfferListDataChangeListener =
            new ListDataChangeListener<FoodOffer>() {
                @Override
                public void onDataAdded(FoodOffer data) {

                    if(isDataListEmpty){
                        isDataListEmpty = false;
                        mCaller.onFoodOffersListNotEmpty();
                    }

                    Log.d(TAG, "onDataAdded: new data -> "+data.toString());

                    mFoodOffers.add(0, data);
                    notifyItemInserted(0);
                }

                @Override
                public void onDataUpdated(FoodOffer data) {

                    int updatePosition = -1;
                    for(int i=0;i<mFoodOffers.size();i++){

                        if(mFoodOffers.get(i).getId().equals(data.getId())){
                            updatePosition = i;
                            break;
                        }
                    }
                    if(updatePosition==-1) return;

                    mFoodOffers.set(updatePosition, data);
                    notifyItemChanged(updatePosition);
                }

                @Override
                public void onDataRemoved(FoodOffer data) {

                    int deletePosition = -1;
                    for(int i=0;i<mFoodOffers.size();i++){

                        if(mFoodOffers.get(i).getId().equals(data.getId())){
                            deletePosition = i;
                            break;
                        }
                    }
                    if(deletePosition==-1) return;

                    mFoodOffers.remove(deletePosition);
                    notifyItemRemoved(deletePosition);
                }
            };


    public FoodOffersAdapter(Context mContext, FoodOffersAdapter.CallerCallback mCaller) {
        this.mContext = mContext;
        this.mCaller = mCaller;
        this.mFoodOffers = new ArrayList<>();

        loadFoodOffers();
    }

    private void loadFoodOffers() {

        mFoodOfferDao = new FoodOfferFirebaseRealtimeDao();
        mFoodOfferDao.readFoodOffers(
                new DatabaseOperationStatusListener<Void, String>() {
                    @Override
                    public void onSuccess(Void successResponse) {
                        // kept blank intentionally
                    }

                    @Override
                    public void onFailed(String failedResponse) {

                        mCaller.onFailedToLoadFoodOffers();

                        Log.d(TAG, "onFailed: food offer read failed -> "+failedResponse);
                    }
                },
                mFoodOfferListDataChangeListener);
    }


    @NonNull
    @Override
    public FoodOffersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.customer_offered_food_item_view, parent, false);

        // Return a new holder instance
        return new FoodOffersAdapter.ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodOffersAdapter.ViewHolder holder, int position) {

        FoodOffer foodOffer = mFoodOffers.get(position);

        holder.foodNameTextView.setText(foodOffer.getmFoodName());

        String price = foodOffer.getmPrice()+ " TK";
        holder.priceTextView.setText(price);

        String location = foodOffer.getmRegion() + "(" + foodOffer.getmAddress() + ")";
        holder.locationTextView.setText(location);

        holder.quantityTextView.setText(foodOffer.getmQuantity());

        Glide.with(mContext)
                .load(foodOffer.getmFoodPhotoUrl())
                .placeholder(R.drawable.ic_action_loading)
                .override(300, 300)
                .fitCenter() // scale to fit entire image within ImageView
                .into(holder.photoImageView);

        holder.tagsTextView.setText(foodOffer.getmTags());

        holder.seeMoreButton.setOnClickListener(v -> mCaller.onSeeMoreClick(foodOffer));
    }

    @Override
    public int getItemCount() {
        return mFoodOffers.size();
    }

    public interface CallerCallback{

        void onSeeMoreClick(FoodOffer foodOffer);
        void onFoodOffersListNotEmpty();
        void onFailedToLoadFoodOffers();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView foodNameTextView, priceTextView, quantityTextView, tagsTextView, locationTextView;
        public ImageView photoImageView;
        public Button seeMoreButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            foodNameTextView = itemView.findViewById(R.id.customer_foodName_listItem_TextView);
            priceTextView = itemView.findViewById(R.id.customer_price_listItem_TextView);
            quantityTextView = itemView.findViewById(R.id.customer_quantity_listItem_TextView);
            tagsTextView = itemView.findViewById(R.id.customer_tags_listItem_TextView);
            photoImageView = itemView.findViewById(R.id.customer_foodImage_listItem_ImageView);
            locationTextView = itemView.findViewById(R.id.customer_location_listItem_TextView);
            seeMoreButton = itemView.findViewById(R.id.customer_foodOfferSeeMore_listItem_Button);
        }
    }
}
