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

public class NutrientsActivity extends Activity {

    private static final String TAG = "NutrientsActivity";

    private NutrientsDBAdapter nutrientsDatabaseHelper;
    private SimpleCursorAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrients);

        // Initialize database adapter for the exercises table
        nutrientsDatabaseHelper = new NutrientsDBAdapter(this);
        //Generate ListView from SQLite Database

        displayListView();
        fillNavigationName();
    }

    private void displayListView() {
        Log.i(TAG, "display list view inicialized");
        Cursor cursor = nutrientsDatabaseHelper.fetchAll();

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

            ListView listView = (ListView) findViewById(R.id.nutrientsList);

            dataAdapter = new SimpleCursorAdapter(
                    this, R.layout.nutritions_list_row,
                    cursor,
                    columns,
                    to,
                    0);

            Log.i(TAG, String.format("dataAdapter row count = "+dataAdapter.getCount()));


            listView.setAdapter(dataAdapter);
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        nutrientsDatabaseHelper.closeDatabaseHelper();
        Log.i(TAG, String.format("View destroyed and database closed"));
    }

    public void navigateBack(View view) {
        finish();
    }

    public void navigateHome(View view) {

        Intent intent = new Intent(NutrientsActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    //Show text in top
    private void fillNavigationName() {
        TextView name = (TextView) findViewById(R.id.navigation_toolbar_name);
        name.setText(R.string.title_activity_nutrients);
    }

}
