package ru.fit4life.app;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.Toast;

public class GlycemicIndexActivity extends Activity {

    private static final String TAG = "GlycemicIndexActivity";

    private GlycemicIndexDBAdapter glycemicIndexDatabaseHelper;
    private SimpleCursorAdapter dataAdapter;
    private ToolbarsManager toolbarsManager;
    ListView listView;
    private int selectedRowId;
    private String selectedName;
    private float selectedValue;
    private String[] menuItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glycemic_idex);

        // Initialize database adapter for the exercises table
        glycemicIndexDatabaseHelper = new GlycemicIndexDBAdapter(this);

        toolbarsManager = new ToolbarsManager(this);

        menuItems = getResources().getStringArray(R.array.tables_context_menu);

        setupNavigation();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!ApplicationState.isForegroud()) {
            Log.i(TAG, "IS NOW FOREGROUND");
            MainActivity.setAppIsUpToDate(false);
            MainActivity.runAppSync(MainActivity.getMainContext());
        }
        displayListView();
        ApplicationState.setBackground();
    }

    @Override
    public void onPause() {
        super.onPause();

        if (!ApplicationState.isForegroud()) {
            Log.i(TAG, "IS NOW BACKGROUND");
        }
    }

    private void displayListView() {
        Log.i(TAG, "display list view inicialized");
        Cursor cursor = glycemicIndexDatabaseHelper.fetchAll();

        if (cursor.getCount() > 0) {
            String[] columns = {
                    GlycemicIndexDBAdapter.KEY_NAME,
                    GlycemicIndexDBAdapter.KEY_VALUE,
            };

            int[] to = {
                    R.id.textViewGlycemicIndexName,
                    R.id.textViewGlycemicIndexValue
            };
            listView = (ListView) findViewById(R.id.glycemicIndexList);

            dataAdapter = new SimpleCursorAdapter(
                    this, R.layout.glycemic_index_list_row,
                    cursor,
                    columns,
                    to,
                    0);

            Log.i(TAG, String.format("dataAdapter row count = " + dataAdapter.getCount()));

            dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
                @Override
                public Cursor runQuery(CharSequence charSequence) {

                    if (charSequence == null)
                        return glycemicIndexDatabaseHelper.fetchAll();

                    return glycemicIndexDatabaseHelper.fetchByFilter(charSequence.toString());

                }
            });

            listView.setAdapter(dataAdapter);


            registerForContextMenu(listView);
/*
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> listView, View view,
                                        int position, long id) {
                    // Get the cursor, positioned to the corresponding row in the result set
                    Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                    // Get the state's capital from this row in the database.
                    String rowId = cursor.getString(cursor.getColumnIndexOrThrow(GlycemicIndexDBAdapter.KEY_ROWID));

                    Toast.makeText(getApplicationContext(), rowId, Toast.LENGTH_SHORT).show();

                }
            });*/
        }

    }


    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.glycemicIndexList) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            Cursor cursor = (Cursor) listView.getItemAtPosition(info.position);

            selectedRowId = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(GlycemicIndexDBAdapter.KEY_ROWID)));
            selectedName = cursor.getString(cursor.getColumnIndexOrThrow(GlycemicIndexDBAdapter.KEY_NAME));
            selectedValue = Float.parseFloat(cursor.getString(cursor.getColumnIndexOrThrow(GlycemicIndexDBAdapter.KEY_VALUE)));

            menu.setHeaderTitle(selectedName);
            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int menuItemIndex = item.getItemId();

        String menuItemName = menuItems[menuItemIndex];

        if (menuItemName.equals("Edit")) {
            Intent intent = new Intent(this, GlycemicIndexNewItemActivity.class);
            intent.setAction(getString(R.string.atEdit));
            intent.putExtra(GlycemicIndexDBAdapter.KEY_ROWID, selectedRowId);
            intent.putExtra(GlycemicIndexDBAdapter.KEY_NAME, selectedName);
            intent.putExtra(GlycemicIndexDBAdapter.KEY_VALUE, selectedValue);

            if (intent != null) {
                startActivity(intent);
                overridePendingTransition(R.anim.animation_in_left, R.anim.animation_out_left);
            }


        } else if (menuItemName.equals("Delete")) {
            Toast.makeText(this, "Item deleted!", Toast.LENGTH_SHORT).show();

            glycemicIndexDatabaseHelper.deleteFoodById(selectedRowId);

            displayListView();
        }

        return true;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        ApplicationState.setForeground();
        overridePendingTransition(R.anim.animation_in_right, R.anim.animation_out_right);
    }

    public void navigateBack(View view) {

        /*final CharSequence[] items = {"Red", "Green", "Blue"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick a color");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
        */
        finish();
        overridePendingTransition(R.anim.animation_in_right, R.anim.animation_out_right);
    }

    public void navigateHome(View view) {

        ApplicationState.setForeground();
        Intent intent = new Intent(GlycemicIndexActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.animation_in_right, R.anim.animation_out_right);
    }

    //Show text in top
    private void setupNavigation() {

        TextWatcher tw = new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                dataAdapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }
            @Override
            public void afterTextChanged(Editable arg0) {
            }
        };

        toolbarsManager.enableSearching(tw);
    }

    public void search(View view) {
        Toast.makeText(this, "Searching...", Toast.LENGTH_SHORT).show();

        toolbarsManager.startSearching();
    }

    public void cancelSearch(View view) {
        Toast.makeText(this, "Cancel buttonSearch...", Toast.LENGTH_SHORT).show();

        toolbarsManager.finishSearching();

    }

    public void addNew(View view) {

        Intent intent = new Intent(this, GlycemicIndexNewItemActivity.class);
        intent.setAction("atNew");

        if (intent != null) {
            startActivity(intent);
            overridePendingTransition(R.anim.animation_in_left, R.anim.animation_out_left);
        }
    }

}
