package com.example.andre.testrecycleview2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by andre on 18.12.2016.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private static final String TAG = "MyAdapter";

    //private Context mAppContext;  //Context aus MainActivity übernommen
    private ArrayList<String> mDataset;
    private LayoutInflater mInflator;  //siehe wiseAss


    // SCHRITT #1: Erweitern des Standard-Klasse ViewHolder mit gewünschten Eigenschaften
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {


        // each data item is just a string in this case
        private TextView txtHeader;
        private TextView txtFooter;
        private View container;  //Ist die View für click-events -> siehe wiseAss


        //constructor
        public ViewHolder(View itemView) {
            super(itemView);


            txtHeader = (TextView) itemView.findViewById(R.id.firstLine);
            txtFooter = (TextView) itemView.findViewById(R.id.secondLine);
            //cast mit (View) wäre redundant, d.h. mehrfach genannt, weil Ergebnis schon eine View
            // ist und nicht z.Bsp. eine spezielle TextView
            container = itemView.findViewById(R.id.container_item_root);


            //TODO: Hier weitere Rückmeldungen einbauen -> Oder besser in onBindViewHolder ???
            //TODO: Verträgt sich das mit setOnClickListener in onBindViewHolder ?????????
            //TODO: Lösung: In onBindViewHolder läßt sich nur einzelnes Element mit "Klick"
            //       auswerten, nicht aber die View als solche

            //setOnClickListener kann rasu -> löschen wird jetzt in LongClick behandelt
            /*
            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    String name = mDataset.get(position);
                    Log.d(TAG, "Element " + position + " clicked.");
                    remove(name);
                }
            });
            */


//TODO: Gehört wohl ins Hauptmenü -> Dann wäre auch mAppContext nicht mehr erforderlich
/*
            itemView.setOnLongClickListener(new View.OnLongClickListener() {



                @Override
                public boolean onLongClick(View v) {

                    //Inflate CAB mit Edit und Delete

 //TODO:                   Problem: Menü muss bereits in der MainActivity gesetzt wein und wird hier nur ergänzt!




                    //Feld einfärben
                    CharSequence text = "Das ist der Longclick!";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(mAppContext, text, duration);
                    toast.show();



                    return false;
                }



            });
*/
        }
    }

    //AG: Einfügen neuer Element
    public void add(int position, String item) {
        mDataset.add(position, item);
        notifyItemInserted(position);
    }

    //AG: Löschen eines Elements
    public void remove(String item) {
        int position = mDataset.indexOf(item);
        mDataset.remove(position);
        notifyItemRemoved(position);
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(Context context, ArrayList<String> myDataset) {
        mDataset = myDataset;
        mInflator = LayoutInflater.from(context);
        //mAppContext = context;
    }


    // SCHRITT #2: onCreateViewHolder -> Eigenen ViewHolder erstellen
    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view -> Mit dem inflator der übergeordneten ViewGroup
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);

        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    // SCHRITT #3: onBindViewHolder
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Log.d(TAG, "Element " + position + " set.");

        final String name = mDataset.get(position);
        holder.txtHeader.setText(mDataset.get(position));
        holder.txtFooter.setText("Foooter: " + mDataset.get(position));

        //Beachte: Klick auf txtHeader, nicht auf ganzes Element
        /*
        holder.txtHeader.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(name);
            }
        });
        */

    }


    // SCHRITT #4: Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
