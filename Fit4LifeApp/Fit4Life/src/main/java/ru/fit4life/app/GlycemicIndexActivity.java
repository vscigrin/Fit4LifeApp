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
    private NavigationToolbarManager ntm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glycemic_idex);

        // Initialize database adapter for the exercises table
        glycemicIndexDatabaseHelper = new GlycemicIndexDBAdapter(this);

        ntm = new NavigationToolbarManager(this);

        displayListView();
        setupNavigation();
    }

    @Override
    public void onResume() {
        super.onResume();

        if(!ApplicationState.isForegroud()) {
            Log.i(TAG, "IS NOW FOREGROUND");
            MainActivity.setAppIsUpToDate(false);
            MainActivity.runAppSync(MainActivity.getMainContext());
        }

        ApplicationState.setBackground();
    }

    @Override
    public void onPause() {
        super.onPause();

        if(!ApplicationState.isForegroud()) {
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
    public void finish() {
        super.finish();

        ApplicationState.setForeground();
        overridePendingTransition(R.anim.animation_in_right, R.anim.animation_out_right);
    }

    public void navigateBack(View view) {
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
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        };

        ntm.enableSearching(tw);
    }

    public void search(View view) {
        Toast.makeText(this, "Searching...", Toast.LENGTH_SHORT).show();

        ntm.startSearching();
    }

    public void cancelSearch(View view) {
        Toast.makeText(this, "Cancel buttonSearch...", Toast.LENGTH_SHORT).show();

        ntm.finishSearching();

    }

}
