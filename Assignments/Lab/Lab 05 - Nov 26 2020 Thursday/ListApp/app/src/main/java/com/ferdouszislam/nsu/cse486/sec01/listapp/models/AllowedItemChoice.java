package com.ferdouszislam.nsu.cse486.sec01.listapp.models;

import com.ferdouszislam.nsu.cse486.sec01.listapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AllowedItemChoice {
// class to load allowed(hardcoded) item choices

    private static ArrayList<Item> itemList = new ArrayList<>();

    public static final String[] LABEL_CHOICES = {
            "Stark",
            "Jon Snow",
            "Lannister",
            "Tyrion",
            "Targeryen",
            "Daenerys",
            "Greyjoy",
            "Theon",
            "Tyrell",
            "Margaery"
    };

    public static final int[] IMAGE_CHOICES = {
            R.drawable.ic_house_stark,
            R.drawable.ic_jon_snow,
            R.drawable.ic_house_lannister,
            R.drawable.ic_tyrion,
            R.drawable.ic_house_targaryen,
            R.drawable.ic_daenerys,
            R.drawable.ic_house_greyjoy,
            R.drawable.ic_theon,
            R.drawable.ic_house_tyrell,
            R.drawable.ic_margaery
    };

    public static final String[] URLS = {
            "https://gameofthrones.fandom.com/wiki/House_Stark",
            "https://gameofthrones.fandom.com/wiki/Jon_Snow",
            "https://gameofthrones.fandom.com/wiki/House_Lannister",
            "https://gameofthrones.fandom.com/wiki/Tyrion_Lannister",
            "https://gameofthrones.fandom.com/wiki/House_Targaryen",
            "https://gameofthrones.fandom.com/wiki/Daenerys_Targaryen",
            "https://gameofthrones.fandom.com/wiki/House_Greyjoy",
            "https://gameofthrones.fandom.com/wiki/Theon_Greyjoy",
            "https://gameofthrones.fandom.com/wiki/House_Tyrell",
            "https://gameofthrones.fandom.com/wiki/Margaery_Tyrell"
    };

    private static HashMap<String, String> labelToURL = new HashMap<>();

    public static String getUrl(String label){
    // get URL for the label

        if(labelToURL.isEmpty()){
        // build the hashmap if it is empty
            for(int i=0;i<LABEL_CHOICES.length;i++)
                labelToURL.put(LABEL_CHOICES[i], URLS[i]);
        }

        if(!labelToURL.containsKey(label))
            return null;

        return labelToURL.get(label);
    }

    public static ArrayList<Item> getAllowedItemsList(){

        if(!itemList.isEmpty())
            return itemList;

        for(int i=0;i<10;i++){

            Item item = new Item(LABEL_CHOICES[i], new ImageResource(IMAGE_CHOICES[i]));
            itemList.add(item);
        }

        return itemList;
    }

}
