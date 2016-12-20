package com.example.andre.testrecycleview2;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<String> myDataset = new ArrayList<String>(Arrays.asList("bla1", "bla2",
            "bla3", "bla4", "bla5", "bla6", "bla7", "bla8",
            "bla9", "bla10", "bla11", "bla12", "bla13", "bla14"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //FAB nur mit CoordinatorLayout und compile 'com.android.support:design:25.0.1'
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Dialogfenster einbauen", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                AlertDialog editItemDialog = createItemDialog("");
                editItemDialog.show();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        //use a linear LayoutManager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //specify an adapter (see also next example)
        mAdapter = new MyAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);
    }


    // Grund für "final" deklarierte Variablen
    // Die innere Klasse onClick greift auch auf den übergebenen itemTitle aus der äußeren
    // Methode zu. itemTitle ist ein String und wird wie ein Object in Java als Referenz übergeben,
    //-> In der inneren Klasse könnte deshalb über die Referenz der Wert der aufrufenden Variablen
    // verändert werden. Mit dem Pflicht-"final" verhindert Java diese Zugriffe.
    private AlertDialog createItemDialog(final String title) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //Inflator der Activity aufrufen
        LayoutInflater inflater = getLayoutInflater();

        View dialogsView = inflater.inflate(R.layout.dialog_edit_item, null);

        //Achtung: Hier wieder "final", siehe oben
        final EditText editTextTitle = (EditText) dialogsView.findViewById(R.id.editText_dialog_title);

        //Bisheriger Wert wird in Dialogfeld eingetragen
        editTextTitle.setText(title);

        AlertDialog.Builder builder1 = builder.setView(dialogsView)
                .setTitle(R.string.dialog_item_title)
                .setPositiveButton(R.string.dialog_button_positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String titleString = editTextTitle.getText().toString();

                        if (TextUtils.isEmpty(titleString)) {
                            Log.d(LOG_TAG, "Ein Eintrag enthielt keinen Text. Deshalb Abbruch der Änderung.");
                            return;
                        }
                        //Speichern der Änderung -> Zuerst möglichen alten Datensatz löschen
                        if (title.isEmpty()) {   //neues Element
                            myDataset.add(titleString);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            if(title.equals(titleString)) {  //Update
                                //Bisher leer, da nur der Titel geändert werden kann
                            } else {
                                int pos = myDataset.indexOf(title);
                                myDataset.add(pos, titleString);
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                     }
                })
                .setNegativeButton(R.string.dialog_button_negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        return builder.create();
    }

}
