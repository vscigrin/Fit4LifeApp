package ru.fit4life.app;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class GlycemicIndexActivity extends Activity {

    private static final String TAG = "GlycemicIndexActivity";

    private GlycemicIndexDBAdapter glycemicIndexDatabaseHelper;
    private SimpleCursorAdapter dataAdapter;
    private TextView nameActivity;
    private Button buttonSearch;
    private Button cancelSearch;
    private EditText inputSearch;
    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glycemic_idex);

        // Initialize database adapter for the exercises table
        glycemicIndexDatabaseHelper = new GlycemicIndexDBAdapter(this);

        imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);

        displayListView();
        setupNavigation();
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

            ListView listView = (ListView) findViewById(R.id.glycemicIndexList);

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
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        glycemicIndexDatabaseHelper.closeDatabaseHelper();
        Log.i(TAG, String.format("View destroyed and database closed"));
    }

    public void navigateBack(View view) {
        finish();
    }

    public void navigateHome(View view) {

        Intent intent = new Intent(GlycemicIndexActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    //Show text in top
    private void setupNavigation() {
        nameActivity = (TextView) findViewById(R.id.navigation_toolbar_name);
        nameActivity.setText(R.string.title_activity_glycemic_index);
        //name.setVisibility(View.GONE);

        buttonSearch = (Button) findViewById(R.id.navigation_toolbar_button_search);
        buttonSearch.setVisibility(View.VISIBLE);

        cancelSearch = (Button) findViewById(R.id.navigation_toolbar_button_cancel_search);
        inputSearch = (EditText) findViewById(R.id.navigation_toolbar_inputSearch);

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                Log.i(TAG, "input text: " + cs);
                // When user changed the Text
                GlycemicIndexActivity.this.dataAdapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void search(View view) {
        Toast.makeText(this, "Searching...", Toast.LENGTH_SHORT).show();

        //change focus to search view and open keyboard
        inputSearch.requestFocus();
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        //change activities visibility
        nameActivity.setVisibility(View.GONE);
        inputSearch.setVisibility(View.VISIBLE);
        buttonSearch.setVisibility(View.GONE);
        cancelSearch.setVisibility(View.VISIBLE);
    }

    public void cancelSearch(View view) {
        Toast.makeText(this, "Cancel buttonSearch...", Toast.LENGTH_SHORT).show();

        //clear text & refresh list
        inputSearch.setText(null, TextView.BufferType.NORMAL);

        //hide keyboard
        imm.hideSoftInputFromWindow(inputSearch.getWindowToken(), 0);

        //change activities visibility
        nameActivity.setVisibility(View.VISIBLE);
        inputSearch.setVisibility(View.GONE);
        buttonSearch.setVisibility(View.VISIBLE);
        cancelSearch.setVisibility(View.GONE);
    }

}
