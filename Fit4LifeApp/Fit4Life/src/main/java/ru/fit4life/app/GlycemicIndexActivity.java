package ru.fit4life.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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

////-XX:MaxHeapSize=512m
//http://stackoverflow.com/questions/5472362/android-automatic-horizontally-scrolling-textview
public class GlycemicIndexActivity extends MyActivity {

    private GlycemicIndexDBAdapter glycemicIndexDBAdapter;
    private SimpleCursorAdapter dataAdapter;
    private ListView listView;
    private int selectedRowId;
    private String selectedName;
    private float selectedValue;
    private String[] menuItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glycemic_idex);

        setTag(this.getClass().getSimpleName());
        glycemicIndexDBAdapter = new GlycemicIndexDBAdapter();
        menuItems = getResources().getStringArray(R.array.tables_context_menu);

        setupNavigation();
    }

    @Override
    public void onResume() {
        super.onResume();

        displayListView();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    private void displayListView() {
        Log.i(getTag(), "display list view inicialized");
        Cursor cursor = glycemicIndexDBAdapter.fetchAll();

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

            Log.i(getTag(), String.format("dataAdapter row count = " + dataAdapter.getCount()));

            dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
                @Override
                public Cursor runQuery(CharSequence charSequence) {

                    if (charSequence == null)
                        return glycemicIndexDBAdapter.fetchAll();

                    return glycemicIndexDBAdapter.fetchByFilter(charSequence.toString());

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
            intent.setAction(Intent.ACTION_EDIT);
            intent.putExtra(GlycemicIndexDBAdapter.KEY_ROWID, selectedRowId);
            intent.putExtra(GlycemicIndexDBAdapter.KEY_NAME, selectedName);
            intent.putExtra(GlycemicIndexDBAdapter.KEY_VALUE, selectedValue);

            if (intent != null) {
                startActivity(intent);
                overridePendingTransition(R.anim.animation_in_left, R.anim.animation_out_left);
            }


        } else if (menuItemName.equals("Delete")) {
            showAlert();
        }

        return true;
    }

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setCancelable(true);
        builder.setTitle("Really delete???");
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        glycemicIndexDBAdapter.deleteRowById(selectedRowId);
                        displayListView();
                        dialog.dismiss();
                    }
                });
        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {

                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private void setupNavigation() {

        toolbarsManager = new ToolbarsManager(this);
        toolbarsManager.setNameActivity(this.getTitle().toString());

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
        intent.setAction(Intent.ACTION_INSERT);

        if (intent != null) {
            startActivity(intent);
            overridePendingTransition(R.anim.animation_in_left, R.anim.animation_out_left);
        }
    }

}
