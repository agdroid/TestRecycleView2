package com.example.andre.testrecycleview2.model;

/**
 * Java Repräsentation of our data to be displayed in the RecyclerView
 * Created by andre on 25.12.2016.
 */

public class ListItem {
    private String title;
    private String subTitle;
    private int imageResId;
    private boolean favourite = false;

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

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
