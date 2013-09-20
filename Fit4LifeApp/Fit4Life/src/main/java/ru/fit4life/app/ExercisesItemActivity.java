package ru.fit4life.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ExercisesItemActivity extends Activity {

    private static final String TAG = "ExercisesItemActivity";

    private TextView textViewExerciseName;
    private ImageView imageViewExerciseIcon;
    private TextView textViewExerciseDescription;
    private TextView textViewExerciseTechnique;
    private TextView textViewExerciseWorkingMuscles;
    private TextView textViewExerciseExerciseType;

    String exerciseName;
    String exerciseIcon;
    String exerciseDescription;
    String exerciseTechnique;
    String exerciseWorkingMuscles;
    String exerciseExerciseType;
    String exerciseExerciseURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_exercises_item);

        Intent myIntent = getIntent();

        exerciseName = myIntent.getStringExtra("exerciseName");
        exerciseIcon = myIntent.getStringExtra("exerciseIcon");
        exerciseDescription = myIntent.getStringExtra("exerciseDescription");
        exerciseTechnique = myIntent.getStringExtra("exerciseTechnique");
        exerciseWorkingMuscles = myIntent.getStringExtra("exerciseWorkingMuscles");
        exerciseExerciseType = myIntent.getStringExtra("exerciseExerciseType");
        exerciseExerciseURL = myIntent.getStringExtra("exerciseExerciseURL");

        textViewExerciseName = (TextView) this.findViewById(R.id.textViewExercisesItemName);
        imageViewExerciseIcon = (ImageView) this.findViewById(R.id.imageViewExercisesItemIcon);
        textViewExerciseDescription = (TextView) this.findViewById(R.id.textViewExercisesItemDescription);
        textViewExerciseTechnique = (TextView) this.findViewById(R.id.textViewExercisesItemTechnique);
        textViewExerciseWorkingMuscles = (TextView) this.findViewById(R.id.textViewExercisesItemWorkingMuscles);
        textViewExerciseExerciseType = (TextView) this.findViewById(R.id.textViewExercisesItemExerciseType);

        if (exerciseName == null || exerciseName.length() == 0) {exerciseName = this.getString(R.string.exercise_item_data_found);}
        textViewExerciseName.setText(exerciseName);

        if (exerciseDescription == null || exerciseDescription.length() == 0) {exerciseDescription = this.getString(R.string.exercise_item_data_found);}
        textViewExerciseDescription.setText(exerciseDescription);

        if (exerciseTechnique == null || exerciseTechnique.length() == 0) {exerciseTechnique = this.getString(R.string.exercise_item_data_found);}
        textViewExerciseTechnique.setText(exerciseTechnique);

        if (exerciseWorkingMuscles == null || exerciseWorkingMuscles.length() == 0) {exerciseWorkingMuscles = this.getString(R.string.exercise_item_data_found);}
        textViewExerciseWorkingMuscles.setText(exerciseWorkingMuscles);

        if (exerciseExerciseType == null || exerciseExerciseType.length() == 0) {exerciseExerciseType = this.getString(R.string.exercise_item_data_found);}
        textViewExerciseExerciseType.setText(exerciseExerciseType);

        if (exerciseExerciseURL == null || exerciseExerciseURL.length() == 0) {
            this.findViewById(R.id.ImageButtonExercisesItemVideo).setVisibility(View.GONE);
        }
        else {
            this.findViewById(R.id.ImageButtonExercisesItemVideo).setVisibility(View.VISIBLE);
        }

        int resID = getApplicationContext().getResources().getIdentifier(exerciseIcon, "drawable",  getApplicationContext().getPackageName());

        if (resID == 0) {
            exerciseIcon = "db_exercise_not_found";
            resID = getApplicationContext().getResources().getIdentifier(exerciseIcon, "drawable",  getApplicationContext().getPackageName());
        }

        imageViewExerciseIcon.setImageDrawable(getApplicationContext().getResources().getDrawable(resID));
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

    @Override
    public void finish() {
        super.finish();

        ApplicationState.setForeground();
        overridePendingTransition(R.anim.animation_in_right, R.anim.animation_out_right);
    }

    public void navigateBack(View view) {
        finish();
    }

    public void navigateHome(View view) {

        ApplicationState.setForeground();
        Intent intent = new Intent(ExercisesItemActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.animation_in_right, R.anim.animation_out_right);
    }

    public void openVideoForExercise(View view) {

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(exerciseExerciseURL));
        startActivity(intent);
    }
}
