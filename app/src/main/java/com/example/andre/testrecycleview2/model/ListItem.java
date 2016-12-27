package com.example.andre.testrecycleview2.model;

/**
 * Java Repräsentation of our data to be displayed in the RecyclerView
 * Created by andre on 25.12.2016.
 */

public class ListItem {
    private String title;
    private int imageResId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }
}
