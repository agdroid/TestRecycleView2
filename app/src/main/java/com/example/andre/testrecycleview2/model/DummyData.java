package com.example.andre.testrecycleview2.model;

/**
 * Created by andre on 25.12.2016.
 */

import com.example.andre.testrecycleview2.R;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a dummy data source, used to simulate the kind of input you might recieve from a
 * Database or Web source.
 * Created by Ryan on 01/03/2016.
 */
public class DummyData {

    private static final String[] titles = {
            "blabla",
            "blabla",
            "blabla"};
    private static final int[] icons = {
            android.R.drawable.ic_popup_reminder,
            android.R.drawable.ic_menu_add,
            android.R.drawable.ic_menu_delete};

    private static final int icon = R.drawable.ic_tonality_black_36dp;

    public static ArrayList<ListItem> getListData() {
        ArrayList<ListItem> data = new ArrayList<>();
        int nr = 0;

        //Repeat process 4 times, so that we have enough data to demonstrate a scrollable
        //RecyclerView
        for (int x = 0; x < 4; x++) {
            //create ListItem with dummy data, then add them to our List
            for (int i = 0; i < titles.length && i < icons.length; i++) {
                ListItem item = new ListItem();
                item.setImageResId(icons[i]);
                item.setTitle(titles[i] + " " + nr);
                item.setSubTitle("Subtitle: " + titles[i] + " " + nr);
                data.add(item);
                nr++;
            }
        }
        return data;
    }
}