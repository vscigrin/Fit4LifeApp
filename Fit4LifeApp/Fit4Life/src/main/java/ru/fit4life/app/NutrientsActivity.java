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

public class NutrientsActivity extends MyActivity {

    private NutrientsDBAdapter nutrientsDBAdapter;
    private SimpleCursorAdapter dataAdapter;
    private ToolbarsManager toolbarsManager;
    private ListView listView;
    private int selectedRowId;
    private String selectedName;
    private float selectedProteins;
    private float selectedFats;
    private float selectedCarbs;
    private float selectedCalories;
    private String[] menuItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrients);

        setTag(this.getClass().getSimpleName());
        nutrientsDBAdapter = new NutrientsDBAdapter();
        menuItems = getResources().getStringArray(R.array.tables_context_menu);

        setupNavigation();
    }

    @Override
    public void onResume() {
        super.onResume();

        displayListView();
    }

    private void displayListView() {
        Log.i(getTag(), "display list view inicialized");
        Cursor cursor = nutrientsDBAdapter.fetchAll();

        if (cursor.getCount() > 0) {
            String[] columns = {
                    NutrientsDBAdapter.KEY_NAME,
                    NutrientsDBAdapter.KEY_PROTEINS,
                    NutrientsDBAdapter.KEY_FATS,
                    NutrientsDBAdapter.KEY_CARBOHYDRATES,
                    NutrientsDBAdapter.KEY_CALORIES
            };

            int[] to = {
                    R.id.textViewNutrientsName,
                    R.id.textViewNutrientsProteins,
                    R.id.textViewNutrientsFats,
                    R.id.textViewNutrientsCarbohydrates,
                    R.id.textViewNutrientsCalories
            };

            listView = (ListView) findViewById(R.id.nutrientsList);

            dataAdapter = new SimpleCursorAdapter(
                    this, R.layout.nutritions_list_row,
                    cursor,
                    columns,
                    to,
                    0);

            Log.i(getTag(), String.format("dataAdapter row count = " + dataAdapter.getCount()));

            dataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
                @Override
                public Cursor runQuery(CharSequence charSequence) {

                    if (charSequence == null)
                        return nutrientsDBAdapter.fetchAll();

                    return nutrientsDBAdapter.fetchByFilter(charSequence.toString());

                }
            });

            listView.setAdapter(dataAdapter);
            registerForContextMenu(listView);
        }
    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.nutrientsList) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            Cursor cursor = (Cursor) listView.getItemAtPosition(info.position);

            selectedRowId = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(NutrientsDBAdapter.KEY_ROWID)));
            selectedName = cursor.getString(cursor.getColumnIndexOrThrow(NutrientsDBAdapter.KEY_NAME));
            selectedProteins = Float.parseFloat(cursor.getString(cursor.getColumnIndexOrThrow(NutrientsDBAdapter.KEY_PROTEINS)));
            selectedFats = Float.parseFloat(cursor.getString(cursor.getColumnIndexOrThrow(NutrientsDBAdapter.KEY_FATS)));
            selectedCarbs = Float.parseFloat(cursor.getString(cursor.getColumnIndexOrThrow(NutrientsDBAdapter.KEY_CARBOHYDRATES)));
            selectedCalories = Float.parseFloat(cursor.getString(cursor.getColumnIndexOrThrow(NutrientsDBAdapter.KEY_CALORIES)));

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
            Intent intent = new Intent(this, NutrientsNewItemActivity.class);
            intent.setAction(Intent.ACTION_EDIT);
            intent.putExtra(NutrientsDBAdapter.KEY_ROWID, selectedRowId);
            intent.putExtra(NutrientsDBAdapter.KEY_NAME, selectedName);
            intent.putExtra(NutrientsDBAdapter.KEY_PROTEINS, selectedProteins);
            intent.putExtra(NutrientsDBAdapter.KEY_FATS, selectedFats);
            intent.putExtra(NutrientsDBAdapter.KEY_CARBOHYDRATES, selectedCarbs);
            intent.putExtra(NutrientsDBAdapter.KEY_CALORIES, selectedCalories);

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
                        nutrientsDBAdapter.deleteRowById(selectedRowId);
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
        //set activity name
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

        Intent intent = new Intent(this, NutrientsNewItemActivity.class);
        intent.setAction(intent.ACTION_INSERT);

        if (intent != null) {
            startActivity(intent);
            overridePendingTransition(R.anim.animation_in_left, R.anim.animation_out_left);
        }
    }
}
