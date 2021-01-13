package com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.RecyclerViewAdapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ferdouszislam.nsu.cse486.sec01.homemealapp.R;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.daos.FoodOfferDao;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.daos.firebaseDaos.FoodOfferFirebaseRealtimeDao;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.chef.models.FoodOffer;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.DatabaseOperationStatusListener;
import com.ferdouszislam.nsu.cse486.sec01.homemealapp.listeners.ListDataChangeListener;

import java.util.ArrayList;

public class ChefFoodOffersAdapter extends RecyclerView.Adapter<ChefFoodOffersAdapter.ViewHolder> {

    private static final String TAG = "CFOAd-debug";

    // caller activity/fragment callbacks
    private ChefFoodOffersAdapter.CallerCallback mCaller;

    private boolean isDataListEmpty = true;

    // model
    private String mChefUid;
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

    public ChefFoodOffersAdapter(CallerCallback mCaller, String mChefUid, ArrayList<FoodOffer> mFoodOffers) {
        this.mCaller = mCaller;
        this.mChefUid = mChefUid;
        this.mFoodOffers = mFoodOffers;

        loadChefFoodOffers();
    }

    private void loadChefFoodOffers() {

        mFoodOfferDao = new FoodOfferFirebaseRealtimeDao();
        mFoodOfferDao.readFoodOffersForChef(
                mChefUid,
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.chef_offered_food_item_view, parent, false);

        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        FoodOffer foodOffer = mFoodOffers.get(position);

        holder.foodNameTextView.setText(foodOffer.getmFoodName());
        holder.priceTextView.setText(foodOffer.getmPrice());
        holder.quantityTextView.setText(foodOffer.getmQuantity());

        // TODO: load the image

        holder.tagsTextView.setText(foodOffer.getmTags());

        holder.createVariationButton.setOnClickListener( v -> mCaller.onCreateVariantClick(foodOffer) );
        holder.deleteButton.setOnClickListener( v -> mCaller.onDeleteFoodOfferClick(foodOffer) );
    }

    @Override
    public int getItemCount() {
        return mFoodOffers.size();
    }

    public interface CallerCallback{

        void onCreateVariantClick(FoodOffer foodOffer);
        void onDeleteFoodOfferClick(FoodOffer foodOffer);
        void onFoodOffersListNotEmpty();
        void onFailedToLoadFoodOffers();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView foodNameTextView, priceTextView, quantityTextView, tagsTextView;
        public ImageView photoImageView;
        public ImageButton createVariationButton, deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            foodNameTextView = itemView.findViewById(R.id.chef_foodName_listItem_TextView);
            priceTextView = itemView.findViewById(R.id.chef_price_listItem_TextView);
            quantityTextView = itemView.findViewById(R.id.chef_quantity_listItem_TextView);
            tagsTextView = itemView.findViewById(R.id.chef_tag_listItem_TextView);
            photoImageView = itemView.findViewById(R.id.chef_foodImage_listItem_ImageView);
            createVariationButton = itemView.findViewById(R.id.chef_createVariant_ImageButton);
            deleteButton = itemView.findViewById(R.id.chef_deleteFoodItem_ImageButton);
        }
    }
}
