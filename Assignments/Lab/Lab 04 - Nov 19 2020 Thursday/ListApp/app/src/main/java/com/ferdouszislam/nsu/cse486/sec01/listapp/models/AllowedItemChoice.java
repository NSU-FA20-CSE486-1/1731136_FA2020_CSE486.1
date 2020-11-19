package com.ferdouszislam.nsu.cse486.sec01.listapp.models;

import com.ferdouszislam.nsu.cse486.sec01.listapp.R;

import java.util.List;

public class AllowedItemChoice {
// class to load allowed(hardcoded) item choices

    private static List<Item> itemList;

    private static final String[] LABEL_CHOICES = {
            "Stark", "Jon Snow",
            "Lannister", "Tyrion",
            "Targeryen", "Daeneris",
            "Greyjoy", "Theon",
            "Tyrell", "Margery"
    };

    private static final int[] IMAGE_CHOICES = {
            R.drawable.ic_house_stark, R.drawable.ic_jon_snow,

            R.drawable.ic_house_lannister, R.drawable.ic_tyrion,

            R.drawable.ic_house_targaryen, R.drawable.ic_daenerys,

            R.drawable.ic_house_greyjoy, R.drawable.ic_theon,

            R.drawable.ic_house_tyrell, R.drawable.ic_margaery
    };

    public static List<Item> getAllowedItemsList(){

        if(!itemList.isEmpty())
            return itemList;

        for(int i=0;i<10;i++){

            Item item = new Item(LABEL_CHOICES[i], new ImageResource(IMAGE_CHOICES[i]));
            itemList.add(item);
        }

        return itemList;
    }

}
