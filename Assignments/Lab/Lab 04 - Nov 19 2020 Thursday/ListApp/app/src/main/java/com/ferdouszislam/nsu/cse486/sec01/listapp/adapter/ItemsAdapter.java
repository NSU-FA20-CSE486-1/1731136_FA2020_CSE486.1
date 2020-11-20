package com.ferdouszislam.nsu.cse486.sec01.listapp.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ferdouszislam.nsu.cse486.sec01.listapp.R;
import com.ferdouszislam.nsu.cse486.sec01.listapp.models.Item;

import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {
// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views

    private static final String TAG = "ItemAdapter-debug";

    public class ViewHolder extends RecyclerView.ViewHolder{
    // Your holder should contain a member variable
    // for any view that will be set as you render a row

        public ImageView icon_imageView;
        public Button name_Button;

        public ViewHolder(View itemView){
            super(itemView);

            icon_imageView = itemView.findViewById(R.id.itemIcon_ImageView);
            name_Button = itemView.findViewById(R.id.itemName_Button);
        }

    }

    // list model to show
    private List<Item> itemsList;
    // activity containing this adapter
    private ISingleItemUserInteraction callerActivityUI;

    public ItemsAdapter(List<Item> itemsList, ISingleItemUserInteraction callerActivityUI) {
        this.itemsList = itemsList;
        this.callerActivityUI = callerActivityUI;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    // Usually involves inflating a layout from XML and returning the holde
    // called when adapter is created

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_view, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    // Involves populating data into the item through holder

        // Get the data model based on position
        Item item = itemsList.get(position);

        // Set item views based on your views and data model
        Button name_Button = holder.name_Button;
        name_Button.setText(item.getLabel());

        // name button onClick
        name_Button.setOnClickListener(v -> callerActivityUI.onItemNameButtonClick(item));

        Log.d(TAG, "onBindViewHolder: image resource id = "+item.getImageResource().getResource_id());
        ImageView item_icon = holder.icon_imageView;
        item_icon.setImageResource(item.getImageResource().getResource_id());
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }
}
