package com.ferdouszislam.nsu.cse486.sec01.listapp.models;

public class Item {

    private String label;
    private ImageResource imageResource;
    

    public Item() {
    }

    public Item(String label, ImageResource imageResource) {
        this.label = label;
        this.imageResource = imageResource;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public ImageResource getImageResource() {
        return imageResource;
    }

    public void setImageResource(ImageResource imageResource) {
        this.imageResource = imageResource;
    }

}
