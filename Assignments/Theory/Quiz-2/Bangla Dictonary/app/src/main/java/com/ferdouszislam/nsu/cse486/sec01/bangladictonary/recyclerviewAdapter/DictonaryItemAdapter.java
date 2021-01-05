package com.ferdouszislam.nsu.cse486.sec01.bangladictonary.recyclerviewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ferdouszislam.nsu.cse486.sec01.bangladictonary.R;
import com.ferdouszislam.nsu.cse486.sec01.bangladictonary.models.DictonaryItem;

import java.util.ArrayList;

public class DictonaryItemAdapter extends RecyclerView.Adapter<DictonaryItemAdapter.ViewHolder> {

    private ArrayList<DictonaryItem> mDictonaryItems;
    private DeleteItemClickListener mDeleteItemClickListener;

    public DictonaryItemAdapter(ArrayList<DictonaryItem> mDictonaryItems, DeleteItemClickListener mDeleteItemClickListener) {
        this.mDictonaryItems = mDictonaryItems;
        this.mDeleteItemClickListener = mDeleteItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View dictonaryItemView = inflater.inflate(R.layout.dictonary_item_view, parent, false);

        return new ViewHolder(dictonaryItemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        DictonaryItem dictonaryItem = mDictonaryItems.get(position);

        holder.englishWordTextView.setText(dictonaryItem.getWordInEnglish());
        holder.banglaWordTextView.setText(dictonaryItem.getWordInBangla());

        holder.deleteButton.setOnClickListener(
                v -> mDeleteItemClickListener.onDeleteItemClick(dictonaryItem, position)
        );
    }

    @Override
    public int getItemCount() {
        return mDictonaryItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView englishWordTextView, banglaWordTextView;
        public ImageButton deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            englishWordTextView = itemView.findViewById(R.id.englishWord_TextView);
            banglaWordTextView = itemView.findViewById(R.id.banglaWord_TextView);
            deleteButton = itemView.findViewById(R.id.deleteItem_ImageButton);
        }
    }

    /*
    interface for activity to implement
    on click method of each item's "delete" button
     */
    public interface DeleteItemClickListener{

        void onDeleteItemClick(DictonaryItem dictonaryItem, int position);
    }
}
