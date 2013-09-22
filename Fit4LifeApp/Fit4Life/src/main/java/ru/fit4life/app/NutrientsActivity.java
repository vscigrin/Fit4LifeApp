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

public class NutrientsActivity extends MyActivity {

    private NutrientsDBAdapter nutrientsDatabaseHelper;
    private SimpleCursorAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrients);

        setTag(this.getClass().getSimpleName());

        // Initialize database adapter for the exercises table
        nutrientsDatabaseHelper = new NutrientsDBAdapter(this);

        //Generate ListView from SQLite Database
        displayListView();
    }

    private void displayListView() {
        Log.i(getTag(), "display list view inicialized");
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

            Log.i(getTag(), String.format("dataAdapter row count = " + dataAdapter.getCount()));


            listView.setAdapter(dataAdapter);
        }


    }
}
