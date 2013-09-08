package ru.fit4life.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.support.v4.widget.SimpleCursorAdapter;

public class ExercisesActivity extends Activity {

    private static final String TAG = "ExercisesActivity";

    private ExercisesDBAdapter exerciseDatabaseHelper;
    private SimpleCursorAdapter dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_exercises);
        // Initialize database adapter for the exercises table
        exerciseDatabaseHelper = new ExercisesDBAdapter(this);
        //Generate ListView from SQLite Database
        displayListView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        exerciseDatabaseHelper.closeDatabaseHelper();
        Log.i(TAG, String.format("View destroyed and database closed"));
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void displayListView() {

        Intent myIntent = getIntent();
        String groupId = myIntent.getStringExtra("groupId");

        Cursor cursor = exerciseDatabaseHelper.fetchExercisesForGroupId(groupId);

        if (cursor.getCount() > 0) {

            // The desired columns to be bound
            String[] columns = {
                    ExercisesDBAdapter.KEY_ICON,
                    ExercisesDBAdapter.KEY_NAME
            };

            // the XML defined views which the data will be bound to
            int[] to = {
                    R.id.imageViewExercisesIcon,
                    R.id.textViewExercisesName
            };

            ListView listView = (ListView) findViewById(R.id.exercisesList);

            // create the adapter using the cursor pointing to the desired data
            //as well as the layout information
            dataAdapter = new SimpleCursorAdapter(
                    this, R.layout.exercises_list_row,
                    cursor,
                    columns,
                    to,
                    0);

            Log.i(TAG, String.format("dataAdapter row count = "+dataAdapter.getCount()));

            dataAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                @Override
                public boolean setViewValue (View view, Cursor cursor, int columnIndex){
                    if (view.getId() != R.id.imageViewExercisesIcon)
                        return false;

                    String iconName = cursor.getString(columnIndex);

                    ImageView IV = (ImageView) view;
                    int resID = getApplicationContext().getResources().getIdentifier(iconName, "drawable",  getApplicationContext().getPackageName());

                    if (resID == 0) {
                        iconName = "db_exercise_not_found";
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
                    String exerciseId = itemCursor.getString(itemCursor.getColumnIndexOrThrow("_id"));
                    String exerciseName = itemCursor.getString(itemCursor.getColumnIndexOrThrow("name"));
                    String exerciseIcon = itemCursor.getString(itemCursor.getColumnIndexOrThrow("icon"));
                    String exerciseDescription = itemCursor.getString(itemCursor.getColumnIndexOrThrow("description"));
                    String exerciseTechnique = itemCursor.getString(itemCursor.getColumnIndexOrThrow("technique"));
                    String exerciseWorkingMuscles = itemCursor.getString(itemCursor.getColumnIndexOrThrow("working_muscles"));
                    String exerciseExerciseType = itemCursor.getString(itemCursor.getColumnIndexOrThrow("exercise_type"));

                    Intent intent = new Intent(getApplicationContext(), ExercisesItemActivity.class);
                    intent.putExtra("exerciseId", exerciseId);
                    intent.putExtra("exerciseName", exerciseName);
                    intent.putExtra("exerciseIcon", exerciseIcon);
                    intent.putExtra("exerciseDescription", exerciseDescription);
                    intent.putExtra("exerciseTechnique", exerciseTechnique);
                    intent.putExtra("exerciseWorkingMuscles", exerciseWorkingMuscles);
                    intent.putExtra("exerciseExerciseType", exerciseExerciseType);

                    startActivity(intent);
                    overridePendingTransition(R.anim.animation_in_left, R.anim.animation_out_left);
                }
            });
        }
        else {

            new AlertDialog.Builder(this)
                .setMessage(getString(R.string.alert_message_empty_body_parts))
                .setTitle(getString(R.string.alert_title_empty_body_parts))
                .setCancelable(true)
                .setNeutralButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton){
                                finish();
                            }
                        })
                .show();
        }
    }

    public void navigateBack(View view) {
        finish();
        overridePendingTransition(R.anim.animation_in_right, R.anim.animation_out_right);
    }

    public void navigateHome(View view) {

        Intent intent = new Intent(ExercisesActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.animation_in_right, R.anim.animation_out_right);
    }
}
