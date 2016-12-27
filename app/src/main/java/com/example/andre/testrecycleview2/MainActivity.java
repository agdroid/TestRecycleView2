package com.example.andre.testrecycleview2;

import android.content.Context;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.andre.testrecycleview2.model.DummyData;
import com.example.andre.testrecycleview2.model.ListItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private List<ListItem> myDataset;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDataset = DummyData.getListData();

        //FAB nur mit CoordinatorLayout und compile 'com.android.support:design:25.0.1'
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Dialogfenster einbauen", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                ListItem item = new ListItem();
                item.setTitle("");
                item.setImageResId(android.R.drawable.ic_input_add);
                AlertDialog editItemDialog = createItemDialog(item);
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
        mAdapter = new MyAdapter(this, myDataset);
        mRecyclerView.setAdapter(mAdapter);

        final Menu m = (Menu) getActionBar();

    }


    // Grund für "final" deklarierte Variablen
    // Die innere Klasse onClick greift auch auf den übergebenen itemTitle aus der äußeren
    // Methode zu. itemTitle ist ein String und wird wie ein Object in Java als Referenz übergeben,
    //-> In der inneren Klasse könnte deshalb über die Referenz der Wert der aufrufenden Variablen
    // verändert werden. Mit dem Pflicht-"final" verhindert Java diese Zugriffe.

    private AlertDialog createItemDialog(final ListItem item) {
        final String title = item.getTitle();
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

                        ListItem itemNeu = new ListItem();
                        itemNeu.setImageResId(item.getImageResId());
                        itemNeu.setTitle("");

                        //Speichern der Änderung -> Zuerst möglichen alten Datensatz löschen
                        if (title.isEmpty()) {   //neues Element
                            itemNeu.setTitle(titleString);
                            myDataset.add(itemNeu);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            if(title.equals(titleString)) {  //Update
                                //Bisher leer, da nur der Titel geändert werden kann
                            } else {
                                itemNeu.setTitle(titleString);
                                itemNeu.setImageResId(item.getImageResId());
                                int pos = myDataset.indexOf(item);
                                myDataset.add(pos, itemNeu);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
