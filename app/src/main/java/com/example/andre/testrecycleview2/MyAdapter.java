package com.example.andre.testrecycleview2;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by andre on 18.12.2016.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private static final String TAG = "MyAdapter";
    private ArrayList<String> mDataset;

    // SCHRITT #1: Erweitern des Standard-Klasse ViewHolder mit gewünschten Eigenschaften
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {

        // each data item is just a string in this case
        public TextView txtHeader;
        public TextView txtFooter;

        public ViewHolder(View itemView) {
            super(itemView);
            txtHeader = (TextView) itemView.findViewById(R.id.firstLine);
            txtFooter = (TextView) itemView.findViewById(R.id.secondLine);

            //TODO: Hier weitere Rückmeldungen einbauen
            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.")
                }
            });
        }
    }

    //AG: Einfügen neuer Element
    public void add(int position, String item) {
        mDataset.add(position, item);
        notifyItemChanged(position);
    }

    //AG: Löschen eines Elements
    public void remove(String item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<String> myDataset) {
        mDataset = myDataset;
    }


    // SCHRITT #2: onCreateViewHolder -> Eigenen ViewHolder erstellen
    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view -> Mit dem inflator der übergeordneten ViewGroup
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout,parent, false);

        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    // SCHRITT #3: onBindViewHolder
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
