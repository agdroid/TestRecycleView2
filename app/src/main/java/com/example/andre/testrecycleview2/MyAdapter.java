package com.example.andre.testrecycleview2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andre.testrecycleview2.model.DummyData;
import com.example.andre.testrecycleview2.model.ListItem;

import java.util.List;

/**
 * Created by andre on 18.12.2016.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private static final String TAG = "MyAdapter";

    //private Context mAppContext;  //Context aus MainActivity übernommen
    //private ArrayList<String> mDataset;
    private List<ListItem> mListData;
    private LayoutInflater mInflator;  //siehe wiseAss


    // SCHRITT #1: Erweitern des Standard-Klasse MyViewHolder mit gewünschten Eigenschaften
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class MyViewHolder extends RecyclerView.ViewHolder {

        // each data item is just a string in this case
        private TextView txtFooter;
        private TextView title;  //wiseAss
        private ImageView icon;  //wiseAss
        private View container;  //Ist die View für click-events -> siehe wiseAss

        //constructor
        public MyViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.tv_title);
            txtFooter = (TextView) itemView.findViewById(R.id.secondLine);
            container = itemView.findViewById(R.id.container_item_root);

        }
    }

    //AG: Einfügen neuer Element
    public void add(int position, ListItem item) {
        mListData.add(position, item);
        notifyItemInserted(position);
    }

    //AG: Löschen eines Elements
    public void remove(ListItem item) {
        int position = mListData.indexOf(item);
        mListData.remove(position);
        notifyItemRemoved(position);
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    // Context der aufrufenden Activity wird mit übergeben.
    public MyAdapter(Context context, List<ListItem> dataset) {
        mListData = dataset;
        mInflator = LayoutInflater.from(context);
    }


    // SCHRITT #2: onCreateViewHolder -> Eigenen MyViewHolder erstellen
    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view -> Mit dem inflator der übergeordneten ViewGroup
        View v = mInflator.inflate(R.layout.row_layout2, parent, false);

        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }


    // SCHRITT #3: onBindViewHolder
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Log.d(TAG, "Element " + position + " set.");

        ListItem item = mListData.get(position);
        holder.title.setText(item.getTitle());
        holder.txtFooter.setText("Foooter: " + item.getTitle());
        holder.icon.setImageResource(item.getImageResId());
    }


    // SCHRITT #4: Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mListData.size();
    }
}
