package ru.fit4life.app;

import android.app.Activity;
import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_exercises_item);

        Intent myIntent = getIntent();

        String exerciseName = myIntent.getStringExtra("exerciseName");
        String exerciseIcon = myIntent.getStringExtra("exerciseIcon");
        String exerciseDescription = myIntent.getStringExtra("exerciseDescription");
        String exerciseTechnique = myIntent.getStringExtra("exerciseTechnique");
        String exerciseWorkingMuscles = myIntent.getStringExtra("exerciseWorkingMuscles");
        String exerciseExerciseType = myIntent.getStringExtra("exerciseExerciseType");

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

        int resID = getApplicationContext().getResources().getIdentifier(exerciseIcon, "drawable",  getApplicationContext().getPackageName());

        if (resID == 0) {
            exerciseIcon = "db_exercise_not_found";
            resID = getApplicationContext().getResources().getIdentifier(exerciseIcon, "drawable",  getApplicationContext().getPackageName());
        }

        imageViewExerciseIcon.setImageDrawable(getApplicationContext().getResources().getDrawable(resID));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, String.format("View destroyed"));
    }

    public void navigateBack(View view) {
        finish();
        overridePendingTransition(R.anim.animation_in_right, R.anim.animation_out_right);
    }

    public void navigateHome(View view) {

        Intent intent = new Intent(ExercisesItemActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.animation_in_right, R.anim.animation_out_right);
    }
}
