package ru.fit4life.app;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.support.v4.widget.SimpleCursorAdapter;

public class BodyPartsActivity extends MyActivity {

    private BodyPartsDBAdapter bodyPartsDatabaseHelper;
    private SimpleCursorAdapter dataAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_parts);

        setTag(this.getClass().getSimpleName());

        // Initialize database adapter for the exercises table
        bodyPartsDatabaseHelper = new BodyPartsDBAdapter();
        //Generate ListView from SQLite Database
        displayListView();

        //set activity name
        toolbarsManager = new ToolbarsManager(this);
        toolbarsManager.setNameActivity(this.getTitle().toString());
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void displayListView() {

        Cursor cursor = bodyPartsDatabaseHelper.fetchAllBodyParts();

        // The desired columns to be bound
        String[] columns = {
                BodyPartsDBAdapter.KEY_ICON,
                BodyPartsDBAdapter.KEY_NAME
        };

        // the XML defined views which the data will be bound to
        int[] to = {
                R.id.imageViewBodyPartsIcon,
                R.id.textViewBodyPartsName
        };

        ListView listView = (ListView) findViewById(R.id.bodyPartsList);

        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.body_parts_list_row,
                cursor,
                columns,
                to,
                0);

        dataAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue (View view, Cursor cursor, int columnIndex){
                if (view.getId() != R.id.imageViewBodyPartsIcon)
                    return false;

                String iconName = cursor.getString(columnIndex);

                ImageView IV=(ImageView) view;
                int resID = getApplicationContext().getResources().getIdentifier(iconName, "drawable",  getApplicationContext().getPackageName());

                if (resID == 0) {
                    iconName = "db_bp_not_found";
                    resID = getApplicationContext().getResources().getIdentifier(iconName, "drawable",  getApplicationContext().getPackageName());
                }

                IV.setImageDrawable(getApplicationContext().getResources().getDrawable(resID));

                return true;
            }
        });

        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view,
                                    int position, long id) {
                // Get the cursor, positioned to the corresponding row in the result set
                Cursor itemCursor = (Cursor) listView.getItemAtPosition(position);

                // Get the state's capital from this row in the database.
                String groupId = itemCursor.getString(itemCursor.getColumnIndexOrThrow("_id"));

                ApplicationState.setForeground();
                Intent intent = new Intent(getApplicationContext(), ExercisesActivity.class);
                intent.putExtra("groupId", groupId);
                startActivity(intent);
                overridePendingTransition(R.anim.animation_in_left, R.anim.animation_out_left);
            }
        });
    }
}
