package ru.fit4life.app;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class GlycemicIndexActivity extends Activity {

    private static final String TAG = "NutrientsActivity";

    private GlycemicIndexDBAdapter glycemicIndexDatabaseHelper;
    private SimpleCursorAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glycemic_idex);

        // Initialize database adapter for the exercises table
        glycemicIndexDatabaseHelper = new GlycemicIndexDBAdapter(this);

        //Generate ListView from SQLite Database
        displayListView();
        fillNavigationName();
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
    private void fillNavigationName() {
        TextView name = (TextView) findViewById(R.id.navigation_toolbar_name);
        name.setText(R.string.title_activity_glycemic_index);
    }

}
