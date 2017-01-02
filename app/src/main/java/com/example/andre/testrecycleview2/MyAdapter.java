package com.example.andre.testrecycleview2;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andre.testrecycleview2.model.ListItem;

import java.util.ArrayList;

/**
 * Created by andre on 18.12.2016.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private static final String TAG = "MyAdapter";

    private ArrayList<ListItem> mListData;
    private LayoutInflater mInflater;  //siehe wiseAss

    //Einbau Interface für Callback-Methoder der Klicks auf das
    // a) ganze WElement oder b) das secondaryItem
    private ItemClickCallback itemClickCallback;

    public interface ItemClickCallback {
        void onItemClick(int p);
        void onSecondaryItemClick(int p);
    }

    //Methode zur Zuweisung der privaten Variable "itemClickCallback"
    public void setItemClickCallback(final ItemClickCallback itemClickCallback) {
        this.itemClickCallback = itemClickCallback;
    }

    // SCHRITT #1: Erweitern des Standard-Klasse MyViewHolder mit gewünschten Eigenschaften
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // each data item is just a string in this case
        private TextView title;
        private TextView subtitle;
        private ImageView thumbnail;
        private ImageView secondaryIcon;
        private View container;  //Ist die View für click-events -> siehe wiseAss

        //constructor
        public MyViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.tv_title);
            subtitle = (TextView) itemView.findViewById(R.id.tv_subtitle);
            thumbnail = (ImageView) itemView.findViewById(R.id.im_item_icon);
            //Die nächsten beiden Elemente erhalten einen onClickListener..
            // -> MyViewHolder wurde deshalb mit "..implements View.OnClickListener" erweitert
            secondaryIcon = (ImageView) itemView.findViewById(R.id.im_item_icon_secondary);
            secondaryIcon.setOnClickListener(this);

            container = itemView.findViewById(R.id.container_item_root2);
            container.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId()== R.id.container_item_root2) {  //Klick auf ganze View
                //Hier wird die aktuelle Pos an die aufrufende Activity übertragen
                itemClickCallback.onItemClick(getAdapterPosition());
            }  else {  //Klick auf secondaryItem
                itemClickCallback.onSecondaryItemClick(getAdapterPosition());
            }
        }
    }


    //upadate von wiseAss -> Einfach die ganze Liste austauschen...
    //WICHTIG: Ist nicht notwendig, da Objekt direkt geändert wird und Kopie in MyAdapter
    // nicht notwendig ist. -> FEHLER bei wiseAss
/*
    public void setMyDataset(ArrayList<ListItem> exerciseList) {
        this.mListData.clear();
        this.mListData.addAll(exerciseList);
    }
*/
/*
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
*/

    // Provide a suitable constructor (depends on the kind of dataset)
    // Context der aufrufenden Activity wird mit übergeben.
    public MyAdapter(Context context, ArrayList<ListItem> dataset) {
        mListData = dataset;
        mInflater = LayoutInflater.from(context);
    }


    // SCHRITT #2: onCreateViewHolder -> Eigenen MyViewHolder erstellen
    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view -> Mit dem inflator der übergeordneten ViewGroup
        View v = mInflater.inflate(R.layout.row_layout2, parent, false);

        // set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }


    // SCHRITT #3: onBindViewHolder
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ListItem item = mListData.get(position);
        holder.title.setText(item.getTitle());
        holder.subtitle.setText(item.getSubTitle());
        if (item.isFavourite()) {
            holder.secondaryIcon.setImageResource(R.drawable.ic_star_black_24dp);
        } else {
            holder.secondaryIcon.setImageResource(R.drawable.ic_star_border_black_24dp);
        }
    }


    // SCHRITT #4: Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mListData.size();
    }


    //Testweise Snakebar wie https://gist.github.com/jirivrany/f1f177b7bbc187e8f980
    public  void onItemRemove(final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder) {
        final int adapterPosition = viewHolder.getAdapterPosition();
        final ListItem item = mListData.get(adapterPosition);

        Snackbar snackbar = Snackbar
                .make(recyclerView, "Remove", 3000)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = viewHolder.getAdapterPosition();
                        mListData.add(pos, item);
                        notifyItemInserted(pos);
                        recyclerView.scrollToPosition(pos);

                    }
                });
        snackbar.show();
        mListData.remove(adapterPosition);
        notifyItemRemoved(adapterPosition);

    }
}
