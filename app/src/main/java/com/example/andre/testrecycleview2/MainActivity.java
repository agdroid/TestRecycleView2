package com.example.andre.testrecycleview2;

import android.app.LauncherActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.andre.testrecycleview2.model.DummyData;
import com.example.andre.testrecycleview2.model.ListItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MyAdapter.ItemClickCallback {
    private static final String BUNDLE_EXTRAS = "BUNDLE_EXTRAS";
    private static final String EXTRA_TITLE = "EXTRA_TITLE";
    private static final String EXTRA_SUBTITLE = "EXTRA_SUBTITLE";

    public static final String LOG_TAG = MainActivity.class.getSimpleName();

    private ArrayList myDataset;

    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDataset = (ArrayList) DummyData.getListData();

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        //use a linear LayoutManager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyAdapter(this, myDataset); //-> GEHT DOCH!
        // --> wiseAss arbeitet in MyAdapter wohl aus Versehen mit einer Kopie von MyDataset, dort mListItem genannt.
        // -> Die Änderungen an myDataset können aber besser direkt vorgenommen werden, weil myDataset
        //    ohnehin nur ein Objekt und damit eine Referenz ist
        //FEHLER in wiseAss: MyAdapter arbeitet in diesem Beispiel mit einer Kopie von myDataset. Wird mit einem
        //     Klick auf Favorite die Variable verändert, so geschieht das zuerst in der Kopie, die
        //     dann das Original myDataset komplett überschreibt.
        //     -> myDataset ist Object, deshalb würde bei Übergabe nur die Referenz übergeben,
        //     -> mit DummyData.getListData() wird deshalb eine "echte" Kopie in MyAdapter erstellt
//TODO: tmp        mAdapter = new MyAdapter(this, DummyData.getListData());

        mRecyclerView.setAdapter(mAdapter);

        //Interface aus MyAdapter
        //TODO: Warum steht hier "this"
        //TODO: -> MyAdapter: public void setItemClickCallback(final ItemClickCallback itemClickCallback)
        //      -> Offensichtlich werden dann die in dieser Activity implementierten Klick-Methoden aufgerufen
        mAdapter.setItemClickCallback(this);

        //Helper Klasse organisiert swipe, delete usw. in der RecyclerView
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallback());
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        //FAB nur mit CoordinatorLayout und compile 'com.android.support:design:25.0.1'
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Dialogfenster einbauen", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                /* Zum Rest Random Variante von wiseAss
                ListItem item = new ListItem();
                item.setTitle("");
                item.setImageResId(android.R.drawable.ic_input_add);
                AlertDialog editItemDialog = createItemDialog(item);
                editItemDialog.show();
                */

                addItemToList();
            }
        });
    }


    private ItemTouchHelper.Callback createHelperCallback() {

        //So sieht die Definition aus: ItemTouchHelper.SimpleCallback(int dragDirs, int swipeDirs)
        //Man kann auch eine Richtung weglassen... -> Dort muss ist die Zahl "0" einzusetzen
        //- ItemTouchHelper.SimpleCallback ist eine einfache "Schale" für die vorgegebene Funktion
        // ItemTouchHelper.Callback. Die wird nur noch erweitert, zBsp. um onMove, onSwipe...
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        moveItem(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                        return true;
                    }

                    @Override
                    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {

                        final View undo = viewHolder.itemView.findViewById(R.id.undo);

                        if (undo != null) {
                            TextView text = (TextView) viewHolder.itemView.findViewById(R.id.undo_text);
                            text.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //callbacks.onDismiss(..) //delete-Funktion
                                    deleteItem(viewHolder.getAdapterPosition());
                                    mRecyclerView.getAdapter().notifyItemRemoved(viewHolder.getAdapterPosition());
                                }
                            });

                            TextView button = (TextView) viewHolder.itemView.findViewById(R.id.undo_button);
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //Neuzeichnen sollte alten Zustand wiederherstellen
                                    mRecyclerView.getAdapter().notifyItemChanged(viewHolder.getAdapterPosition());
                                    clearView(mRecyclerView, viewHolder);
                                    undo.setVisibility(View.GONE);
                                }
                            });

                            undo.setVisibility(View.VISIBLE); //View.VISIBLE
                            //undo.bringToFront();
                            undo.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //Wenn View undo nach UNDO_DELAY Millisekunden immer noch da
                                    // ist wird automatisch gelöscht, sonst geschieht nichts!!!
                                    if (undo.isShown()) {
                                        //callbacks.onDismiss(..) //delete-Funktion
                                        deleteItem(viewHolder.getAdapterPosition());
                                        mRecyclerView.getAdapter().notifyItemRemoved(viewHolder.getAdapterPosition());
                                    }
                                }
                            }, 1000);  //UNDO_DELAY=3000 Millisekunden

                        }

                        //mAdapter.onItemRemove(mRecyclerView, viewHolder);  //Test mit Snakebar
                        //deleteItem(viewHolder.getAdapterPosition());
                    }
                };
        return simpleItemTouchCallback;
    }

    private void addItemToList() {
        ListItem item = DummyData.getRandomListItem();
        myDataset.add(item);
        mAdapter.notifyItemInserted(myDataset.indexOf(item));
    }

    private void moveItem(int oldPos, int newPos) {
        //Wie funktioniert's:
        // 1. item ist nur Referenz auf Element in der Liste
        // 2. remove verbiegt nur den Zeiger um das gelöschte Element herum -> item zeigt weiter darauf
        // 3. item wird an die neue Stelle der Liste eingefügt
        ListItem item = (ListItem) myDataset.get(oldPos);
        myDataset.remove(oldPos);
        myDataset.add(newPos, item);
        mAdapter.notifyItemMoved(oldPos, newPos);
    }

    private void deleteItem(int pos) {
        myDataset.remove(pos);
        mAdapter.notifyItemRemoved(pos);
    }


    @Override
    public void onItemClick(int p) {
        ListItem item = (ListItem) myDataset.get(p);

        Intent i = new Intent(this, DetailActivity.class);

        Bundle extras = new Bundle();
        extras.putString(EXTRA_TITLE, item.getTitle());
        extras.putString(EXTRA_SUBTITLE, item.getSubTitle());
        i.putExtra(BUNDLE_EXTRAS, extras);
        startActivity(i);
    }

    @Override
    public void onSecondaryItemClick(int p) {
        ListItem item = (ListItem) myDataset.get(p);

        //item ist ein Objekt. Deshalb ist es nur ein Verweis auf das echte Element in maDataset
        // -> Die nächsten Zeilen ändern also direkt in myDataset (via item...)
        if (item.isFavourite()) {
            item.setFavourite(false);
        } else {
            item.setFavourite(true);
        }
        //pass new data to adapter and update
        // mAdapter.setMyDataset(myDataset); //ggü wiseAss nicht notwendig, das Object direkt geändert wird
        mAdapter.notifyDataSetChanged();

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
                            if (title.equals(titleString)) {  //Update
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
